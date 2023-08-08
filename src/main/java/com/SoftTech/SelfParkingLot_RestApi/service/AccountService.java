package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.JwtToken;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonLoginDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import jakarta.servlet.http.HttpServletRequest;

public interface AccountService {
    Person showPersonInfo(HttpServletRequest request);

    Person register(PersonDTO dto);

    JwtToken login(PersonLoginDTO dto);
}
