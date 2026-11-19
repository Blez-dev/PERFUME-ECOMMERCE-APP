package org.blezant.perfumeecommerceapp.payment.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class PaystackEventResponse {

    private String event;
    private WebhookData data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WebhookData {
        private String reference;
        private Long amount;
    }
}
