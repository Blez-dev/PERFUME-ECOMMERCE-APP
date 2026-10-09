package org.blezant.perfumeecommerceapp.auth.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteAccountRequestDto {

    @NotBlank(message = "Email Can't be null")
    private String email;
    @NotBlank(message = "Password Can't be null")
    private String password;
}
