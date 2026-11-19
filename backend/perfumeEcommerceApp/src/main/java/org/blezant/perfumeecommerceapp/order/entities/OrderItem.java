package org.blezant.perfumeecommerceapp.order.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blezant.perfumeecommerceapp.product.entities.Perfumes;

import java.math.BigInteger;

@NoArgsConstructor
@Data
@Entity
public class OrderItem {
    @ManyToOne
    @JoinColumn(name = "perfume_id")
    Perfumes perfumes;
    private String orderId;
    private BigInteger units;
    @Id
    private String orderItemId;
}
