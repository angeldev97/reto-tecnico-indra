package com.reto.pedidos.infrastructure.adapter.out.restclient;

import com.reto.pedidos.domain.exception.ProductoNoDisponibleException;
import com.reto.pedidos.domain.port.out.CatalogoPort;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;

@ApplicationScoped
public class CatalogoRestAdapter implements CatalogoPort {

    @RestClient
    InventarioClient inventarioClient;

    // Cualquier falla al resolver el precio (producto inexistente, mcsv-inventario
    // caído) se traduce en "producto no disponible": no hay un precio inventado con
    // el que crear el pedido, así que la creación se rechaza en vez de degradar.
    @Override
    public BigDecimal obtenerPrecio(Long productoId) {
        try {
            return inventarioClient.obtener(productoId).precio;
        } catch (RuntimeException e) {
            throw new ProductoNoDisponibleException(productoId);
        }
    }
}
