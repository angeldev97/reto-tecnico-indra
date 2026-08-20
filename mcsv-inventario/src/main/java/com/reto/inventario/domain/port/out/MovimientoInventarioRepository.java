package com.reto.inventario.domain.port.out;

import com.reto.inventario.domain.model.MovimientoInventario;

public interface MovimientoInventarioRepository {

    void registrar(MovimientoInventario movimiento);
}
