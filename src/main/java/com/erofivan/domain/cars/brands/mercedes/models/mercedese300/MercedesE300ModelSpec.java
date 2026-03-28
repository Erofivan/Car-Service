package com.erofivan.domain.cars.brands.mercedes.models.mercedese300;

import com.erofivan.domain.common.Money;
import com.erofivan.domain.configurations.CarModelSpec;
import com.erofivan.domain.configurations.ComponentOption;
import com.erofivan.domain.configurations.ComponentSlot;

import java.util.List;
import java.util.Set;

public final class MercedesE300ModelSpec implements CarModelSpec {
    @Override
    public String modelCode() {
        return "MERCEDES-E300";
    }

    @Override
    public Money basePrice() {
        return Money.of(4_900_000L);
    }

    @Override
    public List<ComponentSlot> slots() {
        return List.of(
            ComponentSlot.of("Wheels", ComponentOption.of("18'' Standard", Money.of(0), Set.of("MERCEDES-E300")),
                List.of(ComponentOption.of("19'' AMG", Money.of(120_000L), Set.of("MERCEDES-E300")))),
            ComponentSlot.of("Transmission", ComponentOption.of("9G-TRONIC", Money.of(0), Set.of("MERCEDES-E300")), List.of()),
            ComponentSlot.of("Steering wheel", ComponentOption.of("Multifunction", Money.of(0), Set.of("MERCEDES-E300")),
                List.of(ComponentOption.of("AMG wheel", Money.of(45_000L), Set.of("MERCEDES-E300")))),
            ComponentSlot.of("Interior", ComponentOption.of("Artico", Money.of(0), Set.of("MERCEDES-E300")),
                List.of(ComponentOption.of("Nappa leather", Money.of(130_000L), Set.of("MERCEDES-E300"))))
        );
    }
}
