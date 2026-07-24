import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Producto } from '../../models/producto.model';
import { CarritoService } from '../../services/carrito.service';
import { InventarioService } from '../../services/inventario.service';

type Campo = 'nombre' | 'categoria' | 'stock' | 'precio';
type Dir = 'asc' | 'desc';

@Component({
  selector: 'app-catalogo',
  imports: [FormsModule],
  templateUrl: './catalogo.component.html',
  styleUrl: './catalogo.component.scss',
})
export class CatalogoComponent implements OnInit {
  private readonly inventario = inject(InventarioService);
  private readonly carrito = inject(CarritoService);

  readonly cargando = this.inventario.cargando;
  readonly categoria = signal<string>('Todas');
  readonly busqueda = signal<string>('');
  readonly campo = signal<Campo | null>(null);
  readonly dir = signal<Dir>('asc');
  readonly aviso = signal<string | null>(null);

  readonly categorias = computed(() => [
    'Todas',
    ...Array.from(new Set(this.inventario.productos().map((p) => p.categoria))),
  ]);

  /** Productos tras filtro (categoría + búsqueda) y orden por columna. */
  readonly productos = computed<Producto[]>(() => {
    const cat = this.categoria();
    const q = this.busqueda().trim().toLowerCase();

    let lista = this.inventario.productos();
    if (cat !== 'Todas') lista = lista.filter((p) => p.categoria === cat);
    if (q) {
      lista = lista.filter((p) =>
        `${p.nombre} ${p.marca} ${p.categoria} ${p.sku} ${p.descripcion}`
          .toLowerCase()
          .includes(q),
      );
    }

    const campo = this.campo();
    if (!campo) return lista;

    const factor = this.dir() === 'asc' ? 1 : -1;
    return [...lista].sort((a, b) => {
      const va = a[campo];
      const vb = b[campo];
      const cmp = typeof va === 'number' && typeof vb === 'number'
        ? va - vb
        : String(va).localeCompare(String(vb), 'es');
      return cmp * factor;
    });
  });

  readonly totalCatalogo = computed(() => this.inventario.productos().length);

  ngOnInit(): void {
    this.inventario.cargarProductos();
  }

  /** Alterna el orden de una columna (asc → desc → asc). */
  ordenar(campo: Campo): void {
    if (this.campo() === campo) {
      this.dir.update((d) => (d === 'asc' ? 'desc' : 'asc'));
    } else {
      this.campo.set(campo);
      this.dir.set(campo === 'precio' || campo === 'stock' ? 'desc' : 'asc');
    }
  }

  flecha(campo: Campo): string {
    if (this.campo() !== campo) return '↕';
    return this.dir() === 'asc' ? '↑' : '↓';
  }

  filtrar(cat: string): void {
    this.categoria.set(cat);
  }

  limpiar(): void {
    this.busqueda.set('');
    this.categoria.set('Todas');
    this.campo.set(null);
  }

  enCarrito(id: number): number {
    return this.carrito.cantidadDe(id);
  }

  agregar(producto: Producto): void {
    if (producto.stock <= 0) return;
    this.carrito.agregar(producto, 1);
    this.aviso.set(`${producto.nombre} · agregado al pedido`);
    setTimeout(() => this.aviso.set(null), 1800);
  }

  estadoStock(stock: number): 'agotado' | 'bajo' | 'ok' {
    if (stock <= 0) return 'agotado';
    if (stock <= 5) return 'bajo';
    return 'ok';
  }
}
