package org.blezant.perfumeecommerceapp.checkout.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.blezant.perfumeecommerceapp.checkout.models.CheckoutRequestDto;
import org.blezant.perfumeecommerceapp.checkout.models.CheckoutResponseDto;
import org.blezant.perfumeecommerceapp.checkout.services.CheckoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutController {
    private final CheckoutService checkoutService;
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }
    @PostMapping("/checkout")
    public ResponseEntity<?> checkoutCart(@Valid @RequestBody CheckoutRequestDto requestBody, HttpServletRequest httpServletRequest) {
        CheckoutResponseDto responseData = checkoutService.checkout(requestBody, httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
}
