package com.reto.pedidos.domain.exception;

public class PedidoNoEncontradoException extends RuntimeException {
    public PedidoNoEncontradoException(Long id) {
        super("Pedido " + id + " no encontrado");
    }
}
