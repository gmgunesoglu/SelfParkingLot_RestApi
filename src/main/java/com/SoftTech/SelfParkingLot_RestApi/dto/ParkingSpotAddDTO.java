package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

@Data
public class ParkingSpotAddDTO {
    private Long parkingLotId;
    private String name;
    private Long paymentRecipeId;
    private boolean TRUCK;
    private boolean BUS;
    private boolean MINIBUS;
    private boolean CAR;
    private boolean MOTORCYCLE;
    private boolean indoor;
}
