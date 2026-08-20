package com.reto.pedidos.infrastructure.adapter.out.persistence;

import com.reto.pedidos.domain.model.EstadoPedido;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class PedidoEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "cliente_id", nullable = false)
    public String clienteId;

    @Column(nullable = false)
    public LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public EstadoPedido estado;

    @Column(name = "motivo_rechazo")
    public String motivoRechazo;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id")
    public List<PedidoItemEntity> items = new ArrayList<>();
}
