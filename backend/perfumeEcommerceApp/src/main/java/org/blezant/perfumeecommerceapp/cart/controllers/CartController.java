package org.blezant.perfumeecommerceapp.cart.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.blezant.perfumeecommerceapp.cart.models.*;
import org.blezant.perfumeecommerceapp.cart.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {



    private final CartService cartService;


    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart/items")
    public ResponseEntity<PerfumeAddResponseDto> addPerfumeToCart(HttpServletRequest httpServletRequest,  @Valid  @RequestBody PerfumeAddRequestDto requestObject){
        PerfumeAddResponseDto perfumeAddResponseDto= cartService.addPerfumeToCart(httpServletRequest,requestObject);
        return  ResponseEntity.status(HttpStatus.CREATED).body(perfumeAddResponseDto);
    }

    @DeleteMapping("/cart-items/delete")
    public ResponseEntity<DeleteCartItemResponseDto> deleteCartItem(HttpServletRequest httpServletRequest, @Valid @RequestBody DeleteCartItemsRequestDto requestDto){
        DeleteCartItemResponseDto responseDto= cartService.deleteCartItem(httpServletRequest,requestDto);
        return  ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/cart/delete")
    public ResponseEntity<DeleteCartItemResponseDto> deleteCart(HttpServletRequest httpServletRequest, @Valid @RequestBody DeleteCartRequestDto requestBody){
        DeleteCartItemResponseDto responseData= cartService.deleteCart(httpServletRequest,requestBody);
        return  ResponseEntity.status(HttpStatus.OK).body(responseData);
    }


}
