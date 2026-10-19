package org.blezant.perfumeecommerceapp.auth.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminRegisterRequestDto {

    @NotBlank(message = "Email field can't be black")
    private String email;
    @NotBlank(message = "Password field can't be black")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$",
            message = "Password must be at least 8 characters and contain a number and a symbol"
    )
    private String password;
    @Size(message = "6 characters required")
    @NotBlank(message = "EmailOtp field can't be black")
    private String emailOtp;
    @NotBlank(message = "VerificationToken field can't be black")
    private  String verificationToken;
    @NotBlank(message = "Username field can't be black")
    private  String username;
}
