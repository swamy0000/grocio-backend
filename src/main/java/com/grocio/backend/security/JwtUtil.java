package com.grocio.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // ఇది సీక్రెట్ కీ (ఇది ఎవరికీ తెలియకూడదు)
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    
    // టోకెన్ ఎక్స్‌పైరీ టైమ్ (ఉదాహరణకు 10 గంటలు)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10;

    // టోకెన్ జనరేట్ చేసే మెథడ్
    public String generateToken(String phoneNumber) {
        return Jwts.builder()
                .setSubject(phoneNumber)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // టోకెన్ నుండి ఫోన్ నంబర్ (Username) బయటకు తీయడం
    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // టోకెన్ ఇంకా వాలిడ్ గా ఉందా లేదా?
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }
}