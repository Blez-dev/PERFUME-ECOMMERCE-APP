package org.blezant.perfumeecommerceapp.product.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteReviewRequestDto {
    private String commentId;
}
