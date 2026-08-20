package com.reto.pedidos.infrastructure.adapter.in.rest;

import com.reto.pedidos.application.ConsultarPedidosService;
import com.reto.pedidos.application.CrearPedidoService;
import com.reto.pedidos.domain.model.Pedido;
import com.reto.pedidos.domain.port.in.ConsultarPedidosUseCase;
import com.reto.pedidos.domain.port.in.CrearPedidoUseCase;
import com.reto.pedidos.domain.port.out.CatalogoPort;
import com.reto.pedidos.domain.port.out.PedidoEventPublisher;
import com.reto.pedidos.domain.port.out.PedidoRepository;
import com.reto.pedidos.infrastructure.adapter.in.rest.dto.CrearPedidoRequest;
import com.reto.pedidos.infrastructure.adapter.in.rest.dto.PedidoResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {

    private final CrearPedidoUseCase crearPedidoUseCase;
    private final ConsultarPedidosUseCase consultarPedidosUseCase;

    // Los use cases son clases de application planas (sin CDI): este adapter, que sí
    // es un bean gestionado, inyecta los puertos de salida y los compone a mano.
    public PedidoResource(PedidoRepository pedidoRepository, PedidoEventPublisher eventPublisher, CatalogoPort catalogoPort) {
        this.crearPedidoUseCase = new CrearPedidoService(pedidoRepository, eventPublisher, catalogoPort);
        this.consultarPedidosUseCase = new ConsultarPedidosService(pedidoRepository);
    }

    @GET
    public List<PedidoResponse> listar(@QueryParam("clienteId") String clienteId) {
        return consultarPedidosUseCase.listar(clienteId).stream()
                .map(PedidoRestMapper::toResponse)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public PedidoResponse obtener(@PathParam("id") Long id) {
        return PedidoRestMapper.toResponse(consultarPedidosUseCase.obtener(id));
    }

    @POST
    public Response crear(@Valid CrearPedidoRequest request) {
        Pedido creado = crearPedidoUseCase.crear(PedidoRestMapper.toCommand(request));
        return Response.status(Response.Status.CREATED).entity(PedidoRestMapper.toResponse(creado)).build();
    }
}
