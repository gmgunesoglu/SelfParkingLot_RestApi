package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRecipeDTO {

    private Long parkingLotId;
    private String tag;
    private int hours2;
    private int hours4;
    private int hours6;
    private int hours10;
    private int hours24;

    public PaymentRecipeDTO() {
    }

    public PaymentRecipeDTO(Long parkingLotId, String tag, int hours2, int hours4, int hours6, int hours10, int hours24) {
        this.parkingLotId = parkingLotId;
        this.tag = tag;
        this.hours2 = hours2;
        this.hours4 = hours4;
        this.hours6 = hours6;
        this.hours10 = hours10;
        this.hours24 = hours24;
    }
}
