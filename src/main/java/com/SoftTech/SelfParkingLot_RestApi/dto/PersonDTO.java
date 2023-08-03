package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

@Data
public class PersonDTO extends PersonLoginDTO {

    //User ve Customer Save ve Update işlemlerinde...
    private String firstName;
    private String lastName;
    private String email;
    private String phone;


}
