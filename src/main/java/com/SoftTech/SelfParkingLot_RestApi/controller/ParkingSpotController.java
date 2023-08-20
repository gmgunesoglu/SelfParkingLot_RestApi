package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.dto.*;
import com.SoftTech.SelfParkingLot_RestApi.service.ParkingSpotService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user/parkingspots")
@RequiredArgsConstructor
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    @GetMapping
    public List<ParkingLotWithTListDTO<ParkingSpotShowDTO>> getAll(HttpServletRequest request){
        return parkingSpotService.getAll(request);
    }

    @GetMapping("/{id}")
    public ParkingSpotShowDTO get(HttpServletRequest request, @PathVariable Long id){
        return parkingSpotService.get(request,id);
    }

    @PostMapping
    public ParkingSpotShowDTO add(HttpServletRequest request, @RequestBody ParkingSpotAddDTO dto){
        return parkingSpotService.add(request,dto);
    }

    @PutMapping("/{id}")
    public ParkingSpotShowDTO update(HttpServletRequest request, @RequestBody ParkingSpotAddDTO dto, @PathVariable Long id){
        return parkingSpotService.update(request,dto,id);
    }

    @DeleteMapping("/{id}")
    public String disable(HttpServletRequest request, @PathVariable Long id){
        return parkingSpotService.disable(request,id);
    }
}
