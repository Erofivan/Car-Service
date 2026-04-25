-- liquibase formatted sql

-- changeset ivanerofeev:0011-create-model-component-options

CREATE TABLE model_component_options
(
    model_id            UUID    NOT NULL REFERENCES models (id),
    component_option_id UUID    NOT NULL REFERENCES component_options (id),
    required            BOOLEAN NOT NULL,
    PRIMARY KEY (model_id, component_option_id)
);

CREATE INDEX idx_model_component_options_model ON model_component_options (model_id);
CREATE INDEX idx_model_component_options_component ON model_component_options (component_option_id);

-- rollback DROP TABLE model_component_options;
