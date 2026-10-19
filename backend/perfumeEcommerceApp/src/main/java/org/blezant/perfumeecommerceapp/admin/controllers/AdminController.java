package org.blezant.perfumeecommerceapp.admin.controllers;


import jakarta.validation.Valid;
import org.blezant.perfumeecommerceapp.admin.models.UploadPerfumeRequestDto;
import org.blezant.perfumeecommerceapp.admin.models.UploadPerfumeResponseDto;
import org.blezant.perfumeecommerceapp.admin.services.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AdminController {
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    private  final AdminService adminService;


    //admin upload a perfume product
    @PostMapping(value = "/perfumes",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadPerfumeResponseDto> uploadPerfume(@Valid @RequestPart("request") UploadPerfumeRequestDto requestData, @RequestPart ("image")MultipartFile imageData){
        UploadPerfumeResponseDto responseData= adminService.uploadProduct(requestData,imageData);
        return  ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }
}
