package com.reto.inventario.domain.port.in;

import com.reto.inventario.domain.model.Producto;

import java.util.List;

public interface ConsultarProductosUseCase {

    List<Producto> listar();

    Producto obtener(Long id);
}
