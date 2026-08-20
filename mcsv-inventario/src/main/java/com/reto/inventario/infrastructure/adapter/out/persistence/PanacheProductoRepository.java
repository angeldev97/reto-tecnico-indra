package com.reto.inventario.infrastructure.adapter.out.persistence;

import com.reto.inventario.domain.model.Producto;
import com.reto.inventario.domain.port.out.ProductoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class PanacheProductoRepository implements ProductoRepository {

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        return ProductoEntity.<ProductoEntity>findByIdOptional(id).map(ProductoPersistenceMapper::toDomain);
    }

    @Override
    public List<Producto> listarTodos() {
        List<ProductoEntity> entities = ProductoEntity.listAll();
        return entities.stream().map(ProductoPersistenceMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void guardar(Producto producto) {
        ProductoEntity entity = ProductoEntity.findById(producto.getId());
        entity.stock = producto.getStock();
    }
}
