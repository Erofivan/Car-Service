-- liquibase formatted sql

-- changeset ivanerofeev:order-0006-seed-data

INSERT INTO users (id, created_at, updated_at, removed, full_name, role)
VALUES ('50000000-0000-0000-0000-000000000001', now(), now(), FALSE, 'Иван Клиентов', 'CLIENT'),
       ('50000000-0000-0000-0000-000000000002', now(), now(), FALSE, 'Пётр Менеджеров', 'MANAGER'),
       ('50000000-0000-0000-0000-000000000003', now(), now(), FALSE, 'Склад Складов', 'WAREHOUSE_ADMIN'),
       ('50000000-0000-0000-0000-000000000004', now(), now(), FALSE, 'Админ Системный', 'SYSTEM_ADMIN');

-- rollback DELETE FROM users WHERE id IN ('50000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000002', '50000000-0000-0000-0000-000000000003', '50000000-0000-0000-0000-000000000004');
