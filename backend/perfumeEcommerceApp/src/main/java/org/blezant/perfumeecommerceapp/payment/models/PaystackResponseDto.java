package org.blezant.perfumeecommerceapp.payment.models;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaystackResponseDto {
    private boolean status;
    private String message;
    private PaymentData data;
}
