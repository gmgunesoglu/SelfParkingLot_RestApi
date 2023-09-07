package com.SoftTech.SelfParkingLot_RestApi.dto;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

@Data
public class UserAuthorityDto {

    private String jwt;
    private UsernamePasswordAuthenticationToken auth;


    public UserAuthorityDto(String jwt, UserDetails userDetails) {
        this.jwt = jwt;
        auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        System.out.println("yetkilendirme alıyor.");
    }
}
