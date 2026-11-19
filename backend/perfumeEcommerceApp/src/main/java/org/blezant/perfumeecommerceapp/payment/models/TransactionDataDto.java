package org.blezant.perfumeecommerceapp.payment.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDataDto {
    private Long id;
    private String domain;
    private String status;
    private String reference;
    private String receipt_number;
    private Long amount;
    private String message;
    private String gateway_response;
    private String paid_at;
    private String created_at;
    private String channel;
    private String currency;
    private String ip_address;
    private Long fees;
}
