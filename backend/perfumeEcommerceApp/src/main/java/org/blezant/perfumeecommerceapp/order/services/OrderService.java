package org.blezant.perfumeecommerceapp.order.services;


import org.blezant.perfumeecommerceapp.order.entities.Orders;
import org.blezant.perfumeecommerceapp.order.repositories.OrderRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrderService {


    private  final OrderRepo orderRepo;

    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    public Optional<Orders> getOrderById(String orderId) {
        return  orderRepo.findById(orderId);
    }

    public void updateRecord(String authorizationUrl, String reference, String orderId) {
        orderRepo.updateRecord(authorizationUrl,reference,orderId);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updatePaymentStatus(String paid, String orderId) {
        orderRepo.updatePaymentStatus(paid,orderId);
    }
}
