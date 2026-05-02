-- liquibase formatted sql

-- changeset ivanerofeev:order-0005-create-outbox-events

CREATE TABLE outbox_events
(
    id           UUID PRIMARY KEY,
    topic        VARCHAR(128) NOT NULL,
    aggregate_id UUID         NOT NULL,
    payload      JSONB        NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    published_at TIMESTAMP
);

CREATE INDEX idx_outbox_events_created_at ON outbox_events (created_at);

-- rollback DROP TABLE outbox_events;
