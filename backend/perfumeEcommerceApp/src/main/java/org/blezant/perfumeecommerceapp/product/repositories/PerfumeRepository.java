package org.blezant.perfumeecommerceapp.product.repositories;

import org.blezant.perfumeecommerceapp.product.entities.Perfumes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfumes,String> {

    @Query(value = "select  * from perfumes where category=?2 limit ?1",nativeQuery = true)
    List<Perfumes> fetchByPageSizeAndCategory(int limit, String category);



    @Modifying
    @Transactional
    @Query(value = "UPDATE perfumes set quantities=quantities-?1 where pid =?2  ",nativeQuery = true)
    void updateQuantityRecord(Long deductedQuantity,String pid);

}
