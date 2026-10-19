package org.blezant.perfumeecommerceapp.aws.service;


import org.blezant.perfumeecommerceapp.auth.exceptions.CustomBadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class AwsService {


    private S3Client s3Client;

    public AwsService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Value("${cloud.aws.bucket-name}")
    private  String bucketName;



    public  void uploadImageData(MultipartFile imageUploadData,String imageKey){
        //upload image
        PutObjectRequest putObjectRequest= PutObjectRequest.builder()
                .bucket(bucketName)
                .contentType(imageUploadData.getContentType())
                .key(imageKey)
                .build();

        try{
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(imageUploadData.getInputStream(),imageUploadData.getSize()));
        } catch (IOException e) {
            throw new CustomBadRequestException("");
        }
    }



    public String uploadImage(MultipartFile file) {

        //check that file is an image
        boolean isImage= file.getContentType()!=null&&file.getContentType().startsWith("image/");
        if(!isImage){
            throw  new CustomBadRequestException("File must be an image");
        }
        //create object key
        String fileName= file.getOriginalFilename();
        String imageKey=UUID.randomUUID()+fileName;

        //create requestObject
        PutObjectRequest requestObject= PutObjectRequest.builder()
                .bucket(bucketName)
                .key(imageKey)
                .contentType(file.getContentType())
                .build();

       try{
           //upload to  S3
           s3Client.putObject(requestObject, RequestBody.fromInputStream(file.getInputStream(),file.getSize()));

       }catch (IOException e){
           throw  new CustomBadRequestException("Bad file content");
       }
        return  imageKey;
    }
}
