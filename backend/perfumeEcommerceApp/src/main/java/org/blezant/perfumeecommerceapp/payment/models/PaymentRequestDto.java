package org.blezant.perfumeecommerceapp.payment.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentRequestDto {
    private String orderId;
}
