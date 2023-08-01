package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingLot;
import java.util.List;

public interface ParkingLotService {

    List<ParkingLot> getAll();
    ParkingLot get(Long id);
    ParkingLot save(ParkingLot parkingLot);
    ParkingLot delete(Long id);
    ParkingLot update(ParkingLot parkingLot,Long id);
}
