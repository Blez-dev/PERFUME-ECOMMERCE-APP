package org.blezant.perfumeecommerceapp.admin.services;


import org.apache.tika.Tika;
import org.blezant.perfumeecommerceapp.admin.models.ImageUploadData;
import org.blezant.perfumeecommerceapp.admin.models.UploadPerfumeRequestDto;
import org.blezant.perfumeecommerceapp.admin.models.UploadPerfumeResponseDto;
import org.blezant.perfumeecommerceapp.auth.entities.RegisterEntity;
import org.blezant.perfumeecommerceapp.auth.exceptions.CustomBadRequestException;
import org.blezant.perfumeecommerceapp.auth.repositories.AuthRepository;
import org.blezant.perfumeecommerceapp.aws.service.AwsService;
import org.blezant.perfumeecommerceapp.product.entities.Perfumes;
import org.blezant.perfumeecommerceapp.product.repositories.PerfumeRepository;
import org.blezant.perfumeecommerceapp.rabbitMQ.producers.RabbitProducer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdminService {

    private final Tika tika;
    private final AuthRepository authRepository;
    private  final RabbitProducer rabbitProducer;
    private  final PerfumeRepository perfumeRepository;
    private  final AwsService awsService;

    public AdminService(Tika tika, AuthRepository authRepository, RabbitProducer rabbitProducer, PerfumeRepository perfumeRepository, AwsService awsService) {
        this.tika = tika;
        this.authRepository = authRepository;
        this.rabbitProducer = rabbitProducer;
        this.perfumeRepository = perfumeRepository;
        this.awsService = awsService;
    }

    public UploadPerfumeResponseDto uploadProduct(UploadPerfumeRequestDto requestData, MultipartFile imageData) {

        //validate request to be from an admin
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();

        System.out.println(authentication.isAuthenticated());
        RegisterEntity userData= authRepository.findByEmail("mtom04718@gmail.com").get();
        if(userData.getRole().equals("USER")){
            throw new CustomBadRequestException("Forbidden Action");
        }
        //validate image type
        if(imageData.isEmpty()){
            throw new CustomBadRequestException("Image is required");
        }
      try{
          String contentType= tika.detect(imageData.getInputStream());
          if(!(contentType.equals("image/png")||contentType.equals("image/jpeg"))){
              throw new CustomBadRequestException("Only png and jpeg image supported");
          }
      }catch (IOException e){
          throw new CustomBadRequestException("Error reading image");
      }


        //generate image key
        String imageKey= UUID.randomUUID()+imageData.getOriginalFilename();
      //build image upload data

      String perfumeId=UUID.randomUUID().toString().substring(0,13);
      String message="  Perfume prouduct ("+perfumeId+" )has been successfully uploaded";
      String contentType= imageData.getContentType();
      String exchangeName="image.exchange";
      String routingKey="image.upload";
      String toMail= userData.getEmail();


      //upload image data
      awsService.uploadImageData(imageData,imageKey);
 //send image message to rabbit mq
        ImageUploadData imageUploadData=new ImageUploadData();
        imageUploadData.setPid(perfumeId);
        imageUploadData.setMessage(message);
        imageUploadData.setExchangeName(exchangeName);
        imageUploadData.setRoutingKey(routingKey);
        imageUploadData.setToMail(toMail);
        rabbitProducer.uploadImage(imageUploadData);

        //save image data
        Perfumes perfumes = new Perfumes();
        perfumes.setCategory(requestData.getCategory());
        perfumes.setName(requestData.getName());
        perfumes.setDescription(requestData.getDescription());
        perfumes.setPrice(requestData.getPrice());
        perfumes.setPid(perfumeId);
        perfumes.setQuantities(requestData.getQuantity());
        perfumes.setImageKey(imageKey);
        perfumeRepository.save(perfumes);

        //build and return response back to client
        UploadPerfumeResponseDto responseData= new UploadPerfumeResponseDto();
        responseData.setMessage("Perfume uploaded successfully");
        responseData.setStatus(true);
        responseData.setPid(perfumeId);
        return  responseData;
    }
}
