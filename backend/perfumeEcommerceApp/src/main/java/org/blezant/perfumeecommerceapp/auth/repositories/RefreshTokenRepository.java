package org.blezant.perfumeecommerceapp.auth.repositories;

import org.blezant.perfumeecommerceapp.jwt.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,String> {


    @Transactional

    void deleteByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "DELETE from refresh_token_entity where valid_until<?1",nativeQuery = true)
    void deleteToken(BigInteger timeInMills);

    @Transactional
    @Modifying
    @Query(value = "update refresh_token_entity set access_token=?1 where email=?2",nativeQuery = true)
    void updateAccessToken(String accessToken,String email);
}
