package org.blezant.perfumeecommerceapp.cart.models;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteCartItemsRequestDto {
    @NotBlank(message = "cartItemId field cant be blank")
    private  String cartItemId;
}
