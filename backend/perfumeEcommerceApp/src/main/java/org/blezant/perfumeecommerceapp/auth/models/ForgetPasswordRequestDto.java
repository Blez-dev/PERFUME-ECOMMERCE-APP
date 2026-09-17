package org.blezant.perfumeecommerceapp.auth.models;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForgetPasswordRequestDto {
    @NotBlank(message = "Email field can't be blank")
    private String email;
    @NotBlank(message = "newPassword field can't be blank")
    private String newPassword;
    @NotBlank(message = "verificationToken field can't be blank")
    private String verificationToken;
    @NotBlank(message = "otpCode field can't be blank")
    private  String otpCode;

}
