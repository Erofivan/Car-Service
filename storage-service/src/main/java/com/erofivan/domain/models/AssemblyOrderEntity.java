package com.erofivan.domain.models;

import com.erofivan.domain.AssemblyStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "assembly_orders")
public class AssemblyOrderEntity extends BaseEntity {
    @Column(nullable = false)
    private UUID sourceOrderId;

    @Column(nullable = false, length = 64)
    private String modelCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private AssemblyStatus status;
}



