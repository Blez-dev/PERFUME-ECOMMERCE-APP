package org.blezant.perfumeecommerceapp.payment.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentData {
    @JsonProperty("authorization_url")
    String authorizationUrl;



    String reference;
}
