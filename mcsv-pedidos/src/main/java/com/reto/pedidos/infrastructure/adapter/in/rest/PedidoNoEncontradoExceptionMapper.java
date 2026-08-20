package com.reto.pedidos.infrastructure.adapter.in.rest;

import com.reto.pedidos.domain.exception.PedidoNoEncontradoException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PedidoNoEncontradoExceptionMapper implements ExceptionMapper<PedidoNoEncontradoException> {

    // Sin cuerpo en la respuesta a propósito: preserva el comportamiento original,
    // que lanzaba jakarta.ws.rs.NotFoundException directo (404 sin body).
    @Override
    public Response toResponse(PedidoNoEncontradoException exception) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
