package com.erofivan.application.core;

import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.infrastructure.persistence.jpa.model.ModelComponentOptionJpaEntity;
import com.erofivan.infrastructure.persistence.jpa.model.ModelJpaEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.ModelComponentOptionJpaRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.ModelJpaRepository;
import com.erofivan.presentation.dto.ConfigurationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConfigurationCatalogService {
    private final ModelJpaRepository modelRepository;
    private final ModelComponentOptionJpaRepository modelComponentOptionRepository;

    public ConfigurationResponse getDefaultConfiguration(String modelCode) {
        ModelJpaEntity model = modelRepository.findByCodeAndRemovedFalse(modelCode)
            .orElseThrow(() -> new EntityNotFoundException("Model", modelCode));

        List<ModelComponentOptionJpaEntity> links = modelComponentOptionRepository.findByIdModelId(model.getId());

        Map<String, String> selectedOptions = new LinkedHashMap<>();
        long totalSurcharge = 0;
        for (ModelComponentOptionJpaEntity link : links) {
            String slotName = link.getComponentOption().getSlotName();
            String optionName = link.getComponentOption().getName();
            long surcharge = link.getComponentOption().getSurcharge();
            if (!selectedOptions.containsKey(slotName)) {
                selectedOptions.put(slotName, optionName);
                totalSurcharge += surcharge;
            }
        }

        long basePrice = model.getBasePrice();
        return new ConfigurationResponse(
            model.getCode(),
            selectedOptions,
            basePrice,
            totalSurcharge,
            basePrice + totalSurcharge
        );
    }
}
