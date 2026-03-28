package com.erofivan.domain.cars.brands.bmw.models.bmw330i;

import com.erofivan.domain.common.Money;
import com.erofivan.domain.configurations.CarModelSpec;
import com.erofivan.domain.configurations.ComponentOption;
import com.erofivan.domain.configurations.ComponentSlot;

import java.util.List;
import java.util.Set;

public final class Bmw330iModelSpec implements CarModelSpec {
    @Override
    public String modelCode() {
        return "BMW-330I";
    }

    @Override
    public Money basePrice() {
        return Money.of(4_500_000L);
    }

    @Override
    public List<ComponentSlot> slots() {
        return List.of(
            ComponentSlot.of(
                "Wheels",
                ComponentOption.of("18'' Standard", Money.of(0), Set.of("BMW-330I")),
                List.of(ComponentOption.of("19'' M-Sport", Money.of(95_000L), Set.of("BMW-330I")))
            ),
            ComponentSlot.of(
                "Transmission",
                ComponentOption.of("Automatic 8AT", Money.of(0), Set.of("BMW-330I")),
                List.of(ComponentOption.of("Manual 6MT", Money.of(-30_000L), Set.of("BMW-330I")))
            ),
            ComponentSlot.of(
                "Steering wheel",
                ComponentOption.of("Sport leather", Money.of(0), Set.of("BMW-330I")),
                List.of(ComponentOption.of("M-Sport heated", Money.of(25_000L), Set.of("BMW-330I")))
            ),
            ComponentSlot.of(
                "Interior",
                ComponentOption.of("Leather Dakota", Money.of(0), Set.of("BMW-330I")),
                List.of(ComponentOption.of("Sport Performance", Money.of(160_000L), Set.of("BMW-330I")))
            )
        );
    }
}
