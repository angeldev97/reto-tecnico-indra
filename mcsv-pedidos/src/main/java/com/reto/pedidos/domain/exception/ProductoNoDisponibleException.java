package com.reto.pedidos.domain.exception;

public class ProductoNoDisponibleException extends RuntimeException {
    public ProductoNoDisponibleException(Long productoId) {
        super("Producto " + productoId + " no disponible en el catálogo");
    }
}
