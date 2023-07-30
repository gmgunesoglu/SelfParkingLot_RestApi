package com.SoftTech.SelfParkingLot_RestApi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
            name="name",
            nullable = false,
            length = 40
    )
    private  String name;

    //geçici olarak OneToOne
    @OneToOne(targetEntity = Location.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id",referencedColumnName = "id",nullable = false)
    private Location location;


}
