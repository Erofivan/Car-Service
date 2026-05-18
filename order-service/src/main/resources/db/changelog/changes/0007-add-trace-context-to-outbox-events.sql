-- liquibase formatted sql

-- changeset ivanerofeev:order-0007-add-trace-context-to-outbox-events

ALTER TABLE outbox_events
    ADD COLUMN trace_id VARCHAR(64);

ALTER TABLE outbox_events
    ADD COLUMN span_id VARCHAR(32);

CREATE INDEX idx_outbox_events_trace_id ON outbox_events (trace_id);

-- rollback DROP INDEX idx_outbox_events_trace_id;
-- rollback ALTER TABLE outbox_events DROP COLUMN span_id;
-- rollback ALTER TABLE outbox_events DROP COLUMN trace_id;
