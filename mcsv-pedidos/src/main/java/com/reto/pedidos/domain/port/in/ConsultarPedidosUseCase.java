package com.reto.pedidos.domain.port.in;

import com.reto.pedidos.domain.model.Pedido;

import java.util.List;

public interface ConsultarPedidosUseCase {

    List<Pedido> listar(String clienteId);

    Pedido obtener(Long id);
}
