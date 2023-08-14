package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.LocationDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Location;
import com.SoftTech.SelfParkingLot_RestApi.exceptionhandling.GlobalRuntimeException;
import com.SoftTech.SelfParkingLot_RestApi.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.spi.SqlScriptException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.SQLData;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService{

    private final LocationRepository locationRepository;


    @Override
    public List<String> getCities() {
        return locationRepository.getCities();
    }

    @Override
    public List<String> getTowns(String city) {
        return locationRepository.getTowns(city);
    }

    @Override
    public List<String> getDistricts(String city, String town) {
        return locationRepository.getDistricts(city,town);
    }

    @Override
    public Long getId(String city, String town, String district) {
        return locationRepository.getId(city,town,district);
    }

    @Override
    public Location add(LocationDTO dto) {
        Location location = new Location();
        location.setCity(dto.getCity());
        location.setTown(dto.getTown());
        location.setDistrict(dto.getDistrict());
        location.setEnable(true);

        try{
            return locationRepository.save(location);
        }catch (DataIntegrityViolationException e){
            // aynı il/ilçe/mahalle veri var ise...
            Location oldLocation = locationRepository.getLocationsByCityAndTownAndDistrict(dto.getCity(),dto.getTown(),dto.getDistrict());
            if(oldLocation.isEnable()){
                return oldLocation;
            }else{
                oldLocation.setEnable(true);
                locationRepository.save(oldLocation);
                throw new GlobalRuntimeException("Location is enabled! ",HttpStatus.OK);
            }
        }
    }

    @Override
    public Location update(LocationDTO dto, Long id) {
        Location location = locationRepository.findById(id).get();
        if(location.equals(null)){
            throw new GlobalRuntimeException("Location is not found! ", HttpStatus.NOT_FOUND);
        }if(!location.isEnable()){
            throw new GlobalRuntimeException("Location is disable! ", HttpStatus.BAD_REQUEST);
        }
        location.setCity(dto.getCity());
        location.setTown(dto.getTown());
        location.setDistrict(dto.getDistrict());
        return locationRepository.save(location);
    }

    @Override
    public String disable(Long id) {
        Location location = locationRepository.findById(id).get();
        if(location.equals(null)){
            throw new GlobalRuntimeException("Location is not found! ", HttpStatus.NOT_FOUND);
        }if(!location.isEnable()){
            throw new GlobalRuntimeException("Location is already disable! ", HttpStatus.BAD_REQUEST);
        }
        location.setEnable(false);
        locationRepository.save(location);
        return "Location is removed!";
    }

    @Override
    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location get(Long id) {
        return locationRepository.findById(id).get();
    }


}
