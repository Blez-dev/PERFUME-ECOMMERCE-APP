package org.blezant.perfumeecommerceapp.checkout.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Data
public class CheckoutResponseDto {
    private String orderId;
}
