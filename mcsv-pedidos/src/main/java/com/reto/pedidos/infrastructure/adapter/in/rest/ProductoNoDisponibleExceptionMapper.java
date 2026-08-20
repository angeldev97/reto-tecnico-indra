package com.reto.pedidos.infrastructure.adapter.in.rest;

import com.reto.pedidos.domain.exception.ProductoNoDisponibleException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ProductoNoDisponibleExceptionMapper implements ExceptionMapper<ProductoNoDisponibleException> {

    @Override
    public Response toResponse(ProductoNoDisponibleException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }
}
