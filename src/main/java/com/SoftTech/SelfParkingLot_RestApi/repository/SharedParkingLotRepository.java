package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.entity.SharedParkingLot;
import com.SoftTech.SelfParkingLot_RestApi.entity.SharedParkingLotId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedParkingLotRepository extends JpaRepository<SharedParkingLot, SharedParkingLotId> {
}
