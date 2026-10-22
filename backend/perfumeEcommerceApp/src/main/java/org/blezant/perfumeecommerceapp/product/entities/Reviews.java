package org.blezant.perfumeecommerceapp.product.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews")
public class Reviews {
    @Id
    String userId;
    BigInteger starReview;
    String commentReview;
    Instant timeOfComment;
    @ManyToOne
    @JoinColumn(name = "perfumes_pid")
    Perfumes perfumes;
}
