package com.reto.gateway.resource;

import com.reto.gateway.client.InventarioClient;
import com.reto.gateway.client.PedidosClient;
import com.reto.gateway.dto.CrearPedidoBackendRequest;
import com.reto.gateway.dto.CrearPedidoFrontendRequest;
import com.reto.gateway.dto.PedidoBackendDto;
import com.reto.gateway.dto.PedidoFrontendDto;
import com.reto.gateway.dto.ProductoDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Path("/api/pedidos")
@Produces(MediaType.APPLICATION_JSON)
public class PedidoGatewayResource {

    private static final Logger LOG = Logger.getLogger(PedidoGatewayResource.class);

    @RestClient
    PedidosClient pedidosClient;

    @RestClient
    InventarioClient inventarioClient;

    @GET
    public List<PedidoFrontendDto> listar(@QueryParam("clienteId") String clienteId) {
        Map<Long, ProductoDto> catalogo = obtenerCatalogo();
        return pedidosClient.listar(clienteId).stream()
                .map(pedido -> PedidoMapper.toFrontend(pedido, catalogo))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public PedidoFrontendDto obtener(@PathParam("id") Long id) {
        PedidoBackendDto pedido = pedidosClient.obtener(id);
        return PedidoMapper.toFrontend(pedido, obtenerCatalogo());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crear(@Valid CrearPedidoFrontendRequest request) {
        Map<Long, ProductoDto> catalogo = obtenerCatalogo();

        List<CrearPedidoBackendRequest.Item> items = request.items.stream()
                .map(item -> {
                    ProductoDto producto = catalogo.get(item.productoId);
                    if (producto == null) {
                        throw new BadRequestException("Producto " + item.productoId + " no existe");
                    }
                    return new CrearPedidoBackendRequest.Item(item.productoId, item.cantidad, producto.precio);
                })
                .collect(Collectors.toList());

        CrearPedidoBackendRequest backendRequest = new CrearPedidoBackendRequest(request.cliente, items);
        PedidoBackendDto creado = pedidosClient.crear(backendRequest);

        return Response.status(Response.Status.CREATED)
                .entity(PedidoMapper.toFrontend(creado, catalogo))
                .build();
    }

    private Map<Long, ProductoDto> obtenerCatalogo() {
        try {
            return inventarioClient.listar().stream()
                    .collect(Collectors.toMap(p -> p.id, Function.identity()));
        } catch (RuntimeException e) {
            LOG.warn("No se pudo obtener el catálogo de mcsv-inventario", e);
            return Map.of();
        }
    }
}
