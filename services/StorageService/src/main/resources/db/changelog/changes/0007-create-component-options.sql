-- liquibase formatted sql

-- changeset ivanerofeev:0010-create-component-options

CREATE TABLE component_options
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    removed    BOOLEAN                  NOT NULL DEFAULT FALSE,
    slot_name  VARCHAR(64)              NOT NULL,
    name       VARCHAR(128)             NOT NULL,
    surcharge  BIGINT                   NOT NULL
);

-- rollback DROP TABLE component_options;
