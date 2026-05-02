package com.erofivan.application.core.services;

import com.erofivan.domain.exceptions.EntityNotFoundException;
import com.erofivan.domain.models.ComponentOptionEntity;
import com.erofivan.domain.models.ModelComponentOptionEntity;
import com.erofivan.domain.models.ModelComponentOptionId;
import com.erofivan.domain.models.ModelEntity;
import com.erofivan.infrastructure.persistence.jpa.repositories.ComponentOptionRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.ModelComponentOptionRepository;
import com.erofivan.infrastructure.persistence.jpa.repositories.ModelRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfigurationCatalogServiceTest {

    @Mock private ModelRepository modelRepository;
    @Mock private ModelComponentOptionRepository modelComponentOptionRepository;
    @Mock private ComponentOptionRepository componentOptionRepository;

    @InjectMocks
    private ConfigurationCatalogService service;

    @Test
    void getDefaultConfigurationReturnsCorrectPrices() {
        ModelEntity model = createModel("BMW-320I", 50000L);

        ComponentOptionEntity engine = createOption("ENGINE", "2.0L TDI", 5000L);
        ComponentOptionEntity gearbox = createOption("GEARBOX", "Automatic", 3000L);

        when(modelRepository.findByCodeAndRemovedFalse("BMW-320I")).thenReturn(Optional.of(model));
        when(modelComponentOptionRepository.findByIdModelId(model.getId()))
            .thenReturn(List.of(createLink(model, engine, true), createLink(model, gearbox, true)));

        var result = service.getDefaultConfiguration("BMW-320I");

        assertThat(result.modelCode()).isEqualTo("BMW-320I");
        assertThat(result.basePrice()).isEqualTo(50000L);
        assertThat(result.totalSurcharge()).isEqualTo(8000L);
        assertThat(result.totalPrice()).isEqualTo(58000L);
        assertThat(result.selectedOptions()).hasSize(2);
    }

    @Test
    void getDefaultConfigurationPicksOnlyFirstOptionPerSlot() {
        ModelEntity model = createModel("BMW-320I", 50000L);

        ComponentOptionEntity opt1 = createOption("ENGINE", "2.0L TDI", 5000L);
        ComponentOptionEntity opt2 = createOption("ENGINE", "3.0L TDI", 8000L);

        when(modelRepository.findByCodeAndRemovedFalse("BMW-320I")).thenReturn(Optional.of(model));
        when(modelComponentOptionRepository.findByIdModelId(model.getId()))
            .thenReturn(List.of(createLink(model, opt1, true), createLink(model, opt2, false)));

        var result = service.getDefaultConfiguration("BMW-320I");

        assertThat(result.selectedOptions()).hasSize(1);
        assertThat(result.selectedOptions().get(0).optionName()).isEqualTo("2.0L TDI");
    }

    @Test
    void getDefaultConfigurationThrowsWhenModelNotFound() {
        when(modelRepository.findByCodeAndRemovedFalse("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getDefaultConfiguration("UNKNOWN"))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void buildConfigurationWithEmptyOptionIdsReturnsDefault() {
        ModelEntity model = createModel("BMW-320I", 50000L);
        ComponentOptionEntity engine = createOption("ENGINE", "2.0L TDI", 5000L);

        when(modelRepository.findByCodeAndRemovedFalse("BMW-320I")).thenReturn(Optional.of(model));
        when(modelComponentOptionRepository.findByIdModelId(model.getId()))
            .thenReturn(List.of(createLink(model, engine, true)));

        var result = service.buildConfiguration("BMW-320I", List.of());

        assertThat(result.modelCode()).isEqualTo("BMW-320I");
    }

    private ModelEntity createModel(String code, long basePrice) {
        ModelEntity m = new ModelEntity();
        m.setId(UUID.randomUUID());
        m.setCode(code);
        m.setName("3 Series");
        m.setBasePrice(basePrice);
        return m;
    }

    private ComponentOptionEntity createOption(String slotName, String name, long surcharge) {
        ComponentOptionEntity o = new ComponentOptionEntity();
        o.setId(UUID.randomUUID());
        o.setSlotName(slotName);
        o.setName(name);
        o.setSurcharge(surcharge);
        return o;
    }

    private ModelComponentOptionEntity createLink(ModelEntity model, ComponentOptionEntity option, boolean required) {
        ModelComponentOptionEntity link = new ModelComponentOptionEntity();
        link.setId(new ModelComponentOptionId(model.getId(), option.getId()));
        link.setModel(model);
        link.setComponentOption(option);
        link.setRequired(required);
        return link;
    }
}
