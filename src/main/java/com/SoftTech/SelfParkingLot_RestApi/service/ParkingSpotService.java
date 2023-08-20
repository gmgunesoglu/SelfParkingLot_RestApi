package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.ParkingLotWithTListDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.ParkingSpotAddDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.ParkingSpotDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.ParkingSpotShowDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingSpot;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ParkingSpotService {

    List<ParkingLotWithTListDTO<ParkingSpotShowDTO>> getAll(HttpServletRequest request);

    ParkingSpotShowDTO get(HttpServletRequest request, Long id);

    ParkingSpotShowDTO add(HttpServletRequest request, ParkingSpotAddDTO dto);

    ParkingSpotShowDTO update(HttpServletRequest request, ParkingSpotAddDTO dto, Long id);

    String disable(HttpServletRequest request, Long id);
}
