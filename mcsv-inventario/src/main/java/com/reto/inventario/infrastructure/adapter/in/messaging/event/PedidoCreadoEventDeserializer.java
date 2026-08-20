package com.reto.inventario.infrastructure.adapter.in.messaging.event;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PedidoCreadoEventDeserializer extends ObjectMapperDeserializer<PedidoCreadoEvent> {

    public PedidoCreadoEventDeserializer() {
        super(PedidoCreadoEvent.class);
    }
}
