package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.*;
import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingLot;
import jakarta.servlet.http.HttpServletRequest;

public interface ParkingLotService {


    ParkingLotListDTO getAll(HttpServletRequest request);

    ParkingLot get(HttpServletRequest request, Long id);

    ParkingLot add(HttpServletRequest request, ParkingLotDTO dto);

    ParkingLot update(HttpServletRequest request, ParkingLotDTO dto, Long id);

    String disable(HttpServletRequest request, Long id);

    PersonPartnerDTO openShare(HttpServletRequest request, ParkingLotShareDTO dto, Long id);

    PersonPartnerDTO closeShare(HttpServletRequest request, ParkingLotCloseShareDTO dto, Long id);

    String abdicate(HttpServletRequest request, ParkingLotShareDTO dto, Long id);
}
