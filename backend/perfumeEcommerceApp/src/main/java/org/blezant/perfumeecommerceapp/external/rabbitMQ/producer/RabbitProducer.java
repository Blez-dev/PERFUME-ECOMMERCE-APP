package org.blezant.perfumeecommerceapp.external.rabbitMQ.producer;


import org.blezant.perfumeecommerceapp.product.models.ImageUploadData;
import org.blezant.perfumeecommerceapp.auth.models.BrokerMailMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RabbitProducer {
    private  final RabbitTemplate rabbitTemplate;

    public RabbitProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMail(BrokerMailMessage brokerMailMessage) {
        rabbitTemplate.convertAndSend(brokerMailMessage.getExchangeName(),brokerMailMessage.getRoutingKey(),brokerMailMessage);

    }

    public void uploadImage(ImageUploadData imageUploadData){
        rabbitTemplate.convertAndSend(imageUploadData.getExchangeName(),imageUploadData.getRoutingKey(),imageUploadData);
    }

    public void sendPaymentMail(Map<String,String> data){
        rabbitTemplate.convertAndSend(data.get("exchange"),data.get("key"),data);
    }
}
