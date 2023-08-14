package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.dto.LocationDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Location;
import com.SoftTech.SelfParkingLot_RestApi.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final LocationService locationService;

    @GetMapping("/locations")
    public List<Location> getAllLocation(){
        return locationService.getAll();
    }

    @GetMapping("/locations/{id}")
    public Location getLocatin(@PathVariable Long id){
        return locationService.get(id);
    }

    @PostMapping("/locations")
    public Location addLocation(@RequestBody LocationDTO dto){
        return locationService.add(dto);
    }

    @PutMapping("/locations/{id}")
    public Location updateLocation(@RequestBody LocationDTO dto,@PathVariable Long id){
        return locationService.update(dto,id);
    }

    @DeleteMapping("/locations/{id}")
    public String disableLocation(@PathVariable Long id){
        return locationService.disable(id);
    }

}
