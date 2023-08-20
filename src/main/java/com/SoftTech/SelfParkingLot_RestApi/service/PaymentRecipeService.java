package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.ParkingLotWithTListDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PaymentRecipeDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PaymentRecipeDetailDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface PaymentRecipeService {
    List<ParkingLotWithTListDTO<PaymentRecipeDetailDTO>> getAll(HttpServletRequest request);

    PaymentRecipeDetailDTO get(HttpServletRequest request, Long id);

    PaymentRecipeDTO add(HttpServletRequest request, PaymentRecipeDTO dto);

    PaymentRecipeDTO update(HttpServletRequest request, PaymentRecipeDTO dto, Long id);

    String disable(HttpServletRequest request, Long id);
}
