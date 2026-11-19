package org.blezant.perfumeecommerceapp.order.repositories;


import org.blezant.perfumeecommerceapp.order.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepo  extends JpaRepository<OrderItem,String> {
}
