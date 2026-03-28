package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.infrastructure.persistence.jpa.model.InventoryOrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InventoryOrderJpaRepository extends JpaRepository<InventoryOrderJpaEntity, UUID> {
}
