package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.JwtToken;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonLoginDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.security.TokenQueue;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;

public interface AccountService {
    Person showPersonInfo(HttpServletRequest request);

    Person register(PersonDTO dto);

    JwtToken login(PersonLoginDTO dto);

    HashMap<String, String> getTokens();

    List<String> listQueue();

    String logout(HttpServletRequest request);
}
