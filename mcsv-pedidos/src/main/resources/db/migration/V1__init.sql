CREATE TABLE pedido (
    id BIGSERIAL PRIMARY KEY,
    cliente_id VARCHAR(100) NOT NULL,
    fecha TIMESTAMP NOT NULL DEFAULT now(),
    estado VARCHAR(20) NOT NULL
);

CREATE TABLE pedido_item (
    id BIGSERIAL PRIMARY KEY,
    pedido_id BIGINT NOT NULL REFERENCES pedido (id) ON DELETE CASCADE,
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario NUMERIC(12, 2) NOT NULL
);

CREATE INDEX idx_pedido_item_pedido ON pedido_item (pedido_id);
CREATE INDEX idx_pedido_cliente ON pedido (cliente_id);
