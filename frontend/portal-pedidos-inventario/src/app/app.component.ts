import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { CarritoService } from './services/carrito.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  private readonly carrito = inject(CarritoService);
  /** Cantidad de unidades en el carrito (badge del menú "Crear Pedido"). */
  readonly itemsCarrito = this.carrito.cantidadTotal;
}
