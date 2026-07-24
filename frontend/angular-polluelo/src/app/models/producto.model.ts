/**
 * Producto del catálogo.
 * Proviene del microservicio MCSV Inventario (GET /api/productos vía API Gateway).
 */
export interface Producto {
  id: number;
  sku: string;
  nombre: string;
  marca: string;
  descripcion: string;
  categoria: string;
  precio: number;
  stock: number;
}
