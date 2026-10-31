package org.blezant.perfumeecommerceapp.product.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DeleteReviewResponseDto {
    private  String commentId;
    boolean status ;
    private String message;
}
