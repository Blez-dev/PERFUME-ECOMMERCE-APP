package org.blezant.perfumeecommerceapp.payment.services;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.blezant.perfumeecommerceapp.auth.entities.RegisterEntity;
import org.blezant.perfumeecommerceapp.auth.repositories.AuthRepository;
import org.blezant.perfumeecommerceapp.exceptions.CustomBadRequestException;
import org.blezant.perfumeecommerceapp.external.rabbitMQ.producer.RabbitProducer;
import org.blezant.perfumeecommerceapp.order.entities.Orders;
import org.blezant.perfumeecommerceapp.order.repositories.OrderRepo;
import org.blezant.perfumeecommerceapp.order.services.OrderService;
import org.blezant.perfumeecommerceapp.payment.models.*;
import org.blezant.perfumeecommerceapp.product.entities.Perfumes;
import org.blezant.perfumeecommerceapp.product.repositories.PerfumeRepository;
import org.blezant.perfumeecommerceapp.security.jwt.services.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {


    @Value("${paystack.payment.secret.key}")
    private String paystackTestKey;

    private final OrderService orderService;
    private final JwtService jwtService;
    private final RestClient restClient;
    private final OrderRepo orderRepo;

    public PaymentService(OrderService orderService, JwtService jwtService, RestClient restClient, OrderRepo orderRepo, AuthRepository authRepository, RabbitProducer rabbitProducer) {
        this.orderService = orderService;
        this.jwtService = jwtService;
        this.restClient = restClient;
        this.orderRepo = orderRepo;
        this.authRepository = authRepository;
        this.rabbitProducer = rabbitProducer;
    }

    private final AuthRepository authRepository;
    private final RabbitProducer rabbitProducer;




    public PaystackResponseDto initializePay(PaymentRequestDto requestData, HttpServletRequest httpServletRequest){
        //Extract variables
        String jwtToken = jwtService.extractJwtToken(httpServletRequest);
        String reference=null;
        String authUrl=null;
        String userId = jwtService.extractUserId(jwtToken);
        String orderId = requestData.getOrderId();
        //Verify order Id
        Optional<Orders> orders = orderService.getOrderById(orderId);
        if (orders.isEmpty()) {
            throw new CustomBadRequestException("Invalid Order Id");
        }
        //verify user ownership
        boolean isOwner = orders.get().getRegisterEntity().getUserId().equals(userId);
        if (!isOwner) {
            throw new CustomBadRequestException("invalid request");
        }

        //create response object
        PaystackResponseDto res = new PaystackResponseDto();
        //Build request Data
        Map<String, Object> data = new HashMap<>();
        data.put("email", orders.get().getRegisterEntity().getEmail());
        BigDecimal amount = orders.get().getTotalPrice().multiply(new BigDecimal("100"));
        data.put("amount", amount.toString());

        //Initialize payment
        res = restClient
                .post()
                .uri("transaction/initialize")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + paystackTestKey)
                .body(data)
                .retrieve()
                .body(PaystackResponseDto.class);

        if (res == null || !res.isStatus()) {
            throw new CustomBadRequestException("An error occurred with payment initialization");
        }
        //update order record
        reference = res.getData().getReference();
        authUrl   = res.getData().getAuthorizationUrl();
        orderService.updateRecord(authUrl,reference, requestData.getOrderId());
        orders = orderService.getOrderById(orderId);
        //if any state
        res.setStatus(true);
        res.setMessage("Authorization Url");
        PaymentData paymentData = new PaymentData();
        paymentData.setReference(reference);
        paymentData.setAuthorizationUrl(authUrl);
        res.setData(paymentData);

        return res;

    }

    public PaystackResponseDto initializePayment(PaymentRequestDto requestData, HttpServletRequest httpServletRequest) {

        //Extract variables
        String jwtToken = jwtService.extractJwtToken(httpServletRequest);
        String reference=null;
        String authUrl=null;
        String userId = jwtService.extractUserId(jwtToken);
        String orderId = requestData.getOrderId();
        //Verify order Id
        Optional<Orders> orders = orderService.getOrderById(orderId);
        if (orders.isEmpty()) {
            throw new CustomBadRequestException("Invalid Order Id");
        }
        //verify user ownership
        boolean isOwner = orders.get().getRegisterEntity().getUserId().equals(userId);
        if (!isOwner) {
            throw new CustomBadRequestException("invalid request");
        }

        //create response object
        PaystackResponseDto res = new PaystackResponseDto();

        //check order status .only generate new if order has failed
        if (orders.get().getReference() == null) {
            //Build request Data
            Map<String, Object> data = new HashMap<>();
            data.put("email", orders.get().getRegisterEntity().getEmail());
            BigDecimal amount = orders.get().getTotalPrice().multiply(new BigDecimal("100"));
            data.put("amount", amount.toString());

            //Initialize payment
            res = restClient
                    .post()
                    .uri("transaction/initialize")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + paystackTestKey)
                    .body(data)
                    .retrieve()
                    .body(PaystackResponseDto.class);

            if (res == null || !res.isStatus()) {
                throw new CustomBadRequestException("An error occurred with payment initialization");
            }
            //update order record
            reference = res.getData().getReference();
            authUrl   = res.getData().getAuthorizationUrl();
            orderService.updateRecord(authUrl,reference, requestData.getOrderId());
            orders = orderService.getOrderById(orderId);
        }

        //make a new call to fetch order
        Orders newOrderObject = orderService.getOrderById(orderId).get();

        //verify if payment is successful
        VerifyPaymentDto verifyPaymentDto = restClient
                .get()
                .uri("/transaction/verify/" + newOrderObject.getReference())
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + paystackTestKey)
                .retrieve().body(VerifyPaymentDto.class);

        //verify not null
        if (verifyPaymentDto == null) {
            throw new CustomBadRequestException("An error occured");
        }


        boolean isPaid = verifyPaymentDto.getData().getStatus().equals("success");

        //verify success
        if (isPaid) {
            //update status
            orderService.updatePaymentStatus("PAID", requestData.getOrderId());
        }
        if (isPaid) {
            throw new CustomBadRequestException("Order already paid");
        }


        //verify failed
        if (verifyPaymentDto.getData().getStatus().equals("failed")) {
            if (orders.get().getReference() == null) {
                //Build request Data
                Map<String, Object> data = new HashMap<>();
                data.put("email", orders.get().getRegisterEntity().getEmail());
                BigDecimal amount = orders.get().getTotalPrice().multiply(new BigDecimal("100"));
                data.put("amount", amount.toString());

                res = restClient
                        .post()
                        .uri("transaction/initialize")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + paystackTestKey)
                        .body(data)
                        .retrieve()
                        .body(PaystackResponseDto.class);

                if (res == null || !res.isStatus()) {
                    throw new CustomBadRequestException("An error occurred with payment initialization");
                }

                //update order record
                System.out.println("ref"+res.getData().getReference());
                orderService.updateRecord(res.getData().getAuthorizationUrl(), res.getData().getReference(), requestData.getOrderId());
            }


        }

        //if any state
        res.setStatus(true);
        res.setMessage("Authorization Url exists");
        PaymentData paymentData = new PaymentData();
        paymentData.setReference(orders.get().getReference());
        paymentData.setAuthorizationUrl(orders.get().getAuthorizationUrl());
        res.setData(paymentData);
        return res;
    }

    public void updateRecord(String authorizationUrl, String reference, String orderId) {
        orderRepo.updateRecord(authorizationUrl, reference, orderId);
    }






    @Transactional
    public void webhookUpdate(PaystackEventResponse requestData) {
        String referenceNumber = requestData.getData().getReference();
        //check for the referenceObject
        Optional<Orders> orderObject = orderRepo.findByReference(referenceNumber);
        if (orderObject.isEmpty()) {
            throw new CustomBadRequestException("invalid reference Id");
        }


        //Extract all variables
        String email = orderObject.get().getRegisterEntity().getEmail();
        BigDecimal amount = orderObject.get().getTotalPrice();
        String orderId = orderObject.get().getOrderId();
        String paymentStatus = requestData.getEvent();
        String name = orderObject.get().getRegisterEntity().getUsername();
        //Build a map
        Map<String, String> mailData = new HashMap<>();
        mailData.put("email", email);
        mailData.put("exchange", "payment.mail.exchange");
        mailData.put("key", "payment.mail.key");
        //check for event type

        //if payment fails
        if (paymentStatus.equals("transfer.failed")) {
            String message = """
                    
                    Hello %s,
                    
                    We regret to inform you that your payment of %s for your order %s was unsuccessful.
                    
                    Payment Status: Failed \s
                    Amount: %s \s
                    
                    
                    No successful payment was recorded for this transaction.
                    
                    Please try again using your preferred payment method. If you believe this is an error, kindly contact our support team for assistance.
                    
                    Thank you for choosing Scentra.
                    
                    Best regards, \s
                    **Scentra Team**
                    """.formatted(name, amount, orderId, amount);
            //send message to broker
            mailData.put("message", message);
            rabbitProducer.sendPaymentMail(mailData);
            throw new CustomBadRequestException("Failed Payment");
        }

        //if payment is successful
        if (paymentStatus.equals("transfer.success") || paymentStatus.equals("charge.success")) {


            String message = """
                    Hello %s,
                    
                    Good news! Your payment has been received successfully. Thank you for shopping with Scentra.
                    
                    Payment Status: Successful
                    Amount Paid: %s
                    Order ID: %s
                    
                    
                    Your payment has been confirmed, and your order is now being processed.
                    
                    We appreciate your trust in Scentra and look forward to serving you again.
                    
                    Best regards,
                    Scentra Team
                    """.formatted(name, amount, orderId);
            //send message to broker
            mailData.put("message", message);

            rabbitProducer.sendPaymentMail(mailData);

            orderRepo.updatePaymentStatus("PAID",orderId);
        }




    }
}
