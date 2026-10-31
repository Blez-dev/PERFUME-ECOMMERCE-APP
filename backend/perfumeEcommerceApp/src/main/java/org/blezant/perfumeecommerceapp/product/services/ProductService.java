package org.blezant.perfumeecommerceapp.product.services;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.blezant.perfumeecommerceapp.exceptions.CustomBadRequestException;
import org.blezant.perfumeecommerceapp.auth.repositories.AuthRepository;
import org.blezant.perfumeecommerceapp.security.jwt.services.JwtService;
import org.blezant.perfumeecommerceapp.product.entities.Perfumes;
import org.blezant.perfumeecommerceapp.product.entities.Reviews;
import org.blezant.perfumeecommerceapp.product.models.*;
import org.blezant.perfumeecommerceapp.product.repositories.PerfumeRepository;
import org.blezant.perfumeecommerceapp.product.repositories.ReviewsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private JwtService jwtService;

    private  final PerfumeRepository perfumeRepository;
    private final ReviewsRepository reviewsRepository;
    private final AuthRepository authRepository;

    public ProductService(JwtService jwtService, PerfumeRepository perfumeRepository, ReviewsRepository reviewsRepository, AuthRepository authRepository) {
        this.jwtService = jwtService;
        this.perfumeRepository = perfumeRepository;
        this.reviewsRepository = reviewsRepository;
        this.authRepository = authRepository;
    }


    @Transactional
    public PerfumeDetailsResponseDto getPerfumeDetails(PerfumeDetailsRequestDto requestData, int page, int size, HttpServletRequest httpServletRequest) {

        //Verify request came from user
        String jwtToken= httpServletRequest.getHeader("Authorization").substring(7);
        String userRole= jwtService.getUserRole(jwtToken);
        if(userRole.equals("ADMIN")){
            throw new CustomBadRequestException("Forbidden request");
        }

        //extract User Id
        String userId= jwtService.extractUserId(jwtToken);

        //check if product exists
        Optional<Perfumes> perfume= perfumeRepository.findById(requestData.getPerfumeId());
        if(perfume.isEmpty()){
            throw new CustomBadRequestException("Invalid perfume Id");
        }

        //validate page and size
        if(page<0){
            throw new CustomBadRequestException("Invalid page request");
        }
        if(size<10|| size>50){
            throw new CustomBadRequestException("Invalid size request");
        }

        //Construct image url
        String imageKey= perfume.get().getImageKey();
        String imageUrl=getImageUrl(imageKey);

        //fetch stars average
        BigDecimal starsAverage = reviewsRepository.getStarsAvg(requestData.getPerfumeId());

        //fetch total reviews
        int reviews= reviewsRepository.getStarsCount(requestData.getPerfumeId());


        //Get review objects
        List<Reviews> reviewsObj= reviewsRepository.findByPerfumes_Pid(requestData.getPerfumeId());
        int reviewsCount=reviewsObj.size();
        //comments object
        List<UsersComment> usersComments= new ArrayList<>();

        for(int i=0;i<reviewsCount;i++){
            Reviews reviewsTemp=reviewsObj.get(i);
            UsersComment usersComment= new UsersComment();
            usersComment.setComment(reviewsTemp.getCommentReview());
            //fetch username
            String username= authRepository.findById(reviewsTemp.getUserId()).get().getUsername();
            usersComment.setUsername(username);
            usersComment.setStars(reviewsTemp.getStarReview());
            usersComment.setTimeOfComment(reviewsTemp.getTimeOfComment());
            usersComment.setCommentId(reviewsTemp.getReviewId());
            usersComments.add(usersComment);
        }

        //Build response
        PerfumeDetailsResponseDto responseData = new PerfumeDetailsResponseDto();
        responseData.setImageUrl(imageUrl);
        responseData.setPerfumeName(perfume.get().getName());
        responseData.setPrice(perfume.get().getPrice());
        responseData.setCategory(perfume.get().getCategory());
        responseData.setDescription(perfume.get().getDescription());
        responseData.setStarsAvg(starsAverage);
        responseData.setStarsReviews(reviews+" "+"reviews");
        responseData.setUsersComment(usersComments);

        return  responseData;

    }

    public CommentResponseDto saveUserReview(@Valid CommentRequestDto requestData, HttpServletRequest httpServletRequest) {

        //Extract jwt token from HttpServletRequest
        String jwtToken=  httpServletRequest.getHeader("Authorization").substring(7);

        //verifyRole of user
        String role = jwtService.getUserRole(jwtToken);
        if(role.equals("ADMIN")){
            throw new CustomBadRequestException("Forbidden Request");
        }

        //Extract userId from jwtToken
        String userId=jwtService.extractUserId(jwtToken);

        //get perfume object
        Optional<Perfumes> perfumeObject= perfumeRepository.findById(requestData.getPerfumeId());
        if(perfumeObject.isEmpty()){
            throw new CustomBadRequestException("Invalid perfume Id");
        }

        //save review in the database
        Reviews reviews= new Reviews();
        reviews.setTimeOfComment(Instant.now());
        reviews.setUserId(userId);
        reviews.setCommentReview(requestData.getComment());
        reviews.setStarReview(requestData.getStar());
        reviews.setPerfumes(perfumeObject.get());
        reviews.setReviewId(UUID.randomUUID().toString());
        reviewsRepository.save(reviews);

        //Build response and send back to user
        CommentResponseDto commentResponseDto= new CommentResponseDto();
        commentResponseDto.setStatus(true);
        commentResponseDto.setMessage("Comment Saved Successfully");

        return commentResponseDto;

    }

    public DeleteReviewResponseDto deleteReview(HttpServletRequest httpServletRequest,  DeleteReviewRequestDto deleteReviewRequestDto) {
        //Extract user's jwt token
        String jwtToken =jwtService.extractJwtToken(httpServletRequest);
        //extract user's role
        String userRole=jwtService.getUserRole(jwtToken);

        //create response object
        DeleteReviewResponseDto deleteReviewResponseDto= new DeleteReviewResponseDto();
        if( userRole.equals("ADMIN")){
            //check if comment exists
            Optional<Reviews> reviews= reviewsRepository.findById(deleteReviewRequestDto.getCommentId());
            if(reviews.isEmpty()){
                throw new CustomBadRequestException("Invalid comment Id");
            }
            //delete review
            reviewsRepository.deleteById(reviews.get().getReviewId());
            //build response
            deleteReviewResponseDto.setCommentId(reviews.get().getReviewId());
            deleteReviewResponseDto.setStatus(true);
            deleteReviewResponseDto.setMessage("Comment successfully deleted");
        }else{
            //check if comment exists
            Optional<Reviews> review= reviewsRepository.findById(deleteReviewRequestDto.getCommentId());
            if(review.isEmpty()){
                throw new CustomBadRequestException("Invalid comment Id");
            }
            //check if comment is for user
            String userId= jwtService.extractUserId(jwtToken);
            if(!(userId.equals(review.get().getUserId()))){
                throw new CustomBadRequestException("Forbidden Request");
            }

            //delete review
            reviewsRepository.deleteById(review.get().getReviewId());
            //build response
            deleteReviewResponseDto.setCommentId(review.get().getReviewId());
            deleteReviewResponseDto.setStatus(true);
            deleteReviewResponseDto.setMessage("Comment successfully deleted");
        }

        return  deleteReviewResponseDto;
    }

    public List<GetPerfumeResponseDto> getPerfumeObjects( String category, int page, HttpServletRequest httpServletRequest) {
        //validate category
        String objectCategory= category.toLowerCase();
        if(!(objectCategory.equals("men")||objectCategory.equals("women")||objectCategory.equals("luxury vibes")||objectCategory.equals("everyday wear"))){
            throw new CustomBadRequestException("Invalid category");
        }

        //validate page size
        boolean pageIsValid= page>0&&page<10;

        //Get perfume objects based on size
        if(!pageIsValid){
            throw new CustomBadRequestException("page out of bound");
        }



        //Request for perfume objects
        int limit= page*10;
        List<Perfumes> perfumes= perfumeRepository.fetchByPageSizeAndCategory(limit,category);


        //Build response Object
        List<GetPerfumeResponseDto> responseData= new ArrayList<>();
        buildResponseDto(perfumes, responseData);
        return  responseData;


    }

    private void buildResponseDto(List<Perfumes> perfumes, List<GetPerfumeResponseDto> responseData) {
        for(int i = 0; i< perfumes.size(); i++){
            GetPerfumeResponseDto getPerfumeResponseDto= new GetPerfumeResponseDto();
            Perfumes perfumes1= perfumes.get(i);
            getPerfumeResponseDto.setPerfumePrice(perfumes1.getPrice());
            getPerfumeResponseDto.setPerfumeId(perfumes1.getPid());
            getPerfumeResponseDto.setPerfumePrice(perfumes1.getPrice());
            getPerfumeResponseDto.setPerfumeName(perfumes1.getName());
            //Build image url
            //Construct image url
            String imageKey= perfumes1.getImageKey();
            String imageUrl = getImageUrl(imageKey);
            getPerfumeResponseDto.setImageUrl(imageUrl);
            responseData.add(getPerfumeResponseDto);
        }
    }

    private  String getImageUrl(String imageKey) {
        if(imageKey.contains(" ")){
            imageKey= imageKey.replace(" ","+");
        }
        String domain= "https://blezant-perfume-images.s3.eu-central-1.amazonaws.com";
        String imageUrl= domain+"/"+imageKey;
        return imageUrl;
    }
}
