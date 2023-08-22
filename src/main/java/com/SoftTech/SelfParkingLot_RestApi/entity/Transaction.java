package com.SoftTech.SelfParkingLot_RestApi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Table(name="transaction")
@Data
@RequiredArgsConstructor
public class Transaction {

    @Id
    @SequenceGenerator(
            name = "transaction_seq",
            sequenceName = "transaction_seq",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transaction_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name="plate",
            nullable = false,
            length = 12
    )
    private  String plate;

    @Column(
            name = "parking_spot_id",
            nullable = false
    )
    private Long parkingSpotId;

    @Column(
            name="entery_date",
            nullable = false
    )
    private Date enteryDate;

    @Column(name = "exit_date")
    private Date exitDate;

    @Column(
            name = "payment_token",
            length = 64
    )
    private String  paymentToken;

    @Column(
            name = "enable",
            nullable = false
    )
    private boolean enable;
}
