package com.SoftTech.SelfParkingLot_RestApi.security;

import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "e34bc93bce34083f4dc6900e986019e7f08cf994c01046348dd69f8f05f00cb9";
    private static final int TOKEN_DURATION = 1800000;//1800000; // 30dk

    public String extractUsername(String jwtToken){
        return extractClaim(jwtToken,Claims::getSubject);
    }

    public Long extractId(String jwtToken){
        return Long.valueOf(extractClaim(jwtToken, Claims::getId));
    }

    public <T> T extractClaim(String jwtToken, Function<Claims,T> claimResolver){
        final Claims claims = extractAllClaims(jwtToken);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Person person){
        return generateToken(new HashMap<>(),person);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            Person person
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(person.getUsername())
                .setId(person.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ TOKEN_DURATION))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails){
        final String username = extractUsername(jwtToken);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public String getSecretKey(){
        return SECRET_KEY;
    }

    public int getTokenDuration(){
        return TOKEN_DURATION;
    }
}
