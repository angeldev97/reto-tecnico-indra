package com.reto.inventario.application;

import com.reto.inventario.domain.exception.ProductoNoEncontradoException;
import com.reto.inventario.domain.model.Producto;
import com.reto.inventario.domain.port.in.ConsultarProductosUseCase;
import com.reto.inventario.domain.port.out.ProductoRepository;

import java.util.List;

public class ConsultarProductosService implements ConsultarProductosUseCase {

    private final ProductoRepository productoRepository;

    public ConsultarProductosService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> listar() {
        return productoRepository.listarTodos();
    }

    @Override
    public Producto obtener(Long id) {
        return productoRepository.buscarPorId(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }
}
