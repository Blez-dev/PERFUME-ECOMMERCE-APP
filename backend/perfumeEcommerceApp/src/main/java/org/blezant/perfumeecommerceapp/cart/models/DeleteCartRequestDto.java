package org.blezant.perfumeecommerceapp.cart.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DeleteCartRequestDto {
    private String cartId;
}
