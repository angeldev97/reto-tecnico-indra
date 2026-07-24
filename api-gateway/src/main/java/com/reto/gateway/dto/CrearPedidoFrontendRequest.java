package com.reto.gateway.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/** Payload que envía el frontend Angular (POST /api/pedidos): sin precio. */
public class CrearPedidoFrontendRequest {

    @NotBlank
    public String cliente;

    @NotEmpty
    public List<@Valid Item> items;

    public static class Item {
        @NotNull
        public Long productoId;

        @Min(1)
        public int cantidad;
    }
}
