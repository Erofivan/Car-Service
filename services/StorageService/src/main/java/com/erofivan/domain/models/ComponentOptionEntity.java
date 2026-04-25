package com.erofivan.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "component_options")
public class ComponentOptionEntity extends BaseEntity {
    @Column(nullable = false, length = 64)
    private String slotName;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(nullable = false)
    private long surcharge;
}
