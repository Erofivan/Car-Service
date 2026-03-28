package com.erofivan.domain.configurations;

import java.util.List;
import java.util.stream.Stream;

public record ComponentSlot(String slotName, ComponentOption baseOption, List<ComponentOption> alternatives) {
    public static ComponentSlot of(String slotName, ComponentOption baseOption, List<ComponentOption> alternatives) {
        return new ComponentSlot(slotName, baseOption, alternatives);
    }

    public List<ComponentOption> allOptions() {
        return Stream.concat(Stream.of(baseOption), alternatives.stream()).toList();
    }
}
