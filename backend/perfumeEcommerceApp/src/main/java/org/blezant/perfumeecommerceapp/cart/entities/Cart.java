package org.blezant.perfumeecommerceapp.cart.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
public class Cart {
    @OneToMany(cascade = CascadeType.ALL)
            @JoinColumn(name = "cart_cart_item")
    private List<CartItem> cartItem=new ArrayList<>();
    @Id
    private String cartId;
    private String userId;
}
