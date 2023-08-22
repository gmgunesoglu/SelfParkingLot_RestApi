package com.SoftTech.SelfParkingLot_RestApi.entity;

import jakarta.persistence.*;
import jdk.dynalink.linker.LinkerServices;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.Mapping;

import java.util.List;

@Entity
@Table(name = "location", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"city", "town", "district"})
})
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
            name = "enable",
            nullable = false
    )
    private boolean enable;

    //cascade yok!
    @OneToMany(targetEntity = ParkingLot.class, cascade = {}, fetch =  FetchType.LAZY)
    @JoinColumn(name="location_id",referencedColumnName = "id")
    private List<ParkingLot> parkingLots;
}
