package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;

@Data
public class DescriptionDTO {
    private Long parkingLotId;
    private String parkingLotName;
    private Long ownerId;
    private String ownerFirsName;
    private String ownerLastName;
    private String ownerUserName;
    private String ownerEmail;

    public DescriptionDTO() {
    }

    public DescriptionDTO(Long parkingLotId, String parkingLotName, Long ownerId, String ownerFirsName, String ownerLastName, String ownerUserName, String ownerEmail) {
        this.parkingLotId = parkingLotId;
        this.parkingLotName = parkingLotName;
        this.ownerId = ownerId;
        this.ownerFirsName = ownerFirsName;
        this.ownerLastName = ownerLastName;
        this.ownerUserName = ownerUserName;
        this.ownerEmail = ownerEmail;
    }
}
