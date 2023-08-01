package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.entity.Location;
import java.util.List;

public interface LocationService {

    List<Location> getAll();
    Location get(Long id);
    Location save(Location location);
    Location delete(Long id);
    Location update(Location location,Long id);
}
