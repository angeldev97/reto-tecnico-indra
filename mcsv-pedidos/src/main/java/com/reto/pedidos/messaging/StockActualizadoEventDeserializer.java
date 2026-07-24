package com.reto.pedidos.messaging;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class StockActualizadoEventDeserializer extends ObjectMapperDeserializer<StockActualizadoEvent> {

    public StockActualizadoEventDeserializer() {
        super(StockActualizadoEvent.class);
    }
}
