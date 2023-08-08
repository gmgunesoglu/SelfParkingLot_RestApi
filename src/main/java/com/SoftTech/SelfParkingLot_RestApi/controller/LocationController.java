//package com.SoftTech.SelfParkingLot_RestApi.controller;
//
//import com.SoftTech.SelfParkingLot_RestApi.entity.Location;
//import com.SoftTech.SelfParkingLot_RestApi.service.LocationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/locations")
//@RequiredArgsConstructor
//public class LocationController {
//
//    private final LocationService locationService;
//
//    @GetMapping
//    public List<Location> getLocations(){
//        return locationService.getAll();
//    }
//
//    @GetMapping("/{id}")
//    public Location getLocation(@PathVariable Long id){
//        return locationService.get(id);
//    }
//
//    @PostMapping
//    public Location saveLocation(@RequestBody Location location){
//        return locationService.save(location);
//    }
//
//    @PutMapping("/{id}")
//    public Location updateLocation(@PathVariable Long id, @RequestBody Location location){
//        return locationService.update(location,id);
//    }
//
//    @DeleteMapping("/{id}")
//    public Location deleteLocation(@PathVariable Long id){
//        return locationService.delete(id);
//    }
//}
