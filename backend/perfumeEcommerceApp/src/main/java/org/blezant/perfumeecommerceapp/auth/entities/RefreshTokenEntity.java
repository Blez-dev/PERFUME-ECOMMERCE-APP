package org.blezant.perfumeecommerceapp.auth.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "refresh_token_entity")
public class RefreshTokenEntity {
    @Column(name = "refreshToken")
    private String refreshToken;
    @Id
    @Column(name = "email")
    private  String email;
    @Column(name = "validUntil")
    BigInteger validUntil;
    @Column(name = "createdAt")
    BigInteger createdAt;
    @Column(name = "accessToken")
    String accessToken;
}
