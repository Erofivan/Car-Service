package com.erofivan.application.core.services;

import com.erofivan.application.contracts.security.CurrentUserProvider;
import com.erofivan.contracts.kafka.KafkaTopics;
import com.erofivan.domain.OrderStatus;
import com.erofivan.domain.UserRole;
import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.models.InventoryOrderEntity;
import com.erofivan.domain.models.UserEntity;
import com.erofivan.infrastructure.http.StorageServiceClient;
import com.erofivan.infrastructure.kafka.OutboxEventService;
import com.erofivan.infrastructure.persistence.jpa.repositories.InventoryOrderRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.UserRepository;
import com.erofivan.presentation.dtos.requests.PlaceInventoryOrderRequest;
import com.erofivan.presentation.dtos.responses.CarAvailabilityResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryOrderCatalogServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private InventoryOrderRepository inventoryOrderRepository;
    @Mock private CurrentUserProvider currentUserProvider;
    @Mock private OutboxEventService outboxEventService;
    @Mock private StorageServiceClient storageServiceClient;

    @InjectMocks
    private InventoryOrderCatalogService service;

    private static final UUID CLIENT_ID  = UUID.randomUUID();
    private static final UUID MANAGER_ID = UUID.randomUUID();
    private static final UUID CAR_ID     = UUID.randomUUID();

    @Test
    void placeInventoryOrderCreatesOrderWithStatusPlaced() {
        UserEntity client  = createUser(CLIENT_ID,  UserRole.CLIENT);
        UserEntity manager = createUser(MANAGER_ID, UserRole.MANAGER);
        when(storageServiceClient.getCarAvailability(CAR_ID))
            .thenReturn(new CarAvailabilityResponse(CAR_ID, true, false));
        when(currentUserProvider.getCurrentUserId()).thenReturn(CLIENT_ID);
        when(userRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));
        when(userRepository.findByRoleAndRemovedFalse(UserRole.MANAGER)).thenReturn(List.of(manager));
        when(inventoryOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.placeInventoryOrder(new PlaceInventoryOrderRequest(CAR_ID));

        assertThat(result.status()).isEqualTo(OrderStatus.PLACED);
        assertThat(result.carId()).isEqualTo(CAR_ID);
        verify(outboxEventService).saveEvent(eq(KafkaTopics.INVENTORY_ORDER_PLACED), any(), any());
    }

    @Test
    void placeInventoryOrderThrowsWhenCarNotAvailable() {
        when(storageServiceClient.getCarAvailability(CAR_ID))
            .thenReturn(new CarAvailabilityResponse(CAR_ID, false, false));

        assertThatThrownBy(() -> service.placeInventoryOrder(new PlaceInventoryOrderRequest(CAR_ID)))
            .isInstanceOf(DomainValidationException.class);

        verifyNoInteractions(inventoryOrderRepository, outboxEventService);
    }

    @Test
    void placeInventoryOrderThrowsWhenCarIdNull() {
        assertThatThrownBy(() -> service.placeInventoryOrder(new PlaceInventoryOrderRequest(null)))
            .isInstanceOf(DomainValidationException.class);

        verifyNoInteractions(storageServiceClient, inventoryOrderRepository);
    }

    @Test
    void cancelInventoryOrderTransitionsStatusAndPublishesEvent() {
        InventoryOrderEntity order = createOrder(OrderStatus.PLACED);
        when(inventoryOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(inventoryOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.cancelInventoryOrder(order.getId());

        assertThat(result.status()).isEqualTo(OrderStatus.CANCELLED);
        verify(outboxEventService).saveEvent(eq(KafkaTopics.INVENTORY_ORDER_CANCELLED), any(), any());
    }

    @Test
    void cancelInventoryOrderThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(inventoryOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cancelInventoryOrder(id))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void approveInventoryOrderTransitionsStatus() {
        InventoryOrderEntity order = createOrder(OrderStatus.PLACED);
        when(inventoryOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(inventoryOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.approveInventoryOrder(order.getId());

        assertThat(result.status()).isEqualTo(OrderStatus.APPROVED_BY_MANAGER);
        verifyNoInteractions(outboxEventService);
    }

    private UserEntity createUser(UUID id, UserRole role) {
        UserEntity u = new UserEntity();
        u.setId(id);
        u.setFullName("Test User");
        u.setRole(role);
        return u;
    }

    private InventoryOrderEntity createOrder(OrderStatus status) {
        InventoryOrderEntity o = new InventoryOrderEntity();
        o.setId(UUID.randomUUID());
        o.setCarId(CAR_ID);
        o.setStatus(status);
        o.setClient(createUser(CLIENT_ID, UserRole.CLIENT));
        o.setManager(createUser(MANAGER_ID, UserRole.MANAGER));
        return o;
    }
}
