package com.SoftTech.SelfParkingLot_RestApi.dto;

import com.SoftTech.SelfParkingLot_RestApi.entity.Authority;
import lombok.Data;

@Data
public class PersonUpdateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
