package com.SoftTech.SelfParkingLot_RestApi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Table(name="parking_lot")
@Data
@RequiredArgsConstructor
public class ParkingLot {

    @Id
    @SequenceGenerator(
            name = "parking_lot_seq",
            sequenceName = "parking_lot_seq",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "parking_lot_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "owner_id",
            nullable = false
    )
    private Long ownerId;

    @Column(
            name="name",
            nullable = false,
            length = 40
    )
    private  String name;

    @Column(
            name = "location_id",
            nullable = false
    )
    private Long locationId;

    @Column(
            name="address",
            nullable = false,
            length = 300
    )
    private  String address;

    @Column(
            name = "enable",
            nullable = false
    )
    private boolean enable;

    @OneToMany(targetEntity = SharedParkingLot.class)
    @JoinColumn(name="parking_lot_id",referencedColumnName = "id")
    private List<SharedParkingLot> sharedParkingLots;    //Owner olduğu + ortaklık

    @OneToMany(targetEntity = ParkingSpot.class)
    @JoinColumn(name="parking_lot_id",referencedColumnName = "id")
    private List<ParkingSpot> parkingSpots;

    @OneToMany(targetEntity = PaymentRecipe.class)
    @JoinColumn(name="parking_lot_id",referencedColumnName = "id")
    private List<PaymentRecipe> paymentRecipes;
}
