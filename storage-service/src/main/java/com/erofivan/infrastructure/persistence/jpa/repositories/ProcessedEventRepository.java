package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.domain.models.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEventEntity, String> {
}
