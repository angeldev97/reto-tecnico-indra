package com.reto.pedidos.infrastructure.adapter.in.messaging;

import com.reto.pedidos.application.ActualizarEstadoPedidoService;
import com.reto.pedidos.domain.port.in.ActualizarEstadoPedidoUseCase;
import com.reto.pedidos.domain.port.out.PedidoRepository;
import com.reto.pedidos.infrastructure.adapter.in.messaging.event.PedidoRechazadoEvent;
import com.reto.pedidos.infrastructure.adapter.in.messaging.event.StockActualizadoEvent;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EstadoPedidoConsumer {

    private static final Logger LOG = Logger.getLogger(EstadoPedidoConsumer.class);

    private final ActualizarEstadoPedidoUseCase actualizarEstadoPedidoUseCase;

    // El use case es una clase de application plana (sin CDI): este adapter, que sí
    // es un bean gestionado, inyecta el puerto de salida y lo compone a mano.
    public EstadoPedidoConsumer(PedidoRepository pedidoRepository) {
        this.actualizarEstadoPedidoUseCase = new ActualizarEstadoPedidoService(pedidoRepository);
    }

    // @Transactional hace falta aquí, aunque la única escritura (guardar()) ya
    // tenga su propio @Transactional en PanachePedidoRepository: la LECTURA previa
    // (buscarPorId, vía Panache) también necesita una sesión/transacción activa, y
    // este consumer Kafka no tiene una por defecto (a diferencia de un request REST).
    @Incoming("stock-actualizado-in")
    @Blocking
    @Transactional
    public void onStockActualizado(StockActualizadoEvent event) {
        actualizarEstadoPedidoUseCase.confirmar(event.pedidoId);
    }

    @Incoming("pedido-rechazado-in")
    @Blocking
    @Transactional
    public void onPedidoRechazado(PedidoRechazadoEvent event) {
        LOG.infof("Pedido %d rechazado: %s", event.pedidoId, event.motivo);
        actualizarEstadoPedidoUseCase.rechazar(event.pedidoId, event.motivo);
    }
}
