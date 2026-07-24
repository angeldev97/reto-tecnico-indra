package com.reto.gateway.resource;

import com.reto.gateway.client.InventarioClient;
import com.reto.gateway.dto.ProductoDto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/api/productos")
@Produces(MediaType.APPLICATION_JSON)
public class ProductoGatewayResource {

    @RestClient
    InventarioClient inventarioClient;

    @GET
    public List<ProductoDto> listar() {
        return inventarioClient.listar();
    }

    @GET
    @Path("/{id}")
    public ProductoDto obtener(@PathParam("id") Long id) {
        return inventarioClient.obtener(id);
    }
}
