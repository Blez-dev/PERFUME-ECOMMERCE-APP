package org.blezant.perfumeecommerceapp.rabbitMQ.consumers;


import org.blezant.perfumeecommerceapp.auth.models.BrokerMailMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RabbitConsumer {
    private  final JavaMailSender javaMailSender;

    public RabbitConsumer(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @RabbitListener(queues = "email.notification.queue")
    public void consumeMailMessage(BrokerMailMessage brokerMailMessage){
        //create a mail message object
        SimpleMailMessage mailMessage= new SimpleMailMessage();
        mailMessage.setFrom(brokerMailMessage.getFromMail());
        mailMessage.setSentDate(new Date());
        mailMessage.setSubject("Email verification Token");
        mailMessage.setText(
                brokerMailMessage.getMessage()
        );
        mailMessage.setTo(brokerMailMessage.getToMail());
        javaMailSender.send(mailMessage);
    }
}
