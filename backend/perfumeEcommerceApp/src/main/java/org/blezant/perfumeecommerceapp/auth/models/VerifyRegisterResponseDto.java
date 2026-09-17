package org.blezant.perfumeecommerceapp.auth.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyRegisterResponseDto {
   private  String email;
   private String verificationToken;
}
