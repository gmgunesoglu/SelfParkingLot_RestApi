package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.entity.Authorization;
import com.SoftTech.SelfParkingLot_RestApi.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authorizations")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @GetMapping
    public List<Authorization> getAuthorizations(){
        return authorizationService.getAll();
    }

    @GetMapping("/{id}")
    public Authorization getAuthorization(@PathVariable Long id){
        return authorizationService.get(id);
    }

    @DeleteMapping("/{id}")
    public Authorization deleteAuthorization(@PathVariable Long id){
        return authorizationService.delete(id);
    }

    @PutMapping("/{id}")
    public Authorization updateAuthorization(@PathVariable Long id, @RequestBody Authorization authorization){
        return authorizationService.update(authorization,id);
    }

    @PostMapping
    public Authorization saveAuthorization(@RequestBody Authorization authorization){
        return authorizationService.save(authorization);
    }

}
