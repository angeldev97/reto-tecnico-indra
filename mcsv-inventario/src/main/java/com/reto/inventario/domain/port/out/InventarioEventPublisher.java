package com.reto.inventario.domain.port.out;

public interface InventarioEventPublisher {

    void publicarStockActualizado(Long pedidoId);

    void publicarPedidoRechazado(Long pedidoId, String motivo);
}
