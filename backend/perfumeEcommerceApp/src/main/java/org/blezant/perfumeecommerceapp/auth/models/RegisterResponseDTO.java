package org.blezant.perfumeecommerceapp.auth.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDTO {
    private String email;
    private String userId;
    private String username;
    private boolean status;
    private String message ;
}
