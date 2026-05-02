package com.erofivan.application.core.services;

import com.erofivan.application.contracts.security.CurrentUserProvider;
import com.erofivan.application.contracts.services.CustomOrderService;
import com.erofivan.contracts.events.CustomOrderWarehouseApprovedEvent;
import com.erofivan.contracts.kafka.KafkaTopics;
import com.erofivan.domain.OrderStatus;
import com.erofivan.domain.UserRole;
import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.models.CustomOrderEntity;
import com.erofivan.domain.models.UserEntity;
import com.erofivan.infrastructure.kafka.OutboxEventService;
import com.erofivan.infrastructure.persistence.jpa.repositories.CustomOrderRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.UserRepository;
import com.erofivan.presentation.dtos.requests.PlaceCustomOrderRequest;
import com.erofivan.presentation.dtos.responses.CustomOrderResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CustomOrderCatalogService implements CustomOrderService {
    private final UserRepository userRepository;
    private final CustomOrderRepository customOrderRepository;
    private final CurrentUserProvider currentUserProvider;
    private final OutboxEventService outboxEventService;

    @Transactional(readOnly = true)
    public List<CustomOrderResponse> getCustomOrders() {
        List<CustomOrderEntity> orders;

        if (currentUserProvider.hasRole("MANAGER") || currentUserProvider.hasRole("ADMIN")) {
            orders = customOrderRepository.findAllBy();
        } else {
            UUID currentUserId = currentUserProvider.getCurrentUserId();
            orders = customOrderRepository.findByClientIdAndRemovedFalse(currentUserId);
        }

        return orders.stream()
            .map(order -> new CustomOrderResponse(
                order.getId(), order.getClient().getId(),
                order.getManager().getId(), order.getModelCode(),
                order.getStatus(), order.getTotalPrice()))
            .toList();
    }

    @Transactional(readOnly = true)
    public CustomOrderResponse getCustomOrder(UUID id) {
        CustomOrderEntity order = customOrderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CustomOrder", id.toString()));

        return new CustomOrderResponse(
            order.getId(), order.getClient().getId(),
            order.getManager().getId(), order.getModelCode(),
            order.getStatus(), order.getTotalPrice());
    }

    @Transactional
    public CustomOrderResponse placeCustomOrder(@NonNull PlaceCustomOrderRequest request) {
        if (request.modelCode() == null || request.modelCode().isBlank())
            throw new DomainValidationException("modelCode is required");

        UUID clientId = currentUserProvider.getCurrentUserId();

        UserEntity client = userRepository.findById(clientId)
            .filter(u -> !u.isRemoved())
            .orElseThrow(() -> new EntityNotFoundException("Client", clientId.toString()));

        UserEntity manager = assignManager();

        CustomOrderEntity order = new CustomOrderEntity();
        order.setClient(client);
        order.setManager(manager);
        order.setModelCode(request.modelCode());
        order.setStatus(OrderStatus.PLACED);
        order.setTotalPrice(0L);

        customOrderRepository.save(order);

        return new CustomOrderResponse(
            order.getId(), client.getId(), manager.getId(),
            order.getModelCode(), order.getStatus(), order.getTotalPrice());
    }

    @Transactional
    public CustomOrderResponse cancelCustomOrder(UUID id) {
        CustomOrderEntity order = customOrderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CustomOrder", id.toString()));

        validateStatusTransition(order.getStatus(), OrderStatus.CANCELLED);
        order.setStatus(OrderStatus.CANCELLED);
        customOrderRepository.save(order);

        return new CustomOrderResponse(
            order.getId(), order.getClient().getId(),
            order.getManager().getId(), order.getModelCode(),
            order.getStatus(), order.getTotalPrice());
    }

    @Transactional
    public CustomOrderResponse approveCustomOrder(UUID id) {
        CustomOrderEntity order = customOrderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CustomOrder", id.toString()));

        validateStatusTransition(order.getStatus(), OrderStatus.APPROVED_BY_MANAGER);
        order.setStatus(OrderStatus.APPROVED_BY_MANAGER);
        customOrderRepository.save(order);

        return new CustomOrderResponse(
            order.getId(), order.getClient().getId(),
            order.getManager().getId(), order.getModelCode(),
            order.getStatus(), order.getTotalPrice());
    }

    @Transactional
    public CustomOrderResponse confirmWarehouseCustomOrder(UUID id) {
        CustomOrderEntity order = customOrderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("CustomOrder", id.toString()));

        validateStatusTransition(order.getStatus(), OrderStatus.APPROVED_BY_WAREHOUSE);
        order.setStatus(OrderStatus.APPROVED_BY_WAREHOUSE);

        customOrderRepository.save(order);

        outboxEventService.saveEvent(
            KafkaTopics.CUSTOM_ORDER_WAREHOUSE_APPROVED,
            order.getId(),
            new CustomOrderWarehouseApprovedEvent(order.getId(), order.getModelCode())
        );

        return new CustomOrderResponse(
            order.getId(), order.getClient().getId(),
            order.getManager().getId(), order.getModelCode(),
            order.getStatus(), order.getTotalPrice());
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
