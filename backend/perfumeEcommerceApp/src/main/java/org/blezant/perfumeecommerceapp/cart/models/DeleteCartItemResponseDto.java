package org.blezant.perfumeecommerceapp.cart.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteCartItemResponseDto {
    private String message;
    boolean status;
}
