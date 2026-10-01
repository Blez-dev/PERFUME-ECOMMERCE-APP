package org.blezant.perfumeecommerceapp.rabbitMQ.configuration;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    //create bean for exchange
    @Bean
    public DirectExchange authExchange(){
        return  new DirectExchange("auth.exchange");
    }

    //create a bean for the queue
    @Bean
    public Queue emailNotificationQueue(){
        return  new Queue("email.notification.queue");
    }

    //create a bean for the binding
    @Bean
    public Binding authEmailBinding(){
        return BindingBuilder.bind(emailNotificationQueue()).to(authExchange()).with("email.notification");
    }

    //create a bean for converter
    @Bean
    public MessageConverter messageConverter(){
        return new JacksonJsonMessageConverter();
    }

    //tweak rabbitMQ template
    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate= new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;

    }
}
