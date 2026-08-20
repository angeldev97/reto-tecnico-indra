# mcsv-inventario

Microservicio dueño del catálogo de productos y del stock. Valida y descuenta
inventario cuando se crea un pedido, y expone el catálogo de solo lectura —
tanto a través de Kong (`GET /api/productos` del frontend) como a
`mcsv-pedidos`, que lo consulta de forma síncrona para resolver el precio de
cada item al crear un pedido. Java 21 / Quarkus 3.37.4.

Ver también: [README raíz](../README.md) · [docs/ARQUITECTURA.md](../docs/ARQUITECTURA.md)

## Paquete hexagonal

```
src/main/java/com/reto/inventario/
├── domain/
│   ├── model/            Producto, MovimientoInventario, TipoMovimiento (POJOs puros)
│   ├── port/in/           ProcesarPedidoCreadoUseCase, ConsultarProductosUseCase
│   ├── port/out/          ProductoRepository, MovimientoInventarioRepository, InventarioEventPublisher
│   └── exception/        ProductoNoEncontradoException
├── application/
│   ├── ProcesarPedidoCreadoService.java   caso de uso con la lógica de negocio más rica del sistema
│   └── ConsultarProductosService.java
└── infrastructure/adapter/
    ├── in/rest/           ProductoResource (GET /productos)
    ├── in/messaging/      PedidoCreadoConsumer (@Incoming pedido-creado-in)
    ├── out/persistence/   ProductoEntity, MovimientoInventarioEntity (Panache) + repos
    └── out/messaging/     KafkaInventarioEventPublisher (stock-actualizado-out, pedido-rechazado-out)
```

`ProcesarPedidoCreadoService` es el corazón del servicio: por cada evento
`pedido-creado`, valida en una primera pasada si **todos** los items tienen
stock suficiente; si alguno no lo tiene, rechaza el pedido completo sin tocar
inventario. Si todos pasan, descuenta stock, registra un
`MovimientoInventario` por item y publica `stock-actualizado`. Detalle
completo en [docs/ARQUITECTURA.md](../docs/ARQUITECTURA.md#decisiones-de-diseño-y-trade-offs).

## Endpoints

- `GET /productos` — lista el catálogo completo
- `GET /productos/{id}` — detalle de un producto (404 si no existe)
- Swagger UI: `/q/swagger-ui`

## Eventos Kafka

| Dirección | Canal | Topic | Evento |
|---|---|---|---|
| Entrada | `pedido-creado-in` | `pedido-creado` | `PedidoCreadoEvent` |
| Salida | `stock-actualizado-out` | `stock-actualizado` | `StockActualizadoEvent` |
| Salida | `pedido-rechazado-out` | `pedido-rechazado` | `PedidoRechazadoEvent` |

## Cómo correrlo

**En Docker** (como parte del stack completo, ver [README raíz](../README.md)):

```bash
docker compose up -d --build mcsv-inventario
```

**En modo desarrollo local** (requiere `inventario-db` y `kafka` levantados vía `docker compose up -d inventario-db kafka`):

```bash
mvn quarkus:dev -Dquarkus.http.port=8081
```

## Configuración relevante

| Propiedad | `application.properties` (dev local) | Override en Docker (env var) |
|---|---|---|
| Datasource | `jdbc:postgresql://localhost:5433/inventario` | `QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://inventario-db:5432/inventario` |
| Kafka | `localhost:9092` | `KAFKA_BOOTSTRAP_SERVERS=kafka:19092` |
