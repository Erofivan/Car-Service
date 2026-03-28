package com.erofivan.domain.cars.brands.bmw.models.bmwm340i;

import com.erofivan.domain.common.Money;
import com.erofivan.domain.configurations.CarModelSpec;
import com.erofivan.domain.configurations.ComponentOption;
import com.erofivan.domain.configurations.ComponentSlot;

import java.util.List;
import java.util.Set;

public final class BmwM340iModelSpec implements CarModelSpec {
    @Override
    public String modelCode() {
        return "BMW-M340I";
    }

    @Override
    public Money basePrice() {
        return Money.of(5_300_000L);
    }

    @Override
    public List<ComponentSlot> slots() {
        return List.of(
                ComponentSlot.of(
                        "Wheels",
                        ComponentOption.of("19'' M", Money.of(0), Set.of("BMW-M340I")),
                        List.of(ComponentOption.of("20'' Track", Money.of(140_000L), Set.of("BMW-M340I")))
                ),
                ComponentSlot.of(
                        "Transmission",
                        ComponentOption.of("Automatic 8AT", Money.of(0), Set.of("BMW-M340I")),
                        List.of()
                ),
                ComponentSlot.of(
                        "Steering wheel",
                        ComponentOption.of("M Sport", Money.of(0), Set.of("BMW-M340I")),
                        List.of(ComponentOption.of("M Sport Pro", Money.of(45_000L), Set.of("BMW-M340I")))
                ),
                ComponentSlot.of(
                        "Interior",
                        ComponentOption.of("Alcantara", Money.of(0), Set.of("BMW-M340I")),
                        List.of(ComponentOption.of("Sport Performance", Money.of(160_000L), Set.of("BMW-M340I")))
                )
        );
    }
}
