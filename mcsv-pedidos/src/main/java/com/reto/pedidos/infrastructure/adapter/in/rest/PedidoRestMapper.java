package com.reto.pedidos.infrastructure.adapter.in.rest;

import com.reto.pedidos.domain.model.Pedido;
import com.reto.pedidos.domain.model.PedidoItem;
import com.reto.pedidos.domain.port.in.command.CrearPedidoCommand;
import com.reto.pedidos.infrastructure.adapter.in.rest.dto.CrearPedidoRequest;
import com.reto.pedidos.infrastructure.adapter.in.rest.dto.PedidoResponse;

import java.util.List;
import java.util.stream.Collectors;

final class PedidoRestMapper {

    private PedidoRestMapper() {
    }

    static CrearPedidoCommand toCommand(CrearPedidoRequest request) {
        CrearPedidoCommand command = new CrearPedidoCommand();
        command.clienteId = request.clienteId;
        command.items = request.items.stream().map(itemRequest -> {
            CrearPedidoCommand.Item item = new CrearPedidoCommand.Item();
            item.productoId = itemRequest.productoId;
            item.cantidad = itemRequest.cantidad;
            return item;
        }).collect(Collectors.toList());
        return command;
    }

    static PedidoResponse toResponse(Pedido pedido) {
        PedidoResponse response = new PedidoResponse();
        response.id = pedido.getId();
        response.clienteId = pedido.getClienteId();
        response.fecha = pedido.getFecha();
        response.estado = pedido.getEstado().name();
        response.motivoRechazo = pedido.getMotivoRechazo();
        response.items = pedido.getItems().stream().map(PedidoRestMapper::toResponse).collect(Collectors.toList());
        return response;
    }

    private static PedidoResponse.Item toResponse(PedidoItem item) {
        PedidoResponse.Item dto = new PedidoResponse.Item();
        dto.id = item.getId();
        dto.productoId = item.getProductoId();
        dto.cantidad = item.getCantidad();
        dto.precioUnitario = item.getPrecioUnitario();
        return dto;
    }
}
