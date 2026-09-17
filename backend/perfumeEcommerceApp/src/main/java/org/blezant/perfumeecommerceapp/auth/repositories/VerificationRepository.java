package org.blezant.perfumeecommerceapp.auth.repositories;

import jakarta.validation.constraints.NotBlank;
import org.blezant.perfumeecommerceapp.auth.entities.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<VerificationTokenEntity,String> {




    @Modifying
    @Transactional
    @Query(value = "update auth_verification set attempts=?1 where verification_token=?2",nativeQuery = true)
    void incrementAttempt( Long newAttemptValue,String verificationToken);



    @Query(value = "select * from auth_verification where verification_token=?1",nativeQuery = true)
    Optional<VerificationTokenEntity> findByVerificationToken( String verificationToken);


    @Modifying
    @Transactional
    @Query(value = "update auth_verification set verified=?1 where verification_token=?2",nativeQuery = true)
    void updateVerificationStatus(boolean status,String verrificationToken);
}
