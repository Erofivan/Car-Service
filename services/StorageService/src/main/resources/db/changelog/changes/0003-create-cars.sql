-- liquibase formatted sql

-- changeset ivanerofeev:0003-create-cars

CREATE TABLE cars
(
    id                 UUID PRIMARY KEY,
    created_at         TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at         TIMESTAMP WITH TIME ZONE NOT NULL,
    removed            BOOLEAN                  NOT NULL DEFAULT FALSE,
    model_id           UUID                     NOT NULL REFERENCES models (id),
    body_type          VARCHAR(64)              NOT NULL,
    fuel_type          VARCHAR(64)              NOT NULL,
    power_hp           INTEGER                  NOT NULL,
    engine_litres      DOUBLE PRECISION         NOT NULL,
    transmission       VARCHAR(64)              NOT NULL,
    drivetrain         VARCHAR(64)              NOT NULL,
    color              VARCHAR(64)              NOT NULL,
    price              BIGINT                   NOT NULL,
    available          BOOLEAN                  NOT NULL,
    test_drive_enabled BOOLEAN                  NOT NULL
);

CREATE INDEX idx_cars_model_id ON cars (model_id);

-- rollback DROP TABLE cars;
