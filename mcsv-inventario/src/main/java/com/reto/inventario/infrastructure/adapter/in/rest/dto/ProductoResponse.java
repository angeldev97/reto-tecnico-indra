package com.reto.inventario.infrastructure.adapter.in.rest.dto;

import java.math.BigDecimal;

public class ProductoResponse {
    public Long id;
    public String nombre;
    public String descripcion;
    public BigDecimal precio;
    public int stock;
    public String sku;
    public String marca;
    public String categoria;
}
