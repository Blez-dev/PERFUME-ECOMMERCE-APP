package org.blezant.perfumeecommerceapp.auth.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgetPasswordResponseDto {
    private String email;
    private boolean status;
    private String message;
}
