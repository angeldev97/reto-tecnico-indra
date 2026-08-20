package com.reto.pedidos.infrastructure.adapter.in.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CrearPedidoRequest {

    @NotBlank
    public String clienteId;

    @NotEmpty
    public List<@Valid ItemRequest> items;

    public static class ItemRequest {

        @NotNull
        public Long productoId;

        @Min(1)
        public int cantidad;
    }
}
