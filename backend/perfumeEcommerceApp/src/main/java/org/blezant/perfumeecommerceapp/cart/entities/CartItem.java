package org.blezant.perfumeecommerceapp.cart.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blezant.perfumeecommerceapp.product.entities.Perfumes;

import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Data
@NoArgsConstructor
@Table(name = "cart_item")
public class CartItem {

    @ManyToOne
    @JoinColumn(name = "cart_item_perfume")
   private Perfumes perfumes;
    private BigInteger units;
    @Id
    private String cartItemId;
    private String userID;
}
