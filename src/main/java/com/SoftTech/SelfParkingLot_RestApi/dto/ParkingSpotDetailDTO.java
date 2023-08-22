package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

import java.util.List;

@Data
public class ParkingSpotDetailDTO {

    private Long id;
    private String name;

    private List<String> canPark;
    private boolean indoor;
    private int hours2Price;
    private int hours4Price;
    private int hours6Price;
    private int hours10Price;
    private int hours24Price;

    public ParkingSpotDetailDTO() {
    }

    public ParkingSpotDetailDTO(Long id, String name, boolean indoor, int hours2Price, int hours4Price, int hours6Price, int hours10Price, int hours24Price) {
        this.id = id;
        this.name = name;
        this.indoor = indoor;
        this.hours2Price = hours2Price;
        this.hours4Price = hours4Price;
        this.hours6Price = hours6Price;
        this.hours10Price = hours10Price;
        this.hours24Price = hours24Price;
    }

    public void canPark(String vehicle){
        canPark.add(vehicle);
    }
}
