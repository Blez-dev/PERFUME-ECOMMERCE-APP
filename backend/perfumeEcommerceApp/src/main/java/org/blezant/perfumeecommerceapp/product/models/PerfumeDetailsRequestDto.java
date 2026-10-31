package org.blezant.perfumeecommerceapp.product.models;


import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PerfumeDetailsRequestDto {
    @NotBlank(message = "Perfume Id field can't be blank")
    @NotNull(message = "Can't be null")
    String perfumeId;
    @Min(1)
    @Max(5)
    int reviewsStar;
}
