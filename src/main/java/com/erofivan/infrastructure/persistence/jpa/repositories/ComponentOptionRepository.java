package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.domain.models.ComponentOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ComponentOptionRepository extends JpaRepository<ComponentOptionEntity, UUID> {
}
