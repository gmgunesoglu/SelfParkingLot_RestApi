package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.dto.JwtToken;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonLoginDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.service.PersonService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final PersonService personService;

    @GetMapping("/info")    //(user,customer,admin) kendi bilgilerini görebilir dönüş değeri ne? Polimorfizim?
    public Person showPersonInfo(HttpServletRequest request){
        return personService.showPersonInfo(request);
    }

    @PostMapping("register")    //(any) herkes kayıt olabilir, kayıt olup login olmalı yetki için...
    public Person register(@RequestBody PersonDTO dto){
        return personService.register(dto);
    }

    @PostMapping("/login")  //(any) yetkilerini alır
    public JwtToken login(@RequestBody PersonLoginDTO dto){
        return personService.login(dto);
    }

    @GetMapping("/logout")  //(admin,user,customer) yetkileri düşer
    public String logout(){
        //logout işleri yazılacak

        return "Çıkış yapıldı.";
    }

}
