import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { Producto } from '../models/producto.model';
import { environment } from '../../environments/environment';

/**
 * Cliente del catálogo: GET /api/productos a través del API Gateway,
 * que reenvía a MCSV Inventario.
 */
@Injectable({ providedIn: 'root' })
export class InventarioService {
  private readonly http = inject(HttpClient);

  private readonly _productos = signal<Producto[]>([]);
  private readonly _cargando = signal(false);

  readonly productos = this._productos.asReadonly();
  readonly cargando = this._cargando.asReadonly();

  cargarProductos(): void {
    this._cargando.set(true);
    this.http.get<Producto[]>(`${environment.apiGatewayUrl}/api/productos`).subscribe({
      next: (productos) => {
        this._productos.set(productos);
        this._cargando.set(false);
      },
      error: () => this._cargando.set(false),
    });
  }
}
