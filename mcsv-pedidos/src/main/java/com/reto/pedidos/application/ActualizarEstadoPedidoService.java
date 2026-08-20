package com.reto.pedidos.application;

import com.reto.pedidos.domain.model.Pedido;
import com.reto.pedidos.domain.port.in.ActualizarEstadoPedidoUseCase;
import com.reto.pedidos.domain.port.out.PedidoRepository;

import java.util.function.Consumer;

public class ActualizarEstadoPedidoService implements ActualizarEstadoPedidoUseCase {

    private final PedidoRepository pedidoRepository;

    public ActualizarEstadoPedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public void confirmar(Long pedidoId) {
        actualizar(pedidoId, Pedido::confirmar);
    }

    @Override
    public void rechazar(Long pedidoId, String motivo) {
        actualizar(pedidoId, pedido -> pedido.rechazar(motivo));
    }

    // Si el pedido no existe, no se hace nada (no se lanza excepción): un evento
    // Kafka duplicado o fuera de orden es un caso de negocio esperado, no un error
    // del sistema, y no debe reintentar ni tumbar el consumer. El log de ese caso
    // vive en el adapter de mensajería (infraestructura), no aquí.
    private void actualizar(Long pedidoId, Consumer<Pedido> mutacion) {
        pedidoRepository.buscarPorId(pedidoId).ifPresent(pedido -> {
            mutacion.accept(pedido);
            pedidoRepository.guardar(pedido);
        });
    }
}
