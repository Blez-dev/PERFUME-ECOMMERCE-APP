package org.blezant.perfumeecommerceapp.auth.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "auth_verification")
public class VerificationTokenEntity {
    

    @Column(name = "verification_token")
    @Id
    String verificationToken;
    @Column(name = "email")
    String email;
    @Column(name = "otp_hash")
    String otpHash;
    @Column(name = "expiration")
    Long expiration;
    @Column(name = "verified",nullable = false)
    Boolean verified;
    @Column(name = "attempts")
    Integer attempts;
}
