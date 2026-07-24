import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { CrearPedidoRequest, Pedido } from '../models/pedido.model';
import { environment } from '../../environments/environment';

const INTERVALO_POLL_MS = 1500;

/**
 * Cliente de pedidos: habla con el API Gateway (POST/GET /api/pedidos),
 * que reenvía a MCSV Pedidos.
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
  private readonly baseUrl = `${environment.apiGatewayUrl}/api/pedidos`;

  private readonly _pedidos = signal<Pedido[]>([]);

  /** Pedidos ordenados del más reciente al más antiguo. */
  readonly pedidos = computed(() => [...this._pedidos()].sort((a, b) => b.id - a.id));

  constructor() {
    this.cargarPedidos();
  }

  /** Crea un pedido (POST /api/pedidos) y comienza a vigilar su resolución. */
  crearPedido(req: CrearPedidoRequest): Observable<Pedido> {
    return this.http.post<Pedido>(this.baseUrl, req).pipe(
      tap((pedido) => {
        this._pedidos.update((lista) => [...lista, pedido]);
        this.vigilarResolucion(pedido.id);
      }),
    );
  }

  private cargarPedidos(): void {
    this.http.get<Pedido[]>(this.baseUrl).subscribe((pedidos) => {
      this._pedidos.set(pedidos);
      pedidos
        .filter((pedido) => pedido.estado === 'CREADO')
        .forEach((pedido) => this.vigilarResolucion(pedido.id));
    });
  }

  private vigilarResolucion(id: number): void {
    const intervalo = setInterval(() => {
      this.http.get<Pedido>(`${this.baseUrl}/${id}`).subscribe((pedido) => {
        this._pedidos.update((lista) => lista.map((p) => (p.id === id ? pedido : p)));
        if (pedido.estado !== 'CREADO') {
          clearInterval(intervalo);
        }
      });
    }, INTERVALO_POLL_MS);
  }
}
