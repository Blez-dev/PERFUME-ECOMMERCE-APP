package org.blezant.perfumeecommerceapp.product.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.blezant.perfumeecommerceapp.product.models.DeletePerfumeRequest;
import org.blezant.perfumeecommerceapp.product.models.DeletePerfumeResponse;
import org.blezant.perfumeecommerceapp.product.models.UploadPerfumeRequestDto;
import org.blezant.perfumeecommerceapp.product.models.UploadPerfumeResponseDto;
import org.blezant.perfumeecommerceapp.product.services.AdminService;
import org.blezant.perfumeecommerceapp.product.models.*;
import org.blezant.perfumeecommerceapp.product.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;
    private  final AdminService adminService;

    public ProductController(ProductService productService, AdminService adminService) {
        this.productService = productService;
        this.adminService = adminService;
    }

    //User comment

    @GetMapping("/perfumes/details")
    public ResponseEntity<PerfumeDetailsResponseDto> getPerfumeDetails(@Valid @RequestBody PerfumeDetailsRequestDto requestData
            , @RequestParam("page") int page
            , @RequestParam("size") int size
            , HttpServletRequest httpServletRequest
    ) {
        PerfumeDetailsResponseDto responseData = productService.getPerfumeDetails(requestData, page, size, httpServletRequest);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }


    @PostMapping("/perfumes/review")
    public  ResponseEntity<CommentResponseDto> perfumeReview (@Valid @RequestBody CommentRequestDto requestData, HttpServletRequest httpServletRequest){
        CommentResponseDto commentResponseDto= productService.saveUserReview(requestData,httpServletRequest);
        return  ResponseEntity.status(HttpStatus.OK).body(commentResponseDto);

    }

    @DeleteMapping("/perfumes/review/delete")
    public ResponseEntity<DeleteReviewResponseDto> deleteReview(@Valid @RequestBody DeleteReviewRequestDto deleteReviewRequestDto,HttpServletRequest httpServletRequest){
     DeleteReviewResponseDto deleteReviewResponseDto= productService.deleteReview(httpServletRequest,deleteReviewRequestDto);
        return  ResponseEntity.status(HttpStatus.OK).body(deleteReviewResponseDto);
    }


    @DeleteMapping("/perfumes/delete")
    public  ResponseEntity<DeletePerfumeResponse> deletePerfume(@Valid @RequestBody DeletePerfumeRequest requestData, HttpServletRequest httpServletRequest){
        DeletePerfumeResponse responseData= adminService.deletePerfume(requestData,httpServletRequest);
        return  ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
    //admin upload a perfume product
    @PostMapping(value = "/perfumes",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadPerfumeResponseDto> uploadPerfume(@Valid @RequestPart("request") UploadPerfumeRequestDto requestData, @RequestPart ("image") MultipartFile imageData, HttpServletRequest  httpServletRequest){
        UploadPerfumeResponseDto responseData= adminService.uploadProduct(requestData,imageData,httpServletRequest);
        return  ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    @GetMapping("/perfumes")
    public  ResponseEntity<List<GetPerfumeResponseDto>> getPerfumesByCategory(@Valid @RequestParam(name = "category") String category, @RequestParam(name = "page") int page, HttpServletRequest httpServletRequest){
        List<GetPerfumeResponseDto> responseObject= productService.getPerfumeObjects(category,page,httpServletRequest);
        return  ResponseEntity.status(HttpStatus.OK).body(responseObject);
    }
}
