package com.reto.inventario.infrastructure.adapter.out.persistence;

import com.reto.inventario.domain.model.MovimientoInventario;
import com.reto.inventario.domain.port.out.MovimientoInventarioRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PanacheMovimientoInventarioRepository implements MovimientoInventarioRepository {

    @Override
    public void registrar(MovimientoInventario movimiento) {
        MovimientoInventarioEntity entity = new MovimientoInventarioEntity();
        entity.producto = ProductoEntity.findById(movimiento.getProductoId());
        entity.pedidoId = movimiento.getPedidoId();
        entity.cantidad = movimiento.getCantidad();
        entity.tipo = movimiento.getTipo();
        entity.fecha = movimiento.getFecha();
        entity.persist();
    }
}
