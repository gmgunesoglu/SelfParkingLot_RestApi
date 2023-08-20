package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.dto.ParkingSpotShowDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot,Long> {

    @Query("SELECT new com.SoftTech.SelfParkingLot_RestApi.dto.ParkingSpotShowDTO(ps.id,pr.tag,ps.name,ps.indoor,ps.vehicleTypeCode) FROM " +
            "ParkingSpot ps JOIN PaymentRecipe pr ON ps.paymentRecipeId=pr.id WHERE pr.enable=true AND ps.enable=true AND ps.parkingLotId=:parkingLotId")
    List<ParkingSpotShowDTO> getAllParkingSpotShowDTO(Long parkingLotId);

//    @Query("SELECT new com.SoftTech.SelfParkingLot_RestApi.dto.ParkingSpotShowDTO(ps.id,pr.tag,ps.name,ps.indoor,ps.vehicleTypeCode) FROM " +
//            "ParkingSpot ps JOIN PaymentRecipe pr ON ps.paymentRecipeId=pr.id WHERE pr.enable=true AND ps.enable=true AND ps.parkingLotId=:parkingLotId")
//    ParkingSpotShowDTO getParkingSpotShowDTO(Long id);
    ParkingSpot getParkingSpotByIdAndEnable(Long id,boolean enable);

    ParkingSpot getParkingSpotByParkingLotIdAndName(Long parkingLotId, String name);

    @Query("SELECT pr.tag FROM ParkingSpot ps JOIN PaymentRecipe pr ON ps.paymentRecipeId=pr.id " +
            "WHERE pr.enable=true AND ps.enable=true AND ps.id=:id")
    String getTag(Long id);
}
