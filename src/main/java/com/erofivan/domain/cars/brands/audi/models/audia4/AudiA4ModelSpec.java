package com.erofivan.domain.cars.brands.audi.models.audia4;

import com.erofivan.domain.common.Money;
import com.erofivan.domain.configurations.CarModelSpec;
import com.erofivan.domain.configurations.ComponentOption;
import com.erofivan.domain.configurations.ComponentSlot;

import java.util.List;
import java.util.Set;

public final class AudiA4ModelSpec implements CarModelSpec {
    @Override
    public String modelCode() {
        return "AUDI-A4";
    }

    @Override
    public Money basePrice() {
        return Money.of(3_800_000L);
    }

    @Override
    public List<ComponentSlot> slots() {
        return List.of(
                ComponentSlot.of("Wheels", ComponentOption.of("17'' Standard", Money.of(0), Set.of("AUDI-A4")),
                        List.of(ComponentOption.of("18'' Sport", Money.of(55_000L), Set.of("AUDI-A4")))),
                ComponentSlot.of("Transmission", ComponentOption.of("S tronic", Money.of(0), Set.of("AUDI-A4")), List.of()),
                ComponentSlot.of("Steering wheel", ComponentOption.of("Standard", Money.of(0), Set.of("AUDI-A4")),
                        List.of(ComponentOption.of("S line", Money.of(30_000L), Set.of("AUDI-A4")))),
                ComponentSlot.of("Interior", ComponentOption.of("Fabric", Money.of(0), Set.of("AUDI-A4")),
                        List.of(ComponentOption.of("Leather", Money.of(90_000L), Set.of("AUDI-A4"))))
        );
    }
}
