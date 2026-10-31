package org.blezant.perfumeecommerceapp.product.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "review")
public class Reviews {
    String userId;
    @Id
    String reviewId;
    BigInteger starReview;
    String commentReview;
    Instant timeOfComment;
    @ManyToOne
    @JoinColumn(name = "perfumes_pid")
    Perfumes perfumes;
}
