import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, map, tap } from 'rxjs';
import { CrearPedidoRequest, EstadoPedido, ItemPedido, Pedido } from '../models/pedido.model';
import { environment } from '../../environments/environment';
import { InventarioService } from './inventario.service';

const INTERVALO_POLL_MS = 1500;

/** Item tal como lo devuelve mcsv-pedidos (PedidoResponse.Item): sin enriquecer. */
interface ItemPedidoApi {
  productoId: number;
  cantidad: number;
  precioUnitario: number;
}

/** Pedido tal como lo devuelve mcsv-pedidos (PedidoResponse): sin enriquecer. */
interface PedidoApi {
  id: number;
  clienteId: string;
  fecha: string;
  estado: EstadoPedido;
  motivoRechazo?: string;
  items: ItemPedidoApi[];
}

/**
 * Cliente de pedidos: habla con mcsv-pedidos a través de Kong (POST/GET /api/pedidos).
 *
 * Kong solo enruta, no compone datos entre microservicios — así que este servicio
 * hace localmente lo que antes resolvía el api-gateway: agrega `nombreProducto` (del
 * catálogo que ya carga InventarioService) y calcula `subtotal`/`total` a partir del
 * `precioUnitario` que ya devuelve mcsv-pedidos. El precio en sí NUNCA lo decide el
 * navegador: mcsv-pedidos lo resuelve contra mcsv-inventario al crear el pedido,
 * para que no sea manipulable desde el cliente.
 *
 * La confirmación NO es inmediata: el pedido nace en estado CREADO y el
 * backend lo resuelve de forma asíncrona vía Kafka (MCSV Inventario valida
 * stock y responde STOCK_ACTUALIZADO o PEDIDO_RECHAZADO). Como no hay
 * WebSocket/SSE, este servicio hace polling corto sobre GET /api/pedidos/{id}
 * mientras el pedido siga en CREADO, y actualiza la signal local cuando
 * llega el resultado.
 */
@Injectable({ providedIn: 'root' })
export class PedidoService {
  private readonly http = inject(HttpClient);
  private readonly inventarioSrv = inject(InventarioService);
  private readonly baseUrl = `${environment.apiGatewayUrl}/api/pedidos`;

  private readonly _pedidosRaw = signal<PedidoApi[]>([]);

  /** Pedidos enriquecidos, ordenados del más reciente al más antiguo. */
  readonly pedidos = computed(() =>
    this._pedidosRaw()
      .map((pedido) => this.enriquecer(pedido))
      .sort((a, b) => b.id - a.id),
  );

  constructor() {
    if (this.inventarioSrv.productos().length === 0) {
      this.inventarioSrv.cargarProductos();
    }
    this.cargarPedidos();
  }

  /** Crea un pedido (POST /api/pedidos) y comienza a vigilar su resolución. */
  crearPedido(req: CrearPedidoRequest): Observable<Pedido> {
    const body = { clienteId: req.cliente, items: req.items };
    return this.http.post<PedidoApi>(this.baseUrl, body).pipe(
      tap((pedido) => {
        this._pedidosRaw.update((lista) => [...lista, pedido]);
        this.vigilarResolucion(pedido.id);
      }),
      map((pedido) => this.enriquecer(pedido)),
    );
  }

  private cargarPedidos(): void {
    this.http.get<PedidoApi[]>(this.baseUrl).subscribe((pedidos) => {
      this._pedidosRaw.set(pedidos);
      pedidos
        .filter((pedido) => pedido.estado === 'CREADO')
        .forEach((pedido) => this.vigilarResolucion(pedido.id));
    });
  }

  private vigilarResolucion(id: number): void {
    const intervalo = setInterval(() => {
      this.http.get<PedidoApi>(`${this.baseUrl}/${id}`).subscribe((pedido) => {
        this._pedidosRaw.update((lista) => lista.map((p) => (p.id === id ? pedido : p)));
        if (pedido.estado !== 'CREADO') {
          clearInterval(intervalo);
        }
      });
    }, INTERVALO_POLL_MS);
  }

  private enriquecer(pedido: PedidoApi): Pedido {
    const items: ItemPedido[] = pedido.items.map((item) => ({
      productoId: item.productoId,
      nombreProducto: this.nombreProducto(item.productoId),
      precioUnitario: item.precioUnitario,
      cantidad: item.cantidad,
      subtotal: item.precioUnitario * item.cantidad,
    }));
    return {
      id: pedido.id,
      cliente: pedido.clienteId,
      fecha: pedido.fecha,
      estado: pedido.estado,
      motivoRechazo: pedido.motivoRechazo,
      items,
      total: items.reduce((acc, item) => acc + item.subtotal, 0),
    };
  }

  private nombreProducto(productoId: number): string {
    return (
      this.inventarioSrv.productos().find((p) => p.id === productoId)?.nombre ??
      `Producto #${productoId}`
    );
  }
}
