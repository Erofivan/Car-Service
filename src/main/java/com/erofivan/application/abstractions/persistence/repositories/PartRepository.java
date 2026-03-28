package com.erofivan.application.abstractions.persistence.repositories;

import com.erofivan.application.abstractions.persistence.queries.PartQuery;
import com.erofivan.domain.common.ids.PartId;
import com.erofivan.domain.parts.Part;

import java.util.List;
import java.util.Optional;

public interface PartRepository {
    void add(Part part);

    void update(Part part);

    void remove(PartId id);

    Optional<Part> findById(PartId id);

    List<Part> query(PartQuery query);
}
