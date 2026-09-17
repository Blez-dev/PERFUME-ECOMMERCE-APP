package org.blezant.perfumeecommerceapp.auth.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForgetPasswordVerifyResponseDto {
    String email;
    String verificationToken;
    boolean status;
}
