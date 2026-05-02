package com.erofivan.contracts.kafka;

public final class KafkaTopics {
    public static final String INVENTORY_ORDER_PLACED = "orders.inventory.placed";
    public static final String INVENTORY_ORDER_CANCELLED = "orders.inventory.cancelled";
    public static final String CUSTOM_ORDER_WAREHOUSE_APPROVED = "orders.custom.warehouse-approved";

    private KafkaTopics() {
    }
}
