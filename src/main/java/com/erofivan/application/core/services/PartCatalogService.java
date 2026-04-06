package com.erofivan.application.core.services;

import com.erofivan.application.contracts.services.PartService;
import com.erofivan.domain.models.PartEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.PartRepository;
import com.erofivan.presentation.dtos.responses.PartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartCatalogService implements PartService {
    private final PartRepository partRepository;

    public List<PartResponse> listParts() {
        return partRepository.findByRemovedFalse().stream()
            .map(this::toResponse)
            .toList();
    }

    private PartResponse toResponse(PartEntity entity) {
        return new PartResponse(entity.getId(), entity.getName(), entity.getDescription(), entity.getPrice());
    }
}
