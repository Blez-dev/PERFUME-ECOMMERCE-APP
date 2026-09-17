package org.blezant.perfumeecommerceapp.auth.repositories;

import jakarta.validation.constraints.NotBlank;
import org.blezant.perfumeecommerceapp.auth.entities.RegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface AuthRepository extends JpaRepository<RegisterEntity,String> {

    Optional<RegisterEntity> findByEmail(String email);


    @Transactional
    @Modifying
    @Query(value = "update register set password=?1 where email=?2",nativeQuery = true)
    void updateNewPassword(String hashedPassword,  String email);


    //To create individual users account
}
