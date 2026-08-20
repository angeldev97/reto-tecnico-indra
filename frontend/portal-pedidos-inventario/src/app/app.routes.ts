import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'catalogo' },
  {
    path: 'catalogo',
    title: 'Catálogo de productos',
    loadComponent: () =>
      import('./components/catalogo/catalogo.component').then((m) => m.CatalogoComponent),
  },
  {
    path: 'crear-pedido',
    title: 'Crear pedido',
    loadComponent: () =>
      import('./components/crear-pedido/crear-pedido.component').then((m) => m.CrearPedidoComponent),
  },
  {
    path: 'mis-pedidos',
    title: 'Mis pedidos',
    loadComponent: () =>
      import('./components/mis-pedidos/mis-pedidos.component').then((m) => m.MisPedidosComponent),
  },
  {
    path: 'pedidos/:id',
    title: 'Detalle del pedido',
    loadComponent: () =>
      import('./components/detalle-pedido/detalle-pedido.component').then((m) => m.DetallePedidoComponent),
  },
  { path: '**', redirectTo: 'catalogo' },
];
