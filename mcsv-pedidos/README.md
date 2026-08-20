# mcsv-pedidos

Microservicio dueño del ciclo de vida del pedido: lo crea, lo consulta, y
actualiza su estado (`CREADO` → `CONFIRMADO`/`RECHAZADO`) en reacción a los
eventos que publica `mcsv-inventario`. Java 21 / Quarkus 3.37.4.

Ver también: [README raíz](../README.md) · [docs/ARQUITECTURA.md](../docs/ARQUITECTURA.md)

## Paquete hexagonal

```
src/main/java/com/reto/pedidos/
├── domain/
│   ├── model/            Pedido, PedidoItem, EstadoPedido (POJOs puros)
│   ├── port/in/           CrearPedidoUseCase, ConsultarPedidosUseCase, ActualizarEstadoPedidoUseCase
│   ├── port/out/          PedidoRepository, PedidoEventPublisher, CatalogoPort
│   └── exception/        PedidoNoEncontradoException, ProductoNoDisponibleException
├── application/
│   ├── CrearPedidoService.java
│   ├── ConsultarPedidosService.java
│   └── ActualizarEstadoPedidoService.java   reacciona a stock-actualizado / pedido-rechazado
└── infrastructure/adapter/
    ├── in/rest/           PedidoResource (GET/POST /pedidos)
    ├── in/messaging/      EstadoPedidoConsumer (@Incoming stock-actualizado-in, pedido-rechazado-in)
    ├── out/persistence/   PedidoEntity, PedidoItemEntity (Panache) + PanachePedidoRepository
    ├── out/messaging/     KafkaPedidoEventPublisher (pedido-creado-out)
    └── out/restclient/    CatalogoRestAdapter (REST client hacia mcsv-inventario)
```

`CrearPedidoService` resuelve el `precioUnitario` de cada item contra
`mcsv-inventario` (vía `CatalogoPort` — el cliente nunca lo manda, así no es
manipulable), persiste el pedido (estado `CREADO`) y publica `pedido-creado`
**después** de que la persistencia confirma — nunca antes, para no anunciar
un pedido que no llegó a guardarse. Si el producto no existe o
`mcsv-inventario` no responde, la creación se rechaza
(`ProductoNoDisponibleException` → 400) en vez de inventar un precio.
`ActualizarEstadoPedidoService` es quien mueve el pedido a `CONFIRMADO` o
`RECHAZADO` cuando llega el evento correspondiente desde `mcsv-inventario`.
Detalle completo en
[docs/ARQUITECTURA.md](../docs/ARQUITECTURA.md#decisiones-de-diseño-y-trade-offs).

## Endpoints

- `POST /pedidos` — crea un pedido (`{ clienteId, items: [{ productoId, cantidad }] }`, sin precio)
- `GET /pedidos` — lista pedidos (filtro opcional `?clienteId=`)
- `GET /pedidos/{id}` — detalle de un pedido, incluye `motivoRechazo` si fue rechazado (404 si no existe)
- Swagger UI: `/q/swagger-ui`

## Eventos Kafka

| Dirección | Canal | Topic | Evento |
|---|---|---|---|
| Salida | `pedido-creado-out` | `pedido-creado` | `PedidoCreadoEvent` |
| Entrada | `stock-actualizado-in` | `stock-actualizado` | `StockActualizadoEvent` |
| Entrada | `pedido-rechazado-in` | `pedido-rechazado` | `PedidoRechazadoEvent` |

## Cómo correrlo

**En Docker** (como parte del stack completo, ver [README raíz](../README.md)):

```bash
docker compose up -d --build mcsv-pedidos
```

**En modo desarrollo local** (requiere `pedidos-db` y `kafka` levantados vía `docker compose up -d pedidos-db kafka`):

```bash
mvn quarkus:dev -Dquarkus.http.port=8082
```

## Configuración relevante

| Propiedad | `application.properties` (dev local) | Override en Docker (env var) |
|---|---|---|
| Datasource | `jdbc:postgresql://localhost:5434/pedidos` | `QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://pedidos-db:5432/pedidos` |
| Kafka | `localhost:9092` | `KAFKA_BOOTSTRAP_SERVERS=kafka:19092` |
| REST client mcsv-inventario | `http://localhost:8081` | `QUARKUS_REST_CLIENT_MCSV_INVENTARIO_URL=http://mcsv-inventario:8080` |
