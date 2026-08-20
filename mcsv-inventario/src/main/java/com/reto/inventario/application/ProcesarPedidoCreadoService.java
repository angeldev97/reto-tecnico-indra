package com.reto.inventario.application;

import com.reto.inventario.domain.model.MovimientoInventario;
import com.reto.inventario.domain.model.Producto;
import com.reto.inventario.domain.port.in.ProcesarPedidoCreadoUseCase;
import com.reto.inventario.domain.port.in.command.ProcesarPedidoCreadoCommand;
import com.reto.inventario.domain.port.out.InventarioEventPublisher;
import com.reto.inventario.domain.port.out.MovimientoInventarioRepository;
import com.reto.inventario.domain.port.out.ProductoRepository;

import java.util.HashMap;
import java.util.Map;

public class ProcesarPedidoCreadoService implements ProcesarPedidoCreadoUseCase {

    private final ProductoRepository productoRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final InventarioEventPublisher eventPublisher;

    public ProcesarPedidoCreadoService(ProductoRepository productoRepository,
                                        MovimientoInventarioRepository movimientoRepository,
                                        InventarioEventPublisher eventPublisher) {
        this.productoRepository = productoRepository;
        this.movimientoRepository = movimientoRepository;
        this.eventPublisher = eventPublisher;
    }

    // Algoritmo de dos pasadas, a propósito: primero se valida el stock de TODOS
    // los items del pedido; solo si todos pasan se ejecuta la segunda pasada que
    // descuenta stock y registra movimientos. Así nunca se deja el inventario a
    // medio descontar cuando un item a mitad de la lista no tiene stock suficiente.
    // El límite transaccional (@Transactional) vive en el adapter de mensajería que
    // invoca este método (infraestructura), no aquí: application queda en Java puro,
    // y el interceptor JTA igual envuelve todas las escrituras hechas transitivamente
    // durante esa invocación.
    @Override
    public void procesar(ProcesarPedidoCreadoCommand command) {
        Map<Long, Producto> productos = new HashMap<>();

        // Pasada 1: validar. Un producto inexistente o sin stock rechaza el pedido
        // completo de inmediato (return temprano) — es un flujo de negocio válido,
        // no una condición de error.
        for (ProcesarPedidoCreadoCommand.Item item : command.items) {
            Producto producto = productoRepository.buscarPorId(item.productoId).orElse(null);
            if (producto == null || !producto.tieneStockSuficiente(item.cantidad)) {
                eventPublisher.publicarPedidoRechazado(command.pedidoId,
                        "Stock insuficiente para el producto " + item.productoId);
                return;
            }
            productos.put(item.productoId, producto);
        }

        // Pasada 2: aplicar. Ya se sabe que todos los items tienen stock suficiente.
        for (ProcesarPedidoCreadoCommand.Item item : command.items) {
            Producto producto = productos.get(item.productoId);
            producto.descontarStock(item.cantidad);
            productoRepository.guardar(producto);
            movimientoRepository.registrar(MovimientoInventario.salida(producto.getId(), command.pedidoId, item.cantidad));
        }

        eventPublisher.publicarStockActualizado(command.pedidoId);
    }
}
