-- liquibase formatted sql

-- changeset ivanerofeev:0002-create-models

CREATE TABLE models
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    removed    BOOLEAN                  NOT NULL DEFAULT FALSE,
    brand_id   UUID                     NOT NULL REFERENCES brands (id),
    code       VARCHAR(64)              NOT NULL UNIQUE,
    name       VARCHAR(128)             NOT NULL,
    base_price BIGINT                   NOT NULL
);

CREATE INDEX idx_models_brand_id ON models (brand_id);

-- rollback DROP TABLE models;
