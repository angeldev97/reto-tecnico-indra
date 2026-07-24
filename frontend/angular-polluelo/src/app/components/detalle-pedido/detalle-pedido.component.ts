import { DatePipe } from '@angular/common';
import { Component, computed, inject, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { EstadoPedido } from '../../models/pedido.model';
import { PedidoService } from '../../services/pedido.service';

@Component({
  selector: 'app-detalle-pedido',
  imports: [RouterLink, DatePipe],
  templateUrl: './detalle-pedido.component.html',
  styleUrl: './detalle-pedido.component.scss',
})
export class DetallePedidoComponent {
  private readonly pedidoSrv = inject(PedidoService);

  /** Id recibido desde la ruta (withComponentInputBinding). */
  readonly id = input.required<string>();

  /** Pedido reactivo: refleja en vivo el cambio CREADO -> CONFIRMADO/RECHAZADO. */
  readonly pedido = computed(() =>
    this.pedidoSrv.pedidos().find((p) => p.id === Number(this.id())),
  );

  claseEstado(estado: EstadoPedido): string {
    return 'status status-' + estado.toLowerCase();
  }

  etiqueta(estado: EstadoPedido): string {
    return estado.charAt(0) + estado.slice(1).toLowerCase();
  }
}
