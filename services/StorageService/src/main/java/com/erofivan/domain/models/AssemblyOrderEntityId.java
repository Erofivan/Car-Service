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
public class AssemblyOrderEntityId implements Serializable {
    @Column(name = "assembly_id", nullable = false)
    private UUID assemblyId;

    @Column(name = "source_order_id", nullable = false)
    private UUID sourceOrderId;
}
