package org.blezant.perfumeecommerceapp.checkout.services;

import jakarta.servlet.http.HttpServletRequest;
import org.blezant.perfumeecommerceapp.auth.repositories.AuthRepository;
import org.blezant.perfumeecommerceapp.cart.entities.Cart;
import org.blezant.perfumeecommerceapp.cart.entities.CartItem;
import org.blezant.perfumeecommerceapp.cart.repositories.CartItemRepo;
import org.blezant.perfumeecommerceapp.cart.repositories.CartRepo;
import org.blezant.perfumeecommerceapp.checkout.models.CheckoutRequestDto;
import org.blezant.perfumeecommerceapp.checkout.models.CheckoutResponseDto;
import org.blezant.perfumeecommerceapp.exceptions.CustomBadRequestException;
import org.blezant.perfumeecommerceapp.order.entities.Orders;
import org.blezant.perfumeecommerceapp.order.entities.OrderItem;
import org.blezant.perfumeecommerceapp.order.repositories.OrderItemRepo;
import org.blezant.perfumeecommerceapp.order.repositories.OrderRepo;
import org.blezant.perfumeecommerceapp.order.services.OrderService;
import org.blezant.perfumeecommerceapp.security.jwt.services.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CheckoutService {

    private final OrderService orderService;
    private final JwtService jwtService;
    private final CartRepo cartRepo;
    private  final CartItemRepo cartItemRepo;
    private  final AuthRepository authRepository;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;

    public CheckoutService(OrderService orderService, JwtService jwtService, CartRepo cartRepo, CartItemRepo cartItemRepo, AuthRepository authRepository, OrderRepo orderRepo, OrderItemRepo orderItemRepo) {
        this.orderService = orderService;
        this.jwtService = jwtService;
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.authRepository = authRepository;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }


    @Transactional
    public CheckoutResponseDto checkout(CheckoutRequestDto requestBody, HttpServletRequest httpServletRequest) {
        //Verify user
        String jwtToken=jwtService.extractJwtToken(httpServletRequest);
        String userId= jwtService.extractUserId(jwtToken);
        String userRole= jwtService.getUserRole(jwtToken);

        //Extract Cart
       Optional<Cart>  cart= cartRepo.findById(requestBody.getCartId());
       if(cart.isEmpty()){
           throw new CustomBadRequestException("Invalid cartId");
       }
       //recalculate  cartItems price and save orders objects
        List<CartItem> cartItemList =cart.get().getCartItem();


        //create orders object
        Orders orders = new Orders();
        String orderId= "orders"+"-"+UUID.randomUUID().toString().substring(0,8);
        orders.setOrderId(orderId);
        orders.setAddress(requestBody.getAddress());
        orders.setStatus("PENDING");
        orders.setTimeCreated(Instant.now());
        orders.setRegisterEntity(authRepository.findById(userId).get());
        BigDecimal price= new BigDecimal("0");
        for(int i=0;i<cartItemList.size();i++){
            //create orderItem object
            OrderItem orderItem= new OrderItem();
            CartItem cartItemObject= cartItemList.get(i);
            CartItem cartItem= new CartItem();
            orderItem.setOrderId(orderId);
            orderItem.setPerfumes(cartItemObject.getPerfumes());
            orderItem.setOrderItemId("order_item"+"-"+UUID.randomUUID().toString().substring(0,8));
            orderItem.setUnits(cartItemObject.getUnits());
            //add price
            BigDecimal computedPrice= cartItemObject.getPerfumes().getPrice().multiply(new BigDecimal(cartItemObject.getUnits()));
            price=price.add(computedPrice);
            //save orders item
            orderItemRepo.save(orderItem);
            orders.getOrderItems().add(orderItem);
        }
        price=price.add(new BigDecimal("1500"));
        orders.setTotalPrice(price);
        //save orders
        orderRepo.save(orders);

        //delete cart and cartItems
        cartRepo.deleteById(requestBody.getCartId());
        //return response
        return  new CheckoutResponseDto(orderId);
    }



}


