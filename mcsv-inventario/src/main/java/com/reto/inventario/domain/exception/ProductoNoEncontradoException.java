package com.reto.inventario.domain.exception;

public class ProductoNoEncontradoException extends RuntimeException {
    public ProductoNoEncontradoException(Long id) {
        super("Producto " + id + " no encontrado");
    }
}
