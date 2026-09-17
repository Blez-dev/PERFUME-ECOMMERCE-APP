package org.blezant.perfumeecommerceapp.auth.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank
    String email;
    @NotBlank(message = "Password field can't be blank")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,}$",
            message = "Password must be at least 8 characters and contain a number and a symbol"
    )
    String password;
}
