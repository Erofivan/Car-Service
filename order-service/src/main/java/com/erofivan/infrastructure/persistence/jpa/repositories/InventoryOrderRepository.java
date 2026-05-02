package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.domain.models.InventoryOrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InventoryOrderRepository extends JpaRepository<InventoryOrderEntity, UUID> {

    @EntityGraph(attributePaths = {"client", "manager"})
    List<InventoryOrderEntity> findAllBy();

    @EntityGraph(attributePaths = {"client", "manager"})
    List<InventoryOrderEntity> findByClientIdAndRemovedFalse(UUID clientId);
}
