package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.domain.models.ModelComponentOptionEntity;
import com.erofivan.domain.models.ModelComponentOptionId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ModelComponentOptionRepository
    extends JpaRepository<ModelComponentOptionEntity, ModelComponentOptionId> {

    @EntityGraph(attributePaths = {"componentOption"})
    List<ModelComponentOptionEntity> findByIdModelId(UUID modelId);

    @EntityGraph(attributePaths = {"componentOption"})
    List<ModelComponentOptionEntity> findByIdModelIdAndComponentOptionId(UUID modelId, UUID optionId);
}
