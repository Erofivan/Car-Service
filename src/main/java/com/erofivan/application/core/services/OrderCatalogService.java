package com.erofivan.application.core.services;

import com.erofivan.domain.OrderStatus;
import com.erofivan.domain.UserRole;
import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.models.CarEntity;
import com.erofivan.domain.models.CustomOrderEntity;
import com.erofivan.domain.models.InventoryOrderEntity;
import com.erofivan.domain.models.ModelEntity;
import com.erofivan.domain.models.UserEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.CarRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.CustomOrderRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.InventoryOrderRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.ModelRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.UserRepository;
import com.erofivan.presentation.dtos.requests.PlaceCustomOrderRequest;
import com.erofivan.presentation.dtos.requests.PlaceInventoryOrderRequest;
import com.erofivan.presentation.dtos.responses.ConfigurationResponse;
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
    private final ConfigurationCatalogService configurationCatalogService;

    @Transactional(readOnly = true)
    public List<InventoryOrderResponse> getInventoryOrders() {
        return inventoryOrderRepository.findAllBy().stream()
            .map(order -> new InventoryOrderResponse(
                order.getId(), order.getClient().getId(),
                order.getManager().getId(), order.getCar().getId(),
                order.getStatus()))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<CustomOrderResponse> getCustomOrders() {
        return customOrderRepository.findAllBy().stream()
            .map(order -> new CustomOrderResponse(
                order.getId(), order.getClient().getId(),
                order.getManager().getId(), order.getModel().getCode(),
                order.getStatus(), order.getTotalPrice()))
            .toList();
    }

    @Transactional
    public InventoryOrderResponse placeInventoryOrder(PlaceInventoryOrderRequest request) {
        if (request.clientId() == null)
            throw new DomainValidationException("clientId is required");

        if (request.carId() == null)
            throw new DomainValidationException("carId is required");

        UserEntity client = userRepository.findByIdAndRoleAndRemovedFalse(request.clientId(), UserRole.CLIENT)
            .orElseThrow(() -> new EntityNotFoundException("Client", request.clientId().toString()));

        CarEntity car = carRepository.findById(request.carId())
            .filter(c -> !c.isRemoved())
            .orElseThrow(() -> new EntityNotFoundException("Car", request.carId().toString()));

        if (!car.isAvailable())
            throw new DomainValidationException("Car is not available");

        UserEntity manager = assignManager();

        car.setAvailable(false);

        carRepository.save(car);

        InventoryOrderEntity order = new InventoryOrderEntity();
        order.setClient(client);
        order.setManager(manager);
        order.setCar(car);
        order.setStatus(OrderStatus.PLACED);

        inventoryOrderRepository.save(order);

        return new InventoryOrderResponse(
            order.getId(), client.getId(), manager.getId(), car.getId(), order.getStatus()
        );
    }

    @Transactional
    public CustomOrderResponse placeCustomOrder(PlaceCustomOrderRequest request) {
        if (request.clientId() == null)
            throw new DomainValidationException("clientId is required");

        if (request.modelCode() == null || request.modelCode().isBlank())
            throw new DomainValidationException("modelCode is required");

        if (request.optionIds() == null || request.optionIds().isEmpty())
            throw new DomainValidationException("optionIds is required");

        UserEntity client = userRepository.findByIdAndRoleAndRemovedFalse(request.clientId(), UserRole.CLIENT)
            .orElseThrow(() -> new EntityNotFoundException("Client", request.clientId().toString()));

        ConfigurationResponse config = configurationCatalogService
            .buildConfiguration(request.modelCode(), request.optionIds());

        ModelEntity model = modelRepository.findByCodeAndRemovedFalse(request.modelCode())
            .orElseThrow(() -> new EntityNotFoundException("Model", request.modelCode()));

        UserEntity manager = assignManager();

        CustomOrderEntity order = new CustomOrderEntity();
        order.setClient(client);
        order.setManager(manager);
        order.setModel(model);
        order.setStatus(OrderStatus.PLACED);
        order.setTotalPrice(config.totalPrice());

        customOrderRepository.save(order);

        return new CustomOrderResponse(
            order.getId(), client.getId(), manager.getId(),
            model.getCode(), order.getStatus(), order.getTotalPrice()
        );
    }

    private UserEntity assignManager() {
        List<UserEntity> managers = userRepository.findByRoleAndRemovedFalse(UserRole.MANAGER);

        if (managers.isEmpty())
            throw new DomainValidationException("No managers available");

        int index = ThreadLocalRandom.current().nextInt(managers.size());

        return managers.get(index);
    }
}
