package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

@Data
public class PersonLoginDTO {

    //user veya customer login işlemi için
    private String userName;
    private String password;

}
