# Portal de Pedidos e Inventario — Frontend Angular

Portal web (Angular 19) del **Reto 2: Sistema de Pedidos e Inventario**.
Es la capa **Portal Angular** de la arquitectura: consume los microservicios
(MCSV Pedidos y MCSV Inventario) a través del **API Gateway (Quarkus)**.

> Estado actual: el portal funciona con **datos simulados (mock) en memoria**.
> No requiere backend para ejecutarse. Cuando los microservicios estén listos,
> solo hay que activar las llamadas HttpClient (ver *Conectar el backend real*).

## Funcionalidades

1. **Ver catálogo** de productos disponibles, con stock y filtro por categoría.
2. **Crear nuevo pedido** desde un carrito.
3. **Consultar mis pedidos y su estado** (lista que se actualiza sola).
4. **Ver el detalle** de un pedido y sus productos.

## Simulación del flujo por eventos (Kafka)

El estado de un pedido reproduce el flujo de la arquitectura:

```
CREADO  ─(el inventario valida el stock de forma asíncrona)─►  CONFIRMADO
                                                            └─►  RECHAZADO (sin stock)
```

- Al confirmar, el pedido nace en **CREADO** (equivale a persistir + publicar `PEDIDO_CREADO`).
- Tras 2–4 s, el `InventarioService` "consume" el evento, valida y descuenta stock,
  y el pedido pasa a **CONFIRMADO** (`STOCK_ACTUALIZADO`) o **RECHAZADO** (`PEDIDO_RECHAZADO`).
- Como el estado vive en *signals*, las pantallas "Mis pedidos" y "Detalle" se
  actualizan automáticamente cuando llega el resultado.

Para ver un **rechazo**: en *Crear pedido*, sube la cantidad de un producto por
encima de su stock (aparece un aviso ⚠) y confirma.

## Cómo ejecutar

```bash
npm install
npm start        # ng serve  →  http://localhost:4200
```

## Estructura

```
src/app/
├─ models/                 Producto, Pedido, EstadoPedido...
├─ services/
│  ├─ inventario.service.ts   Catálogo + validar/descontar stock (mock MCSV Inventario)
│  ├─ carrito.service.ts      Carrito compartido (estado del front)
│  └─ pedido.service.ts       Crear/listar pedidos + simulación async (mock MCSV Pedidos)
└─ components/
   ├─ catalogo/            Ver catálogo
   ├─ crear-pedido/        Crear nuevo pedido
   ├─ mis-pedidos/         Consultar pedidos + estado
   └─ detalle-pedido/      Ver detalle del pedido
```

## Conectar el backend real

1. Ajusta la URL del Gateway en `src/environments/environment.ts` (`apiGatewayUrl`).
2. En `inventario.service.ts` y `pedido.service.ts`, reemplaza los cuerpos mock por
   llamadas `HttpClient` (ya está provisto en `app.config.ts`). Endpoints esperados:

   | Acción                | Método / Ruta (vía Gateway) |
   |-----------------------|-----------------------------|
   | Listar productos      | `GET /productos`            |
   | Crear pedido          | `POST /pedidos`             |
   | Listar pedidos        | `GET /pedidos`              |
   | Consultar pedido      | `GET /pedidos/{id}`         |

3. La transición de estado dejará de simularse con temporizador: vendrá del backend
   (por ejemplo, refrescando `GET /pedidos/{id}` o vía WebSocket/SSE cuando el
   MCSV Pedidos consuma `STOCK_ACTUALIZADO`).
