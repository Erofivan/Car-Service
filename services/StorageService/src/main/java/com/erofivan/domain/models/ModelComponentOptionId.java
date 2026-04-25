package com.erofivan.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ModelComponentOptionId implements Serializable {
    @Column(name = "model_id", nullable = false)
    private UUID modelId;

    @Column(name = "component_option_id", nullable = false)
    private UUID componentOptionId;
}
