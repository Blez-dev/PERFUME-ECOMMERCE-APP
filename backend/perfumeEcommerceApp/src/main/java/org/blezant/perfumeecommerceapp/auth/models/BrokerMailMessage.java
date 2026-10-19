package org.blezant.perfumeecommerceapp.auth.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrokerMailMessage {
    private  String fromMail;
    private String toMail;
    private  String message;
    private  String exchangeName;
    private String routingKey;
}
