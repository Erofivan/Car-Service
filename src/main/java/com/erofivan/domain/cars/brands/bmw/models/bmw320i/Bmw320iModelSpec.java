package com.erofivan.domain.cars.brands.bmw.models.bmw320i;

import com.erofivan.domain.common.Money;
import com.erofivan.domain.configurations.CarModelSpec;
import com.erofivan.domain.configurations.ComponentOption;
import com.erofivan.domain.configurations.ComponentSlot;

import java.util.List;
import java.util.Set;

public final class Bmw320iModelSpec implements CarModelSpec {
    @Override
    public String modelCode() {
        return "BMW-320I";
    }

    @Override
    public Money basePrice() {
        return Money.of(4_000_000L);
    }

    @Override
    public List<ComponentSlot> slots() {
        return List.of(
                ComponentSlot.of(
                        "Wheels",
                        ComponentOption.of("17'' Standard", Money.of(0), Set.of("BMW-320I")),
                        List.of(
                                ComponentOption.of("18'' Aero", Money.of(45_000L), Set.of("BMW-320I")),
                                ComponentOption.of("19'' M-Sport", Money.of(95_000L), Set.of("BMW-320I"))
                        )
                ),
                ComponentSlot.of(
                        "Transmission",
                        ComponentOption.of("Automatic 8AT", Money.of(0), Set.of("BMW-320I")),
                        List.of(ComponentOption.of("Manual 6MT", Money.of(-30_000L), Set.of("BMW-320I")))
                ),
                ComponentSlot.of(
                        "Steering wheel",
                        ComponentOption.of("Sport leather", Money.of(0), Set.of("BMW-320I")),
                        List.of(ComponentOption.of("M-Sport heated", Money.of(25_000L), Set.of("BMW-320I")))
                ),
                ComponentSlot.of(
                        "Interior",
                        ComponentOption.of("Fabric Graphite", Money.of(0), Set.of("BMW-320I")),
                        List.of(ComponentOption.of("Leather Dakota", Money.of(110_000L), Set.of("BMW-320I")))
                )
        );
    }
}
