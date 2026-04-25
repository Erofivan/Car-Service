package com.erofivan.application.core.services;

import com.erofivan.application.contracts.services.AssemblyOrderService;
import com.erofivan.domain.AssemblyStatus;
import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.models.AssemblyOrderEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.AssemblyOrderRepository;
import com.erofivan.presentation.dtos.responses.AssemblyOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssemblyOrderCatalogService implements AssemblyOrderService {
    private final AssemblyOrderRepository assemblyOrderRepository;

    @Transactional(readOnly = true)
    public List<AssemblyOrderResponse> getAssemblyOrders() {
        return assemblyOrderRepository.findAll().stream()
            .map(e -> new AssemblyOrderResponse(e.getId(), e.getSourceOrderId(), e.getModelCode(), e.getStatus()))
            .toList();
    }

    @Transactional(readOnly = true)
    public AssemblyOrderResponse getAssemblyOrder(UUID id) {
        AssemblyOrderEntity entity = assemblyOrderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("AssemblyOrder", id.toString()));
        return new AssemblyOrderResponse(entity.getId(), entity.getSourceOrderId(), entity.getModelCode(), entity.getStatus());
    }

    @Transactional
    public AssemblyOrderResponse createAssemblyOrder(UUID sourceOrderId, String modelCode) {
        if (sourceOrderId == null)
            throw new DomainValidationException("sourceOrderId is required");

        if (modelCode == null || modelCode.isBlank())
            throw new DomainValidationException("modelCode is required");

        AssemblyOrderEntity entity = new AssemblyOrderEntity();
        entity.setSourceOrderId(sourceOrderId);
        entity.setModelCode(modelCode);
        entity.setStatus(AssemblyStatus.CREATED);

        assemblyOrderRepository.save(entity);

        return new AssemblyOrderResponse(entity.getId(), entity.getSourceOrderId(), entity.getModelCode(), entity.getStatus());
    }

    @Transactional
    public AssemblyOrderResponse markAssembled(UUID id) {
        AssemblyOrderEntity entity = assemblyOrderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("AssemblyOrder", id.toString()));

        if (entity.getStatus() != AssemblyStatus.CREATED)
            throw new DomainValidationException("Only CREATED orders can be marked as assembled");

        entity.setStatus(AssemblyStatus.ASSEMBLED);

        assemblyOrderRepository.save(entity);

        return new AssemblyOrderResponse(entity.getId(), entity.getSourceOrderId(), entity.getModelCode(), entity.getStatus());
    }

    @Transactional
    public AssemblyOrderResponse markFailed(UUID id) {
        AssemblyOrderEntity entity = assemblyOrderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("AssemblyOrder", id.toString()));

        if (entity.getStatus() != AssemblyStatus.CREATED)
            throw new DomainValidationException("Only CREATED orders can be marked as failed");

        entity.setStatus(AssemblyStatus.FAIL);

        assemblyOrderRepository.save(entity);

        return new AssemblyOrderResponse(entity.getId(), entity.getSourceOrderId(), entity.getModelCode(), entity.getStatus());
    }
}
