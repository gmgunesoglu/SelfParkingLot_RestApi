package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.dto.AuthenticationResponseDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonLoginDTO;
import com.SoftTech.SelfParkingLot_RestApi.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody PersonDTO dto){
        return ResponseEntity.ok(authenticationService.register(dto));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody PersonLoginDTO dto){
        return ResponseEntity.ok(authenticationService.authenticate(dto));
    }
}
