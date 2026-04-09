package com.erofivan.application.core.services;

import com.erofivan.application.contracts.security.CurrentUserProvider;
import com.erofivan.application.contracts.services.InventoryOrderService;
import com.erofivan.domain.OrderStatus;
import com.erofivan.domain.UserRole;
import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.models.CarEntity;
import com.erofivan.domain.models.InventoryOrderEntity;
import com.erofivan.domain.models.UserEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.CarRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.InventoryOrderRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.UserRepository;
import com.erofivan.presentation.dtos.requests.PlaceInventoryOrderRequest;
import com.erofivan.presentation.dtos.responses.InventoryOrderResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class InventoryOrderCatalogService implements InventoryOrderService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final InventoryOrderRepository inventoryOrderRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    public List<InventoryOrderResponse> getInventoryOrders() {
        List<InventoryOrderEntity> orders;

        if (currentUserProvider.hasRole("MANAGER") || currentUserProvider.hasRole("ADMIN")) {
            orders = inventoryOrderRepository.findAllBy();
        } else {
            UUID currentUserId = currentUserProvider.getCurrentUserId();
            orders = inventoryOrderRepository.findByClientIdAndRemovedFalse(currentUserId);
        }

        return orders.stream()
            .map(order -> new InventoryOrderResponse(
                order.getId(), order.getClient().getId(),
                order.getManager().getId(), order.getCar().getId(),
                order.getStatus()))
            .toList();
    }

    @Transactional(readOnly = true)
    public InventoryOrderResponse getInventoryOrder(UUID id) {
        InventoryOrderEntity order = inventoryOrderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("InventoryOrder", id.toString()));

        return new InventoryOrderResponse(
            order.getId(), order.getClient().getId(),
            order.getManager().getId(), order.getCar().getId(),
            order.getStatus());
    }

    @Transactional
    public InventoryOrderResponse placeInventoryOrder(@NonNull PlaceInventoryOrderRequest request) {
        if (request.carId() == null)
            throw new DomainValidationException("carId is required");

        UUID clientId = currentUserProvider.getCurrentUserId();

        UserEntity client = userRepository.findById(clientId)
            .filter(u -> !u.isRemoved())
            .orElseThrow(() -> new EntityNotFoundException("Client", clientId.toString()));

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
    public InventoryOrderResponse cancelInventoryOrder(UUID id) {
        InventoryOrderEntity order = inventoryOrderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("InventoryOrder", id.toString()));

        validateStatusTransition(order.getStatus(), OrderStatus.CANCELLED);
        order.setStatus(OrderStatus.CANCELLED);

        order.getCar().setAvailable(true);
        carRepository.save(order.getCar());

        inventoryOrderRepository.save(order);

        return new InventoryOrderResponse(
            order.getId(), order.getClient().getId(),
            order.getManager().getId(), order.getCar().getId(),
            order.getStatus());
    }

    @Transactional
    public InventoryOrderResponse approveInventoryOrder(UUID id) {
        InventoryOrderEntity order = inventoryOrderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("InventoryOrder", id.toString()));

        validateStatusTransition(order.getStatus(), OrderStatus.APPROVED_BY_MANAGER);
        order.setStatus(OrderStatus.APPROVED_BY_MANAGER);
        inventoryOrderRepository.save(order);

        return new InventoryOrderResponse(
            order.getId(), order.getClient().getId(),
            order.getManager().getId(), order.getCar().getId(),
            order.getStatus());
    }

    @Transactional
    public InventoryOrderResponse confirmWarehouseInventoryOrder(UUID id) {
        InventoryOrderEntity order = inventoryOrderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("InventoryOrder", id.toString()));

        validateStatusTransition(order.getStatus(), OrderStatus.APPROVED_BY_WAREHOUSE);
        order.setStatus(OrderStatus.APPROVED_BY_WAREHOUSE);
        inventoryOrderRepository.save(order);

        return new InventoryOrderResponse(
            order.getId(), order.getClient().getId(),
            order.getManager().getId(), order.getCar().getId(),
            order.getStatus());
    }

    private UserEntity assignManager() {
        List<UserEntity> managers = userRepository.findByRoleAndRemovedFalse(UserRole.MANAGER);

        if (managers.isEmpty())
            throw new DomainValidationException("No managers available");

        int index = ThreadLocalRandom.current().nextInt(managers.size());

        return managers.get(index);
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus target) {
        boolean valid = switch (target) {
            case APPROVED_BY_MANAGER -> current == OrderStatus.PLACED;
            case APPROVED_BY_WAREHOUSE -> current == OrderStatus.APPROVED_BY_MANAGER;
            case CANCELLED -> current == OrderStatus.PLACED
                || current == OrderStatus.APPROVED_BY_MANAGER;
            default -> false;
        };

        if (!valid) {
            throw new DomainValidationException(
                "Cannot transition from " + current + " to " + target);
        }
    }
}
