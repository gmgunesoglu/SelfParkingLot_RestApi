package com.SoftTech.SelfParkingLot_RestApi.security.service;

import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import io.jsonwebtoken.Claims;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUserName(String token);

    Claims extractAllClaims(String token);

    String generateToken(Map<String,Object> extraClaims, Person person);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(Person person);

    boolean isTokenValid(String token);

}
