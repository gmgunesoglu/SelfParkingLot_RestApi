package com.SoftTech.SelfParkingLot_RestApi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name="authorizations")   //postgresql de authorization adında olmuyor
@Data
@RequiredArgsConstructor
@IdClass(AuthorizationId.class)
public class Authorization {

    @Id
    @Column(
            name = "user_id",
            updatable = false
    )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Id
    @Column(
            name="authority",
            nullable = false,
            length = 30
    )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  String authority;
}
