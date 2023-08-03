package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.dto.PersonDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonLoginDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final PersonService personService;

    @PostMapping
    public Person login(@RequestBody PersonLoginDTO dto){
        //login işleri yazılacak

        return null;
    }

    @GetMapping
    public String logout(){
        //logout işleri yazılacak

        return "Çıkış yapıldı.";
    }

}
