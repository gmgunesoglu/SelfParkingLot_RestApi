package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.LocationDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Location;
import java.util.List;

public interface LocationService {

    List<String> getCities();

    List<String> getTowns(String city);

    List<String> getDistricts(String city, String town);

    Long getId(String city, String town, String district);

    Location add(LocationDTO dto);


    Location update(LocationDTO dto, Long id);

    String disable(Long id);

    List<Location> getAll();

    Location get(Long id);
}
