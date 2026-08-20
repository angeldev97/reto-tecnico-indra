package com.reto.inventario.domain.model;

import java.time.LocalDateTime;

public class MovimientoInventario {
    private Long id;
    private Long productoId;
    private Long pedidoId;
    private int cantidad;
    private TipoMovimiento tipo;
    private LocalDateTime fecha;

    public static MovimientoInventario salida(Long productoId, Long pedidoId, int cantidad) {
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.productoId = productoId;
        movimiento.pedidoId = pedidoId;
        movimiento.cantidad = cantidad;
        movimiento.tipo = TipoMovimiento.SALIDA;
        movimiento.fecha = LocalDateTime.now();
        return movimiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
