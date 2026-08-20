package com.reto.inventario.infrastructure.adapter.in.rest;

import com.reto.inventario.domain.exception.ProductoNoEncontradoException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ProductoNoEncontradoExceptionMapper implements ExceptionMapper<ProductoNoEncontradoException> {

    // Sin cuerpo en la respuesta a propósito: preserva el comportamiento original,
    // que lanzaba jakarta.ws.rs.NotFoundException directo (404 sin body).
    @Override
    public Response toResponse(ProductoNoEncontradoException exception) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
