package com.erofivan.application.core.services;

import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.exceptions.IncompatibleComponentException;
import com.erofivan.domain.models.ComponentOptionEntity;
import com.erofivan.domain.models.ModelComponentOptionEntity;
import com.erofivan.domain.models.ModelEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.ComponentOptionRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.ModelComponentOptionRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.ModelRepository;
import com.erofivan.presentation.dtos.ConfigurationOption;
import com.erofivan.presentation.dtos.responses.ConfigurationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConfigurationCatalogService {
    private final ModelRepository modelRepository;
    private final ModelComponentOptionRepository modelComponentOptionRepository;
    private final ComponentOptionRepository componentOptionRepository;

    public ConfigurationResponse getDefaultConfiguration(String modelCode) {
        ModelEntity model = modelRepository.findByCodeAndRemovedFalse(modelCode)
            .orElseThrow(() -> new EntityNotFoundException("Model", modelCode));

        List<ModelComponentOptionEntity> links =
            modelComponentOptionRepository.findByIdModelId(model.getId());

        List<ConfigurationOption> selectedOptions = new ArrayList<>();
        Set<String> seenSlots = new HashSet<>();

        long totalSurcharge = 0;

        for (ModelComponentOptionEntity link : links) {
            String slotName = link.getComponentOption().getSlotName();
            String optionName = link.getComponentOption().getName();
            long surcharge = link.getComponentOption().getSurcharge();
            if (seenSlots.add(slotName)) {
                selectedOptions.add(new ConfigurationOption(slotName, optionName));
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

    public ConfigurationResponse buildConfiguration(String modelCode, List<UUID> optionIds) {
        ModelEntity model = modelRepository.findByCodeAndRemovedFalse(modelCode)
            .orElseThrow(() -> new EntityNotFoundException("Model", modelCode));

        List<ModelComponentOptionEntity> allLinks =
            modelComponentOptionRepository.findByIdModelId(model.getId());

        Set<String> requiredSlots = allLinks.stream()
            .filter(ModelComponentOptionEntity::isRequired)
            .map(link -> link.getComponentOption().getSlotName())
            .collect(Collectors.toSet());

        Map<UUID, ModelComponentOptionEntity> compatibleOptions = allLinks.stream()
            .collect(Collectors.toMap(
                link -> link.getComponentOption().getId(),
                link -> link,
                (a, b) -> a
            ));

        List<ConfigurationOption> selectedOptions = new ArrayList<>();
        Set<String> coveredSlots = new HashSet<>();

        long totalSurcharge = 0;

        for (UUID optionId : optionIds) {
            ComponentOptionEntity option = componentOptionRepository.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("ComponentOption", optionId.toString()));

            if (!compatibleOptions.containsKey(optionId)) {
                throw new IncompatibleComponentException(option.getName(), modelCode);
            }

            coveredSlots.add(option.getSlotName());
            selectedOptions.add(new ConfigurationOption(option.getSlotName(), option.getName()));
            totalSurcharge += option.getSurcharge();
        }

        for (String requiredSlot : requiredSlots) {
            if (!coveredSlots.contains(requiredSlot)) {
                throw new DomainValidationException(
                    "Missing required slot '" + requiredSlot + "'");
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
