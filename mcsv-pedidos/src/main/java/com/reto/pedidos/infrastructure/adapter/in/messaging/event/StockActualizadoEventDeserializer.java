package com.reto.pedidos.infrastructure.adapter.in.messaging.event;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class StockActualizadoEventDeserializer extends ObjectMapperDeserializer<StockActualizadoEvent> {

    public StockActualizadoEventDeserializer() {
        super(StockActualizadoEvent.class);
    }
}
