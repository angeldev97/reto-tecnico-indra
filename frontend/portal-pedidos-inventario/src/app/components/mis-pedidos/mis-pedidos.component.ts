import { DatePipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { EstadoPedido } from '../../models/pedido.model';
import { PedidoService } from '../../services/pedido.service';

@Component({
  selector: 'app-mis-pedidos',
  imports: [RouterLink, DatePipe],
  templateUrl: './mis-pedidos.component.html',
  styleUrl: './mis-pedidos.component.scss',
})
export class MisPedidosComponent {
  private readonly pedidoSrv = inject(PedidoService);

  /** Lista reactiva: se actualiza sola cuando cambia el estado de un pedido. */
  readonly pedidos = this.pedidoSrv.pedidos;

  /** Clase CSS del estado. */
  claseEstado(estado: EstadoPedido): string {
    return 'status status-' + estado.toLowerCase();
  }

  /** Estado en Título (Creado / Confirmado / Rechazado). */
  etiqueta(estado: EstadoPedido): string {
    return estado.charAt(0) + estado.slice(1).toLowerCase();
  }

  totalUnidades(items: { cantidad: number }[]): number {
    return items.reduce((acc, i) => acc + i.cantidad, 0);
  }
}
