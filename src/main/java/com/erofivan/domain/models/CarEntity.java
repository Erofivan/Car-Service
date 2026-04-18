package com.erofivan.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cars")
public class CarEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "model_id", nullable = false)
    private ModelEntity model;

    @Column(nullable = false, length = 64)
    private String bodyType;

    @Column(nullable = false, length = 64)
    private String fuelType;

    @Column(nullable = false)
    private int powerHp;

    @Column(nullable = false)
    private double engineLitres;

    @Column(nullable = false, length = 64)
    private String transmission;

    @Column(nullable = false, length = 64)
    private String drivetrain;

    @Column(nullable = false, length = 64)
    private String color;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    private boolean available;

    @Column(nullable = false)
    private boolean testDriveEnabled;
}
