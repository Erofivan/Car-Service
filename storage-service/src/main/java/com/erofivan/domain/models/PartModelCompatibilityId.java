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
public class PartModelCompatibilityId implements Serializable {
    @Column(name = "part_id", nullable = false)
    private UUID partId;

    @Column(name = "model_id", nullable = false)
    private UUID modelId;
}
