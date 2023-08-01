package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingLot;
import com.SoftTech.SelfParkingLot_RestApi.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/parkinglots")
@RequiredArgsConstructor
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @GetMapping
    public List<ParkingLot> getParkingLots(){
        return parkingLotService.getAll();
    }

    @GetMapping("/{id}")
    public ParkingLot getParkingLot(@PathVariable Long id){
        return parkingLotService.get(id);
    }

    @PostMapping
    public ParkingLot saveParkingLot(@RequestBody ParkingLot parkingLot){
        return parkingLotService.save(parkingLot);
    }

    @PutMapping("/{id}")
    public ParkingLot updateParkingLot(@PathVariable Long id, @RequestBody ParkingLot parkingLot){
        return parkingLotService.update(parkingLot, id);
    }

    @DeleteMapping("/{id}")
    public ParkingLot deleteParkingLot(@PathVariable Long id){
        return parkingLotService.delete(id);
    }
}
