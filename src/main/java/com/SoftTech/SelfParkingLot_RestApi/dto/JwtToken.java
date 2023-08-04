package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class JwtToken {
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }
}
