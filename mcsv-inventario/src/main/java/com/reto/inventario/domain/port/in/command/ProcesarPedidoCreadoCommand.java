package com.reto.inventario.domain.port.in.command;

import java.util.List;

public class ProcesarPedidoCreadoCommand {
    public Long pedidoId;
    public List<Item> items;

    public static class Item {
        public Long productoId;
        public int cantidad;
    }
}
