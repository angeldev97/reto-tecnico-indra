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
| pedidos-db | 5432 | PostgreSQL, base `pedidos` |
| inventario-db | 5433 | PostgreSQL, base `inventario` |

## Estado del proyecto

- [x] Fase 0 — Infraestructura base (docker-compose, repo)
- [ ] Fase 1 — mcsv-inventario (modelo + endpoints de lectura)
- [ ] Fase 2 — mcsv-pedidos (crear/consultar pedidos)
- [ ] Fase 3 — Integración Kafka (pedido-creado → validación de stock)
- [ ] Fase 4 — mcsv-pedidos consume eventos de stock/rechazo
- [ ] Fase 5 — api-gateway
- [ ] Fase 6 — Integración con frontend Angular
- [ ] Fase 7 — Tests
- [ ] Fase 8 — Documentación final y push al repositorio remoto
