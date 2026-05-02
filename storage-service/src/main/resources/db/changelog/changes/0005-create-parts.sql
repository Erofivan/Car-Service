-- liquibase formatted sql

-- changeset ivanerofeev:0005-create-parts

CREATE TABLE parts
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    removed     BOOLEAN                  NOT NULL DEFAULT FALSE,
    name        VARCHAR(128)             NOT NULL,
    description VARCHAR(512),
    price       BIGINT                   NOT NULL
);

-- rollback DROP TABLE parts;
