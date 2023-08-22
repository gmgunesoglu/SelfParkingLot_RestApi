package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.dto.*;
import com.SoftTech.SelfParkingLot_RestApi.service.LocationService;
import com.SoftTech.SelfParkingLot_RestApi.service.ParkingLotService;
import com.SoftTech.SelfParkingLot_RestApi.service.ParkingSpotService;
import com.SoftTech.SelfParkingLot_RestApi.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer/park")
public class ParkController {

    private final LocationService locationService;
    private final ParkingSpotService parkingSpotService;
    private final TransactionService transactionService;

    @PostMapping("/parkinglots")
    public List<ParkingLotFindDTO> findParkingLots(@RequestBody LocationDTO dto){
        return locationService.findParkingLots(dto);
    }

    @GetMapping("/parkinglots/{parkingLotId}/parkingspots")
    public ParkingLotWithTListDTO<ParkingSpotDetailDTO> findParkingSpots(@PathVariable Long parkingLotId){
        return parkingSpotService.findParkingSpots(parkingLotId);
    }

    @PostMapping()
    public CheckInInfoDTO checkIn(@RequestBody CheckInDTO dto){
        return transactionService.checkIn(dto);
    }

    @PostMapping("/payment")
    public PaymentTokenDTO getPaymentToken(@RequestBody CardDTO dto){
        return transactionService.getPaymentToken(dto);
    }

    @GetMapping("/payment/{vehiclePlate}")
    public PaymentInfoDTO showPaymentInfo(@PathVariable String vehiclePlate){
        System.out.println(vehiclePlate);
        return transactionService.showPaymentInfo(vehiclePlate);
    }

    @PutMapping
    public CheckOutInfoDTO checkOut(@RequestBody CheckOutDTO dto){
        return transactionService.checkOut(dto);
    }

}
