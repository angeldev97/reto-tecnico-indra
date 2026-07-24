package com.reto.gateway.client;

import com.reto.gateway.dto.ProductoDto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "mcsv-inventario")
@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
public interface InventarioClient {

    @GET
    List<ProductoDto> listar();

    @GET
    @Path("/{id}")
    ProductoDto obtener(@PathParam("id") Long id);
}
