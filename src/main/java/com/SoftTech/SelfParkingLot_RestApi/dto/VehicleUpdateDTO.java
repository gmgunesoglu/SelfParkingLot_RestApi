package com.SoftTech.SelfParkingLot_RestApi.dto;

import com.SoftTech.SelfParkingLot_RestApi.entity.VehicleType;
import lombok.Data;

@Data
public class VehicleUpdateDTO {

    private  String color;
    private  int model;
    private VehicleType vehicleType;
}
