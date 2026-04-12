package com.erofivan.application.core.services;

import com.erofivan.application.contracts.security.CurrentUserProvider;
import com.erofivan.domain.OrderStatus;
import com.erofivan.domain.UserRole;
import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.models.CustomOrderEntity;
import com.erofivan.domain.models.ModelEntity;
import com.erofivan.domain.models.UserEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.CustomOrderRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.ModelRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.UserRepository;
import com.erofivan.presentation.dtos.requests.PlaceCustomOrderRequest;
import com.erofivan.presentation.dtos.responses.ConfigurationResponse;
import com.erofivan.presentation.dtos.responses.CustomOrderResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomOrderCatalogServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelRepository modelRepository;
    @Mock
    private CustomOrderRepository customOrderRepository;
    @Mock
    private ConfigurationCatalogService configurationCatalogService;
    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private CustomOrderCatalogService service;

    @Test
    void getCustomOrdersReturnsAllForManager() {
        when(currentUserProvider.hasRole("MANAGER")).thenReturn(true);

        CustomOrderEntity order = createOrder(OrderStatus.PLACED);
        when(customOrderRepository.findAllBy()).thenReturn(List.of(order));

        List<CustomOrderResponse> result = service.getCustomOrders();

        assertEquals(1, result.size());
        assertEquals(OrderStatus.PLACED, result.get(0).status());
    }

    @Test
    void getCustomOrdersReturnsOnlyOwnForUser() {
        UUID userId = UUID.randomUUID();
        when(currentUserProvider.hasRole("MANAGER")).thenReturn(false);
        when(currentUserProvider.hasRole("ADMIN")).thenReturn(false);
        when(currentUserProvider.getCurrentUserId()).thenReturn(userId);

        when(customOrderRepository.findByClientIdAndRemovedFalse(userId)).thenReturn(List.of());

        List<CustomOrderResponse> result = service.getCustomOrders();

        assertEquals(0, result.size());
    }

    @Test
    void placeCustomOrderCreatesOrderWithPlacedStatus() {
        UUID clientId = UUID.randomUUID();
        UserEntity client = createUser(clientId, UserRole.CLIENT);
        UserEntity manager = createUser(UUID.randomUUID(), UserRole.MANAGER);
        ModelEntity model = createModel("X5", 5_000_000L);

        when(currentUserProvider.getCurrentUserId()).thenReturn(clientId);
        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(userRepository.findByRoleAndRemovedFalse(UserRole.MANAGER)).thenReturn(List.of(manager));
        when(modelRepository.findByCodeAndRemovedFalse("X5")).thenReturn(Optional.of(model));
        when(configurationCatalogService.buildConfiguration("X5", null))
            .thenReturn(new ConfigurationResponse("X5", List.of(), 5_000_000L, 0L, 5_000_000L));
        when(customOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        PlaceCustomOrderRequest request = new PlaceCustomOrderRequest("X5", null);
        CustomOrderResponse result = service.placeCustomOrder(request);

        assertEquals(OrderStatus.PLACED, result.status());
        assertEquals(5_000_000L, result.totalPrice());
        verify(customOrderRepository).save(any(CustomOrderEntity.class));
    }

    @Test
    void placeCustomOrderThrowsWhenModelCodeBlank() {
        PlaceCustomOrderRequest request = new PlaceCustomOrderRequest("", null);

        assertThrows(DomainValidationException.class, () -> service.placeCustomOrder(request));
    }

    @Test
    void placeCustomOrderThrowsWhenNoManagers() {
        UUID clientId = UUID.randomUUID();
        UserEntity client = createUser(clientId, UserRole.CLIENT);
        ModelEntity model = createModel("X5", 5_000_000L);

        when(currentUserProvider.getCurrentUserId()).thenReturn(clientId);
        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(configurationCatalogService.buildConfiguration("X5", null))
            .thenReturn(new ConfigurationResponse("X5", List.of(), 5_000_000L, 0L, 5_000_000L));
        when(modelRepository.findByCodeAndRemovedFalse("X5")).thenReturn(Optional.of(model));
        when(userRepository.findByRoleAndRemovedFalse(UserRole.MANAGER)).thenReturn(List.of());

        PlaceCustomOrderRequest request = new PlaceCustomOrderRequest("X5", null);
        assertThrows(DomainValidationException.class, () -> service.placeCustomOrder(request));
    }

    @Test
    void cancelCustomOrderTransitionsFromPlaced() {
        CustomOrderEntity order = createOrder(OrderStatus.PLACED);

        when(customOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(customOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CustomOrderResponse result = service.cancelCustomOrder(order.getId());

        assertEquals(OrderStatus.CANCELLED, result.status());
    }

    @Test
    void cancelCustomOrderTransitionsFromApprovedByManager() {
        CustomOrderEntity order = createOrder(OrderStatus.APPROVED_BY_MANAGER);

        when(customOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(customOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CustomOrderResponse result = service.cancelCustomOrder(order.getId());

        assertEquals(OrderStatus.CANCELLED, result.status());
    }

    @Test
    void cancelCustomOrderThrowsWhenAlreadyCancelled() {
        CustomOrderEntity order = createOrder(OrderStatus.CANCELLED);

        when(customOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThrows(DomainValidationException.class, () -> service.cancelCustomOrder(order.getId()));
    }

    @Test
    void approveCustomOrderTransitionsFromPlaced() {
        CustomOrderEntity order = createOrder(OrderStatus.PLACED);

        when(customOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(customOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CustomOrderResponse result = service.approveCustomOrder(order.getId());

        assertEquals(OrderStatus.APPROVED_BY_MANAGER, result.status());
    }

    @Test
    void approveCustomOrderThrowsFromWrongStatus() {
        CustomOrderEntity order = createOrder(OrderStatus.APPROVED_BY_WAREHOUSE);

        when(customOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThrows(DomainValidationException.class, () -> service.approveCustomOrder(order.getId()));
    }

    @Test
    void confirmWarehouseTransitionsFromApprovedByManager() {
        CustomOrderEntity order = createOrder(OrderStatus.APPROVED_BY_MANAGER);

        when(customOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(customOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CustomOrderResponse result = service.confirmWarehouseCustomOrder(order.getId());

        assertEquals(OrderStatus.APPROVED_BY_WAREHOUSE, result.status());
    }

    @Test
    void confirmWarehouseThrowsFromPlaced() {
        CustomOrderEntity order = createOrder(OrderStatus.PLACED);

        when(customOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThrows(DomainValidationException.class, () -> service.confirmWarehouseCustomOrder(order.getId()));
    }

    @Test
    void getCustomOrderThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(customOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getCustomOrder(id));
    }

    private CustomOrderEntity createOrder(OrderStatus status) {
        UserEntity client = createUser(UUID.randomUUID(), UserRole.CLIENT);
        UserEntity manager = createUser(UUID.randomUUID(), UserRole.MANAGER);
        ModelEntity model = createModel("X5", 5_000_000L);

        CustomOrderEntity order = new CustomOrderEntity();
        order.setId(UUID.randomUUID());
        order.setClient(client);
        order.setManager(manager);
        order.setModel(model);
        order.setStatus(status);
        order.setTotalPrice(5_000_000L);
        return order;
    }

    private UserEntity createUser(UUID id, UserRole role) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setRole(role);
        user.setFullName("Test User");
        return user;
    }

    private ModelEntity createModel(String code, long basePrice) {
        ModelEntity model = new ModelEntity();
        model.setId(UUID.randomUUID());
        model.setCode(code);
        model.setBasePrice(basePrice);
        return model;
    }
}
