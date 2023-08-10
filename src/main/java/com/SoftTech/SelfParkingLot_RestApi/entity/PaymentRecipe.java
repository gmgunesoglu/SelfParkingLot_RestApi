package com.SoftTech.SelfParkingLot_RestApi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Table(name = "payment_recipe", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"parking_lot_id", "commet"})
})
@Data
@RequiredArgsConstructor
public class PaymentRecipe {

    @Id
    @SequenceGenerator(
            name = "payment_recipe_seq",
            sequenceName = "payment_recipe_seq",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "payment_recipe_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "parking_lot_id",
            nullable = false
    )
    private Long parkingLotId;

    @Column(
            name="commet",
            nullable = false,
            length = 20
    )
    private  String commet;

    @Column(
            name = "hours_2",
            nullable = false
    )
    private int hours2;

    @Column(
            name = "hours_4",
            nullable = false
    )
    private int hours4;

    @Column(
            name = "hours_6",
            nullable = false
    )
    private int hours6;

    @Column(
            name = "hours_10",
            nullable = false
    )
    private int hours10;

    @Column(
            name = "hours_24",
            nullable = false
    )
    private int hours24;

    @Column(
            name = "enable",
            nullable = false
    )
    private boolean enable;

    @OneToMany(targetEntity = ParkingSpot.class)
    @JoinColumn(name="payment_recipe_id",referencedColumnName = "id")
    private List<ParkingSpot> parkingSpots;
}
