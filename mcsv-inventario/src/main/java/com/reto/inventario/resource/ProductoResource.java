package com.reto.inventario.resource;

import com.reto.inventario.model.Producto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
public class ProductoResource {

    @GET
    public List<Producto> listar() {
        return Producto.listAll();
    }

    @GET
    @Path("/{id}")
    public Producto obtener(@PathParam("id") Long id) {
        return Producto.<Producto>findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Producto " + id + " no encontrado"));
    }
}
