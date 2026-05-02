package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.domain.models.TestDriveRequestEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TestDriveRequestRepository extends JpaRepository<TestDriveRequestEntity, UUID> {

    @EntityGraph(attributePaths = {"client"})
    List<TestDriveRequestEntity> findAllBy();
}
