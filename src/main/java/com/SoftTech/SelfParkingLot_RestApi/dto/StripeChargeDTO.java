package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

@Data
public class StripeChargeDTO {

    private Double amount;
    private String currency;
    private String paymentToken;
    private String description;

}
