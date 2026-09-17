package org.blezant.perfumeecommerceapp.auth.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "register")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterEntity {

    @Column(name = "userId")
    @Id
    String userId;
    @Column(name = "email")
    String email;
    @Column(name = "username")
    String username;
    @Column(name = "password")
    String password;
    @Column(name = "created_at")
    Instant createdAt;
    @Column(name = "role")
    String role;
    @Column(name = "accLocked")
    boolean accLocked;
}
