package org.blezant.perfumeecommerceapp.jwt.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.blezant.perfumeecommerceapp.jwt.entities.RefreshTokenEntity;
import org.blezant.perfumeecommerceapp.auth.entities.RegisterEntity;
import org.blezant.perfumeecommerceapp.auth.exceptions.CustomAuthenticationEntryPoint;
import org.blezant.perfumeecommerceapp.auth.exceptions.CustomBadRequestException;
import org.blezant.perfumeecommerceapp.auth.repositories.AuthRepository;
import org.blezant.perfumeecommerceapp.auth.repositories.RefreshTokenRepository;
import org.blezant.perfumeecommerceapp.auth.services.CustomUserDetailsService;
import org.blezant.perfumeecommerceapp.jwt.models.RefreshTokenRequestDto;
import org.blezant.perfumeecommerceapp.jwt.models.RefreshTokenResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.util.Optional;

@Service
public class RefreshTokenService {
    @Value("${api.secret.key}")
    private String secretKey;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;
    private final AuthRepository authRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, CustomUserDetailsService customUserDetailsService, CustomAuthenticationEntryPoint customAuthenticationEntryPoint, JwtService jwtService, AuthRepository authRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.jwtService = jwtService;
        this.authRepository = authRepository;
    }

    public Optional<RefreshTokenEntity> fetchUser(String email) {
        return refreshTokenRepository.findById(email);
    }

    public RefreshTokenResponseDto updateAccessToken(RefreshTokenRequestDto requestBody) {
        //extract all data
        String email = requestBody.getEmail();
        String accessToken = requestBody.getAccessToken();
        String refreshToken = requestBody.getRefreshToken();
        String databaseRefreshToken = null;
        String databaseAccessToken = null;

        //check if user exists
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        //check users status
        if (!(userDetails.isAccountNonExpired()) && userDetails.isEnabled() && userDetails.isAccountNonLocked() && userDetails.isCredentialsNonExpired()) {
            throw new CustomBadRequestException("User account disabled");
        }
        //check if refreshToken Exists
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findById(email);
        if (refreshTokenEntity.isEmpty()) {
            throw new CustomBadRequestException("Refresh Token Does Not Exist");
        }

        //get present time
        BigInteger currentTimeInMills= BigInteger.valueOf(System.currentTimeMillis());

        //check if refresh token is still valid
        boolean isValid= refreshTokenEntity.get().getValidUntil().compareTo(currentTimeInMills) <0;
        if(!isValid){
            throw  new CustomBadRequestException("Refresh token expired");
        }


        //compare access Token
        if (!(accessToken.equals(refreshTokenEntity.get().getAccessToken()))) {
            throw new CustomBadRequestException("Access Token Does Not Exist");
        }

        //validate jwt token
        if (!isTokenExpired(accessToken, secretKey)) {
            throw new CustomBadRequestException("Invalid Token request");
        }

        //validate claims
        if (!isClaimForUser(email, accessToken)) {
            throw new CustomBadRequestException("Email does not match claims");
        }

        //Generate new access token
        //first fetch user data from db
        RegisterEntity userData = authRepository.findByEmail(email).get();
        accessToken = jwtService.generateAccessToken(userData);

        //save access token in refresh-token database
        refreshTokenRepository.updateAccessToken(accessToken, email);

        //build response and send back to user
        return buildResponse(accessToken, email);

    }

    public boolean isTokenExpired(String accessToken, String secretKey) {
        byte[] byteKey = Decoders.BASE64.decode(secretKey);
        SecretKey secretKey1 = Keys.hmacShaKeyFor(byteKey);
        try {
            Claims claims = Jwts.parser().verifyWith(secretKey1).build().parseSignedClaims(accessToken).getPayload();
            return false;

        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    public RefreshTokenResponseDto buildResponse(String accessToken, String email) {
        RefreshTokenResponseDto refreshTokenResponseDto = new RefreshTokenResponseDto();
        refreshTokenResponseDto.setAccessToken(accessToken);
        refreshTokenResponseDto.setStatus(true);
        refreshTokenResponseDto.setEmail(email);

        return refreshTokenResponseDto;
    }

    public boolean isClaimForUser(String email, String accessToken) {
        byte[] byteKey = Decoders.BASE64.decode(secretKey);
        SecretKey secretKey1 = Keys.hmacShaKeyFor(byteKey);
        Claims claims = null;
        String subject = null;
        boolean status=false;
        try {
            claims = Jwts.parser().verifyWith(secretKey1).build().parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e) {
            //
            if (!(e.getClaims().getSubject() != null && e.getClaims().getSubject().equals(email))) {
                status=true;
            }
        } catch (JwtException e) {
            System.out.println(email);
            throw new CustomBadRequestException("Invalid jwt token");
        }
        return status;
    }

}
