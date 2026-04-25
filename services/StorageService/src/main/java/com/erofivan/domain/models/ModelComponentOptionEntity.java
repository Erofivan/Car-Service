package com.erofivan.domain.models;

import jakarta.persistence.Column;
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
@Table(name = "model_component_options")
public class ModelComponentOptionEntity {
    @EmbeddedId
    private ModelComponentOptionId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("modelId")
    @JoinColumn(name = "model_id", nullable = false)
    private ModelEntity model;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("componentOptionId")
    @JoinColumn(name = "component_option_id", nullable = false)
    private ComponentOptionEntity componentOption;

    @Column(nullable = false)
    private boolean required;
}
