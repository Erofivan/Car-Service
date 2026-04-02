package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.infrastructure.persistence.jpa.model.BrandJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BrandJpaRepository extends JpaRepository<BrandJpaEntity, UUID> {
    Optional<BrandJpaEntity> findByCodeAndRemovedFalse(String code);
}
