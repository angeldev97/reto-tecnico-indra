# Portal de Pedidos e Inventario — Frontend Angular

Portal web (Angular 19, standalone components + signals) del **Reto 2:
Sistema de Pedidos e Inventario**. Consume el backend real a través de
**Kong (API Gateway)** — no usa datos simulados.

Ver también: [README raíz](../../README.md) · [docs/ARQUITECTURA.md](../../docs/ARQUITECTURA.md)

## Funcionalidades

1. **Ver catálogo** de productos disponibles, con stock y filtro por categoría.
2. **Crear nuevo pedido** desde un carrito.
3. **Consultar mis pedidos y su estado** (lista que se actualiza sola).
4. **Ver el detalle** de un pedido y sus productos.

## Cómo se resuelve el flujo por eventos (Kafka) en la UI

El estado de un pedido refleja el flujo real de la arquitectura, no una
simulación:

```
CREADO  ─(mcsv-inventario valida el stock de forma asíncrona vía Kafka)─►  CONFIRMADO
                                                                        └─►  RECHAZADO (sin stock)
```

- Al confirmar el carrito, `PedidoService.crearPedido()` hace
  `POST /api/pedidos` (a través de Kong) sin precio — `mcsv-pedidos` lo
  resuelve contra `mcsv-inventario`; el pedido nace en `CREADO`.
- `PedidoService` también arma localmente `nombreProducto`, `subtotal` y
  `total` cruzando la respuesta con el catálogo (`InventarioService`), porque
  Kong solo enruta y no compone datos entre microservicios.
- Como el backend no expone WebSocket/SSE, `PedidoService` hace un *polling*
  corto (cada 1.5 s, ver `INTERVALO_POLL_MS` en
  [`pedido.service.ts`](src/app/services/pedido.service.ts)) sobre
  `GET /api/pedidos/{id}` mientras el pedido siga `CREADO`.
- El resultado (`CONFIRMADO` o `RECHAZADO`, con `motivoRechazo` si aplica)
  actualiza una signal local, y las pantallas "Mis pedidos" y "Detalle" se
  refrescan solas al ser reactivas a esa signal.

## Estructura

```
src/app/
├─ models/                 Producto, Pedido, EstadoPedido...
├─ services/
│  ├─ inventario.service.ts   Catálogo (GET /api/productos vía HttpClient)
│  ├─ carrito.service.ts      Carrito compartido (estado del front, en memoria)
│  └─ pedido.service.ts       Crear/listar pedidos + polling de resolución
└─ components/
   ├─ catalogo/            Ver catálogo
   ├─ crear-pedido/        Crear nuevo pedido
   ├─ mis-pedidos/         Consultar pedidos + estado
   └─ detalle-pedido/      Ver detalle del pedido
```

## Cómo ejecutar

**En Docker** (como parte del stack completo, ver [README raíz](../../README.md)):

```bash
docker compose up -d --build frontend
```

Build multi-stage: `node:20-alpine` compila (`ng build`, modo producción) y
`nginx:1.27-alpine` sirve el resultado estático en el puerto 80 del
contenedor (publicado como `4200:80`). Ver [Dockerfile](Dockerfile) y
[nginx.conf](nginx.conf) (el `try_files ... /index.html` es necesario para
que el ruteo del lado del cliente de Angular funcione al recargar cualquier
ruta que no sea `/`).

**En modo desarrollo local** (con *live reload*):

```bash
npm install
npm start        # ng serve → http://localhost:4200
```

En ambos casos apunta a Kong, definido en
[`src/environments/environment.ts`](src/environments/environment.ts)
(`apiGatewayUrl: 'http://localhost:8080'`) — no requiere ningún cambio entre
correr en Docker o en local, porque esa URL la resuelve el navegador del
usuario, no el contenedor (detalle en
[docs/ARQUITECTURA.md](../../docs/ARQUITECTURA.md#arquitectura-de-contenedores)).
