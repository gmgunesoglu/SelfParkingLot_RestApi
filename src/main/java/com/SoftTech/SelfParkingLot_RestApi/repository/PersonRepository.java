package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person,Long> {

    Optional<Person> findByUsername(String username);
    Optional<Person> findByUsernameOrEmail(String email,String username);
    Optional<Person> findByEmail(String email);

}
