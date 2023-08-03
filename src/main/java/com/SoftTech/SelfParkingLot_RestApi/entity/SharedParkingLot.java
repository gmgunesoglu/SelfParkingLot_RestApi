package com.SoftTech.SelfParkingLot_RestApi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name="shared_parking_lot")
@Data
@RequiredArgsConstructor
@IdClass(SharedParkingLotId.class)
public class SharedParkingLot {


    @Id
    @Column(name="parking_lot_id")
    private Long parkingLotId;


    @Id
    @Column(name="partner_id")
    private Long partnerId;
}
