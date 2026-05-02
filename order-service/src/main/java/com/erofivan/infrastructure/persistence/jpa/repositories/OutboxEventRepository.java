package com.erofivan.infrastructure.persistence.jpa.repositories;

import com.erofivan.domain.models.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEventEntity, UUID> {
    List<OutboxEventEntity> findTop99ByPublishedAtIsNullOrderByCreatedAtAsc();
}
