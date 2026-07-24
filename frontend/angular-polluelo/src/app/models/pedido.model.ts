/**
 * Estado del pedido.
 * El ciclo de vida refleja el flujo de eventos de la arquitectura:
 *   CREADO      -> el MCSV Pedidos guarda el pedido y publica PEDIDO_CREADO
 *   CONFIRMADO  -> el MCSV Inventario validó stock y publicó STOCK_ACTUALIZADO
 *   RECHAZADO   -> no había stock suficiente -> PEDIDO_RECHAZADO
 */
export type EstadoPedido = 'CREADO' | 'CONFIRMADO' | 'RECHAZADO';

/** Línea de un pedido (producto + cantidad). */
export interface ItemPedido {
  productoId: number;
  nombreProducto: string;
  precioUnitario: number;
  cantidad: number;
  subtotal: number;
}

/**
 * Pedido completo.
 * En producción proviene del microservicio MCSV Pedidos
 * (endpoints: POST /pedidos, GET /pedidos, GET /pedidos/{id}).
 */
export interface Pedido {
  id: number;
  cliente: string;
  fecha: string; // ISO 8601
  estado: EstadoPedido;
  items: ItemPedido[];
  total: number;
  motivoRechazo?: string;
}

/** Cuerpo enviado al crear un pedido (POST /pedidos). */
export interface CrearPedidoRequest {
  cliente: string;
  items: { productoId: number; cantidad: number }[];
}
