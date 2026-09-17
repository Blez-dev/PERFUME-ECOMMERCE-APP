package org.blezant.perfumeecommerceapp.jwt.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponseDto {
    @NotBlank(message = "access token can't be null or empty")
    private String accessToken;
    @NotBlank(message = "email  can't be null or empty")
    private String email;
    @NotBlank(message = "status can't be null or empty")
    private boolean status;
}
