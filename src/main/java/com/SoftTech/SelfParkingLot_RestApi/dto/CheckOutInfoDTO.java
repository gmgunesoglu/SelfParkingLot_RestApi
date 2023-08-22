package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CheckOutInfoDTO {

    private String parkingLotName;
    private Date enteryDate;
    private Date exitDate;
    private String amount;
    private String vehiclePlate;
}
