package com.erofivan.application.core.services;

import com.erofivan.application.contracts.security.CurrentUserProvider;
import com.erofivan.domain.OrderStatus;
import com.erofivan.domain.UserRole;
import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.models.CarEntity;
import com.erofivan.domain.models.InventoryOrderEntity;
import com.erofivan.domain.models.ModelEntity;
import com.erofivan.domain.models.UserEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.CarRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.InventoryOrderRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.UserRepository;
import com.erofivan.presentation.dtos.requests.PlaceInventoryOrderRequest;
import com.erofivan.presentation.dtos.responses.InventoryOrderResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryOrderCatalogServiceTest {

    @Mock
    private CarRepository carRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private InventoryOrderRepository inventoryOrderRepository;
    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private InventoryOrderCatalogService service;

    @Test
    void placeInventoryOrderCreatesOrderAndMakesCarUnavailable() {
        UUID clientId = UUID.randomUUID();
        UserEntity client = createUser(clientId, UserRole.CLIENT);
        UserEntity manager = createUser(UUID.randomUUID(), UserRole.MANAGER);
        CarEntity car = createCar(true);

        when(currentUserProvider.getCurrentUserId()).thenReturn(clientId);
        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        when(userRepository.findByRoleAndRemovedFalse(UserRole.MANAGER)).thenReturn(List.of(manager));
        when(carRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(inventoryOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        InventoryOrderResponse result = service.placeInventoryOrder(new PlaceInventoryOrderRequest(car.getId()));

        assertEquals(OrderStatus.PLACED, result.status());
        assertFalse(car.isAvailable());
        verify(carRepository).save(car);
    }

    @Test
    void placeInventoryOrderThrowsWhenCarNotAvailable() {
        UUID clientId = UUID.randomUUID();
        CarEntity car = createCar(false);

        when(currentUserProvider.getCurrentUserId()).thenReturn(clientId);
        when(userRepository.findById(clientId)).thenReturn(Optional.of(createUser(clientId, UserRole.CLIENT)));
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        assertThrows(DomainValidationException.class,
            () -> service.placeInventoryOrder(new PlaceInventoryOrderRequest(car.getId())));
    }

    @Test
    void placeInventoryOrderThrowsWhenCarIdNull() {
        assertThrows(DomainValidationException.class,
            () -> service.placeInventoryOrder(new PlaceInventoryOrderRequest(null)));
    }

    @Test
    void cancelInventoryOrderRestoresCarAvailability() {
        CarEntity car = createCar(false);
        InventoryOrderEntity order = createOrder(OrderStatus.PLACED, car);

        when(inventoryOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(inventoryOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(carRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        InventoryOrderResponse result = service.cancelInventoryOrder(order.getId());

        assertEquals(OrderStatus.CANCELLED, result.status());
        assertTrue(car.isAvailable());
    }

    @Test
    void cancelInventoryOrderThrowsFromApprovedByWarehouse() {
        InventoryOrderEntity order = createOrder(OrderStatus.APPROVED_BY_WAREHOUSE, createCar(false));

        when(inventoryOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThrows(DomainValidationException.class, () -> service.cancelInventoryOrder(order.getId()));
    }

    @Test
    void approveInventoryOrderTransitionsFromPlaced() {
        InventoryOrderEntity order = createOrder(OrderStatus.PLACED, createCar(false));

        when(inventoryOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(inventoryOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        InventoryOrderResponse result = service.approveInventoryOrder(order.getId());

        assertEquals(OrderStatus.APPROVED_BY_MANAGER, result.status());
    }

    @Test
    void approveInventoryOrderThrowsFromCancelled() {
        InventoryOrderEntity order = createOrder(OrderStatus.CANCELLED, createCar(false));

        when(inventoryOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThrows(DomainValidationException.class, () -> service.approveInventoryOrder(order.getId()));
    }

    @Test
    void confirmWarehouseTransitionsFromApprovedByManager() {
        InventoryOrderEntity order = createOrder(OrderStatus.APPROVED_BY_MANAGER, createCar(false));

        when(inventoryOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(inventoryOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        InventoryOrderResponse result = service.confirmWarehouseInventoryOrder(order.getId());

        assertEquals(OrderStatus.APPROVED_BY_WAREHOUSE, result.status());
    }

    @Test
    void getInventoryOrderThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(inventoryOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getInventoryOrder(id));
    }

    @Test
    void getInventoryOrdersReturnsAllForAdmin() {
        when(currentUserProvider.hasRole("MANAGER")).thenReturn(false);
        when(currentUserProvider.hasRole("ADMIN")).thenReturn(true);

        InventoryOrderEntity order = createOrder(OrderStatus.PLACED, createCar(true));
        when(inventoryOrderRepository.findAllBy()).thenReturn(List.of(order));

        List<InventoryOrderResponse> result = service.getInventoryOrders();

        assertEquals(1, result.size());
    }

    private UserEntity createUser(UUID id, UserRole role) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setRole(role);
        user.setFullName("Test User");
        return user;
    }

    private CarEntity createCar(boolean available) {
        CarEntity car = new CarEntity();
        car.setId(UUID.randomUUID());
        car.setAvailable(available);
        car.setModel(new ModelEntity());
        return car;
    }

    private InventoryOrderEntity createOrder(OrderStatus status, CarEntity car) {
        InventoryOrderEntity order = new InventoryOrderEntity();
        order.setId(UUID.randomUUID());
        order.setClient(createUser(UUID.randomUUID(), UserRole.CLIENT));
        order.setManager(createUser(UUID.randomUUID(), UserRole.MANAGER));
        order.setCar(car);
        order.setStatus(status);
        return order;
    }
}
