package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.entity.Location;
import com.SoftTech.SelfParkingLot_RestApi.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService{

    private final LocationRepository locationRepository;

    @Override
    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location get(Long id) {
        return locationRepository.findById(id).get();
    }

    @Override
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location delete(Long id) {
        Location location = locationRepository.findById(id).get();
        locationRepository.delete(location);
        return location;
    }

    @Override
    public Location update(Location location, Long id) {
        Location updatedLocation = locationRepository.findById(id).get();
        updatedLocation.setAddress(location.getAddress());
        updatedLocation.setTown(location.getTown());
        updatedLocation.setDistrict(location.getDistrict());
        updatedLocation.setCity(location.getCity());
        return locationRepository.save(updatedLocation);
    }
}
