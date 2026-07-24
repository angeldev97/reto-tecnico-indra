CREATE TABLE producto (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(500),
    precio NUMERIC(12, 2) NOT NULL,
    stock_disponible INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE movimiento_inventario (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL REFERENCES producto (id),
    pedido_id BIGINT,
    cantidad INTEGER NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_movimiento_producto ON movimiento_inventario (producto_id);

INSERT INTO producto (nombre, descripcion, precio, stock_disponible) VALUES
    ('Teclado mecánico', 'Teclado mecánico switches rojos, retroiluminado', 189.90, 50),
    ('Mouse inalámbrico', 'Mouse óptico inalámbrico 2.4GHz', 79.90, 120),
    ('Monitor 24"', 'Monitor LED Full HD 24 pulgadas', 649.00, 25),
    ('Audífonos USB', 'Audífonos con micrófono y cancelación de ruido', 149.50, 0),
    ('Webcam Full HD', 'Webcam 1080p con enfoque automático', 129.90, 40);
