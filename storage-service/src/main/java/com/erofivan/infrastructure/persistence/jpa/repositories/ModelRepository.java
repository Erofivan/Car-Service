package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.domain.models.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModelRepository extends JpaRepository<ModelEntity, UUID> {
    Optional<ModelEntity> findByCodeAndRemovedFalse(String code);

    List<ModelEntity> findByBrandCodeAndRemovedFalse(String brandCode);
}
