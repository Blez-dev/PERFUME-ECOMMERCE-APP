package org.blezant.perfumeecommerceapp.admin.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadData {
    String exchangeName;
    String routingKey;
    String toMail;
    String pid;
    String message;



}
