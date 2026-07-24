package com.reto.inventario.messaging;

import com.reto.inventario.model.MovimientoInventario;
import com.reto.inventario.model.Producto;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class PedidoCreadoConsumer {

    private static final Logger LOG = Logger.getLogger(PedidoCreadoConsumer.class);

    @Inject
    @Channel("stock-actualizado-out")
    Emitter<StockActualizadoEvent> stockActualizadoEmitter;

    @Inject
    @Channel("pedido-rechazado-out")
    Emitter<PedidoRechazadoEvent> pedidoRechazadoEmitter;

    @Incoming("pedido-creado-in")
    @Blocking
    @Transactional
    public void onPedidoCreado(PedidoCreadoEvent event) {
        Map<Long, Producto> productosPorId = new HashMap<>();

        for (PedidoCreadoEvent.Item item : event.items) {
            Producto producto = Producto.<Producto>findByIdOptional(item.productoId).orElse(null);
            if (producto == null || producto.stock < item.cantidad) {
                LOG.infof("Pedido %d rechazado: stock insuficiente para producto %d", event.pedidoId, item.productoId);
                pedidoRechazadoEmitter.send(new PedidoRechazadoEvent(event.pedidoId,
                        "Stock insuficiente para el producto " + item.productoId));
                return;
            }
            productosPorId.put(item.productoId, producto);
        }

        for (PedidoCreadoEvent.Item item : event.items) {
            Producto producto = productosPorId.get(item.productoId);
            producto.stock -= item.cantidad;

            MovimientoInventario movimiento = new MovimientoInventario();
            movimiento.producto = producto;
            movimiento.pedidoId = event.pedidoId;
            movimiento.cantidad = item.cantidad;
            movimiento.tipo = MovimientoInventario.Tipo.SALIDA;
            movimiento.persist();
        }

        LOG.infof("Pedido %d: stock actualizado", event.pedidoId);
        stockActualizadoEmitter.send(new StockActualizadoEvent(event.pedidoId));
    }
}
