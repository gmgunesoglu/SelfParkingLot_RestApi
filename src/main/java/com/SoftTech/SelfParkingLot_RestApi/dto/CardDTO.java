package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

@Data
public class CardDTO {

    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
}
