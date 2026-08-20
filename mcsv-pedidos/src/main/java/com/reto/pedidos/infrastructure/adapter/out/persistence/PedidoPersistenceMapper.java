package com.reto.pedidos.infrastructure.adapter.out.persistence;

import com.reto.pedidos.domain.model.Pedido;
import com.reto.pedidos.domain.model.PedidoItem;

import java.util.List;
import java.util.stream.Collectors;

final class PedidoPersistenceMapper {

    private PedidoPersistenceMapper() {
    }

    static Pedido toDomain(PedidoEntity entity) {
        Pedido pedido = new Pedido();
        pedido.setId(entity.id);
        pedido.setClienteId(entity.clienteId);
        pedido.setFecha(entity.fecha);
        pedido.setEstado(entity.estado);
        pedido.setMotivoRechazo(entity.motivoRechazo);
        pedido.setItems(entity.items.stream()
                .map(PedidoPersistenceMapper::toDomain)
                .collect(Collectors.toList()));
        return pedido;
    }

    private static PedidoItem toDomain(PedidoItemEntity entity) {
        PedidoItem item = new PedidoItem();
        item.setId(entity.id);
        item.setProductoId(entity.productoId);
        item.setCantidad(entity.cantidad);
        item.setPrecioUnitario(entity.precioUnitario);
        return item;
    }

    static List<PedidoItemEntity> toEntities(List<PedidoItem> items, PedidoEntity pedidoEntity) {
        return items.stream().map(item -> {
            PedidoItemEntity entity = new PedidoItemEntity();
            entity.pedido = pedidoEntity;
            entity.productoId = item.getProductoId();
            entity.cantidad = item.getCantidad();
            entity.precioUnitario = item.getPrecioUnitario();
            return entity;
        }).collect(Collectors.toList());
    }
}
