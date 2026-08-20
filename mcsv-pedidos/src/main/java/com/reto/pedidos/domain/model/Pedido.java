package com.reto.pedidos.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public class Pedido {
    private Long id;
    private String clienteId;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private String motivoRechazo;
    private List<PedidoItem> items;

    public static Pedido crear(String clienteId, List<PedidoItem> items) {
        Pedido pedido = new Pedido();
        pedido.clienteId = clienteId;
        pedido.fecha = LocalDateTime.now();
        pedido.estado = EstadoPedido.CREADO;
        pedido.items = items;
        return pedido;
    }

    public void confirmar() {
        this.estado = EstadoPedido.CONFIRMADO;
    }

    public void rechazar(String motivo) {
        this.estado = EstadoPedido.RECHAZADO;
        this.motivoRechazo = motivo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public List<PedidoItem> getItems() {
        return items;
    }

    public void setItems(List<PedidoItem> items) {
        this.items = items;
    }
}
