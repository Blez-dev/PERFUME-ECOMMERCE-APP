package org.blezant.perfumeecommerceapp.product.repositories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.blezant.perfumeecommerceapp.product.entities.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;


@Repository
public interface ReviewsRepository extends JpaRepository<Reviews,String> {



    @Query(value = "select COALESCE(AVG(star_review),0) from  review where perfumes_pid=?1  ",nativeQuery = true)
    BigDecimal getStarsAvg(String perfumeId);



    @Query(value = "SELECT COUNT(star_review) FROM review WHERE perfumes_pid = ?1", nativeQuery = true)
    int getStarsCount( String perfumeId);



    List<Reviews> findByPerfumes_Pid( String perfumeId);
}
