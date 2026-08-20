# Build multi-stage: se compila con Node (ng build, modo producción) y se sirve
# el resultado estático con Nginx — el mismo patrón de los Dockerfile de los
# microservicios (compilar en una imagen, correr en otra más liviana).
FROM node:20-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
RUN npm run build

# La app Angular es una SPA: no necesita Node en runtime, solo servir archivos
# estáticos. Nginx además resuelve el ruteo del lado del cliente (ver nginx.conf).
FROM nginx:1.27-alpine
COPY --from=build /app/dist/portal-pedidos-inventario/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
