package org.blezant.perfumeecommerceapp.product.repositories;

import org.blezant.perfumeecommerceapp.product.entities.Perfumes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfumes,String> {
}
