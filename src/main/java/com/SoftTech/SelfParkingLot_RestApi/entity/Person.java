package com.SoftTech.SelfParkingLot_RestApi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="person")
@Data
@RequiredArgsConstructor
public class Person {

    @Id
    @SequenceGenerator(
            name = "person_seq",
            sequenceName = "person_seq",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "person_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name="user_name",
            nullable = false,
            length = 20,
            unique = true
    )
    private  String userName;

    @Column(
            name="password",
            nullable = false,
            length = 60
    )
    private  String password;

    @Column(
            name="first_name",
            nullable = false,
            length = 30
    )
    private  String firstName;

    @Column(
            name="last_name",
            nullable = false,
            length = 20
    )
    private  String lastName;

    //yetkilendirme
    @Column(
            name = "authority",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(
            name="phone_number",
            nullable = false,
            length = 15
    )
    private String phoneNumber;

    @Column(
            name="email",
            nullable = false,
            length = 30,
            unique = true
    )
    private String email;

//    @ManyToMany(cascade = CascadeType.REMOVE)
//    @JoinTable(
//            name = "shared_parking_lot",
//            joinColumns = @JoinColumn(name = "part_owner_id"),
//            inverseJoinColumns = @JoinColumn(name = "parking_lot_id"),
//            uniqueConstraints = @UniqueConstraint(columnNames = {"part_owner_id", "parking_lot_id"})
//    )
//    private List<ParkingLot> allParkingLots;    //Owner olduğu + ortaklık

    @OneToMany(targetEntity = SharedParkingLot.class)
    @JoinColumn(name="partner_id",referencedColumnName = "id")
    private List<SharedParkingLot> sharedParkingLots;    //Ortak olduğu

    @OneToMany(targetEntity = ParkingLot.class)
    @JoinColumn(name="owner_id",referencedColumnName = "id")
    private List<ParkingLot> parkingLots;     //Owner olduğu

    @OneToMany(targetEntity = Vehicle.class,fetch = FetchType.LAZY)
    @JoinColumn(name="person_id",referencedColumnName = "id")
    private List<Vehicle> Vehicles;

    public Collection<? extends GrantedAuthority> getAuthhorities(){
        return List.of(new SimpleGrantedAuthority(authority.name()));
    }

}
