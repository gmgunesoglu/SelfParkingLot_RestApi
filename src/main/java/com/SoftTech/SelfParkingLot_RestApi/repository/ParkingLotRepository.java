package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.dto.ParkingLotWithTListDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.ParkingSpotShowDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PaymentRecipeDetailDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParkingLotRepository extends JpaRepository<ParkingLot,Long> {

    List<ParkingLot> findByOwnerIdAndEnable(Long ownerId,Boolean enable);

    ParkingLot getParkingLotByIdAndEnable(Long parkingLotId,Boolean enable);

    ParkingLot getParkingLotById(Long parkingLotId);

    @Query("SELECT  p.id FROM ParkingLot p WHERE p.ownerId = :ownerId AND p.enable=:enable")
    List<Long> getIds(Long ownerId,boolean enable);

//    @Query("SELECT new com.SoftTech.SelfParkingLot_RestApi.dto.PaymentRecipeInfoDTO(pl.name,l.city,l.town,l.district,pl.address) FROM " +
//            "ParkingLot pl JOIN Location l ON pl.locationId=l.id WHERE pl.id=:parkingLotId AND pl.enable=true AND l.enable=true")
//    PaymentRecipeInfoDTO getPaymentRecipeInfoDTO(Long parkingLotId);

    @Query("SELECT p.ownerId FROM ParkingLot p WHERE p.id=:parkingLotId AND p.enable=true")
    Long getOwnerIdById(Long parkingLotId);

    @Query("SELECT true FROM ParkingLot p WHERE p.id=:id AND p.ownerId=:ownerId AND p.enable=true")
    Boolean checkWithIdAndOwnerIdAndEnable(Long id,Long ownerId);

    @Query("SELECT new com.SoftTech.SelfParkingLot_RestApi.dto.ParkingLotWithTListDTO(p.id,p.name,l.city,l.town,l.district,p.address) FROM " +
            "ParkingLot p JOIN Location l ON p.locationId=l.id WHERE p.enable=true AND l.enable=true AND p.id = :parkingLotId")
    ParkingLotWithTListDTO getParkingLotWithTListDTO(Long parkingLotId);



    @Query("SELECT true FROM ParkingLot p WHERE p.id=:id AND p.enable=true")
    Boolean checkWithIdAndEnable(Long id);



}
