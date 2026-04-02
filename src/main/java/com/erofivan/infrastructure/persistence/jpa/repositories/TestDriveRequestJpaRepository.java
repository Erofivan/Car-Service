package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.infrastructure.persistence.jpa.model.TestDriveRequestJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestDriveRequestJpaRepository extends JpaRepository<TestDriveRequestJpaEntity, UUID> {
}
