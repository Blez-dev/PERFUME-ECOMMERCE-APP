package org.blezant.perfumeecommerceapp.auth.models;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    @Email(message = "Email format not valid")
    @NotBlank(message = "Email field can't be blank")
    private String email;

    @NotBlank(message = "Username field can't be blank")
    @Size(message = "Least of 2 characters")
    private String username;

    @NotBlank(message = "Password field can't be blank")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$",
            message = "Password must be at least 8 characters and contain a number and a symbol"
            )
    private String password;

    @NotBlank(message = "VerificationTokenEntity field can't be blank")
    private String verificationToken;
    @NotBlank(message = "EmailOtp field can't be blank")
    @Size(message = "Least of 6 characters")
    @Column(name = "otpCode")
    private String emailOtp;
}
