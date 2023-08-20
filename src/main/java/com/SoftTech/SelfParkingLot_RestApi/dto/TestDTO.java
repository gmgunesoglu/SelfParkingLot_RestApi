package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;
import java.util.List;

@Data
public class TestDTO {

    private Long id;

    private String email;


    public TestDTO(Long id, String email) {
        this.id = id;
        this.email = email;
    }

}
