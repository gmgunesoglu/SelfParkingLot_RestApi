package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.dto.*;
import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingLot;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.service.ParkingLotService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user/parkinglots")
@RequiredArgsConstructor
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @GetMapping
    public ParkingLotListDTO getAll(HttpServletRequest request){
        return parkingLotService.getAll(request);
    }

    @GetMapping("/{id}")
    public ParkingLot get(HttpServletRequest request, @PathVariable Long id){
        return parkingLotService.get(request, id);
    }

    @PostMapping
    public ParkingLot add(HttpServletRequest request, @RequestBody ParkingLotDTO dto){
        return parkingLotService.add(request,dto);
    }

    @PutMapping("/{id}")
    public ParkingLot update(HttpServletRequest request, @RequestBody ParkingLotDTO dto, @PathVariable Long id){
        return parkingLotService.update(request,dto,id);
    }

    @DeleteMapping("/{id}")
    public String disable(HttpServletRequest request, @PathVariable Long id){
        return parkingLotService.disable(request,id);
    }

    @PostMapping("/share/{id}")
    public PersonPartnerDTO openShare(HttpServletRequest request, @RequestBody ParkingLotShareDTO dto, @PathVariable Long id){
        return parkingLotService.openShare(request,dto,id);
    }

    @DeleteMapping("/share/{id}")
    public PersonPartnerDTO closeShare(HttpServletRequest request, @RequestBody ParkingLotCloseShareDTO dto, @PathVariable Long id){
        return parkingLotService.closeShare(request,dto,id);
    }

    @DeleteMapping("/abdicate/{id}")
    public String abdicate(HttpServletRequest request, @RequestBody ParkingLotShareDTO dto, @PathVariable Long id){
        return parkingLotService.abdicate(request,dto,id);
    }

}
