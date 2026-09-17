package org.blezant.perfumeecommerceapp.auth.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    String userId;
    String email;
    String refreshToken;
    String accessToken;
    boolean status;
    String message;
}
