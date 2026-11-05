package org.blezant.perfumeecommerceapp.cart.models;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PerfumeAddResponseDto {
    private boolean status;
    private String message;
    private String perfumeId;
}
