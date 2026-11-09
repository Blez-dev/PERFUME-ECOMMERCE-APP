package org.blezant.perfumeecommerceapp.cart.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemRequestDto {

    @NotBlank(message = "This field cant be blank")
    @NotNull(message = "Field cant be null")
    private  String cartId;
    @NotNull(message = "Field cant be null")
    @NotBlank(message = "This field cant be blank")
    private String cartItemId;
}
