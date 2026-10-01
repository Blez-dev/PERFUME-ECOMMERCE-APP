package org.blezant.perfumeecommerceapp.rabbitMQ.consumers;


import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.blezant.perfumeecommerceapp.auth.exceptions.CustomBadRequestException;
import org.blezant.perfumeecommerceapp.auth.models.BrokerMailMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RabbitConsumer {


    @Value("${resend.api.key}")
    private  String resendApiKey;

    public RabbitConsumer() {

    }

    @RabbitListener(queues = "email.notification.queue")
    public void consumeMailMessage(BrokerMailMessage brokerMailMessage){
//        //create a mail message object
//        SimpleMailMessage mailMessage= new SimpleMailMessage();
//        mailMessage.setFrom(brokerMailMessage.getFromMail());
//        mailMessage.setSentDate(new Date());
//        mailMessage.setSubject("Email verification Token");
//        mailMessage.setText(
//                brokerMailMessage.getMessage()
//        );
//        mailMessage.setTo(brokerMailMessage.getToMail());
//        javaMailSender.send(mailMessage);


        //create Resend Object
        Resend resend= new Resend(resendApiKey);


        CreateEmailOptions messageBody= CreateEmailOptions.builder()
                .from("onboarding@resend.dev").
                to(brokerMailMessage.getToMail())
                .text(brokerMailMessage.getMessage())
                .subject("Email Verification Token")
                .build();

        try{
            CreateEmailResponse response=resend.emails().send(messageBody);
        } catch (ResendException e) {
            throw new CustomBadRequestException(e.getMessage());
        }

    }
}
