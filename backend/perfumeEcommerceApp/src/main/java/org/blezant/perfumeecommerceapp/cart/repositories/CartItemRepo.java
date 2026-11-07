package org.blezant.perfumeecommerceapp.cart.repositories;


import org.blezant.perfumeecommerceapp.cart.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,String> {


    @Modifying
    @Transactional
    @Query(value = "update cart_item set units=units+?1 where cart_item_id=?2",nativeQuery = true)
    void updateStock(int quantity, String cartItemId);



    @Query(value = "select * from cart_item where cart_cart_item=?1",nativeQuery = true)
    List<CartItem> findByCartId(String cartId);


    @Modifying
    @Transactional
    @Query(value = "delete from cart_item where cart_cart_item=?1",nativeQuery = true)
    void deleteByCartId(String cartId);
}
