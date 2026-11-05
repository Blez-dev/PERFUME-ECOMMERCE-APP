package org.blezant.perfumeecommerceapp.cart.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.blezant.perfumeecommerceapp.cart.models.PerfumeAddRequestDto;
import org.blezant.perfumeecommerceapp.cart.models.PerfumeAddResponseDto;
import org.blezant.perfumeecommerceapp.cart.repositories.CartItemRepo;
import org.blezant.perfumeecommerceapp.cart.repositories.CartRepo;
import org.blezant.perfumeecommerceapp.cart.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {



    private final CartService cartService;
    private  final CartItemRepo cartItemRepo;
    private  final CartRepo cartRepo;

    public CartController(CartService cartService, CartItemRepo cartItemRepo, CartRepo cartRepo) {
        this.cartService = cartService;
        this.cartItemRepo = cartItemRepo;
        this.cartRepo = cartRepo;
    }

    @PostMapping("/cart/items")
    public ResponseEntity<PerfumeAddResponseDto> addPerfumeToCart(HttpServletRequest httpServletRequest,  @Valid  @RequestBody PerfumeAddRequestDto requestObject){
        PerfumeAddResponseDto perfumeAddResponseDto= cartService.addPerfumeToCart(httpServletRequest,requestObject);
        return  ResponseEntity.status(HttpStatus.CREATED).body(perfumeAddResponseDto);
    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest httpServletRequest){
        cartRepo.deleteAll();
        cartItemRepo.deleteAll();
return "done";
    }
}
