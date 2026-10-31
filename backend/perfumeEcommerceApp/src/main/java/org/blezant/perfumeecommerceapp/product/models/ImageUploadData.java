package org.blezant.perfumeecommerceapp.product.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
