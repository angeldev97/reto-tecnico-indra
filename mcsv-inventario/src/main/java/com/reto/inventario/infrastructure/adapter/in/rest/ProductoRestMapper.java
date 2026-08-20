package com.reto.inventario.infrastructure.adapter.in.rest;

import com.reto.inventario.domain.model.Producto;
import com.reto.inventario.infrastructure.adapter.in.rest.dto.ProductoResponse;

final class ProductoRestMapper {

    private ProductoRestMapper() {
    }

    static ProductoResponse toResponse(Producto producto) {
        ProductoResponse response = new ProductoResponse();
        response.id = producto.getId();
        response.nombre = producto.getNombre();
        response.descripcion = producto.getDescripcion();
        response.precio = producto.getPrecio();
        response.stock = producto.getStock();
        response.sku = producto.getSku();
        response.marca = producto.getMarca();
        response.categoria = producto.getCategoria();
        return response;
    }
}
