package com.reto.pedidos.infrastructure.adapter.in.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponse {
    public Long id;
    public String clienteId;
    public LocalDateTime fecha;
    public String estado;
    public String motivoRechazo;
    public List<Item> items;

    public static class Item {
        public Long id;
        public Long productoId;
        public int cantidad;
        public BigDecimal precioUnitario;
    }
}
