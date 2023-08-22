package com.SoftTech.SelfParkingLot_RestApi.dto;

import com.SoftTech.SelfParkingLot_RestApi.entity.VehicleType;
import lombok.Data;

@Data
public class CheckInDTO {

    private Long parkingSpotId;
    private VehicleType vehicleType;
    private String vehiclePlate;
}
