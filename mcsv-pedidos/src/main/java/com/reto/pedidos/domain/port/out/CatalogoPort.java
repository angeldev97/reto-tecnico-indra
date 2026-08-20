package com.reto.pedidos.domain.port.out;

import java.math.BigDecimal;

// Puerto de salida (hexagonal): lo implementa CatalogoRestAdapter (REST client hacia
// mcsv-inventario). El dominio y los use cases solo ven esta interfaz — no saben que
// el precio viene de otro microservicio por HTTP.
public interface CatalogoPort {

    BigDecimal obtenerPrecio(Long productoId);
}
