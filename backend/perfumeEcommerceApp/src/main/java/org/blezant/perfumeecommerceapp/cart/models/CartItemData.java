package org.blezant.perfumeecommerceapp.cart.models;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;


@Data
@NoArgsConstructor
public class CartItemData {
    private String cartItemId;
    private String imageUrl;
    private String name;
    private String category;
    private  BigDecimal price;
    private BigInteger units;

}
