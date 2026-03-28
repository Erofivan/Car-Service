package com.erofivan.infrastructure.persistence.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class PartModelCompatibilityId implements Serializable {
    @Column(name = "part_id", nullable = false)
    private UUID partId;

    @Column(name = "model_id", nullable = false)
    private UUID modelId;

    public PartModelCompatibilityId(UUID partId, UUID modelId) {
        this.partId = partId;
        this.modelId = modelId;
    }
}
