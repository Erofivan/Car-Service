package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.infrastructure.persistence.jpa.model.InventoryOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InventoryOrderRepository extends JpaRepository<InventoryOrderEntity, UUID> {
}
