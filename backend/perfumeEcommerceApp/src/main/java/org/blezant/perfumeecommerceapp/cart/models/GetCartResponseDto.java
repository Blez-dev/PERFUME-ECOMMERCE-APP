package org.blezant.perfumeecommerceapp.cart.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class GetCartResponseDto {
    private String cartId;
    private BigDecimal subTotal;
    private BigDecimal deliveryFee;
    private BigDecimal total;
    List<CartItemData> cartItemData= new ArrayList<>();

}
