-- liquibase formatted sql

-- changeset ivanerofeev:0001-create-brands

CREATE TABLE brands
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    removed    BOOLEAN                  NOT NULL DEFAULT FALSE,
    code       VARCHAR(64)              NOT NULL UNIQUE,
    name       VARCHAR(128)             NOT NULL
);

-- rollback DROP TABLE brands;
