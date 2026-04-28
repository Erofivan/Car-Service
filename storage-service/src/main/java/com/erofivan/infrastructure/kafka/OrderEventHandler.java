package com.erofivan.infrastructure.kafka;

import com.erofivan.application.core.services.AssemblyOrderCatalogService;
import com.erofivan.contracts.events.CustomOrderWarehouseApprovedEvent;
import com.erofivan.contracts.events.InventoryOrderCancelledEvent;
import com.erofivan.contracts.events.InventoryOrderPlacedEvent;
import com.erofivan.domain.models.ProcessedEventEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.CarRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OrderEventHandler {
    private final CarRepository carRepository;
    private final AssemblyOrderCatalogService assemblyOrderCatalogService;
    private final ProcessedEventRepository processedEventRepository;

    @Transactional
    public void handleInventoryOrderPlaced(String eventId, InventoryOrderPlacedEvent event) {
        if (processedEventRepository.existsById(eventId)) return;

        carRepository.findById(event.carId()).ifPresent(car -> {
            car.setAvailable(false);
            carRepository.save(car);
        });

        markProcessed(eventId);
    }

    @Transactional
    public void handleInventoryOrderCancelled(String eventId, InventoryOrderCancelledEvent event) {
        if (processedEventRepository.existsById(eventId)) return;

        carRepository.findById(event.carId()).ifPresent(car -> {
            car.setAvailable(true);
            carRepository.save(car);
        });

        markProcessed(eventId);
    }

    @Transactional
    public void handleCustomOrderWarehouseApproved(String eventId, CustomOrderWarehouseApprovedEvent event) {
        if (processedEventRepository.existsById(eventId)) return;

        assemblyOrderCatalogService.createAssemblyOrder(event.orderId(), event.modelCode());
        markProcessed(eventId);
    }

    private void markProcessed(String eventId) {
        ProcessedEventEntity processed = new ProcessedEventEntity();
        processed.setId(eventId);
        processedEventRepository.save(processed);
    }
}
