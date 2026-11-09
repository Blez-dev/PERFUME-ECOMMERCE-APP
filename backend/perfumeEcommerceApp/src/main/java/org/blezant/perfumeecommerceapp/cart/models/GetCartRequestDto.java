package org.blezant.perfumeecommerceapp.cart.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GetCartRequestDto {
    @NotBlank(message = "This field cant be blank")
    @NotNull(message = "Field cant be null")
    private String userId;
}
