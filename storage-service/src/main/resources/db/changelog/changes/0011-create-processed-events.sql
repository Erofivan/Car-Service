-- liquibase formatted sql

-- changeset ivanerofeev:storage-0011-create-processed-events

CREATE TABLE processed_events
(
    id           VARCHAR(128) PRIMARY KEY,
    processed_at TIMESTAMP NOT NULL
);

-- rollback DROP TABLE processed_events;
