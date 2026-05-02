package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.domain.models.CustomOrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomOrderRepository extends JpaRepository<CustomOrderEntity, UUID> {
    @EntityGraph(attributePaths = {"client", "manager"})
    List<CustomOrderEntity> findAllBy();

    @EntityGraph(attributePaths = {"client", "manager"})
    List<CustomOrderEntity> findByClientIdAndRemovedFalse(UUID clientId);
}
