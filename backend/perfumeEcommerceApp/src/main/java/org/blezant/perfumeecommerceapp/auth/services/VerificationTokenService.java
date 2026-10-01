package org.blezant.perfumeecommerceapp.auth.services;


import org.blezant.perfumeecommerceapp.auth.entities.RegisterEntity;
import org.blezant.perfumeecommerceapp.auth.entities.VerificationTokenEntity;
import org.blezant.perfumeecommerceapp.auth.exceptions.CustomBadRequestException;
import org.blezant.perfumeecommerceapp.auth.models.BrokerMailMessage;
import org.blezant.perfumeecommerceapp.auth.models.VerifyRegisterRequestDto;
import org.blezant.perfumeecommerceapp.auth.models.VerifyRegisterResponseDto;
import org.blezant.perfumeecommerceapp.auth.repositories.AuthRepository;
import org.blezant.perfumeecommerceapp.auth.repositories.VerificationRepository;
import org.blezant.perfumeecommerceapp.rabbitMQ.producers.RabbitProducer;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class VerificationTokenService {


    private final AuthRepository authRepository;

    @Value("${resend.mail}")
    private String appMail;



    private  final RabbitProducer rabbitProducer;



    private final VerificationRepository verificationRepository;

    VerificationTokenService(VerificationRepository verificationRepository,AuthRepository authRepository,RabbitProducer rabbitProducer){
        this.verificationRepository=verificationRepository;

        this.authRepository=authRepository;
        this.rabbitProducer=rabbitProducer;

    }
    public  boolean verifyToken(String verificationToken,String otp,String email){

        return  true;
    }



    public VerifyRegisterResponseDto requestOtp(VerifyRegisterRequestDto data) {

        //confirm email is not senders mail
        //confirm email isn't present already
        emailCheck(data);
        //Generate random 6 digits
        Random random= new Random();
        String randomNumber=Integer.toString(random.nextInt(900000));
        //Hash OtpToken
        BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();
        String hashedToken= encoder.encode(randomNumber);
       //generate verification token
        String verificationToken = storeToken(data, hashedToken);
        //send to user's email
        sendMailToBroker(data, randomNumber);
        //return response
        VerifyRegisterResponseDto verifyRegisterResponseDto= new VerifyRegisterResponseDto();
        verifyRegisterResponseDto.setEmail(data.getEmail());
        verifyRegisterResponseDto.setVerificationToken(verificationToken);
        return  verifyRegisterResponseDto;
    }



    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void incrementAttempts(Optional<VerificationTokenEntity> veriToken) {
        VerificationTokenEntity verificationTokenData= verificationRepository.findById(veriToken.get().getVerificationToken()).get();
        long newAttemptValue= (long) verificationTokenData.getAttempts()+1;
        verificationRepository.incrementAttempt(newAttemptValue, veriToken.get().getVerificationToken());
    }

    public void emailCheck(VerifyRegisterRequestDto data) {
        if(data.getEmail().equals(appMail)){
            throw  new CustomBadRequestException("Forbidden Mail");
        }

        //check if email exists
        Optional<RegisterEntity> userData= authRepository.findByEmail(data.getEmail());
        if(userData.isPresent()){
            throw new CustomBadRequestException("User Already exists");
        }
    }

    public String storeToken(VerifyRegisterRequestDto data, String hashedToken) {
        String verificationToken= UUID.randomUUID().toString();
        //store token in database
        VerificationTokenEntity verificationTokenEntity= new VerificationTokenEntity();
        verificationTokenEntity.setVerificationToken(verificationToken);
        verificationTokenEntity.setAttempts(0);
        verificationTokenEntity.setEmail(data.getEmail());
        verificationTokenEntity.setExpiration(System.currentTimeMillis()+900000);
        verificationTokenEntity.setVerified(false);
        verificationTokenEntity.setOtpHash(hashedToken);
        verificationRepository.save(verificationTokenEntity);
        return verificationToken;
    }

    public void sendMailToBroker(VerifyRegisterRequestDto data, String randomNumber) {
        //send mail to broker
        BrokerMailMessage brokerMailMessage= new BrokerMailMessage();
        brokerMailMessage.setFromMail(appMail);
        brokerMailMessage.setToMail(data.getEmail());
        brokerMailMessage.setMessage( "Email Verification\n\n" +
                "Hello,\n\n" +
                "Your email verification code is: " + randomNumber + "\n\n" +
                "Please enter this code to verify your email address and complete your registration.\n\n" +
                "This verification code will expire in 15 minutes. " +
                "For your security, please do not share this code with anyone.\n\n" +
                "If you did not request this verification code, you can safely ignore this email.\n\n" +
                "Thank you,\n" +
                "The Scentra Team");
        brokerMailMessage.setExchangeName("auth.exchange");
        brokerMailMessage.setRoutingKey("email.notification");

        rabbitProducer.sendMail(brokerMailMessage);






    }
}
