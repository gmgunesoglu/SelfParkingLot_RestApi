package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.dto.TestDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.service.ParkingLotService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person,Long> {

    Optional<Person> findByUsername(String username);
    Optional<Person> findByUsernameOrEmail(String email,String username);
    Optional<Person> findByEmail(String email);

    Person findByIdAndEnable(Long partnerId,Boolean enable);
    Person getPersonByIdAndEnable(Long partnerId,Boolean enable);

    Person getPersonByUsernameAndEnable(String userName,Boolean enable);

    @Query("SELECT new com.SoftTech.SelfParkingLot_RestApi.dto.TestDTO(p.id, p.email) FROM Person p")
    List<TestDTO> test();

    @Query("SELECT true FROM Person p WHERE p.username=:username OR p.email=:email")
    Boolean checkWithUsernameOrEmail(String username, String email);

    Person getPersonByEmail(String email);
}
