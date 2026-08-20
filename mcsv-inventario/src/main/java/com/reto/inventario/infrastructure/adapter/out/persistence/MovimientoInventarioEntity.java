package com.reto.inventario.infrastructure.adapter.out.persistence;

import com.reto.inventario.domain.model.TipoMovimiento;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento_inventario")
public class MovimientoInventarioEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(optional = false)
    public ProductoEntity producto;

    @Column(name = "pedido_id")
    public Long pedidoId;

    @Column(nullable = false)
    public int cantidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public TipoMovimiento tipo;

    @Column(nullable = false)
    public LocalDateTime fecha;
}
