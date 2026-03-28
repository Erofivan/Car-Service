package com.erofivan.infrastructure.persistence.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "component_options")
public class ComponentOptionJpaEntity extends BaseJpaEntity {
    @Column(nullable = false, length = 64)
    private String slotName;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(nullable = false)
    private long surcharge;
}
