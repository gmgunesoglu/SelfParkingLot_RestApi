package com.SoftTech.SelfParkingLot_RestApi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name="location")
@Data
@RequiredArgsConstructor
public class Location {

    @Id
    @SequenceGenerator(
            name = "location_seq",
            sequenceName = "location_seq",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "location_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name="city",
            nullable = false,
            length = 30
    )
    private  String city;

    @Column(
            name="town",
            nullable = false,
            length = 30
    )
    private  String town;

    @Column(
            name="district",
            nullable = false,
            length = 40
    )
    private  String district;

    @Column(
            name="address",
            nullable = false,
            length = 200
    )
    private  String address;
}
