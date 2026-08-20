package com.reto.pedidos.domain.port.out;

import com.reto.pedidos.domain.model.Pedido;

public interface PedidoEventPublisher {

    void publicarPedidoCreado(Pedido pedido);
}
