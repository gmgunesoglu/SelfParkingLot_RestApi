package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.entity.Location;
import com.SoftTech.SelfParkingLot_RestApi.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public List<String> getCities(){
        return locationService.getCities();
    }

    @GetMapping("/{city}")
    public List<String> getTowns(@PathVariable String city){
        return locationService.getTowns(city);
    }

    @GetMapping("/{city}/{town}")
    public List<String> getDistricts(@PathVariable String city,@PathVariable String town){
        return locationService.getDistricts(city,town);
    }

    @GetMapping("/{city}/{town}/{district}")
    public Long getLocation(@PathVariable String city,@PathVariable String town,@PathVariable String district){
        return locationService.getId(city, town, district);
    }

}
