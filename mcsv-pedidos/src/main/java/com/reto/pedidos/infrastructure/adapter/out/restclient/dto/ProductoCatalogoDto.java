package com.reto.pedidos.infrastructure.adapter.out.restclient.dto;

import java.math.BigDecimal;

// Forma en la que mcsv-inventario expone un producto; solo se mapean los campos que
// mcsv-pedidos necesita para resolver el precio (contrato interno, no el de dominio).
public class ProductoCatalogoDto {
    public Long id;
    public BigDecimal precio;
}
