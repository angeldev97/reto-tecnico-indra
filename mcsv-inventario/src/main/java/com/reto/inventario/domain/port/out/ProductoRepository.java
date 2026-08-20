package com.reto.inventario.domain.port.out;

import com.reto.inventario.domain.model.Producto;

import java.util.List;
import java.util.Optional;

// Puerto de salida (hexagonal): lo implementa PanacheProductoRepository. El dominio
// y los use cases dependen solo de esta interfaz, nunca de Panache/Hibernate.
public interface ProductoRepository {

    Optional<Producto> buscarPorId(Long id);

    List<Producto> listarTodos();

    void guardar(Producto producto);
}
