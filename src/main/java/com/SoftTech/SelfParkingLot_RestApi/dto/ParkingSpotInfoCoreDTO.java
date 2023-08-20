package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

@Data
public class ParkingSpotInfoCoreDTO {

    private String name;
    private int vehicleTypeCode;
    private String paymentComet;
    private boolean indoor;
}
