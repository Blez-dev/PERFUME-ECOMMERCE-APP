package org.blezant.perfumeecommerceapp.auth.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordResponseDto {
    String email;
    String message ;
    boolean status;
}
