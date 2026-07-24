package com.reto.inventario.messaging;

public class StockActualizadoEvent {

    public Long pedidoId;

    public StockActualizadoEvent() {
    }

    public StockActualizadoEvent(Long pedidoId) {
        this.pedidoId = pedidoId;
    }
}
