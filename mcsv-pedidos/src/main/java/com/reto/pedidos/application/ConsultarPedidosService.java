package com.reto.pedidos.application;

import com.reto.pedidos.domain.exception.PedidoNoEncontradoException;
import com.reto.pedidos.domain.model.Pedido;
import com.reto.pedidos.domain.port.in.ConsultarPedidosUseCase;
import com.reto.pedidos.domain.port.out.PedidoRepository;

import java.util.List;

public class ConsultarPedidosService implements ConsultarPedidosUseCase {

    private final PedidoRepository pedidoRepository;

    public ConsultarPedidosService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public List<Pedido> listar(String clienteId) {
        return pedidoRepository.listar(clienteId);
    }

    @Override
    public Pedido obtener(Long id) {
        return pedidoRepository.buscarPorId(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));
    }
}
