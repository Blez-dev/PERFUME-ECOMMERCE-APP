package org.blezant.perfumeecommerceapp.auth.models;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequestDto {
    @NotBlank(message = "Email field can't be blank or null")
    private String email;
    @NotBlank(message = "New Password field can't be blank or null")
    private String newPassword;
    @NotBlank(message = "Old Password field can't be blank or null")
    private String oldPassword;
}
