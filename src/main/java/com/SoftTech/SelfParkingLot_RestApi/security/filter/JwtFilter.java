package com.SoftTech.SelfParkingLot_RestApi.security.filter;

import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.security.service.JwtService;
import com.SoftTech.SelfParkingLot_RestApi.security.service.JwtServiceImpl;
import com.SoftTech.SelfParkingLot_RestApi.service.AccountService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AccountService accountService;


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

            final String header = request.getHeader("Authorization");
            final String jwt;
            final String userName;

            if(header != null && header.startsWith("Bearer ")){
                // elimizde bir jwt token var...
                jwt=header.substring(7);
                userName = jwtService.extractUserName(jwt);
                if(userName!=null && jwtService.isTokenValid(jwt) && SecurityContextHolder.getContext().getAuthentication()==null){
                    //token ok ama yetkilendirilmemiş ise...
                    Person person = accountService.findByUserName(userName);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            person,
                            null,
                            person.getAuthhorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request,response);
    }
}
