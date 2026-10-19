package org.blezant.perfumeecommerceapp.admin.models;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadPerfumeRequestDto {

    @Min(0)
    Long quantity;
    @NotBlank(message = "Category field can't be blank")
    String category;

    @DecimalMin(value = "100.00",message = "Price must be greater than 100 naira")
    BigDecimal price;
    @NotBlank(message = "Name field can't be blank")
    String name;
    @NotBlank
    String description;
}
