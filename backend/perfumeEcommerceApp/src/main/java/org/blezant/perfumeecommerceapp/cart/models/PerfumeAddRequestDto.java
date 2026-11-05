package org.blezant.perfumeecommerceapp.cart.models;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class PerfumeAddRequestDto {

    @NotBlank(message = "PerfumeId field can't be blank")
    private String perfumeId;
    @Min(value = 1,message = "minimum unit is 1")
    private BigInteger units;

}
