package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.infrastructure.persistence.jpa.model.ModelJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModelJpaRepository extends JpaRepository<ModelJpaEntity, UUID> {
    Optional<ModelJpaEntity> findByCodeAndRemovedFalse(String code);

    List<ModelJpaEntity> findByBrandCodeAndRemovedFalse(String brandCode);
}
