package com.reto.gateway.dto;

import java.math.BigDecimal;
import java.util.List;

/** Forma que espera el frontend Angular (modelo Pedido de pedido.model.ts). */
public class PedidoFrontendDto {
    public Long id;
    public String cliente;
    public String fecha;
    public String estado;
    public List<Item> items;
    public BigDecimal total;
    public String motivoRechazo;

    public static class Item {
        public Long productoId;
        public String nombreProducto;
        public BigDecimal precioUnitario;
        public int cantidad;
        public BigDecimal subtotal;
    }
}
