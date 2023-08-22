package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.*;

public interface TransactionService {
    CheckInInfoDTO checkIn(CheckInDTO dto);

    PaymentTokenDTO getPaymentToken(CardDTO dto);

    PaymentInfoDTO showPaymentInfo(String vehiclePlate);

    CheckOutInfoDTO checkOut(CheckOutDTO dto);
}
