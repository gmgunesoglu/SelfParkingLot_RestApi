package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

@Data
public class PaymentInfoDTO {

    private int amount;
    private String currency;
}
