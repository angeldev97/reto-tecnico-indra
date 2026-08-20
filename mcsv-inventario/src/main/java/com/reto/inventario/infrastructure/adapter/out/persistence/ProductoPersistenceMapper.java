package com.reto.inventario.infrastructure.adapter.out.persistence;

import com.reto.inventario.domain.model.Producto;

final class ProductoPersistenceMapper {

    private ProductoPersistenceMapper() {
    }

    static Producto toDomain(ProductoEntity entity) {
        Producto producto = new Producto();
        producto.setId(entity.id);
        producto.setNombre(entity.nombre);
        producto.setDescripcion(entity.descripcion);
        producto.setPrecio(entity.precio);
        producto.setStock(entity.stock);
        producto.setSku(entity.sku);
        producto.setMarca(entity.marca);
        producto.setCategoria(entity.categoria);
        return producto;
    }
}
