package org.blezant.perfumeecommerceapp.admin.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadPerfumeResponseDto {
    String pid;
    String message;
    boolean status;
}
