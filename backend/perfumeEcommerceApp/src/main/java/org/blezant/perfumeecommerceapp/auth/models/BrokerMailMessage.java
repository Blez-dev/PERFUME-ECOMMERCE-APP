package org.blezant.perfumeecommerceapp.auth.models;


import lombok.Data;

@Data
public class BrokerMailMessage {
    private  String fromMail;
    private String toMail;
    private  String message;
    private  String exchangeName;
    private String routingKey;
}
