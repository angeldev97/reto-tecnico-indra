package com.reto.pedidos.resource;

import com.reto.pedidos.dto.CrearPedidoRequest;
import com.reto.pedidos.messaging.PedidoCreadoEvent;
import com.reto.pedidos.model.EstadoPedido;
import com.reto.pedidos.model.Pedido;
import com.reto.pedidos.model.PedidoItem;
import io.quarkus.narayana.jta.QuarkusTransaction;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.List;
import java.util.stream.Collectors;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {

    @Inject
    @Channel("pedido-creado-out")
    Emitter<PedidoCreadoEvent> pedidoCreadoEmitter;

    @GET
    public List<Pedido> listar(@QueryParam("clienteId") String clienteId) {
        if (clienteId != null && !clienteId.isBlank()) {
            return Pedido.list("clienteId", clienteId);
        }
        return Pedido.listAll();
    }

    @GET
    @Path("/{id}")
    public Pedido obtener(@PathParam("id") Long id) {
        return Pedido.<Pedido>findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Pedido " + id + " no encontrado"));
    }

    @POST
    public Response crear(@Valid CrearPedidoRequest request) {
        Pedido pedido = QuarkusTransaction.requiringNew().call(() -> {
            Pedido nuevo = new Pedido();
            nuevo.clienteId = request.clienteId;
            nuevo.estado = EstadoPedido.CREADO;

            for (CrearPedidoRequest.ItemRequest itemRequest : request.items) {
                PedidoItem item = new PedidoItem();
                item.pedido = nuevo;
                item.productoId = itemRequest.productoId;
                item.cantidad = itemRequest.cantidad;
                item.precioUnitario = itemRequest.precioUnitario;
                nuevo.items.add(item);
            }

            nuevo.persist();
            return nuevo;
        });

        List<PedidoCreadoEvent.Item> eventItems = pedido.items.stream()
                .map(item -> new PedidoCreadoEvent.Item(item.productoId, item.cantidad))
                .collect(Collectors.toList());
        pedidoCreadoEmitter.send(new PedidoCreadoEvent(pedido.id, pedido.clienteId, eventItems));

        return Response.status(Response.Status.CREATED).entity(pedido).build();
    }
}
