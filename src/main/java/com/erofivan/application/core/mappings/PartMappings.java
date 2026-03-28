package com.erofivan.application.core.mappings;

import com.erofivan.application.contracts.parts.dtos.PartDto;
import com.erofivan.domain.parts.Part;

public final class PartMappings {
    private PartMappings() {
    }

    public static PartDto toDto(Part part) {
        return new PartDto(
            part.getId().toString(),
            part.getName(),
            part.getDescription(),
            part.getPrice().value()
        );
    }
}
