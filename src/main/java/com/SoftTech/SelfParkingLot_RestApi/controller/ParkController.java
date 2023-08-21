package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.dto.LocationDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.ParkingLotFindDTO;
import com.SoftTech.SelfParkingLot_RestApi.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer/park")
public class ParkController {

    private final LocationService locationService;

    @PostMapping("/parkinglots")
    public List<ParkingLotFindDTO> findParkingLots(LocationDTO dto){
        return locationService.findParkingLots(dto);
    }


}
