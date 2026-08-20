import { Injectable, computed, signal } from '@angular/core';
import { Producto } from '../models/producto.model';

/** Línea del carrito: producto seleccionado + cantidad. */
export interface ItemCarrito {
  producto: Producto;
  cantidad: number;
}

/**
 * Carrito de compra en memoria (estado del front, no del backend).
 * Se comparte entre el Catálogo (agregar) y Crear Pedido (revisar/confirmar).
 */
@Injectable({ providedIn: 'root' })
export class CarritoService {
  private readonly _items = signal<ItemCarrito[]>([]);

  readonly items = this._items.asReadonly();

  /** Cantidad total de unidades (para el badge del menú). */
  readonly cantidadTotal = computed(() =>
    this._items().reduce((acc, i) => acc + i.cantidad, 0),
  );

  /** Importe total del carrito. */
  readonly total = computed(() =>
    this._items().reduce((acc, i) => acc + i.producto.precio * i.cantidad, 0),
  );

  /**
   * Agrega un producto; si ya está, incrementa la cantidad.
   * No se topa contra el stock: el front es optimista y el inventario valida
   * la disponibilidad de forma asíncrona (puede terminar en PEDIDO_RECHAZADO).
   */
  agregar(producto: Producto, cantidad = 1): void {
    this._items.update((items) => {
      const existente = items.find((i) => i.producto.id === producto.id);
      if (existente) {
        return items.map((i) =>
          i.producto.id === producto.id ? { ...i, cantidad: i.cantidad + cantidad } : i,
        );
      }
      return [...items, { producto, cantidad: Math.max(1, cantidad) }];
    });
  }

  /** Fija la cantidad exacta de una línea (mínimo 1). */
  cambiarCantidad(productoId: number, cantidad: number): void {
    this._items.update((items) =>
      items.map((i) =>
        i.producto.id === productoId ? { ...i, cantidad: Math.max(1, cantidad) } : i,
      ),
    );
  }

  /** Quita una línea del carrito. */
  quitar(productoId: number): void {
    this._items.update((items) => items.filter((i) => i.producto.id !== productoId));
  }

  /** Vacía el carrito (tras confirmar el pedido). */
  vaciar(): void {
    this._items.set([]);
  }

  /** Cantidad ya agregada de un producto (para topes en el catálogo). */
  cantidadDe(productoId: number): number {
    return this._items().find((i) => i.producto.id === productoId)?.cantidad ?? 0;
  }
}
