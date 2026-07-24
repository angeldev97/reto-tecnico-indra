package com.reto.pedidos.messaging;

import java.util.List;

public class PedidoCreadoEvent {

    public Long pedidoId;
    public String clienteId;
    public List<Item> items;

    public PedidoCreadoEvent() {
    }

    public PedidoCreadoEvent(Long pedidoId, String clienteId, List<Item> items) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.items = items;
    }

    public static class Item {
        public Long productoId;
        public int cantidad;

        public Item() {
        }

        public Item(Long productoId, int cantidad) {
            this.productoId = productoId;
            this.cantidad = cantidad;
        }
    }
}
