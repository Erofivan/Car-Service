package com.erofivan.domain.parts;

import java.util.Set;

public record PartCompatibility(Set<String> modelCodes) {
    public static PartCompatibility of(Set<String> modelCodes) {
        return new PartCompatibility(modelCodes);
    }

    public boolean supports(String modelCode) {
        return modelCodes.contains(modelCode);
    }
}
