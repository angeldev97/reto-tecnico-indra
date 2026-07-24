package com.reto.inventario.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "producto")
public class Producto extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String nombre;

    public String descripcion;

    @Column(nullable = false)
    public BigDecimal precio;

    @Column(nullable = false)
    public int stock;

    @Column(nullable = false)
    public String sku;

    @Column(nullable = false)
    public String marca;

    @Column(nullable = false)
    public String categoria;
}
