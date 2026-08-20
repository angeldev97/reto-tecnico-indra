package com.reto.inventario.infrastructure.adapter.out.messaging;

import com.reto.inventario.domain.port.out.InventarioEventPublisher;
import com.reto.inventario.infrastructure.adapter.out.messaging.event.PedidoRechazadoEvent;
import com.reto.inventario.infrastructure.adapter.out.messaging.event.StockActualizadoEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

@ApplicationScoped
public class KafkaInventarioEventPublisher implements InventarioEventPublisher {

    private static final Logger LOG = Logger.getLogger(KafkaInventarioEventPublisher.class);

    @Inject
    @Channel("stock-actualizado-out")
    Emitter<StockActualizadoEvent> stockActualizadoEmitter;

    @Inject
    @Channel("pedido-rechazado-out")
    Emitter<PedidoRechazadoEvent> pedidoRechazadoEmitter;

    @Override
    public void publicarStockActualizado(Long pedidoId) {
        LOG.infof("Pedido %d: stock actualizado", pedidoId);
        stockActualizadoEmitter.send(new StockActualizadoEvent(pedidoId));
    }

    @Override
    public void publicarPedidoRechazado(Long pedidoId, String motivo) {
        LOG.infof("Pedido %d rechazado: %s", pedidoId, motivo);
        pedidoRechazadoEmitter.send(new PedidoRechazadoEvent(pedidoId, motivo));
    }
}
