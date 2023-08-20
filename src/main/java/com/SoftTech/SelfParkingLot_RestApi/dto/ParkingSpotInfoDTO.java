package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

import java.util.List;

@Data
public class ParkingSpotInfoDTO {

    private String name;
    private String city;
    private String town;
    private String district;
    private String address;
    private List<ParkingSpotInfoCoreDTO> spots;

    public ParkingSpotInfoDTO() {
    }

    public ParkingSpotInfoDTO(String name, String city, String town, String district, String address) {
        this.name = name;
        this.city = city;
        this.town = town;
        this.district = district;
        this.address = address;
    }
}
