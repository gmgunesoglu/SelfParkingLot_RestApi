package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ParkingLotPartnerDTO {

    private List<PersonPartnerDTO> partners;

    private Long id;
    private String name;
    private String city;
    private String town;
    private String district;
    private String address;
}
