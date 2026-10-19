package org.blezant.perfumeecommerceapp.product.repositories;

import org.blezant.perfumeecommerceapp.product.entities.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReviewsRepository extends JpaRepository<Reviews,String> {
}
