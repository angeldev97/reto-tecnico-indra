package com.reto.pedidos.infrastructure.adapter.out.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "pedido_item")
public class PedidoItemEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id")
    public PedidoEntity pedido;

    @Column(name = "producto_id", nullable = false)
    public Long productoId;

    @Column(nullable = false)
    public int cantidad;

    @Column(name = "precio_unitario", nullable = false)
    public BigDecimal precioUnitario;
}
