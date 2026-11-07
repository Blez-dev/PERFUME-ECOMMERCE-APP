package org.blezant.perfumeecommerceapp.cart.services;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.blezant.perfumeecommerceapp.cart.entities.Cart;
import org.blezant.perfumeecommerceapp.cart.entities.CartItem;
import org.blezant.perfumeecommerceapp.cart.models.*;
import org.blezant.perfumeecommerceapp.cart.repositories.CartItemRepo;
import org.blezant.perfumeecommerceapp.cart.repositories.CartRepo;
import org.blezant.perfumeecommerceapp.exceptions.CustomBadRequestException;
import org.blezant.perfumeecommerceapp.product.entities.Perfumes;
import org.blezant.perfumeecommerceapp.product.repositories.PerfumeRepository;
import org.blezant.perfumeecommerceapp.security.jwt.services.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    private final JwtService jwtService;
    private final PerfumeRepository perfumeRepository;
    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;


    public CartService(JwtService jwtService, PerfumeRepository perfumeRepository, CartRepo cartRepo, CartItemRepo cartItemRepo) {
        this.jwtService = jwtService;
        this.perfumeRepository = perfumeRepository;
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
    }

    public PerfumeAddResponseDto addPerfumeToCart(HttpServletRequest httpServletRequest, PerfumeAddRequestDto requestObject) {
        //Validate User
        //Extract jwt Token
        String jwtToken = jwtService.extractJwtToken(httpServletRequest);
        //Extract user's role
        String role = jwtService.getUserRole(jwtToken);
        //validate user's Role
        if (role.equals("ADMIN")) {
            throw new CustomBadRequestException("Forbidden Request");
        }

        //Validate perfume Id
        //Extract perfumeId
        String perfumeId = requestObject.getPerfumeId();
        //check if perfume exists
        Optional<Perfumes> perfume = perfumeRepository.findById(perfumeId);
        if (perfume.isEmpty()) {
            throw new CustomBadRequestException("Perfume Does Not Exist");
        }

        //validate available quantity
        //Get Perfume Object
        Perfumes perfumeObject = perfume.get();
        //convert data type
        BigInteger dbQuantity = BigInteger.valueOf(perfumeObject.getQuantities());
        int isGreater = dbQuantity.compareTo(requestObject.getUnits());
        if (isGreater < 0) {
            throw new CustomBadRequestException("Out Of Stock Exception");
        }
        //Update Quantity record
        Long requestedUnit = requestObject.getUnits().longValue();
        perfumeRepository.updateQuantityRecord(requestedUnit, requestObject.getPerfumeId());

        //Check if any cart exists with user
        String userId = jwtService.extractUserId(jwtToken);
        Optional<Cart> cart = cartRepo.findByUserId(userId);

        //create new cart
        if (cart.isEmpty()) {
            //create a cartItem
            CartItem cartItem = new CartItem();
            cartItem.setPerfumes(perfumeObject);
            cartItem.setUserID(userId);
            cartItem.setCartItemId("cart-item-" + UUID.randomUUID().toString().substring(0, 8));
            BigInteger requestedQuantity = BigInteger.valueOf(requestedUnit);
            cartItem.setUnits(requestedQuantity);
            cartItemRepo.save(cartItem);
            //create cart object
            Cart cartObject = new Cart();
            cartObject.getCartItem().add(cartItem);
            cartObject.setUserId(userId);
            cartObject.setCartId("cart-" + UUID.randomUUID().toString().substring(0, 8));
            cartRepo.save(cartObject);
        } else {
            //check if product exists
            String perfumeIdFromRequest = requestObject.getPerfumeId();
            //fetch list of cart items
            List<CartItem> cartItems = cart.get().getCartItem();
            String cartItemIdHit= null;
            //item exists?
            boolean isPresent= false;
            for (int i = 0; i < cartItems.size(); i++) {
                String perfumeIdTemp = cartItems.get(i).getPerfumes().getPid();
                if (perfumeIdFromRequest.equals(perfumeIdTemp)) {
                    isPresent=true;
                    cartItemIdHit=cartItems.get(i).getCartItemId();
                    break;
            }
        }

            if(isPresent){
                int quantity = requestObject.getUnits().intValue();
                   cartItemRepo.updateStock(quantity, cartItemIdHit);
            }else{
                //create a cartItem
                    CartItem cartItem = new CartItem();
                    cartItem.setPerfumes(perfumeObject);
                    cartItem.setUserID(userId);
                    cartItem.setCartItemId("cart-item-" + UUID.randomUUID().toString().substring(0, 8));
                    cartItem.setUnits(requestObject.getUnits());
                    cartItemRepo.save(cartItem);
                //Add to Cart
                    Cart cart1 = cart.get();
                   cart1.getCartItem().add(cartItem);
                    cartRepo.save(cart1);
            }




        }
        //Build response
        PerfumeAddResponseDto perfumeAddResponseDto = new PerfumeAddResponseDto();
        perfumeAddResponseDto.setPerfumeId(requestObject.getPerfumeId());
        perfumeAddResponseDto.setStatus(true);
        perfumeAddResponseDto.setMessage("Added to cart successfully");


        //return respomse
        return perfumeAddResponseDto;
    }

    @Transactional
    public DeleteCartItemResponseDto deleteCartItem(HttpServletRequest httpServletRequest, DeleteCartItemsRequestDto requestDto) {
        //Check if cartItem exists
        String cartItemId= requestDto.getCartItemId();
        Optional<CartItem> cartItem= cartItemRepo.findById(cartItemId);
        if(cartItem.isEmpty()){
            throw new CustomBadRequestException("Invalid cart Item");
        }
        //Extract user Id
        String jwtToken= jwtService.extractJwtToken(httpServletRequest);
        String userId=jwtService.extractUserId(jwtToken);
        //validate user's ownership
        if(!(userId.equals(cartItem.get().getUserID()))){
            throw new CustomBadRequestException("Forbidden request");
        }
        //update quantity
        int itemUnits= cartItem.get().getUnits().intValue();
        String perfumeId= cartItem.get().getPerfumes().getPid();
        perfumeRepository.updateQuantity(itemUnits,perfumeId);

        //find cartId
         Optional<Cart> cart=cartRepo.findByUserId(userId);
        String cartId=cart.get().getCartId();

        //Delete cart Item
        cartItemRepo.deleteById(cartItemId);


        //Delete whole cart if no item remains
        List<CartItem> cartItems=cartItemRepo.findByCartId(cartId);
        if(cartItems.isEmpty()){
            cartRepo.deleteById(cartId);
        }

        //Build response
        DeleteCartItemResponseDto deleteCartItemResponseDto= new DeleteCartItemResponseDto();
        deleteCartItemResponseDto.setMessage("Cart Item Deleted Successfully");
        deleteCartItemResponseDto.setStatus(true);
        return  deleteCartItemResponseDto;


    }


    @Transactional
    public DeleteCartItemResponseDto deleteCart(HttpServletRequest httpServletRequest, DeleteCartRequestDto requestBody) {
        //verify cart exists
        Optional<Cart> cart= cartRepo.findById(requestBody.getCartId());
        if(cart.isEmpty()){
            throw new CustomBadRequestException("Invalid cart Item");
        }

        //confirm user's ownership
        String jwtToken= jwtService.extractJwtToken(httpServletRequest);
        String userId=jwtService.extractUserId(jwtToken);
        if(!(userId.equals(cart.get().getUserId()))){
            throw new CustomBadRequestException("Forbidden request");
        }

        //Delete all cart Items in the cart
        cartItemRepo.deleteByCartId(requestBody.getCartId());

        //Delete cart itself
        cartRepo.deleteById(requestBody.getCartId());

        //Build response
        DeleteCartItemResponseDto deleteCartItemResponseDto= new DeleteCartItemResponseDto();
        deleteCartItemResponseDto.setMessage("Cart  Deleted Successfully");
        deleteCartItemResponseDto.setStatus(true);
        return  deleteCartItemResponseDto;
    }
}
