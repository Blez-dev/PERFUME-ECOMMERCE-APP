package org.blezant.perfumeecommerceapp.product.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Reviews {
    String userId;
    int rating;
    String comment;
    Instant timeOfComment;
    @Id
    String pid;
    String username;
    @ManyToOne
    @JoinColumn(name = "perfumes_pid")
    Perfumes perfumes;
}
