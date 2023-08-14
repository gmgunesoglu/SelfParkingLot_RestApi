package com.SoftTech.SelfParkingLot_RestApi.dto;

import com.SoftTech.SelfParkingLot_RestApi.entity.Authority;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonPartnerDTO {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phoneNumber;
}
