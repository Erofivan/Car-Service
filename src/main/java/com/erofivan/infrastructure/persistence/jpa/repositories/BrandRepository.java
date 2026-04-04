package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.domain.models.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<BrandEntity, UUID> {
    Optional<BrandEntity> findByCodeAndRemovedFalse(String code);
}
