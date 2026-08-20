package com.reto.pedidos.domain.port.out;

import com.reto.pedidos.domain.model.Pedido;

import java.util.List;
import java.util.Optional;

// Puerto de salida (hexagonal): lo que el dominio necesita del exterior, sin saber
// cómo se implementa. Lo implementa PanachePedidoRepository (Hibernate/Panache),
// pero el dominio y los use cases solo ven esta interfaz.
public interface PedidoRepository {

    Pedido guardar(Pedido pedido);

    Optional<Pedido> buscarPorId(Long id);

    List<Pedido> listar(String clienteId);
}
