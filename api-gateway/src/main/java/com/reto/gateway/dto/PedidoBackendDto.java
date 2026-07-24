package com.reto.gateway.dto;

import java.math.BigDecimal;
import java.util.List;

/** Forma en la que mcsv-pedidos expone un pedido (contrato interno). */
public class PedidoBackendDto {
    public Long id;
    public String clienteId;
    public String fecha;
    public String estado;
    public String motivoRechazo;
    public List<Item> items;

    public static class Item {
        public Long id;
        public Long productoId;
        public int cantidad;
        public BigDecimal precioUnitario;
    }
}
