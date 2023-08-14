package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.service.ParkingLotService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person,Long> {

    Optional<Person> findByUsername(String username);
    Optional<Person> findByUsernameOrEmail(String email,String username);
    Optional<Person> findByEmail(String email);

    Person findByIdAndEnable(Long partnerId,Boolean enable);
    Person getPersonByIdAndEnable(Long partnerId,Boolean enable);

    Person getPersonByUsernameAndEnable(String userName,Boolean enable);
}
