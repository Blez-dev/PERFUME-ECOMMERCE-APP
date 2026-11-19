package org.blezant.perfumeecommerceapp.payment.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient restClient(RestClient.Builder builder){
        return  builder.baseUrl("https://api.paystack.co").build();
    }
}
