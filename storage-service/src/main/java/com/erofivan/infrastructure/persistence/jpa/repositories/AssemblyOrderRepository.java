package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.domain.models.AssemblyOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssemblyOrderRepository extends JpaRepository<AssemblyOrderEntity, UUID> {
}
