package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.infrastructure.persistence.jpa.model.ComponentOptionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ComponentOptionJpaRepository extends JpaRepository<ComponentOptionJpaEntity, UUID> {
}
