package org.blezant.perfumeecommerceapp.rabbitMQ.consumers;


import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.blezant.perfumeecommerceapp.admin.models.ImageUploadData;
import org.blezant.perfumeecommerceapp.auth.exceptions.CustomBadRequestException;
import org.blezant.perfumeecommerceapp.auth.models.BrokerMailMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class RabbitConsumer {


    @Value("${resend.api.key}")
    private  String resendApiKey;

    @Value("${cloud.aws.bucket-name}")
    private  String bucketName;

    private  final S3Client s3Client;

    public RabbitConsumer(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @RabbitListener(queues = "image.queue")
    public  void uploadImage(ImageUploadData imageUploadData){


        //create Resend Object
        Resend resend= new Resend(resendApiKey);
        CreateEmailOptions messageBody= CreateEmailOptions.builder()
                .from("scentra-app@api.blezant.com").
                to(imageUploadData.getToMail())
                .text(imageUploadData.getMessage())
                .subject("Image Upload")
                .build();

        try{
            CreateEmailResponse response=resend.emails().send(messageBody);
        } catch (ResendException e) {
            throw new CustomBadRequestException(e.getMessage());
        }

    }

    @RabbitListener(queues = "email.notification.queue")
    public void consumeMailMessage(BrokerMailMessage brokerMailMessage){



        //create Resend Object
        Resend resend= new Resend(resendApiKey);


        CreateEmailOptions messageBody= CreateEmailOptions.builder()
                .from("scentra-app@api.blezant.com").
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
