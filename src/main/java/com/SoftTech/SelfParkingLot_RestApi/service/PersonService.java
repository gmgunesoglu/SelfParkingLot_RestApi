package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.JwtToken;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonLoginDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface PersonService {

    List<Person> getAll();
    Person get(Long id);
    Person save(Person user);
    Person delete(Long id);
    Person update(Person user, Long id);

}
