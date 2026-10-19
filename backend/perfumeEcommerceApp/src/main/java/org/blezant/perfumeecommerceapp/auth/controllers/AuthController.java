package org.blezant.perfumeecommerceapp.auth.controllers;


import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.blezant.perfumeecommerceapp.auth.models.*;
import org.blezant.perfumeecommerceapp.auth.services.AuthService;
import org.blezant.perfumeecommerceapp.auth.services.VerificationTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;

@RestController
@RequestMapping("/auth")
public class AuthController {

   private final  AuthService authService;
   private final VerificationTokenService verificationTokenService;

   AuthController(AuthService authService,VerificationTokenService verificationTokenService){
       this.authService=authService;
       this.verificationTokenService=verificationTokenService;
   }


    //controllers to register
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> signUp(@Valid @RequestBody RegisterRequestDto registerRequestDto){
        RegisterResponseDTO signUpResponse= authService.RegisterUser(registerRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpResponse);
    }

    //controllers to verify email to register
    @PostMapping("/register/verify")
    public ResponseEntity<VerifyRegisterResponseDto> verifyEmailRegistration(@Valid @RequestBody VerifyRegisterRequestDto verifyRegisterRequestDto){
       VerifyRegisterResponseDto verifyRegisterResponseDto=verificationTokenService.requestOtp(verifyRegisterRequestDto);
       return  ResponseEntity.status(HttpStatus.OK).body(verifyRegisterResponseDto);
    }


    @PostMapping("/register/admin")
    public  ResponseEntity<AdminRegisterResponseDto> registerAdmin(@Valid @RequestBody AdminRegisterRequestDto requestData){
        AdminRegisterResponseDto responseData= authService.registerAdmin(requestData);
        return  ResponseEntity.status(HttpStatus.CREATED).body(responseData);

    }

    //controllers to login admin
    @PostMapping("/login/admin")
    public ResponseEntity<LoginResponseDto> loginAdmin(@Valid @RequestBody LoginRequestDto requestData){
       LoginResponseDto responseData= authService.loginAdmin(requestData);
       return  ResponseEntity.status(HttpStatus.OK).body(responseData);
    }


    //Controller to verify admin
    @PostMapping("/register/verify/admin")
    public ResponseEntity<VerifyRegisterAdminResponseDto> verifyAdminEmail(@Valid@RequestBody VerifyRegisterAdminRequestDto requestData){
       VerifyRegisterAdminResponseDto responseData= authService.verifyAdminAccount(requestData);
       return  ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    //controllers to login
    @PostMapping("/login")
    public ResponseEntity<?> login( @Valid @RequestBody LoginRequestDto loginData ){
       LoginResponseDto loginResponse= authService.login(loginData);
       return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    //controllers to verify email to forget password
    @PostMapping("/forget-password/verify")
    public ResponseEntity<ForgetPasswordVerifyResponseDto> verifyEmailForgetPassword(@Valid @RequestBody ForgetPasswordVerifyRequestDto requestData){
       ForgetPasswordVerifyResponseDto responseData= authService.verifyEmailForgetPassword(requestData);
       return  ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @GetMapping("/test")
    public String test(){
       return  "Done";
    }

    //controllers to forget password
    @PostMapping("/forget-password")
    public  ResponseEntity<ForgetPasswordResponseDto> forgetPassword(@Valid @RequestBody ForgetPasswordRequestDto requestData){
       ForgetPasswordResponseDto responseData= authService.forgetPassword(requestData);
       return  ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    //controllers to reset password
    @PostMapping("/reset/password")
    public ResponseEntity<ResetPasswordResponseDto> resetPassword(@Valid @RequestBody ResetPasswordRequestDto requestData){
       ResetPasswordResponseDto responseData=authService.resetPassword(requestData);
       return  ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    @PostMapping("/delete/account")
    public ResponseEntity<DeleteAccountResponseDto> deleteAcc(@Valid @RequestBody DeleteAccountRequestDto requestData){
       DeleteAccountResponseDto responseData= authService.deleteAccount(requestData);
       return  ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

}

