package org.blezant.perfumeecommerceapp.admin.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeletePerfumeResponse {
    boolean status;
    String message;
}
