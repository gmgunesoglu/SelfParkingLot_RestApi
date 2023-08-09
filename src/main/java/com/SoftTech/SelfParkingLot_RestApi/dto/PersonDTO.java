package com.SoftTech.SelfParkingLot_RestApi.dto;

import com.SoftTech.SelfParkingLot_RestApi.entity.Authority;
import lombok.Data;

@Data
public class PersonDTO {

    //User ve Customer Save ve Update işlemlerinde...
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private Authority authority;
}
