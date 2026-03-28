package com.erofivan.domain.configurations;

import com.erofivan.domain.common.Money;

import java.util.Set;

public record ComponentOption(String name, Money surcharge, Set<String> compatibleModelCodes) {
    public static ComponentOption of(String name, Money surcharge, Set<String> compatibleModelCodes) {
        return new ComponentOption(name, surcharge, compatibleModelCodes);
    }

    public boolean isCompatibleWith(String modelCode) {
        return compatibleModelCodes.contains(modelCode);
    }
}
