package org.blezant.perfumeecommerceapp.product.models;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class GetPerfumeResponseDto {



    private String perfumeName;
    private String perfumeId;
    private BigDecimal perfumePrice;
    private String imageUrl;
}
