package com.reto.gateway.client;

import com.reto.gateway.dto.CrearPedidoBackendRequest;
import com.reto.gateway.dto.PedidoBackendDto;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "mcsv-pedidos")
@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
public interface PedidosClient {

    @GET
    List<PedidoBackendDto> listar(@QueryParam("clienteId") String clienteId);

    @GET
    @Path("/{id}")
    PedidoBackendDto obtener(@PathParam("id") Long id);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    PedidoBackendDto crear(CrearPedidoBackendRequest request);
}
