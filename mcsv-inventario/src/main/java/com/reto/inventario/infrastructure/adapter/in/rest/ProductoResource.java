package com.reto.inventario.infrastructure.adapter.in.rest;

import com.reto.inventario.application.ConsultarProductosService;
import com.reto.inventario.domain.port.in.ConsultarProductosUseCase;
import com.reto.inventario.domain.port.out.ProductoRepository;
import com.reto.inventario.infrastructure.adapter.in.rest.dto.ProductoResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.stream.Collectors;

@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
public class ProductoResource {

    private final ConsultarProductosUseCase consultarProductosUseCase;

    // El use case es una clase de application plana (sin CDI): este adapter, que sí
    // es un bean gestionado, inyecta el puerto de salida y lo compone a mano.
    public ProductoResource(ProductoRepository productoRepository) {
        this.consultarProductosUseCase = new ConsultarProductosService(productoRepository);
    }

    @GET
    public List<ProductoResponse> listar() {
        return consultarProductosUseCase.listar().stream()
                .map(ProductoRestMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public ProductoResponse obtener(@PathParam("id") Long id) {
        return ProductoRestMapper.toResponse(consultarProductosUseCase.obtener(id));
    }
}
