package com.reto.inventario.infrastructure.adapter.out.messaging.event;

public class StockActualizadoEvent {

    public Long pedidoId;

    public StockActualizadoEvent() {
    }

    public StockActualizadoEvent(Long pedidoId) {
        this.pedidoId = pedidoId;
    }
}
