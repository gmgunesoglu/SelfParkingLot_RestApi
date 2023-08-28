package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

@Data
public class JwtToken {
    private String token;

    public JwtToken() {
    }

    public JwtToken(String token) {
        this.token = token;
    }
}
