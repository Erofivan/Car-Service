package com.erofivan.infrastructure.persistence.jpa.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "part_model_compatibility")
public class PartModelCompatibilityJpaEntity {
    @EmbeddedId
    private PartModelCompatibilityId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("partId")
    @JoinColumn(name = "part_id", nullable = false)
    private PartJpaEntity part;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("modelId")
    @JoinColumn(name = "model_id", nullable = false)
    private ModelJpaEntity model;
}
