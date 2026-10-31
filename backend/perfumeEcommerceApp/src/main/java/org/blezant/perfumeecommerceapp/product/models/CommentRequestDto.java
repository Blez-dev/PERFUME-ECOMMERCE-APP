package org.blezant.perfumeecommerceapp.product.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@AllArgsConstructor
@Data
public class CommentRequestDto {
    private String perfumeId;
    private String comment;
    private BigInteger star;
}
