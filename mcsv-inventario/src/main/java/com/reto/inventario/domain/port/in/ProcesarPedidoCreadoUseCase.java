package com.reto.inventario.domain.port.in;

import com.reto.inventario.domain.port.in.command.ProcesarPedidoCreadoCommand;

// Puerto de entrada (hexagonal): invocado por el adapter Kafka (PedidoCreadoConsumer)
// e implementado por ProcesarPedidoCreadoService, que concentra toda la regla de
// negocio de validación/descuento de stock.
public interface ProcesarPedidoCreadoUseCase {

    void procesar(ProcesarPedidoCreadoCommand command);
}
