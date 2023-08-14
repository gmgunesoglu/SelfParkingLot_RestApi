package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ParkingLotDTO {

    private String name;
    private String city;
    private String town;
    private String district;
    private String address;
}
