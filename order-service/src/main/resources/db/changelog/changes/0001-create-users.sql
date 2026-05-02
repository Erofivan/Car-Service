-- liquibase formatted sql

-- changeset ivanerofeev:order-0001-create-users

CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    removed    BOOLEAN                  NOT NULL DEFAULT FALSE,
    full_name  VARCHAR(128)             NOT NULL,
    role       VARCHAR(32)              NOT NULL
);

-- rollback DROP TABLE users;
