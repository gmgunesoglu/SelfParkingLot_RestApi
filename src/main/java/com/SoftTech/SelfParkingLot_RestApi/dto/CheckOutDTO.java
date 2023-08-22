package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

@Data
public class CheckOutDTO {

    private String vehiclePlate;
    private Double amount;
    private String paymentToken;
}
