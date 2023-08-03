package com.SoftTech.SelfParkingLot_RestApi.security;

import com.SoftTech.SelfParkingLot_RestApi.dto.AuthenticationResponseDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonLoginDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Authority;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.repository.PersonRepository;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PersonRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO register(PersonDTO dto){
        Person person = new Person();
        person.setUsername(dto.getUserName());
        person.setPassword(passwordEncoder.encode(dto.getPassword()));
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setAuthority(Authority.USER);
        person.setPhoneNumber(dto.getPhoneNumber());
        person.setEmail(dto.getEmail());
        repository.save(person);
        var jwtToken = jwtService.generateToken(person);
        return AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponseDTO authenticate(PersonLoginDTO dto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );
        Person person = repository.findByEmail(dto.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(person);
        return AuthenticationResponseDTO.builder()
                .token(jwtToken)
                .build();
    }
}
