package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.dto.VehicleDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.VehicleUpdateDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Vehicle;
import com.SoftTech.SelfParkingLot_RestApi.service.VehicleService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public List<Vehicle> getAll(HttpServletRequest request){
        return vehicleService.getAll(request);
    }

    @GetMapping("/{id}")
    public Vehicle get(@PathVariable Long id, HttpServletRequest request){
        return vehicleService.get(id,request);
    }

    @PostMapping
    public Vehicle add(@RequestBody VehicleDTO dto, HttpServletRequest request){
        return vehicleService.add(dto,request);
    }

    @PutMapping("/{id}")
    public Vehicle update(@RequestBody VehicleUpdateDTO dto, @PathVariable Long id, HttpServletRequest request){
        return vehicleService.update(dto,id,request);
    }

    @DeleteMapping("/{id}")
    public String disable(@PathVariable Long id, HttpServletRequest request){
        return vehicleService.disable(id,request);
    }
}
