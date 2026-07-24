package com.reto.pedidos.messaging;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PedidoRechazadoEventDeserializer extends ObjectMapperDeserializer<PedidoRechazadoEvent> {

    public PedidoRechazadoEventDeserializer() {
        super(PedidoRechazadoEvent.class);
    }
}
