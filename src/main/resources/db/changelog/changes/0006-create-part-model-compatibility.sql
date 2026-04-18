-- liquibase formatted sql

-- changeset ivanerofeev:0006-create-part-model-compatibility
CREATE TABLE part_model_compatibility
(
    part_id  UUID NOT NULL REFERENCES parts (id),
    model_id UUID NOT NULL REFERENCES models (id),
    PRIMARY KEY (part_id, model_id)
);

-- rollback DROP TABLE part_model_compatibility;
