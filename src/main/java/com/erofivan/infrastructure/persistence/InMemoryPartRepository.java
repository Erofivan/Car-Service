package com.erofivan.infrastructure.persistence;

import com.erofivan.application.abstractions.persistence.queries.PartQuery;
import com.erofivan.application.abstractions.persistence.repositories.PartRepository;
import com.erofivan.domain.common.ids.PartId;
import com.erofivan.domain.parts.Part;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class InMemoryPartRepository implements PartRepository {
    private final Map<PartId, Part> storage = new LinkedHashMap<>();

    @Override
    public void add(Part part) {
        storage.put(part.getId(), part);
    }

    @Override
    public void update(Part part) {
        storage.put(part.getId(), part);
    }

    @Override
    public void remove(PartId id) {
        storage.remove(id);
    }

    @Override
    public Optional<Part> findById(PartId id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Part> query(PartQuery query) {
        if (query.getNameLike() == null || query.getNameLike().isBlank()) {
            return storage.values().stream().toList();
        }

        String nameLike = query.getNameLike().toLowerCase();
        return storage.values().stream()
                .filter(part -> part.getName().toLowerCase().contains(nameLike))
                .toList();
    }
}
