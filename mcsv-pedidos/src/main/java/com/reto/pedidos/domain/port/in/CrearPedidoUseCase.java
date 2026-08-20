package com.reto.pedidos.domain.port.in;

import com.reto.pedidos.domain.model.Pedido;
import com.reto.pedidos.domain.port.in.command.CrearPedidoCommand;

// Puerto de entrada (hexagonal): contrato que expone el dominio hacia afuera.
// Lo implementa un application service (CrearPedidoService) y lo invoca un
// adapter de entrada (PedidoResource); ninguno de los dos conoce al otro.
public interface CrearPedidoUseCase {

    Pedido crear(CrearPedidoCommand command);
}
