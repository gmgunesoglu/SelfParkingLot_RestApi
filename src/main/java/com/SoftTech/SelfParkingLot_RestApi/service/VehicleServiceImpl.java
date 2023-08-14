package com.SoftTech.SelfParkingLot_RestApi.service;

import ch.qos.logback.core.net.ObjectWriter;
import com.SoftTech.SelfParkingLot_RestApi.dto.VehicleDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.VehicleUpdateDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Vehicle;
import com.SoftTech.SelfParkingLot_RestApi.exceptionhandling.GlobalRuntimeException;
import com.SoftTech.SelfParkingLot_RestApi.repository.VehicleRepository;
import com.SoftTech.SelfParkingLot_RestApi.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService{

    private final VehicleRepository vehicleRepository;
    private final JwtService jwtService;


    @Override
    public List<Vehicle> getAll(HttpServletRequest request) {
        Long ownerId = getOwnerIdFromRequest(request);
        return vehicleRepository.getVehiclesByOwnerIdAndEnable(ownerId,true);
    }

    @Override
    public Vehicle get(Long id, HttpServletRequest request) {
        Long ownerId = getOwnerIdFromRequest(request);
        Vehicle vehicle = vehicleRepository.getVehicleByIdAndEnable(id,true);
        if(vehicle.getOwnerId().equals(ownerId)){
            return vehicle;
        }else{
            throw new GlobalRuntimeException("You cannot see other users vehicle!", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public Vehicle add(VehicleDTO dto, HttpServletRequest request) {
        List<Vehicle> vehicles= vehicleRepository.findByPlateAndEnable(dto.getPlate(),true);
        if(vehicles.size()>0){
            // plaka bir kullanıcı tarafondan kullanılıyor...
            throw new GlobalRuntimeException("This plate is already using! ",HttpStatus.BAD_REQUEST);
        }
        Long ownerId = getOwnerIdFromRequest(request);
        Vehicle vehicle = vehicleRepository.findByOwnerIdAndPlate(ownerId,dto.getPlate());
        if(vehicle==null){
            // kullanıcının bu plakada aracı yok ise
            vehicle = new Vehicle();
        }
        vehicle.setColor(dto.getColor());
        vehicle.setVehicleType(dto.getVehicleType());
        vehicle.setModel(dto.getModel());
        vehicle.setPlate(dto.getPlate());
        vehicle.setOwnerId(ownerId);
        vehicle.setAddedDate(new Date(System.currentTimeMillis()));
        vehicle.setEnable(true);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle update(VehicleUpdateDTO dto, Long id, HttpServletRequest request) {
        Long ownerId = getOwnerIdFromRequest(request);
        Vehicle vehicle = vehicleRepository.getVehicleByIdAndEnable(id,true);
        if(vehicle.getOwnerId().equals(ownerId)){
            vehicle.setVehicleType(dto.getVehicleType());
            vehicle.setColor(dto.getColor());
            vehicle.setModel(dto.getModel());
            return vehicleRepository.save(vehicle);
        }else{
            throw new GlobalRuntimeException("You cannot update other users vehicle!", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public String disable(Long id, HttpServletRequest request) {
        Long ownerId = getOwnerIdFromRequest(request);
        Vehicle vehicle = vehicleRepository.getVehicleByIdAndEnable(id,true);
        if(vehicle.getOwnerId().equals(ownerId)){
            vehicle.setEnable(false);
            vehicle.setRemovedDate(new Date(System.currentTimeMillis()));
            vehicleRepository.save(vehicle);
            return "The vehicle is removed!";
        }else{
            throw new GlobalRuntimeException("You cannot remove other users vehicle!", HttpStatus.UNAUTHORIZED);
        }
    }

    private Long getOwnerIdFromRequest(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token=token.substring(7);
        return jwtService.extractId(token);
    }
}
