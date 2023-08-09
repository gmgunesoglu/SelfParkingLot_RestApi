package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

@Data
public class PersonChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
}
