package com.reto.pedidos.infrastructure.adapter.in.messaging.event;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PedidoRechazadoEventDeserializer extends ObjectMapperDeserializer<PedidoRechazadoEvent> {

    public PedidoRechazadoEventDeserializer() {
        super(PedidoRechazadoEvent.class);
    }
}
