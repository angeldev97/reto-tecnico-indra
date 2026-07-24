# Reto 2 — Sistema de Pedidos e Inventario

Arquitectura de microservicios event-driven: portal Angular, API Gateway, dos
microservicios Quarkus (Pedidos e Inventario) comunicados vía Apache Kafka, cada uno
con su propia base de datos PostgreSQL.

## Arquitectura

Ver [docs/arquitectura.png](docs/arquitectura.png).

- **frontend**: portal Angular (catálogo, crear pedido, mis pedidos).
- **api-gateway**: punto único de entrada, expone REST y reenvía a los microservicios.
- **mcsv-pedidos**: crea y consulta pedidos, publica el evento `pedido-creado`.
- **mcsv-inventario**: consume `pedido-creado`, valida y descuenta stock, publica
  `stock-actualizado` o `pedido-rechazado`.

## Requisitos

- Java 21+
- Maven 3.9+
- Docker + Docker Compose
- Node.js 18+ y Angular CLI (solo para el frontend)

## Levantar la infraestructura local

```bash
docker compose up -d
```

Esto levanta:

| Servicio | Puerto host | Descripción |
|---|---|---|
| Kafka | 9092 | Broker (modo KRaft, sin Zookeeper) |
| Kafka UI | 8090 | http://localhost:8090 — inspección visual de tópicos/mensajes |
| pedidos-db | 5434 | PostgreSQL, base `pedidos` (5432 se evita: puede chocar con un PostgreSQL nativo local) |
| inventario-db | 5433 | PostgreSQL, base `inventario` |

## Servicios

Para correr los tres microservicios Quarkus en simultáneo en local, cada uno usa un
puerto distinto (por defecto todos usan 8080):

### mcsv-inventario (puerto 8081)

```bash
cd mcsv-inventario
mvn quarkus:dev -Dquarkus.http.port=8081
```

- Swagger UI en `/q/swagger-ui`
- `GET /productos` — listar productos (`id, nombre, descripcion, precio, stock, sku, marca, categoria`)
- `GET /productos/{id}` — detalle de un producto
- Requiere `inventario-db` levantada (`docker compose up -d`)

### mcsv-pedidos (puerto 8082)

```bash
cd mcsv-pedidos
mvn quarkus:dev -Dquarkus.http.port=8082
```

- Swagger UI en `/q/swagger-ui`
- `POST /pedidos` — crear pedido (`{ clienteId, items: [{ productoId, cantidad, precioUnitario }] }`)
- `GET /pedidos` — listar (filtro opcional `?clienteId=`)
- `GET /pedidos/{id}` — detalle de un pedido (incluye `motivoRechazo` si fue rechazado)
- Requiere `pedidos-db` levantada (`docker compose up -d`)

### api-gateway (puerto 8080)

```bash
cd api-gateway
mvn quarkus:dev
```

- Swagger UI en `/q/swagger-ui`
- Punto único de entrada para el frontend; adapta el contrato "simple" del cliente al
  contrato interno de cada microservicio:
  - `GET /api/productos`, `GET /api/productos/{id}` → reenvía a `mcsv-inventario` (pass-through)
  - `POST /api/pedidos` — recibe `{ cliente, items: [{ productoId, cantidad }] }` **sin precio**;
    el gateway resuelve el precio actual contra `mcsv-inventario` antes de reenviar a `mcsv-pedidos`
  - `GET /api/pedidos`, `GET /api/pedidos/{id}` — enriquece la respuesta de `mcsv-pedidos` con
    `nombreProducto`, `subtotal` y `total`, cruzando con el catálogo
  - CORS habilitado para `http://localhost:4200` (`quarkus.http.cors.enabled=true` — ojo,
    en Quarkus 3.37 la propiedad es `.enabled`, no `quarkus.http.cors=true`)
- Requiere `mcsv-inventario` (8081) y `mcsv-pedidos` (8082) corriendo

### frontend (puerto 4200)

```bash
cd frontend/angular-polluelo
npm install
npm start
```

- Portal Angular en http://localhost:4200 (catálogo, crear pedido, mis pedidos, detalle)
- Consume el `api-gateway` vía `HttpClient` (`environment.apiGatewayUrl`)
- Como no hay WebSocket/SSE, el estado CREADO → CONFIRMADO/RECHAZADO se refleja con
  *polling* corto (cada 1.5 s) sobre `GET /api/pedidos/{id}` mientras el pedido esté pendiente
- Requiere el `api-gateway` (8080) corriendo

## Estado del proyecto

- [x] Fase 0 — Infraestructura base (docker-compose, repo)
- [x] Fase 1 — mcsv-inventario (modelo + endpoints de lectura)
- [x] Fase 2 — mcsv-pedidos (crear/consultar pedidos)
- [x] Fase 3 — Integración Kafka (pedido-creado → validación de stock)
- [x] Fase 4 — mcsv-pedidos consume eventos de stock/rechazo
- [x] Fase 5 — api-gateway
- [x] Fase 6 — Integración con frontend Angular
- [ ] Fase 7 — Tests
- [ ] Fase 8 — Documentación final y push al repositorio remoto
