package com.erofivan.application.core.services;

import com.erofivan.domain.exceptions.DomainValidationException;
import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.exceptions.IncompatibleComponentException;
import com.erofivan.domain.models.ComponentOptionEntity;
import com.erofivan.domain.models.ModelComponentOptionEntity;
import com.erofivan.domain.models.ModelComponentOptionId;
import com.erofivan.domain.models.ModelEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.ComponentOptionRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.ModelComponentOptionRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.ModelRepository;
import com.erofivan.presentation.dtos.responses.ConfigurationResponse;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfigurationCatalogServiceTest {

    @Mock
    private ModelRepository modelRepository;
    @Mock
    private ModelComponentOptionRepository modelComponentOptionRepository;
    @Mock
    private ComponentOptionRepository componentOptionRepository;

    @InjectMocks
    private ConfigurationCatalogService service;

    @Test
    void getDefaultConfigurationReturnsFirstOptionPerSlot() {
        ModelEntity model = createModel("X5", 5_000_000L);

        ComponentOptionEntity opt1 = createOption("engine", "V6", 100_000L);
        ComponentOptionEntity opt2 = createOption("engine", "V8", 200_000L);
        ComponentOptionEntity opt3 = createOption("color", "Red", 50_000L);

        when(modelRepository.findByCodeAndRemovedFalse("X5")).thenReturn(Optional.of(model));
        when(modelComponentOptionRepository.findByIdModelId(model.getId()))
            .thenReturn(List.of(link(model, opt1, false), link(model, opt2, false), link(model, opt3, false)));

        ConfigurationResponse result = service.getDefaultConfiguration("X5");

        assertEquals("X5", result.modelCode());
        assertEquals(2, result.selectedOptions().size());
        assertEquals(5_000_000L, result.basePrice());
        assertEquals(150_000L, result.totalSurcharge());
        assertEquals(5_150_000L, result.totalPrice());
    }

    @Test
    void getDefaultConfigurationThrowsWhenModelNotFound() {
        when(modelRepository.findByCodeAndRemovedFalse("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getDefaultConfiguration("UNKNOWN"));
    }

    @Test
    void buildConfigurationDelegatesToDefaultWhenOptionIdsNull() {
        ModelEntity model = createModel("X5", 5_000_000L);

        when(modelRepository.findByCodeAndRemovedFalse("X5")).thenReturn(Optional.of(model));
        when(modelComponentOptionRepository.findByIdModelId(model.getId())).thenReturn(List.of());

        ConfigurationResponse result = service.buildConfiguration("X5", null);

        assertEquals(5_000_000L, result.totalPrice());
    }

    @Test
    void buildConfigurationDelegatesToDefaultWhenOptionIdsEmpty() {
        ModelEntity model = createModel("X5", 5_000_000L);

        when(modelRepository.findByCodeAndRemovedFalse("X5")).thenReturn(Optional.of(model));
        when(modelComponentOptionRepository.findByIdModelId(model.getId())).thenReturn(List.of());

        ConfigurationResponse result = service.buildConfiguration("X5", List.of());

        assertEquals(5_000_000L, result.totalPrice());
    }

    @Test
    void buildConfigurationCalculatesTotalWithSelectedOptions() {
        ModelEntity model = createModel("X5", 5_000_000L);

        ComponentOptionEntity opt1 = createOption("engine", "V8", 200_000L);
        ComponentOptionEntity opt2 = createOption("color", "Red", 50_000L);

        when(modelRepository.findByCodeAndRemovedFalse("X5")).thenReturn(Optional.of(model));
        when(modelComponentOptionRepository.findByIdModelId(model.getId()))
            .thenReturn(List.of(link(model, opt1, true), link(model, opt2, true)));
        when(componentOptionRepository.findById(opt1.getId())).thenReturn(Optional.of(opt1));
        when(componentOptionRepository.findById(opt2.getId())).thenReturn(Optional.of(opt2));

        ConfigurationResponse result = service.buildConfiguration(
            "X5", List.of(opt1.getId(), opt2.getId()));

        assertEquals(5_000_000L, result.basePrice());
        assertEquals(250_000L, result.totalSurcharge());
        assertEquals(5_250_000L, result.totalPrice());
    }

    @Test
    void buildConfigurationThrowsWhenOptionNotCompatible() {
        ModelEntity model = createModel("X5", 5_000_000L);

        ComponentOptionEntity incompatible = createOption("engine", "Rocket", 999_999L);

        when(modelRepository.findByCodeAndRemovedFalse("X5")).thenReturn(Optional.of(model));
        when(modelComponentOptionRepository.findByIdModelId(model.getId())).thenReturn(List.of());
        when(componentOptionRepository.findById(incompatible.getId())).thenReturn(Optional.of(incompatible));

        assertThrows(IncompatibleComponentException.class,
            () -> service.buildConfiguration("X5", List.of(incompatible.getId())));
    }

    @Test
    void buildConfigurationThrowsWhenRequiredSlotMissing() {
        ModelEntity model = createModel("X5", 5_000_000L);

        ComponentOptionEntity engineOpt = createOption("engine", "V6", 100_000L);
        ComponentOptionEntity colorOpt = createOption("color", "Red", 50_000L);

        when(modelRepository.findByCodeAndRemovedFalse("X5")).thenReturn(Optional.of(model));
        when(modelComponentOptionRepository.findByIdModelId(model.getId()))
            .thenReturn(List.of(link(model, engineOpt, true), link(model, colorOpt, true)));
        when(componentOptionRepository.findById(engineOpt.getId())).thenReturn(Optional.of(engineOpt));

        assertThrows(DomainValidationException.class,
            () -> service.buildConfiguration("X5", List.of(engineOpt.getId())));
    }

    private ModelEntity createModel(String code, long basePrice) {
        ModelEntity model = new ModelEntity();
        model.setId(UUID.randomUUID());
        model.setCode(code);
        model.setBasePrice(basePrice);
        return model;
    }

    private ComponentOptionEntity createOption(String slot, String name, long surcharge) {
        ComponentOptionEntity option = new ComponentOptionEntity();
        option.setId(UUID.randomUUID());
        option.setSlotName(slot);
        option.setName(name);
        option.setSurcharge(surcharge);
        return option;
    }

    private ModelComponentOptionEntity link(ModelEntity model, ComponentOptionEntity option, boolean required) {
        ModelComponentOptionEntity link = new ModelComponentOptionEntity();
        link.setId(new ModelComponentOptionId(model.getId(), option.getId()));
        link.setModel(model);
        link.setComponentOption(option);
        link.setRequired(required);
        return link;
    }
}
