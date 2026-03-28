package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.infrastructure.persistence.jpa.model.ModelComponentOptionId;
import com.erofivan.infrastructure.persistence.jpa.model.ModelComponentOptionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ModelComponentOptionJpaRepository
    extends JpaRepository<ModelComponentOptionJpaEntity, ModelComponentOptionId> {
    List<ModelComponentOptionJpaEntity> findByIdModelId(UUID modelId);
}
