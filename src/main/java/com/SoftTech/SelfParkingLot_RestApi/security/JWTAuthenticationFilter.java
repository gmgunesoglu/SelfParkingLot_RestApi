package com.SoftTech.SelfParkingLot_RestApi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private CurrentTokens currentTokens;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JWTAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        currentTokens = new CurrentTokens(jwtService.getTokenDuration());
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userName;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        jwtToken = authHeader.substring(7);
        userName = jwtService.extractUsername(jwtToken);
        if(!currentTokens.getTokenOfUser(userName).equals(jwtToken)){
            //burada ne yapmalıyım?

            throw new UsernameNotFoundException("Oturum sonlandırılmış! Tekrar login olun. ");
        }

        String str = currentTokens.getTokenOfUser(userName);
        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //yetkilendirilmemiş ise check edip yetkilendirmek gerekir...
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
            if(jwtService.isTokenValid(jwtToken,userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request,response);
    }

    public void add(String token,String userName){
        currentTokens.add(token, userName);
    }

    public HashMap<String, String> getTokens() {
        return currentTokens.getTokens();
    }

    public List<String> listQueue() {
        return currentTokens.listQueue();
    }

    public String logout(String username) {
        return currentTokens.remove(username);
    }
}
