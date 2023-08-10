package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle,Long> {
    List<Vehicle> getVehiclesByOwnerIdAndEnable(Long ownerId,boolean enabled);
    Vehicle getVehicleByIdAndEnable(Long id,boolean enabled);


}
