package com.SoftTech.SelfParkingLot_RestApi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name="vehicle")
@Data
@RequiredArgsConstructor
public class Vehicle {

    @Id
    @SequenceGenerator(
            name = "vehicle_seq",
            sequenceName = "vehicle_seq",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "vehicle_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name="color",
            nullable = false,
            length = 12
    )
    private  String color;

    @Column(
            name="model",
            nullable = false
    )
    private  int model;

    @Column(
            name="vehicleType"
    )
    @Enumerated(EnumType.ORDINAL)
    private  VehicleType vehicleType;

    @Column(
            name="plate",
            nullable = false,
            length = 12,
            unique = true
    )
    private  String plate;

    @Column(
            name = "owner_id",
            nullable = false
    )
    private Long ownerId;

    @Column(
            name = "enable",
            nullable = false
    )
    private boolean enable;
}
