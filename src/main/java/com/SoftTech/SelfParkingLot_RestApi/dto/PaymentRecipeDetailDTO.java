package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

@Data
public class PaymentRecipeDetailDTO {
    private Long id;
    private String tag;
    private int hours2;
    private int hours4;
    private int hours6;
    private int hours10;
    private int hours24;

    public PaymentRecipeDetailDTO() {
    }

    public PaymentRecipeDetailDTO(Long id, String tag, int hours2, int hours4, int hours6, int hours10, int hours24) {
        this.id = id;
        this.tag = tag;
        this.hours2 = hours2;
        this.hours4 = hours4;
        this.hours6 = hours6;
        this.hours10 = hours10;
        this.hours24 = hours24;
    }
}
