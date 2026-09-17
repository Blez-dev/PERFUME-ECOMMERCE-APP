package org.blezant.perfumeecommerceapp.jwt.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.blezant.perfumeecommerceapp.auth.entities.RegisterEntity;
import org.blezant.perfumeecommerceapp.auth.repositories.AuthRepository;
import org.blezant.perfumeecommerceapp.auth.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;

@Service
public class JwtService {

    @Value(value = "${api.secret.key}")
    private String secretKey;
    private final AuthRepository authRepository;
    private  final CustomUserDetailsService customUserDetailsService;


    public JwtService(AuthRepository authRepository,CustomUserDetailsService customUserDetailsService){
        this.authRepository=authRepository;
        this.customUserDetailsService=customUserDetailsService;

    }

    public String generateAccessToken(RegisterEntity userData) {
        Long currentDate = System.currentTimeMillis();
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userData.getRole()
        );

        return Jwts.builder().signWith(getKey())
                .claims(claims)
                .subject(userData.getUserId())
                .issuedAt(new Date(currentDate))
                .expiration(new Date(currentDate + 900000))
                .compact();
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }


    Key getKey() {
        System.out.println(Encoders.BASE64.encode(Jwts.SIG.HS256.key().build().getEncoded()));

        System.out.println();
        byte[] byteKey = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(byteKey);
    }

    public  String getUserEmail(String userId){
      return authRepository.findById(userId).get().getEmail();

    }

    public String extractUserId(String jwtToken) {
        byte[] byteKey = Decoders.BASE64.decode(secretKey);
        SecretKey secretKey = Keys.hmacShaKeyFor(byteKey);
        return  Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwtToken).getPayload().getSubject();
    }

    public void validateToken( String jwtToken) {
        byte[] byteKey = Decoders.BASE64.decode(secretKey);
        SecretKey secretKey = Keys.hmacShaKeyFor(byteKey);


            //check token signature
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwtToken);
            //check user's validation

    }

    public boolean checkExpiry(String accessToken) {
        byte[] byteKey= Decoders.BASE64.decode(secretKey);
        SecretKey secret=Keys.hmacShaKeyFor(byteKey);
        try{
            Claims claims= Jwts.parser().verifyWith(secret).build().parseSignedClaims(accessToken).getPayload();
            return  false;
        }catch (ExpiredJwtException e){
            return true;
        }
    }
}