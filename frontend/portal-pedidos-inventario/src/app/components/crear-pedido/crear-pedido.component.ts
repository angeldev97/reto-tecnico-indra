import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CarritoService } from '../../services/carrito.service';
import { PedidoService } from '../../services/pedido.service';

@Component({
  selector: 'app-crear-pedido',
  imports: [FormsModule, RouterLink],
  templateUrl: './crear-pedido.component.html',
  styleUrl: './crear-pedido.component.scss',
})
export class CrearPedidoComponent {
  private readonly carritoSrv = inject(CarritoService);
  private readonly pedidoSrv = inject(PedidoService);
  private readonly router = inject(Router);

  readonly items = this.carritoSrv.items;
  readonly total = this.carritoSrv.total;
  readonly unidades = computed(() => this.items().reduce((acc, i) => acc + i.cantidad, 0));

  cliente = '';
  readonly enviando = signal(false);

  cambiarCantidad(productoId: number, valor: number): void {
    this.carritoSrv.cambiarCantidad(productoId, valor);
  }

  quitar(productoId: number): void {
    this.carritoSrv.quitar(productoId);
  }

  confirmar(): void {
    if (this.items().length === 0 || this.enviando()) return;

    this.enviando.set(true);

    // POST /api/pedidos -> el pedido nace en estado CREADO.
    this.pedidoSrv
      .crearPedido({
        cliente: this.cliente,
        items: this.items().map((i) => ({ productoId: i.producto.id, cantidad: i.cantidad })),
      })
      .subscribe({
        next: (pedido) => {
          this.carritoSrv.vaciar();
          // Vamos al detalle para ver cómo el estado pasa de CREADO a CONFIRMADO/RECHAZADO.
          this.router.navigate(['/pedidos', pedido.id]);
        },
        error: () => this.enviando.set(false),
      });
  }
}
