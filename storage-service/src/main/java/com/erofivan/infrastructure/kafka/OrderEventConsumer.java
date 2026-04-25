package com.erofivan.infrastructure.kafka;

import com.erofivan.application.core.services.AssemblyOrderCatalogService;
import com.erofivan.contracts.events.CustomOrderWarehouseApprovedEvent;
import com.erofivan.contracts.events.InventoryOrderCancelledEvent;
import com.erofivan.contracts.events.InventoryOrderPlacedEvent;
import com.erofivan.contracts.kafka.KafkaTopics;
import com.erofivan.domain.models.ProcessedEventEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.CarRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.ProcessedEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final CarRepository carRepository;
    private final AssemblyOrderCatalogService assemblyOrderCatalogService;
    private final ProcessedEventRepository processedEventRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = KafkaTopics.INVENTORY_ORDER_PLACED)
    @Transactional
    public void onInventoryOrderPlaced(ConsumerRecord<String, String> record) {
        UUID eventId = UUID.fromString(record.key());

        if (processedEventRepository.existsById(eventId)) return;

        try {
            InventoryOrderPlacedEvent event =
                objectMapper.readValue(record.value(), InventoryOrderPlacedEvent.class);

            carRepository.findById(event.carId()).ifPresent(car -> {
                car.setAvailable(false);
                carRepository.save(car);
            });

            markProcessed(eventId);
        } catch (Exception e) {
            log.error("Failed to process InventoryOrderPlacedEvent {}: {}", eventId, e.getMessage());
        }
    }

    @KafkaListener(topics = KafkaTopics.INVENTORY_ORDER_CANCELLED)
    @Transactional
    public void onInventoryOrderCancelled(ConsumerRecord<String, String> record) {
        UUID eventId = UUID.fromString(record.key());

        if (processedEventRepository.existsById(eventId)) return;

        try {
            InventoryOrderCancelledEvent event =
                objectMapper.readValue(record.value(), InventoryOrderCancelledEvent.class);

            carRepository.findById(event.carId()).ifPresent(car -> {
                car.setAvailable(true);
                carRepository.save(car);
            });

            markProcessed(eventId);
        } catch (Exception e) {
            log.error("Failed to process InventoryOrderCancelledEvent {}: {}", eventId, e.getMessage());
        }
    }

    @KafkaListener(topics = KafkaTopics.CUSTOM_ORDER_WAREHOUSE_APPROVED)
    @Transactional
    public void onCustomOrderWarehouseApproved(ConsumerRecord<String, String> record) {
        UUID eventId = UUID.fromString(record.key());
        
        if (processedEventRepository.existsById(eventId)) return;

        try {
            CustomOrderWarehouseApprovedEvent event =
                objectMapper.readValue(record.value(), CustomOrderWarehouseApprovedEvent.class);

            assemblyOrderCatalogService.createAssemblyOrder(event.orderId(), event.modelCode());

            markProcessed(eventId);
        } catch (Exception e) {
            log.error("Failed to process CustomOrderWarehouseApprovedEvent {}: {}", eventId, e.getMessage());
        }
    }

    private void markProcessed(UUID eventId) {
        ProcessedEventEntity processed = new ProcessedEventEntity();
        processed.setId(eventId);
        processedEventRepository.save(processed);
    }
}
