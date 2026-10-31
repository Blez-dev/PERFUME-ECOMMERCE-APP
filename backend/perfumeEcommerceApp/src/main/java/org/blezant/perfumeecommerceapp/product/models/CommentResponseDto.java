package org.blezant.perfumeecommerceapp.product.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CommentResponseDto {
    private boolean status;
    private String message;
}
