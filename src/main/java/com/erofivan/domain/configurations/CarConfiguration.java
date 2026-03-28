package com.erofivan.domain.configurations;

import com.erofivan.domain.common.Money;

import java.util.Map;

public record CarConfiguration(String modelCode, Map<String, ComponentOption> selectedOptions) {
    public static CarConfiguration of(String modelCode, Map<String, ComponentOption> selectedOptions) {
        return new CarConfiguration(modelCode, selectedOptions);
    }

    public Money totalSurcharge() {
        return selectedOptions.values().stream()
                .map(ComponentOption::surcharge)
                .reduce(Money.zero(), Money::add);
    }
}
