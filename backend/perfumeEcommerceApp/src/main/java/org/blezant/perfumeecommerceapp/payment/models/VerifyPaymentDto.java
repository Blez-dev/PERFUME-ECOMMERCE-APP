package org.blezant.perfumeecommerceapp.payment.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VerifyPaymentDto {
    private boolean status;
    private String message;
    private TransactionDataDto data;
}
