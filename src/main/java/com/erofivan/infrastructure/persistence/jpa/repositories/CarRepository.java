package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.domain.models.CarEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface CarRepository extends JpaRepository<CarEntity, UUID>, JpaSpecificationExecutor<CarEntity> {
    @Override
    @EntityGraph(attributePaths = {"model", "model.brand"})
    List<CarEntity> findAll(Specification<CarEntity> spec);
}
