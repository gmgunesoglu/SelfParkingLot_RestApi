package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.dto.*;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.service.AccountService;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @GetMapping("/info")    //(user,customer,admin) kendi bilgilerini görebilir dönüş değeri ne? Polimorfizim?
    public Person showPersonInfo(HttpServletRequest request){
        return accountService.showPersonInfo(request);
    }

    @PostMapping("/register")
    public Person register(@RequestBody PersonDTO dto){
        return accountService.register(dto);
    }

    @PostMapping("/login")
    public JwtToken login(@RequestBody PersonLoginDTO dto){
        return accountService.login(dto);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        return accountService.logout(request);
    }

    @PutMapping("/changepassword")
    public String changePassword(@RequestBody PersonChangePasswordDTO dto, HttpServletRequest request){
        return accountService.changePassword(dto, request);
    }

    @PutMapping
    public Person personUpdate(@RequestBody PersonUpdateDTO dto, HttpServletRequest request){
        return accountService.personUpdate(dto,request);
    }

    @DeleteMapping
    public String disable(HttpServletRequest request, @RequestBody PersonLoginDTO dto){
        return accountService.disable(request,dto);
    }

    //geçici
    @GetMapping("/tokens")
    public HashMap<String, UserAuthorityDto> getTokens(){
        return accountService.getTokens();
    }

    //geçici
    @GetMapping("/queue")
    public List<String> listQueue(){
        return accountService.listQueue();
    }
}
