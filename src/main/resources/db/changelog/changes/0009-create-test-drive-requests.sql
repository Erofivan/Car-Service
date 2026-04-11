-- liquibase formatted sql

-- changeset ivanerofeev:0009-create-test-drive-requests
CREATE TABLE test_drive_requests
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    removed    BOOLEAN                  NOT NULL DEFAULT FALSE,
    client_id  UUID                     NOT NULL REFERENCES users (id),
    car_id     UUID                     NOT NULL REFERENCES cars (id),
    starts_at  TIMESTAMP                NOT NULL
);

CREATE INDEX idx_test_drive_requests_client ON test_drive_requests (client_id);
CREATE INDEX idx_test_drive_requests_car ON test_drive_requests (car_id);

-- rollback DROP TABLE test_drive_requests;
