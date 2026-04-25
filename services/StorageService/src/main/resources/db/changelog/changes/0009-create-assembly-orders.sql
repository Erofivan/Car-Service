-- liquibase formatted sql

-- changeset ivanerofeev:storage-0009-create-assembly-orders

CREATE TABLE assembly_orders
(
    id              UUID PRIMARY KEY,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    removed         BOOLEAN                  NOT NULL DEFAULT FALSE,
    source_order_id UUID                     NOT NULL,
    model_code      VARCHAR(64)              NOT NULL,
    status          VARCHAR(64)              NOT NULL
);

-- rollback DROP TABLE assembly_orders;
