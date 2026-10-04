package org.blezant.perfumeecommerceapp.rabbitMQ.producers;


import org.blezant.perfumeecommerceapp.auth.models.BrokerMailMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitProducer {
    private  final RabbitTemplate rabbitTemplate;

    public RabbitProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMail(BrokerMailMessage brokerMailMessage) {
        rabbitTemplate.convertAndSend(brokerMailMessage.getExchangeName(),brokerMailMessage.getRoutingKey(),brokerMailMessage);

    }
}
