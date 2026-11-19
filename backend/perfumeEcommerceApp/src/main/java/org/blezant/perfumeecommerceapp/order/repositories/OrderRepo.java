package org.blezant.perfumeecommerceapp.order.repositories;


import org.blezant.perfumeecommerceapp.order.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Orders,String> {


    @Transactional
    @Modifying
    @Query(value = "update orders set reference=?2,authorization_url=?1 where order_id=?3",nativeQuery = true)
    void updateRecord(String authorizationUrl, String reference, String orderId);


    @Transactional
    @Modifying
    @Query(value = "update orders set status=?1 where order_id=?2",nativeQuery = true)
    void updatePaymentStatus(String paid, String orderId);



    Optional<Orders> findByReference(String referenceNumber);
}
