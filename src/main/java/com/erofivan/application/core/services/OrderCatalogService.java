package com.erofivan.application.core.services;

import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.infrastructure.persistence.jpa.model.CarEntity;
import com.erofivan.infrastructure.persistence.jpa.model.CustomOrderEntity;
import com.erofivan.infrastructure.persistence.jpa.model.InventoryOrderEntity;
import com.erofivan.infrastructure.persistence.jpa.model.ModelEntity;
import com.erofivan.infrastructure.persistence.jpa.model.UserEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.CarRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.CustomOrderRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.InventoryOrderRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.ModelRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.UserRepository;
import com.erofivan.presentation.dtos.requests.PlaceCustomOrderRequest;
import com.erofivan.presentation.dtos.requests.PlaceInventoryOrderRequest;
import com.erofivan.presentation.dtos.responses.CustomOrderResponse;
import com.erofivan.presentation.dtos.responses.InventoryOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OrderCatalogService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final ModelRepository modelRepository;
    private final InventoryOrderRepository inventoryOrderRepository;
    private final CustomOrderRepository customOrderRepository;

    @Transactional
    public InventoryOrderResponse placeInventoryOrder(PlaceInventoryOrderRequest request) {
        UserEntity client = userRepository.findByIdAndRoleAndRemovedFalse(request.clientId(), "CLIENT")
            .orElseThrow(() -> new EntityNotFoundException("Client", request.clientId().toString()));

        CarEntity car = carRepository.findById(request.carId())
            .filter(c -> !c.isRemoved())
            .orElseThrow(() -> new EntityNotFoundException("Car", request.carId().toString()));

        if (!car.isAvailable()) {
            throw new IllegalStateException("Car is not available");
        }

        UserEntity manager = assignManager();

        car.setAvailable(false);
        carRepository.save(car);

        InventoryOrderEntity order = new InventoryOrderEntity();
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
        UserEntity client = userRepository.findByIdAndRoleAndRemovedFalse(request.clientId(), "CLIENT")
            .orElseThrow(() -> new EntityNotFoundException("Client", request.clientId().toString()));

        ModelEntity model = modelRepository.findByCodeAndRemovedFalse(request.modelCode())
            .orElseThrow(() -> new EntityNotFoundException("Model", request.modelCode()));

        UserEntity manager = assignManager();

        long totalPrice = model.getBasePrice();

        CustomOrderEntity order = new CustomOrderEntity();
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

    private UserEntity assignManager() {
        List<UserEntity> managers = userRepository.findByRoleAndRemovedFalse("MANAGER");
        if (managers.isEmpty()) {
            throw new IllegalStateException("No managers available");
        }
        int index = ThreadLocalRandom.current().nextInt(managers.size());
        return managers.get(index);
    }
}
