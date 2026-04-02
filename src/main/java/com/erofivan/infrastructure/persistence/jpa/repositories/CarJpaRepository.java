package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.infrastructure.persistence.jpa.model.CarJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CarJpaRepository extends JpaRepository<CarJpaEntity, UUID>, JpaSpecificationExecutor<CarJpaEntity> {
}
