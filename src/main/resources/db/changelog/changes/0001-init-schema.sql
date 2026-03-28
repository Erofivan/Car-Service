-- liquibase formatted sql

-- changeset ivanerofeev:0001-init-schema
CREATE TABLE brands
(
	id         UUID PRIMARY KEY,
	created_at TIMESTAMP WITH TIME ZONE NOT NULL,
	updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
	removed    BOOLEAN                  NOT NULL DEFAULT FALSE,
	code       VARCHAR(64)              NOT NULL UNIQUE,
	name       VARCHAR(128)             NOT NULL
);

CREATE TABLE models
(
	id         UUID PRIMARY KEY,
	created_at TIMESTAMP WITH TIME ZONE NOT NULL,
	updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
	removed    BOOLEAN                  NOT NULL DEFAULT FALSE,
	brand_id   UUID                     NOT NULL REFERENCES brands (id),
	code       VARCHAR(64)              NOT NULL UNIQUE,
	name       VARCHAR(128)             NOT NULL,
	base_price BIGINT                   NOT NULL
);

CREATE TABLE cars
(
	id                UUID PRIMARY KEY,
	created_at        TIMESTAMP WITH TIME ZONE NOT NULL,
	updated_at        TIMESTAMP WITH TIME ZONE NOT NULL,
	removed           BOOLEAN                  NOT NULL DEFAULT FALSE,
	model_id          UUID                     NOT NULL REFERENCES models (id),
	body_type         VARCHAR(64)              NOT NULL,
	fuel_type         VARCHAR(64)              NOT NULL,
	power_hp          INTEGER                  NOT NULL,
	engine_litres     DOUBLE PRECISION         NOT NULL,
	transmission      VARCHAR(64)              NOT NULL,
	drivetrain        VARCHAR(64)              NOT NULL,
	color             VARCHAR(64)              NOT NULL,
	price             BIGINT                   NOT NULL,
	available         BOOLEAN                  NOT NULL,
	test_drive_enabled BOOLEAN                 NOT NULL
);

CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    removed    BOOLEAN                  NOT NULL DEFAULT FALSE,
    full_name  VARCHAR(128)             NOT NULL,
    role       VARCHAR(32)              NOT NULL
);

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

CREATE TABLE part_model_compatibility
(
    part_id  UUID NOT NULL REFERENCES parts (id),
    model_id UUID NOT NULL REFERENCES models (id),
    PRIMARY KEY (part_id, model_id)
);

CREATE TABLE inventory_orders
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    removed    BOOLEAN                  NOT NULL DEFAULT FALSE,
    client_id  UUID                     NOT NULL REFERENCES users (id),
    manager_id UUID                     NOT NULL REFERENCES users (id),
    car_id     UUID                     NOT NULL REFERENCES cars (id),
    status     VARCHAR(64)              NOT NULL
);

CREATE TABLE custom_orders
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    removed     BOOLEAN                  NOT NULL DEFAULT FALSE,
    client_id   UUID                     NOT NULL REFERENCES users (id),
    manager_id  UUID                     NOT NULL REFERENCES users (id),
    model_id    UUID                     NOT NULL REFERENCES models (id),
    status      VARCHAR(64)              NOT NULL,
    total_price BIGINT                   NOT NULL
);

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

CREATE TABLE model_component_options
(
	model_id            UUID    NOT NULL REFERENCES models (id),
	component_option_id UUID    NOT NULL REFERENCES component_options (id),
	required            BOOLEAN NOT NULL,
	PRIMARY KEY (model_id, component_option_id)
);

CREATE INDEX idx_models_brand_id ON models (brand_id);
CREATE INDEX idx_cars_model_id ON cars (model_id);
CREATE INDEX idx_model_component_options_model ON model_component_options (model_id);
CREATE INDEX idx_model_component_options_component ON model_component_options (component_option_id);
CREATE INDEX idx_inventory_orders_client ON inventory_orders (client_id);
CREATE INDEX idx_inventory_orders_manager ON inventory_orders (manager_id);
CREATE INDEX idx_custom_orders_client ON custom_orders (client_id);
CREATE INDEX idx_custom_orders_manager ON custom_orders (manager_id);
CREATE INDEX idx_test_drive_requests_client ON test_drive_requests (client_id);
CREATE INDEX idx_test_drive_requests_car ON test_drive_requests (car_id);

INSERT INTO brands (id, created_at, updated_at, removed, code, name)
VALUES ('10000000-0000-0000-0000-000000000001', now(), now(), FALSE, 'BMW', 'BMW'),
	   ('10000000-0000-0000-0000-000000000002', now(), now(), FALSE, 'AUDI', 'Audi'),
	   ('10000000-0000-0000-0000-000000000003', now(), now(), FALSE, 'MERCEDES', 'Mercedes-Benz');

INSERT INTO models (id, created_at, updated_at, removed, brand_id, code, name, base_price)
VALUES ('20000000-0000-0000-0000-000000000001', now(), now(), FALSE, '10000000-0000-0000-0000-000000000001',
		'BMW-320I', '320i', 4000000),
	   ('20000000-0000-0000-0000-000000000002', now(), now(), FALSE, '10000000-0000-0000-0000-000000000001',
		'BMW-330I', '330i', 4500000),
	   ('20000000-0000-0000-0000-000000000003', now(), now(), FALSE, '10000000-0000-0000-0000-000000000001',
		'BMW-M340I', 'M340i', 5300000),
	   ('20000000-0000-0000-0000-000000000004', now(), now(), FALSE, '10000000-0000-0000-0000-000000000002',
		'AUDI-A4', 'A4', 3800000),
	   ('20000000-0000-0000-0000-000000000005', now(), now(), FALSE, '10000000-0000-0000-0000-000000000003',
		'MERCEDES-E300', 'E300', 4900000);

	INSERT INTO cars (id, created_at, updated_at, removed, model_id, body_type, fuel_type, power_hp, engine_litres,
			  transmission, drivetrain, color, price, available, test_drive_enabled)
	VALUES ('40000000-0000-0000-0000-000000000001', now(), now(), FALSE, '20000000-0000-0000-0000-000000000001',
		'Sedan', 'Petrol', 184, 2.0, 'Automatic 8AT', 'Rear', 'Black', 4200000, TRUE, TRUE),
	       ('40000000-0000-0000-0000-000000000002', now(), now(), FALSE, '20000000-0000-0000-0000-000000000002',
		'Sedan', 'Petrol', 245, 2.0, 'Automatic 8AT', 'Rear', 'White', 4700000, TRUE, TRUE),
	       ('40000000-0000-0000-0000-000000000003', now(), now(), FALSE, '20000000-0000-0000-0000-000000000004',
		'Sedan', 'Petrol', 204, 2.0, 'Automatic 8AT', 'Front', 'Gray', 3950000, TRUE, FALSE);

	INSERT INTO component_options (id, created_at, updated_at, removed, slot_name, name, surcharge)
	VALUES ('30000000-0000-0000-0000-000000000001', now(), now(), FALSE, 'Wheels', '18'' Aero', 120000),
	       ('30000000-0000-0000-0000-000000000002', now(), now(), FALSE, 'Transmission', 'Automatic 8AT', 160000),
	       ('30000000-0000-0000-0000-000000000003', now(), now(), FALSE, 'Interior', 'Leather Dakota', 210000);

	INSERT INTO model_component_options (model_id, component_option_id, required)
	VALUES ('20000000-0000-0000-0000-000000000001', '30000000-0000-0000-0000-000000000001', FALSE),
	       ('20000000-0000-0000-0000-000000000001', '30000000-0000-0000-0000-000000000002', TRUE),
	       ('20000000-0000-0000-0000-000000000001', '30000000-0000-0000-0000-000000000003', FALSE),
	       ('20000000-0000-0000-0000-000000000002', '30000000-0000-0000-0000-000000000001', FALSE),
	       ('20000000-0000-0000-0000-000000000002', '30000000-0000-0000-0000-000000000002', TRUE);

INSERT INTO users (id, created_at, updated_at, removed, full_name, role)
VALUES ('50000000-0000-0000-0000-000000000001', now(), now(), FALSE, 'Иван Клиентов', 'CLIENT'),
       ('50000000-0000-0000-0000-000000000002', now(), now(), FALSE, 'Пётр Менеджеров', 'MANAGER'),
       ('50000000-0000-0000-0000-000000000003', now(), now(), FALSE, 'Склад Складов', 'WAREHOUSE_ADMIN'),
       ('50000000-0000-0000-0000-000000000004', now(), now(), FALSE, 'Админ Системный', 'SYSTEM_ADMIN');

INSERT INTO parts (id, created_at, updated_at, removed, name, description, price)
VALUES ('60000000-0000-0000-0000-000000000001', now(), now(), FALSE, 'Тормозные колодки', 'Передние керамические', 15000),
       ('60000000-0000-0000-0000-000000000002', now(), now(), FALSE, 'Масляный фильтр', 'Оригинал', 3500);

INSERT INTO part_model_compatibility (part_id, model_id)
VALUES ('60000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000001'),
       ('60000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000002'),
       ('60000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000001');
