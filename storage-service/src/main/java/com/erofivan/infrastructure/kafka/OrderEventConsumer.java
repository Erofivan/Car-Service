package com.erofivan.infrastructure.kafka;

import com.erofivan.contracts.events.CustomOrderWarehouseApprovedEvent;
import com.erofivan.contracts.events.InventoryOrderCancelledEvent;
import com.erofivan.contracts.events.InventoryOrderPlacedEvent;
import com.erofivan.contracts.kafka.KafkaTopics;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final OrderEventHandler orderEventHandler;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = KafkaTopics.INVENTORY_ORDER_PLACED)
    public void onInventoryOrderPlaced(ConsumerRecord<String, JsonNode> record) {
        String eventId = record.key();
        if (eventId == null || eventId.isBlank()) {
            log.error("Skipping InventoryOrderPlacedEvent with empty key");
            return;
        }

        try {
            InventoryOrderPlacedEvent event =
                objectMapper.treeToValue(record.value(), InventoryOrderPlacedEvent.class);
            orderEventHandler.handleInventoryOrderPlaced(eventId, event);
        } catch (Exception e) {
            log.error("Failed to process InventoryOrderPlacedEvent {}: {}", eventId, e.getMessage());
        }
    }

    @KafkaListener(topics = KafkaTopics.INVENTORY_ORDER_CANCELLED)
    public void onInventoryOrderCancelled(ConsumerRecord<String, JsonNode> record) {
        String eventId = record.key();
        if (eventId == null || eventId.isBlank()) {
            log.error("Skipping InventoryOrderCancelledEvent with empty key");
            return;
        }

        try {
            InventoryOrderCancelledEvent event =
                objectMapper.treeToValue(record.value(), InventoryOrderCancelledEvent.class);
            orderEventHandler.handleInventoryOrderCancelled(eventId, event);
        } catch (Exception e) {
            log.error("Failed to process InventoryOrderCancelledEvent {}: {}", eventId, e.getMessage());
        }
    }

    @KafkaListener(topics = KafkaTopics.CUSTOM_ORDER_WAREHOUSE_APPROVED)
    public void onCustomOrderWarehouseApproved(ConsumerRecord<String, JsonNode> record) {
        String eventId = record.key();
        if (eventId == null || eventId.isBlank()) {
            log.error("Skipping CustomOrderWarehouseApprovedEvent with empty key");
            return;
        }

        try {
            CustomOrderWarehouseApprovedEvent event =
                objectMapper.treeToValue(record.value(), CustomOrderWarehouseApprovedEvent.class);
            orderEventHandler.handleCustomOrderWarehouseApproved(eventId, event);
        } catch (Exception e) {
            log.error("Failed to process CustomOrderWarehouseApprovedEvent {}: {}", eventId, e.getMessage());
        }
    }
}
