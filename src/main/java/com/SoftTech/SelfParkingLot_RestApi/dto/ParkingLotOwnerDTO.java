package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParkingLotOwnerDTO {

    private PersonPartnerDTO owner;
    private String name;
    private String city;
    private String town;
    private String district;
    private String address;
}
