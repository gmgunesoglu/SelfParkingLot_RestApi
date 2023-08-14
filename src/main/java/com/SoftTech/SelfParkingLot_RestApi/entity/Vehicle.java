package com.SoftTech.SelfParkingLot_RestApi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "vehicle")
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
            name="vehicleType",
            nullable = false
    )
    @Enumerated(EnumType.ORDINAL)
    private  VehicleType vehicleType;

    @Column(
            name="plate",
            nullable = false,
            length = 12
    )
    private  String plate;

    @Column(
            name = "owner_id",
            nullable = false
    )
    private Long ownerId;

    @Column(
            name = "added_date",
            nullable = false
    )
    private Date addedDate;

    @Column(
            name = "removed_date"
    )
    private Date removedDate;

    @Column(
            name = "enable",
            nullable = false
    )
    private boolean enable;
}
