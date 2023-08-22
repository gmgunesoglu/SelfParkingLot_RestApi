package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CheckInInfoDTO {
    private Long parkingLotId;
    private String name;
    private Boolean indoor;
    private String vehiclePlate;
    private Date enteryDate;
    private int hours2Price;
    private int hours4Price;
    private int hours6Price;
    private int hours10Price;
    private int hours24Price;
}
