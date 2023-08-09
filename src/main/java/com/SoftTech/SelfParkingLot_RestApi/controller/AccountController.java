package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.dto.JwtToken;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonLoginDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.security.TokenQueue;
import com.SoftTech.SelfParkingLot_RestApi.service.AccountService;
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

    @PostMapping("register")
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

    @GetMapping("/tokens")
    public HashMap<String, String> getTokens(){
        return accountService.getTokens();
    }

    @GetMapping("/queue")
    public List<String> listQueue(){
        return accountService.listQueue();
    }


}
