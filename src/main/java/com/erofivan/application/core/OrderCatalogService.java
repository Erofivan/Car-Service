package com.erofivan.application.core;

import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.infrastructure.persistence.jpa.model.CarJpaEntity;
import com.erofivan.infrastructure.persistence.jpa.model.CustomOrderJpaEntity;
import com.erofivan.infrastructure.persistence.jpa.model.InventoryOrderJpaEntity;
import com.erofivan.infrastructure.persistence.jpa.model.ModelJpaEntity;
import com.erofivan.infrastructure.persistence.jpa.model.UserJpaEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.CarJpaRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.CustomOrderJpaRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.InventoryOrderJpaRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.ModelJpaRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import com.erofivan.presentation.dto.CustomOrderResponse;
import com.erofivan.presentation.dto.InventoryOrderResponse;
import com.erofivan.presentation.dto.PlaceCustomOrderRequest;
import com.erofivan.presentation.dto.PlaceInventoryOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OrderCatalogService {
    private final CarJpaRepository carRepository;
    private final UserJpaRepository userRepository;
    private final ModelJpaRepository modelRepository;
    private final InventoryOrderJpaRepository inventoryOrderRepository;
    private final CustomOrderJpaRepository customOrderRepository;

    @Transactional
    public InventoryOrderResponse placeInventoryOrder(PlaceInventoryOrderRequest request) {
        UserJpaEntity client = userRepository.findByIdAndRoleAndRemovedFalse(request.clientId(), "CLIENT")
            .orElseThrow(() -> new EntityNotFoundException("Client", request.clientId().toString()));

        CarJpaEntity car = carRepository.findById(request.carId())
            .filter(c -> !c.isRemoved())
            .orElseThrow(() -> new EntityNotFoundException("Car", request.carId().toString()));

        if (!car.isAvailable()) {
            throw new IllegalStateException("Car is not available");
        }

        UserJpaEntity manager = assignManager();

        car.setAvailable(false);
        carRepository.save(car);

        InventoryOrderJpaEntity order = new InventoryOrderJpaEntity();
        order.setClient(client);
        order.setManager(manager);
        order.setCar(car);
        order.setStatus("PLACED");
        inventoryOrderRepository.save(order);

        return new InventoryOrderResponse(
            order.getId(), client.getId(), manager.getId(), car.getId(), order.getStatus()
        );
    }

    @Transactional
    public CustomOrderResponse placeCustomOrder(PlaceCustomOrderRequest request) {
        UserJpaEntity client = userRepository.findByIdAndRoleAndRemovedFalse(request.clientId(), "CLIENT")
            .orElseThrow(() -> new EntityNotFoundException("Client", request.clientId().toString()));

        ModelJpaEntity model = modelRepository.findByCodeAndRemovedFalse(request.modelCode())
            .orElseThrow(() -> new EntityNotFoundException("Model", request.modelCode()));

        UserJpaEntity manager = assignManager();

        long totalPrice = model.getBasePrice();

        CustomOrderJpaEntity order = new CustomOrderJpaEntity();
        order.setClient(client);
        order.setManager(manager);
        order.setModel(model);
        order.setStatus("PLACED");
        order.setTotalPrice(totalPrice);
        customOrderRepository.save(order);

        return new CustomOrderResponse(
            order.getId(), client.getId(), manager.getId(), model.getCode(), order.getStatus(), order.getTotalPrice()
        );
    }

    private UserJpaEntity assignManager() {
        List<UserJpaEntity> managers = userRepository.findByRoleAndRemovedFalse("MANAGER");
        if (managers.isEmpty()) {
            throw new IllegalStateException("No managers available");
        }
        int index = ThreadLocalRandom.current().nextInt(managers.size());
        return managers.get(index);
    }
}
