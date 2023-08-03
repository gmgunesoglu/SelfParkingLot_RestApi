package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot,Long> {
}
