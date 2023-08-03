package com.SoftTech.SelfParkingLot_RestApi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Table(name = "parking_spot", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"parking_lot_id", "name"})
})
@Data
@RequiredArgsConstructor
public class ParkingSpot {

    @Id
    @SequenceGenerator(
            name = "parking_spot_seq",
            sequenceName = "parking_spot_seq",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "parking_spot_seq"
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
            name="name",
            nullable = false,
            length = 30
    )
    private  String name;

    @Column(
            name = "payment_recipe_id",
            nullable = false
    )
    private Long paymentRecipeId;

    @Column(
            name="vehicle_type_code",
            nullable = false
    )
    private int vehicleTypeCode;

    @Column(
            name = "indoor",
            nullable = false
    )
    private boolean indoor;

    @OneToMany(targetEntity = Transaction.class)
    @JoinColumn(name="parking_spot_id",referencedColumnName = "id")
    private List<Transaction> transactions;
}
