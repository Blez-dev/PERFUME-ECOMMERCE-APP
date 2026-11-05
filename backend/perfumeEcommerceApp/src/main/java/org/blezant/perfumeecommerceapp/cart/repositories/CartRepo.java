package org.blezant.perfumeecommerceapp.cart.repositories;


import org.blezant.perfumeecommerceapp.cart.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo  extends JpaRepository<Cart,String> {


    @Query(value = "select * from cart where user_id=?1;",nativeQuery = true)
    Optional<Cart> findByUserId(String userId);
}
