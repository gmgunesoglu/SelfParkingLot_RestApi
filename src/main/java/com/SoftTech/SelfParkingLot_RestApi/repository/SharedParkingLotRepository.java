package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingLot;
import com.SoftTech.SelfParkingLot_RestApi.entity.SharedParkingLot;
import com.SoftTech.SelfParkingLot_RestApi.entity.SharedParkingLotId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SharedParkingLotRepository extends JpaRepository<SharedParkingLot, SharedParkingLotId> {

    @Query("SELECT  s.parkingLotId FROM SharedParkingLot s WHERE s.partnerId = :partnerId")
    List<Long> getParkingLotIds(Long partnerId);

    @Query("SELECT  s.partnerId FROM SharedParkingLot s WHERE s.parkingLotId = :parkingLotId")
    List<Long> getPartnerIds(Long parkingLotId);

    SharedParkingLot getSharedParkingLotByParkingLotIdAndPartnerId(Long parkingLotId,Long partnerId);


}
