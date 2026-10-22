package org.blezant.perfumeecommerceapp.admin.models;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeletePerfumeRequest {

    @NotBlank(message = "Field can't be blank")
            @NotNull(message = "field can't be null")
    String perfumeId;

}
