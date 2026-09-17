package org.blezant.perfumeecommerceapp.auth.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private  Boolean status;
    private String message;
    private String error;
}
