package com.reto.pedidos.application;

import com.reto.pedidos.domain.model.Pedido;
import com.reto.pedidos.domain.model.PedidoItem;
import com.reto.pedidos.domain.port.in.CrearPedidoUseCase;
import com.reto.pedidos.domain.port.in.command.CrearPedidoCommand;
import com.reto.pedidos.domain.port.out.CatalogoPort;
import com.reto.pedidos.domain.port.out.PedidoEventPublisher;
import com.reto.pedidos.domain.port.out.PedidoRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CrearPedidoService implements CrearPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final PedidoEventPublisher eventPublisher;
    private final CatalogoPort catalogoPort;

    public CrearPedidoService(PedidoRepository pedidoRepository, PedidoEventPublisher eventPublisher, CatalogoPort catalogoPort) {
        this.pedidoRepository = pedidoRepository;
        this.eventPublisher = eventPublisher;
        this.catalogoPort = catalogoPort;
    }

    @Override
    public Pedido crear(CrearPedidoCommand command) {
        // El precio se resuelve aquí, contra mcsv-inventario, y nunca se confía en
        // el que mande el llamador: con Kong como proxy puro delante (sin gateway que
        // valide), un precioUnitario recibido del cliente sería manipulable.
        List<PedidoItem> items = command.items.stream()
                .map(item -> new PedidoItem(item.productoId, item.cantidad, catalogoPort.obtenerPrecio(item.productoId)))
                .collect(Collectors.toList());

        // El evento se publica solo después de que guardar() retorna (persistencia
        // ya confirmada): así nunca anunciamos un pedido que no llegó a persistirse.
        Pedido guardado = pedidoRepository.guardar(Pedido.crear(command.clienteId, items));
        eventPublisher.publicarPedidoCreado(guardado);
        return guardado;
    }
}
