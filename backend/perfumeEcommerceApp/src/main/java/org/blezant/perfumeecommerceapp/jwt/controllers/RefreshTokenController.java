package org.blezant.perfumeecommerceapp.jwt.controllers;


import org.blezant.perfumeecommerceapp.jwt.services.RefreshTokenService;
import org.blezant.perfumeecommerceapp.jwt.models.RefreshTokenRequestDto;
import org.blezant.perfumeecommerceapp.jwt.models.RefreshTokenResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshTokenController {


    private final RefreshTokenService refreshTokenService;

    public RefreshTokenController(RefreshTokenService refreshTokenService) {

        this.refreshTokenService = refreshTokenService;
    }


    @PostMapping("/refresh/token")
    public ResponseEntity<RefreshTokenResponseDto> refreshAccessToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        RefreshTokenResponseDto response = refreshTokenService.updateAccessToken(refreshTokenRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
