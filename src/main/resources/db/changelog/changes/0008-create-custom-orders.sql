-- liquibase formatted sql

-- changeset ivanerofeev:0008-create-custom-orders
CREATE TABLE custom_orders
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    removed     BOOLEAN                  NOT NULL DEFAULT FALSE,
    client_id   UUID                     NOT NULL REFERENCES users (id),
    manager_id  UUID                     NOT NULL REFERENCES users (id),
    model_id    UUID                     NOT NULL REFERENCES models (id),
    status      VARCHAR(64)              NOT NULL,
    total_price BIGINT                   NOT NULL
);

CREATE INDEX idx_custom_orders_client ON custom_orders (client_id);
CREATE INDEX idx_custom_orders_manager ON custom_orders (manager_id);

-- rollback DROP TABLE custom_orders;
