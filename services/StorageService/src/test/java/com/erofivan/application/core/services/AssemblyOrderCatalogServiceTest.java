package com.erofivan.application.core.services;

import com.erofivan.domain.AssemblyStatus;
import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.models.AssemblyOrderEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.AssemblyOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssemblyOrderCatalogServiceTest {

    @Mock private AssemblyOrderRepository assemblyOrderRepository;

    @InjectMocks
    private AssemblyOrderCatalogService service;

    @Test
    void createAssemblyOrderSetsStatusCreated() {
        when(assemblyOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.createAssemblyOrder(UUID.randomUUID(), "BMW-320I");

        assertThat(result.status()).isEqualTo(AssemblyStatus.CREATED);
        assertThat(result.modelCode()).isEqualTo("BMW-320I");
    }

    @Test
    void createAssemblyOrderThrowsWhenSourceOrderIdNull() {
        assertThatThrownBy(() -> service.createAssemblyOrder(null, "BMW-320I"))
            .isInstanceOf(DomainValidationException.class);
    }

    @Test
    void createAssemblyOrderThrowsWhenModelCodeBlank() {
        assertThatThrownBy(() -> service.createAssemblyOrder(UUID.randomUUID(), ""))
            .isInstanceOf(DomainValidationException.class);
    }

    @Test
    void markAssembledTransitionsToAssembled() {
        AssemblyOrderEntity entity = createEntity(AssemblyStatus.CREATED);
        when(assemblyOrderRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(assemblyOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.markAssembled(entity.getId());

        assertThat(result.status()).isEqualTo(AssemblyStatus.ASSEMBLED);
    }

    @Test
    void markAssembledThrowsWhenAlreadyAssembled() {
        AssemblyOrderEntity entity = createEntity(AssemblyStatus.ASSEMBLED);
        when(assemblyOrderRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> service.markAssembled(entity.getId()))
            .isInstanceOf(DomainValidationException.class);
    }

    @Test
    void markFailedTransitionsToFail() {
        AssemblyOrderEntity entity = createEntity(AssemblyStatus.CREATED);
        when(assemblyOrderRepository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(assemblyOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = service.markFailed(entity.getId());

        assertThat(result.status()).isEqualTo(AssemblyStatus.FAIL);
    }

    @Test
    void markFailedThrowsWhenAlreadyFailed() {
        AssemblyOrderEntity entity = createEntity(AssemblyStatus.FAIL);
        when(assemblyOrderRepository.findById(entity.getId())).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> service.markFailed(entity.getId()))
            .isInstanceOf(DomainValidationException.class);
    }

    @Test
    void markAssembledThrowsWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(assemblyOrderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.markAssembled(id))
            .isInstanceOf(EntityNotFoundException.class);
    }

    private AssemblyOrderEntity createEntity(AssemblyStatus status) {
        AssemblyOrderEntity e = new AssemblyOrderEntity();
        e.setId(UUID.randomUUID());
        e.setSourceOrderId(UUID.randomUUID());
        e.setModelCode("BMW-320I");
        e.setStatus(status);
        return e;
    }
}
