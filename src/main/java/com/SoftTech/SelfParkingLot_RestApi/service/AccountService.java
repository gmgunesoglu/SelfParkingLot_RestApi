package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.*;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
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

    String changePassword(PersonChangePasswordDTO dto, HttpServletRequest request);

    Person personUpdate(PersonUpdateDTO dto, HttpServletRequest request);

    String disable(HttpServletRequest request,PersonLoginDTO dto);
}
