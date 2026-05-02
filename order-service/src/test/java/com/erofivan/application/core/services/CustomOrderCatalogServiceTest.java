package com.erofivan.application.core.services;

import com.erofivan.application.contracts.security.CurrentUserProvider;
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
class CustomOrderCatalogServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private CustomOrderRepository customOrderRepository;
    @Mock private CurrentUserProvider currentUserProvider;
    @Mock private OutboxEventService outboxEventService;

    @InjectMocks
    private CustomOrderCatalogService service;

    private static final UUID CLIENT_ID  = UUID.randomUUID();
    private static final UUID MANAGER_ID = UUID.randomUUID();

    @Test
    void placeCustomOrderCreatesOrderWithStatusPlaced() {
        UserEntity client  = createUser(CLIENT_ID,  UserRole.CLIENT);
        UserEntity manager = createUser(MANAGER_ID, UserRole.MANAGER);
        when(currentUserProvider.getCurrentUserId()).thenReturn(CLIENT_ID);
        when(userRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));
        when(userRepository.findByRoleAndRemovedFalse(UserRole.MANAGER)).thenReturn(List.of(manager));
        when(customOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.placeCustomOrder(new PlaceCustomOrderRequest("BMW-320I", null));

        assertThat(result.modelCode()).isEqualTo("BMW-320I");
        assertThat(result.status()).isEqualTo(OrderStatus.PLACED);
        assertThat(result.totalPrice()).isEqualTo(0L);
        verifyNoInteractions(outboxEventService);
    }

    @Test
    void placeCustomOrderThrowsWhenModelCodeBlank() {
        assertThatThrownBy(() -> service.placeCustomOrder(new PlaceCustomOrderRequest("", null)))
            .isInstanceOf(DomainValidationException.class);
    }

    @Test
    void placeCustomOrderThrowsWhenModelCodeNull() {
        assertThatThrownBy(() -> service.placeCustomOrder(new PlaceCustomOrderRequest(null, null)))
            .isInstanceOf(DomainValidationException.class);
    }

    @Test
    void confirmWarehouseCustomOrderTransitionsStatusAndPublishesEvent() {
        CustomOrderEntity order = createOrder(OrderStatus.APPROVED_BY_MANAGER);
        when(customOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(customOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.confirmWarehouseCustomOrder(order.getId());

        assertThat(result.status()).isEqualTo(OrderStatus.APPROVED_BY_WAREHOUSE);
        verify(outboxEventService).saveEvent(eq(KafkaTopics.CUSTOM_ORDER_WAREHOUSE_APPROVED), any(), any());
    }

    @Test
    void approveCustomOrderTransitionsStatus() {
        CustomOrderEntity order = createOrder(OrderStatus.PLACED);
        when(customOrderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(customOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.approveCustomOrder(order.getId());

        assertThat(result.status()).isEqualTo(OrderStatus.APPROVED_BY_MANAGER);
    }

    @Test
    void cancelCustomOrderThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(customOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cancelCustomOrder(id))
            .isInstanceOf(EntityNotFoundException.class);
    }

    private UserEntity createUser(UUID id, UserRole role) {
        UserEntity u = new UserEntity();
        u.setId(id);
        u.setFullName("Test User");
        u.setRole(role);
        return u;
    }

    private CustomOrderEntity createOrder(OrderStatus status) {
        CustomOrderEntity o = new CustomOrderEntity();
        o.setId(UUID.randomUUID());
        o.setModelCode("BMW-320I");
        o.setStatus(status);
        o.setTotalPrice(0L);
        o.setClient(createUser(CLIENT_ID, UserRole.CLIENT));
        o.setManager(createUser(MANAGER_ID, UserRole.MANAGER));
        return o;
    }
}
