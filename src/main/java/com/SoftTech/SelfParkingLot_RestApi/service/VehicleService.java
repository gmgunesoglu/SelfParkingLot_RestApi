package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.VehicleDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.VehicleUpdateDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Vehicle;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface VehicleService {

    List<Vehicle> getAll(HttpServletRequest request);

    Vehicle get(Long id, HttpServletRequest request);

    Vehicle add(VehicleDTO dto, HttpServletRequest request);

    Vehicle update(VehicleUpdateDTO dto, Long id, HttpServletRequest request);

    String disable(Long id, HttpServletRequest request);
}
