package org.blezant.perfumeecommerceapp.order.entities;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blezant.perfumeecommerceapp.auth.entities.RegisterEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
public class Orders {
    @OneToOne
    @JoinColumn(name = "user_id")
    @ManyToOne
    private RegisterEntity registerEntity;
    private String address;
    private BigDecimal totalPrice;
    private Instant timeCreated;
    private String status;
    private String authorizationUrl;
    private String reference;
    @OneToMany
            @JoinColumn(name = "order_items")
    List<OrderItem> orderItems=new ArrayList<>();
    @Id
    private String orderId;
}
