package org.blezant.perfumeecommerceapp.aws;


import org.blezant.perfumeecommerceapp.aws.service.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AwsController {

    public AwsController(AwsService awsService) {
        this.awsService = awsService;
    }

    AwsService awsService;


    @PostMapping("/upload")
    public  String uploadImage(@RequestParam("image")MultipartFile file){
        return  awsService.uploadImage(file);
    }
}
