package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.JwtToken;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonInfoDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonLoginDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;

import java.util.Optional;

public interface AccountService {

    Person findByUserName(String userName);
    Person findByEmail(String email);
    Person findByUserNameOrEmail(String userNameOrEmail,String emailOrUserName);

    JwtToken login(PersonLoginDTO dto);

    Person register(PersonDTO dto);

    PersonInfoDTO showPersonInfo(HttpServletRequest request);
}
