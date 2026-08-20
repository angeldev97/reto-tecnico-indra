package com.reto.pedidos.infrastructure.adapter.out.restclient;

import com.reto.pedidos.infrastructure.adapter.out.restclient.dto.ProductoCatalogoDto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "mcsv-inventario")
@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
public interface InventarioClient {

    @GET
    @Path("/{id}")
    ProductoCatalogoDto obtener(@PathParam("id") Long id);
}
