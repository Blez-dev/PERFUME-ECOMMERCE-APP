package org.blezant.perfumeecommerceapp.product.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsersComment {
    String username;
     BigInteger stars;
    String comment;
    Instant timeOfComment;
    private String commentId;
}
