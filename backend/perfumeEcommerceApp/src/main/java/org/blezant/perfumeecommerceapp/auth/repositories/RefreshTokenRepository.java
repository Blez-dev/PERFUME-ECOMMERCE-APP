package org.blezant.perfumeecommerceapp.auth.repositories;

import org.blezant.perfumeecommerceapp.auth.entities.RefreshTokenEntity;
import org.blezant.perfumeecommerceapp.auth.entities.RegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity,String> {

    @Transactional
    @Modifying
    @Query(value = "update refresh_token_entity set access_token=?1 where email=?2",nativeQuery = true)
    void updateAccessToken(String accessToken,String email);
}
