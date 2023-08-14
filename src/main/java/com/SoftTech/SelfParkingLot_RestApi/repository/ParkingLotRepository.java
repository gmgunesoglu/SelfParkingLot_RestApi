package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingLotRepository extends JpaRepository<ParkingLot,Long> {

    List<ParkingLot> findByOwnerIdAndEnable(Long ownerId,Boolean enable);

    ParkingLot getParkingLotByIdAndEnable(Long parkingLotId,Boolean enable);

}
