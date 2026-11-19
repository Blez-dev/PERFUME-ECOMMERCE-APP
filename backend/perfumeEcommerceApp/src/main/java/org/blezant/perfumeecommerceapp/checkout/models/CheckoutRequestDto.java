package org.blezant.perfumeecommerceapp.checkout.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CheckoutRequestDto {
    private String cartId;
    private  String address;
}
