package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.infrastructure.persistence.jpa.model.CustomOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomOrderRepository extends JpaRepository<CustomOrderEntity, UUID> {
}
