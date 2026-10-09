package org.blezant.perfumeecommerceapp.auth.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteAccountResponseDto {
    private String message;
    private boolean status;
}
