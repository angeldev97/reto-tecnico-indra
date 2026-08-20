package com.reto.inventario.infrastructure.adapter.out.messaging.event;

public class PedidoRechazadoEvent {

    public Long pedidoId;
    public String motivo;

    public PedidoRechazadoEvent() {
    }

    public PedidoRechazadoEvent(Long pedidoId, String motivo) {
        this.pedidoId = pedidoId;
        this.motivo = motivo;
    }
}
