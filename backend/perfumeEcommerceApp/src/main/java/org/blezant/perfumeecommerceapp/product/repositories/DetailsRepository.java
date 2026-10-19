package org.blezant.perfumeecommerceapp.product.repositories;


import org.blezant.perfumeecommerceapp.product.entities.Details;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailsRepository extends JpaRepository<Details,String> {
}
