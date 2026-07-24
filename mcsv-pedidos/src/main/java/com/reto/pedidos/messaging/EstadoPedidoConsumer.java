package com.reto.pedidos.messaging;

import com.reto.pedidos.model.EstadoPedido;
import com.reto.pedidos.model.Pedido;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EstadoPedidoConsumer {

    private static final Logger LOG = Logger.getLogger(EstadoPedidoConsumer.class);

    @Incoming("stock-actualizado-in")
    @Blocking
    @Transactional
    public void onStockActualizado(StockActualizadoEvent event) {
        actualizarEstado(event.pedidoId, EstadoPedido.CONFIRMADO);
    }

    @Incoming("pedido-rechazado-in")
    @Blocking
    @Transactional
    public void onPedidoRechazado(PedidoRechazadoEvent event) {
        LOG.infof("Pedido %d rechazado: %s", event.pedidoId, event.motivo);
        Pedido pedido = Pedido.<Pedido>findByIdOptional(event.pedidoId).orElse(null);
        if (pedido == null) {
            LOG.warnf("Pedido %d no encontrado al procesar evento de estado", event.pedidoId);
            return;
        }
        pedido.estado = EstadoPedido.RECHAZADO;
        pedido.motivoRechazo = event.motivo;
    }

    private void actualizarEstado(Long pedidoId, EstadoPedido estado) {
        Pedido pedido = Pedido.<Pedido>findByIdOptional(pedidoId).orElse(null);
        if (pedido == null) {
            LOG.warnf("Pedido %d no encontrado al procesar evento de estado", pedidoId);
            return;
        }
        pedido.estado = estado;
    }
}
