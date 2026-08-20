package com.reto.inventario.infrastructure.adapter.in.messaging.event;

import java.util.List;

public class PedidoCreadoEvent {

    public Long pedidoId;
    public String clienteId;
    public List<Item> items;

    public static class Item {
        public Long productoId;
        public int cantidad;
    }
}
