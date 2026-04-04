package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.infrastructure.persistence.jpa.model.TestDriveRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestDriveRequestRepository extends JpaRepository<TestDriveRequestEntity, UUID> {
}
