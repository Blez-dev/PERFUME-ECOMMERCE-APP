package org.blezant.perfumeecommerceapp.jwt.models;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequestDto {
    @NotBlank(message = "Email Field can't be blank or null")
    private String email;
    @NotBlank(message = "Access Token Field can't be blank or null")
    private String accessToken;
    @NotBlank(message = " Refresh Token Field can't be blank or null")
    private String refreshToken;

}
