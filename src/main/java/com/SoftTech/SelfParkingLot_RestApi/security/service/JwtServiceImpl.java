package com.SoftTech.SelfParkingLot_RestApi.security.service;

import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.catalina.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtServiceImpl implements JwtService{

    private static final String SECRET_KEY = "e34bc93bce34083f4dc6900e986019e7f08cf994c01046348dd69f8f05f00cb9";
    private final int tokenDuration = 1800000; //30 minutes

    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(Map<String,Object> extraClaims, Person person){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(person.getUserName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1800000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //editledim kontrol et!
    @Override
    public String generateToken(Person person){
        //return generateToken(new HashMap<>(),user);
        Map<String,Object> claims=new HashMap<>();
        claims.put("id",person.getId());
        claims.put("userName",person.getUserName());
        claims.put("authority",person.getAuthority());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(person.getUserName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1800000))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public boolean isTokenValid(String token){
        return (extractUserName(token)!=null && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
