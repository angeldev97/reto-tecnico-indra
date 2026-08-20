package com.reto.pedidos.infrastructure.adapter.out.persistence;

import com.reto.pedidos.domain.model.Pedido;
import com.reto.pedidos.domain.port.out.PedidoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class PanachePedidoRepository implements PedidoRepository {

    // guardar() hace de "upsert": id == null es un alta (crea la entidad y sus items),
    // id != null es una actualización de estado sobre una entidad ya existente.
    // El @Transactional vive aquí (y no en el use case) porque la creación se invoca
    // desde el adapter REST, que no abre transacción propia — así se preserva el
    // mismo comportamiento que el QuarkusTransaction.requiringNew() del código original.
    @Override
    @Transactional
    public Pedido guardar(Pedido pedido) {
        PedidoEntity entity;
        if (pedido.getId() == null) {
            entity = new PedidoEntity();
            entity.clienteId = pedido.getClienteId();
            entity.fecha = pedido.getFecha();
            entity.estado = pedido.getEstado();
            entity.motivoRechazo = pedido.getMotivoRechazo();
            entity.items = PedidoPersistenceMapper.toEntities(pedido.getItems(), entity);
            entity.persist();
        } else {
            entity = PedidoEntity.findById(pedido.getId());
            entity.estado = pedido.getEstado();
            entity.motivoRechazo = pedido.getMotivoRechazo();
        }
        return PedidoPersistenceMapper.toDomain(entity);
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return PedidoEntity.<PedidoEntity>findByIdOptional(id).map(PedidoPersistenceMapper::toDomain);
    }

    @Override
    public List<Pedido> listar(String clienteId) {
        List<PedidoEntity> entities = (clienteId != null && !clienteId.isBlank())
                ? PedidoEntity.list("clienteId", clienteId)
                : PedidoEntity.listAll();
        return entities.stream().map(PedidoPersistenceMapper::toDomain).collect(Collectors.toList());
    }
}
