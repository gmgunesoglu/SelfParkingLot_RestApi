package com.SoftTech.SelfParkingLot_RestApi.dto;

import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingLot;
import lombok.Data;

import java.util.List;

@Data
public class ParkingLotListDTO {

    private List<ParkingLotPartnerDTO> underMyRule;
    private List<ParkingLotOwnerDTO> upperMyRule;
}
