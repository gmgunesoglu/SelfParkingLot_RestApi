package com.SoftTech.SelfParkingLot_RestApi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Table(name="users")
@Data
@RequiredArgsConstructor
public class User {

    @Id
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_seq",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name="user_name",
            nullable = false,
            length = 20
    )
    private  String userName;

    @Column(
            name="password",
            nullable = false,
            length = 20
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

    @Column(
            name="phone_number",
            nullable = false,
            length = 30,
            unique = true
    )
    private String phoneNumber;

    @Column(
            name="email",
            nullable = false,
            length = 30,
            unique = true
    )
    private String email;

    @OneToMany(targetEntity = Authorization.class, cascade = CascadeType.ALL)
    @JoinColumn(name="user_id",referencedColumnName = "id")
    private List<Authorization> authorizations;

    @ManyToMany
    @JoinTable(
            name = "users_parking_lot",
             joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "parking_lot_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "parking_lot_id"})
    )
    private List<ParkingLot> parkingLots;

}
