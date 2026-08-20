package com.reto.pedidos.infrastructure.adapter.out.messaging;

import com.reto.pedidos.domain.model.Pedido;
import com.reto.pedidos.domain.port.out.PedidoEventPublisher;
import com.reto.pedidos.infrastructure.adapter.out.messaging.event.PedidoCreadoEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class KafkaPedidoEventPublisher implements PedidoEventPublisher {

    @Inject
    @Channel("pedido-creado-out")
    Emitter<PedidoCreadoEvent> pedidoCreadoEmitter;

    @Override
    public void publicarPedidoCreado(Pedido pedido) {
        List<PedidoCreadoEvent.Item> items = pedido.getItems().stream()
                .map(item -> new PedidoCreadoEvent.Item(item.getProductoId(), item.getCantidad()))
                .collect(Collectors.toList());
        pedidoCreadoEmitter.send(new PedidoCreadoEvent(pedido.getId(), pedido.getClienteId(), items));
    }
}
