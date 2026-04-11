-- liquibase formatted sql

-- changeset ivanerofeev:0007-create-inventory-orders
CREATE TABLE inventory_orders
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    removed    BOOLEAN                  NOT NULL DEFAULT FALSE,
    client_id  UUID                     NOT NULL REFERENCES users (id),
    manager_id UUID                     NOT NULL REFERENCES users (id),
    car_id     UUID                     NOT NULL REFERENCES cars (id),
    status     VARCHAR(64)              NOT NULL
);

CREATE INDEX idx_inventory_orders_client ON inventory_orders (client_id);
CREATE INDEX idx_inventory_orders_manager ON inventory_orders (manager_id);

-- rollback DROP TABLE inventory_orders;
