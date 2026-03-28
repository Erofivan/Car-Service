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
public class ModelComponentOptionId implements Serializable {
    @Column(name = "model_id", nullable = false)
    private UUID modelId;

    @Column(name = "component_option_id", nullable = false)
    private UUID componentOptionId;

    public ModelComponentOptionId(UUID modelId, UUID componentOptionId) {
        this.modelId = modelId;
        this.componentOptionId = componentOptionId;
    }
}
