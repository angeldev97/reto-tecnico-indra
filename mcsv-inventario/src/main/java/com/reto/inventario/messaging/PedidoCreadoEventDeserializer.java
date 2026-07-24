package com.reto.inventario.messaging;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PedidoCreadoEventDeserializer extends ObjectMapperDeserializer<PedidoCreadoEvent> {

    public PedidoCreadoEventDeserializer() {
        super(PedidoCreadoEvent.class);
    }
}
