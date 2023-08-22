package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.dto.DescriptionDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.ParkingSpotDetailDTO;
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

    @Query("SELECT new com.SoftTech.SelfParkingLot_RestApi.dto.ParkingSpotDetailDTO(ps.id,ps.name,ps.indoor,pr.hours2,pr.hours4,pr.hours6,pr.hours10,pr.hours24) FROM " +
            "ParkingSpot ps JOIN PaymentRecipe pr ON ps.paymentRecipeId=pr.id WHERE pr.enable=true AND ps.enable=true AND ps.occupied=false AND ps.parkingLotId=:parkingLotId")
    List<ParkingSpotDetailDTO> getAllParkingSpotDetailDTO(Long parkingLotId);

    @Query("Select p.vehicleTypeCode FROM ParkingSpot p WHERE p.id=:id")
    int getVehicleType(Long id);

    @Query("SELECT new com.SoftTech.SelfParkingLot_RestApi.dto.DescriptionDTO(pl.id,pl.name,p.id,p.firstName,p.lastName,p.username,p.email) FROM ParkingSpot ps " +
            "JOIN ParkingLot pl ON ps.parkingLotId=pl.id " +
            "JOIN Person p ON pl.ownerId=p.id WHERE ps.id=:parkingSpotId")
    DescriptionDTO getDescriptionDTO(Long parkingSpotId);
}
