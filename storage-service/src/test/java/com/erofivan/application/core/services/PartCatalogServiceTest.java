package com.erofivan.application.core.services;

import com.erofivan.domain.models.PartEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.PartRepository;
import com.erofivan.presentation.dtos.responses.PartResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartCatalogServiceTest {

    @Mock private PartRepository partRepository;

    @InjectMocks
    private PartCatalogService service;

    @Test
    void listPartsReturnsMappedList() {
        PartEntity part = new PartEntity();
        part.setId(UUID.randomUUID());
        part.setName("Tire");
        part.setDescription("All-season");
        part.setPrice(200L);

        when(partRepository.findByRemovedFalse()).thenReturn(List.of(part));

        List<PartResponse> result = service.listParts();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Tire");
        assertThat(result.get(0).price()).isEqualTo(200L);
    }

    @Test
    void listPartsReturnsEmptyWhenNone() {
        when(partRepository.findByRemovedFalse()).thenReturn(List.of());

        assertThat(service.listParts()).isEmpty();
    }
}
