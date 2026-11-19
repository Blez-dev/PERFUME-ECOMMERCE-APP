package org.blezant.perfumeecommerceapp.payment.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.blezant.perfumeecommerceapp.payment.models.PaymentRequestDto;
import org.blezant.perfumeecommerceapp.payment.models.PaystackEventResponse;
import org.blezant.perfumeecommerceapp.payment.models.PaystackResponseDto;
import org.blezant.perfumeecommerceapp.payment.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    private final PaymentService paymentService;


    @PostMapping("/payment/initialize")
    public ResponseEntity<PaystackResponseDto> initializePayment(@Valid @RequestBody PaymentRequestDto requestData, HttpServletRequest httpServletRequest) {
        PaystackResponseDto paystackResponseDto = paymentService.initializePayment(requestData, httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(paystackResponseDto);
    }


    @PostMapping("/payment/webhook")
    public ResponseEntity<?> webhookUpdate(@RequestBody PaystackEventResponse requestData){
        paymentService.webhookUpdate(requestData);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
