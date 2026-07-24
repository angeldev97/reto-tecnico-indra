package com.reto.gateway.resource;

import com.reto.gateway.dto.PedidoBackendDto;
import com.reto.gateway.dto.PedidoFrontendDto;
import com.reto.gateway.dto.ProductoDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class PedidoMapper {

    private PedidoMapper() {
    }

    static PedidoFrontendDto toFrontend(PedidoBackendDto backend, Map<Long, ProductoDto> productos) {
        PedidoFrontendDto dto = new PedidoFrontendDto();
        dto.id = backend.id;
        dto.cliente = backend.clienteId;
        dto.fecha = backend.fecha;
        dto.estado = backend.estado;
        dto.motivoRechazo = backend.motivoRechazo;

        List<PedidoFrontendDto.Item> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (PedidoBackendDto.Item backendItem : backend.items) {
            PedidoFrontendDto.Item item = new PedidoFrontendDto.Item();
            item.productoId = backendItem.productoId;
            ProductoDto producto = productos.get(backendItem.productoId);
            item.nombreProducto = producto != null ? producto.nombre : "Producto #" + backendItem.productoId;
            item.precioUnitario = backendItem.precioUnitario;
            item.cantidad = backendItem.cantidad;
            item.subtotal = backendItem.precioUnitario.multiply(BigDecimal.valueOf(backendItem.cantidad));
            items.add(item);
            total = total.add(item.subtotal);
        }

        dto.items = items;
        dto.total = total;
        return dto;
    }
}
