package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ParkingSpotShowDTO {
    private Long id;
    private String paymentRecipe;
    private String name;
    private boolean indoor;
    private List<String> canPark;


    public ParkingSpotShowDTO() {
    }

    public ParkingSpotShowDTO(Long id, String tag, String name, boolean indoor, int vehicleTypeCode) {
        this.id = id;
        this.paymentRecipe = tag;
        this.name = name;
        this.indoor = indoor;
        canPark = new ArrayList<>();
        if((1&vehicleTypeCode)==1){
            canPark.add("MOTORCYCLE");
        }if((2&vehicleTypeCode)==2){
            canPark.add("CAR");
        }if((4&vehicleTypeCode)==4){
            canPark.add("MINIBUS");
        }if((8&vehicleTypeCode)==8){
            canPark.add("BUS");
        }if((16&vehicleTypeCode)==16){
            canPark.add("TRUCK");
        }
    }


}
