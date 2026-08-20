package com.reto.pedidos.domain.port.in;

public interface ActualizarEstadoPedidoUseCase {

    void confirmar(Long pedidoId);

    void rechazar(Long pedidoId, String motivo);
}
