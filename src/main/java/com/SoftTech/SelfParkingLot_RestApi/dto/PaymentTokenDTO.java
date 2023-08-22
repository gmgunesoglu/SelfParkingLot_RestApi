package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

@Data
public class PaymentTokenDTO {
    private String stripeToken;
}
