package com.reto.gateway.dto;

import java.math.BigDecimal;
import java.util.List;

/** Payload que espera mcsv-pedidos (POST /pedidos): precio ya resuelto. */
public class CrearPedidoBackendRequest {
    public String clienteId;
    public List<Item> items;

    public CrearPedidoBackendRequest() {
    }

    public CrearPedidoBackendRequest(String clienteId, List<Item> items) {
        this.clienteId = clienteId;
        this.items = items;
    }

    public static class Item {
        public Long productoId;
        public int cantidad;
        public BigDecimal precioUnitario;

        public Item() {
        }

        public Item(Long productoId, int cantidad, BigDecimal precioUnitario) {
            this.productoId = productoId;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
        }
    }
}
