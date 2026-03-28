package com.erofivan.application.core;

import com.erofivan.application.abstractions.persistence.IPersistenceContext;
import com.erofivan.application.abstractions.persistence.queries.PartQuery;
import com.erofivan.application.contracts.parts.PartServiceContract;
import com.erofivan.application.contracts.parts.dtos.PartDto;
import com.erofivan.application.core.mappings.PartMappings;

import java.util.List;

public final class PartService implements PartServiceContract {
    private final IPersistenceContext context;

    public PartService(IPersistenceContext context) {
        this.context = context;
    }

    @Override
    public List<PartDto> listParts() {
        return context.parts().query(PartQuery.builder().build()).stream().map(PartMappings::toDto).toList();
    }
}
