package com.erofivan.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "processed_events")
public class ProcessedEventEntity {
    @Id
    @Column(nullable = false, updatable = false, length = 128)
    private String id;

    @Column(nullable = false, updatable = false)
    private Instant processedAt;

    @PrePersist
    private void onCreate() {
        this.processedAt = Instant.now();
    }
}
