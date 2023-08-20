package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

import java.util.List;

@Data
public class ParkingLotWithTListDTO<T> {

    private Long id;
    private String name;
    private String city;
    private String town;
    private String district;
    private String address;
    private List<T> tList;

    public ParkingLotWithTListDTO() {
    }

    public ParkingLotWithTListDTO(Long id, String name, String city, String town, String district, String address) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.town = town;
        this.district = district;
        this.address = address;
    }
}
