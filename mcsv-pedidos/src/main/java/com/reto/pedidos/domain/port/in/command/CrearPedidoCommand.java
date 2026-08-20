package com.reto.pedidos.domain.port.in.command;

import java.util.List;

public class CrearPedidoCommand {
    public String clienteId;
    public List<Item> items;

    public static class Item {
        public Long productoId;
        public int cantidad;
    }
}
