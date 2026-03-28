package com.erofivan.domain.configurations;

import com.erofivan.domain.exceptions.IncompatibleComponentException;
import com.erofivan.domain.exceptions.MissingRequiredSlotException;

import java.util.HashMap;
import java.util.Map;

public final class CarConfigurator {
    private final CarModelSpec modelSpec;
    private final Map<String, ComponentOption> selectedOptions = new HashMap<>();

    public CarConfigurator(CarModelSpec modelSpec) {
        this.modelSpec = modelSpec;
    }

    public void select(String slotName, String optionName) {
        ComponentSlot slot = modelSpec.slots().stream()
            .filter(item -> item.slotName().equals(slotName))
                .findFirst()
                .orElseThrow(() -> new MissingRequiredSlotException(slotName));

        ComponentOption option = slot.allOptions().stream()
            .filter(item -> item.name().equals(optionName))
                .findFirst()
                .orElseThrow(() -> new IncompatibleComponentException(optionName, modelSpec.modelCode()));

        if (!option.isCompatibleWith(modelSpec.modelCode())) {
            throw new IncompatibleComponentException(option.name(), modelSpec.modelCode());
        }

        selectedOptions.put(slotName, option);
    }

    public CarConfiguration build() {
        for (ComponentSlot slot : modelSpec.slots()) {
            if (!selectedOptions.containsKey(slot.slotName())) {
                throw new MissingRequiredSlotException(slot.slotName());
            }
        }

        return CarConfiguration.of(modelSpec.modelCode(), Map.copyOf(selectedOptions));
    }
}
