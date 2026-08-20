package com.reto.inventario.infrastructure.adapter.in.messaging;

import com.reto.inventario.application.ProcesarPedidoCreadoService;
import com.reto.inventario.domain.port.in.ProcesarPedidoCreadoUseCase;
import com.reto.inventario.domain.port.in.command.ProcesarPedidoCreadoCommand;
import com.reto.inventario.domain.port.out.InventarioEventPublisher;
import com.reto.inventario.domain.port.out.MovimientoInventarioRepository;
import com.reto.inventario.domain.port.out.ProductoRepository;
import com.reto.inventario.infrastructure.adapter.in.messaging.event.PedidoCreadoEvent;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.stream.Collectors;

@ApplicationScoped
public class PedidoCreadoConsumer {

    private final ProcesarPedidoCreadoUseCase procesarPedidoCreadoUseCase;

    // El use case es una clase de application plana (sin CDI): este adapter, que sí
    // es un bean gestionado, inyecta los puertos de salida y lo compone a mano.
    public PedidoCreadoConsumer(ProductoRepository productoRepository,
                                 MovimientoInventarioRepository movimientoRepository,
                                 InventarioEventPublisher eventPublisher) {
        this.procesarPedidoCreadoUseCase = new ProcesarPedidoCreadoService(productoRepository, movimientoRepository, eventPublisher);
    }

    // @Transactional vive aquí (y no en el use case, que es Java puro) porque debe
    // cubrir, como una sola unidad atómica, el descuento de stock de N productos +
    // el registro de N movimientos: el interceptor JTA envuelve todo lo invocado
    // durante esta llamada, incluidas las escrituras hechas a través de
    // ProcesarPedidoCreadoService aunque ya no sea un bean gestionado.
    @Incoming("pedido-creado-in")
    @Blocking
    @Transactional
    public void onPedidoCreado(PedidoCreadoEvent event) {
        ProcesarPedidoCreadoCommand command = new ProcesarPedidoCreadoCommand();
        command.pedidoId = event.pedidoId;
        command.items = event.items.stream().map(item -> {
            ProcesarPedidoCreadoCommand.Item commandItem = new ProcesarPedidoCreadoCommand.Item();
            commandItem.productoId = item.productoId;
            commandItem.cantidad = item.cantidad;
            return commandItem;
        }).collect(Collectors.toList());

        procesarPedidoCreadoUseCase.procesar(command);
    }
}
