package org.blezant.perfumeecommerceapp.auth.models;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgetPasswordVerifyRequestDto {
    @NotBlank(message = "email can't be null or empty")
    String email;
}
