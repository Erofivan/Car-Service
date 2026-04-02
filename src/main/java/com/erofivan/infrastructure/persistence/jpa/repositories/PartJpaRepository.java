package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.infrastructure.persistence.jpa.model.PartJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PartJpaRepository extends JpaRepository<PartJpaEntity, UUID> {
    List<PartJpaEntity> findByRemovedFalse();
}
