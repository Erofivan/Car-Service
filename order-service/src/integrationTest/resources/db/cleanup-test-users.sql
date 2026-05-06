DELETE FROM test_drive_requests;
DELETE FROM inventory_orders;
DELETE FROM custom_orders;
DELETE FROM outbox_events;
DELETE FROM users;
WHERE id IN (
             '50000000-0000-0000-0000-000000000001',
             '50000000-0000-0000-0000-000000000002'
    );
