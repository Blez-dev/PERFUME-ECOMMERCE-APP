package org.blezant.perfumeecommerceapp.auth.services;

import jakarta.validation.Valid;
import org.blezant.perfumeecommerceapp.auth.entities.RefreshTokenEntity;
import org.blezant.perfumeecommerceapp.auth.entities.RegisterEntity;
import org.blezant.perfumeecommerceapp.auth.entities.VerificationTokenEntity;
import org.blezant.perfumeecommerceapp.auth.exceptions.CustomBadRequestException;
import org.blezant.perfumeecommerceapp.auth.models.*;
import org.blezant.perfumeecommerceapp.auth.repositories.AuthRepository;
import org.blezant.perfumeecommerceapp.auth.repositories.RefreshTokenRepository;
import org.blezant.perfumeecommerceapp.auth.repositories.VerificationRepository;
import org.blezant.perfumeecommerceapp.jwt.services.JwtService;
import org.blezant.perfumeecommerceapp.jwt.services.RefreshTokenService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;


@Service
public class AuthService {


    @Value("${spring.mail.username}")
    private String appMail;

    private final AuthRepository authRepository;
    private final VerificationTokenService verificationTokenService;
    private final VerificationRepository verificationRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    RefreshTokenService refreshTokenService;
    RefreshTokenRepository refreshTokenRepository;
    CustomUserDetailsService customUserDetailsService;
    JavaMailSender javaMailSender;


    AuthService(AuthRepository authRepository, VerificationTokenService verificationTokenService, VerificationRepository verificationRepository, AuthenticationManager authenticationManager, JwtService jwtService, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository, CustomUserDetailsService customUserDetailsService, JavaMailSender javaMailSender) {
        this.authRepository = authRepository;
        this.verificationTokenService = verificationTokenService;
        this.verificationRepository = verificationRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.javaMailSender = javaMailSender;
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto loginData) {

        Authentication authenticationToken = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getPassword()));
        //create variables
        String accessToken = null;
        String refreshToken = null;
        //fetch user data
        RegisterEntity registerEntity = authRepository.findByEmail(loginData.getEmail()).get();

        //check if user already has a token
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenService.fetchUser(loginData.getEmail());
        if (refreshTokenEntity.isPresent()) {
            accessToken = refreshTokenEntity.get().getAccessToken();
            refreshToken = refreshTokenEntity.get().getRefreshToken();
            //check token expiry
            boolean isExpired = jwtService.checkExpiry(accessToken);
            if (isExpired) {
                //generate new accessToken
                accessToken = jwtService.generateAccessToken(registerEntity);
                //update new access token
                refreshTokenRepository.updateAccessToken(accessToken, loginData.getEmail());
            }
            LoginResponseDto loginResponseDto = getLoginResponseDto(accessToken, refreshToken, registerEntity);
            return loginResponseDto;
        }
        //generate refresh token and access token


        accessToken = jwtService.generateAccessToken(registerEntity);
        refreshToken = jwtService.generateRefreshToken();
        //store refresh token in the database

        //save Token data
        saveTokenData(accessToken, registerEntity, refreshToken);
        //build login response
        LoginResponseDto loginResponseDto = getLoginResponseDto(accessToken, refreshToken, registerEntity);
        //return response
        return loginResponseDto;


    }

    private void saveTokenData(String accessToken, RegisterEntity registerEntity, String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setAccessToken(accessToken);
        refreshTokenEntity.setEmail(registerEntity.getEmail());
        //create big integer value
        BigInteger number1 = new BigInteger(String.valueOf(System.currentTimeMillis()));
        BigInteger number2 = new BigInteger("2592000000");
        refreshTokenEntity.setValidUntil(number1.add(number2));
        refreshTokenEntity.setCreatedAt(new BigInteger(String.valueOf(System.currentTimeMillis())));
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);
    }

    private LoginResponseDto getLoginResponseDto(String accessToken, String refreshToken, RegisterEntity registerEntity) {
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setAccessToken(accessToken);
        loginResponseDto.setRefreshToken(refreshToken);
        loginResponseDto.setStatus(true);
        loginResponseDto.setMessage("User successfully logged in");
        loginResponseDto.setEmail(registerEntity.getEmail());
        loginResponseDto.setUserId(registerEntity.getUserId());
        return loginResponseDto;
    }


    @Transactional
    public RegisterResponseDTO RegisterUser(RegisterRequestDto registerRequestDto) {

        //generate a user id
        String userId = UUID.randomUUID().toString().substring(0, 13);
        //fetch verificationToken data
        Optional<VerificationTokenEntity> veriToken = verificationRepository.findByVerificationToken(registerRequestDto.getVerificationToken());
        //verify verificationToken (check if the token is still valid and exists0)
        verifyToken(registerRequestDto, veriToken);
        //increment number of attempts
        verificationTokenService.incrementAttempts(veriToken);
        //verify Otp
        verifyOtp(registerRequestDto, veriToken);
        //set token to verified
        verificationRepository.updateVerificationStatus(true, registerRequestDto.getVerificationToken());
        //Hash Password
        String hashedPassword = hashPassword(registerRequestDto);
        //save user details in database
        createAccountInDatabase(registerRequestDto, hashedPassword, userId);
        //construct response and send
        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO();
        setUserResponse(registerResponseDTO, registerRequestDto, userId);


        return registerResponseDTO;
    }


    private void createAccountInDatabase(RegisterRequestDto registerRequestDto, String hashedPassword, String userId) {
        RegisterEntity registerEntity = new RegisterEntity();
        registerEntity.setUsername(registerRequestDto.getUsername());
        registerEntity.setRole("USER");
        registerEntity.setPassword(hashedPassword);
        registerEntity.setUserId(userId);
        registerEntity.setEmail(registerRequestDto.getEmail());
        registerEntity.setCreatedAt(Instant.now());
        registerEntity.setAccLocked(false);
        authRepository.save(registerEntity);
    }

    private static void setUserResponse(RegisterResponseDTO registerResponseDTO, RegisterRequestDto registerRequestDto, String userId) {
        registerResponseDTO.setEmail(registerRequestDto.getEmail());
        registerResponseDTO.setUsername(registerRequestDto.getUsername());
        registerResponseDTO.setStatus(true);
        registerResponseDTO.setMessage("Account created successfully");
        registerResponseDTO.setUserId(userId);
    }

    private static String hashPassword(RegisterRequestDto registerRequestDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        return encoder.encode(registerRequestDto.getPassword());
    }

    private static void verifyOtp(RegisterRequestDto registerRequestDto, Optional<VerificationTokenEntity> veriToken) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        boolean otpResult = encoder.matches(registerRequestDto.getEmailOtp(), veriToken.get().getOtpHash());
        if (!otpResult) {
            throw new CustomBadRequestException("Invalid otp code");
        }
    }

    private static void verifyToken(RegisterRequestDto registerRequestDto, Optional<VerificationTokenEntity> veriToken) {
        if (veriToken.isEmpty()) {
            throw new CustomBadRequestException(" Verification token does not exist");
        }

        if (!veriToken.get().getEmail().equals(registerRequestDto.getEmail())) {
            throw new CustomBadRequestException("Verification token error(email mismatch)");
        }
        if (veriToken.get().getVerified()) {
            throw new CustomBadRequestException("Verification token already verified");
        }

        if (veriToken.get().getExpiration().equals(System.currentTimeMillis()) || veriToken.get().getExpiration() < System.currentTimeMillis()) {
            throw new CustomBadRequestException("Verification token expired");
        }

        if (veriToken.get().getAttempts().equals(3)) {
            throw new CustomBadRequestException("Max attempts used");
        }
    }

    public ForgetPasswordVerifyResponseDto verifyEmailForgetPassword(ForgetPasswordVerifyRequestDto requestData) {
        //check email doesn't belong to sender
        System.out.println("1");
        checkEmail(requestData.getEmail());
        //load user vTokenEntity
        System.out.println("2");
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(requestData.getEmail());
        //verify acc status
        System.out.println("3");
        if (!(userDetails.isEnabled() && userDetails.isAccountNonLocked() && userDetails.isCredentialsNonExpired() && userDetails.isEnabled())) {
            throw new CustomBadRequestException("Account is disabled");
        }
        //Generate and send otp code to user
        String randomNumber = UUID.randomUUID().toString();
        Random random = new Random();
        String otpCode = Integer.toString(random.nextInt(900000) + 100000);
        //generate verification token
        String vToken = UUID.randomUUID().toString();
        //Hash and save otpcode
        hashAndSaveOtp(requestData, otpCode, vToken);
        //create simple mail message object and send to user's mail
        sendOtp(requestData, otpCode);
        //build response and send to user
        return responseData(requestData, vToken);
    }

    @Transactional
    private static ForgetPasswordVerifyResponseDto responseData(ForgetPasswordVerifyRequestDto requestData, String vToken) {
        ForgetPasswordVerifyResponseDto forgetPasswordVerifyResponseDto = new ForgetPasswordVerifyResponseDto();
        forgetPasswordVerifyResponseDto.setEmail(requestData.getEmail());
        forgetPasswordVerifyResponseDto.setVerificationToken(vToken);
        forgetPasswordVerifyResponseDto.setStatus(true);
        return forgetPasswordVerifyResponseDto;
    }

    private void hashAndSaveOtp(ForgetPasswordVerifyRequestDto requestData, String otpCode, String vToken) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String hashedOtp = encoder.encode(otpCode);
        VerificationTokenEntity vTokenEntity = new VerificationTokenEntity();
        vTokenEntity.setVerified(false);
        vTokenEntity.setAttempts(0);
        vTokenEntity.setOtpHash(hashedOtp);
        vTokenEntity.setEmail(requestData.getEmail());
        vTokenEntity.setVerificationToken(vToken);
        Long expirationTime = System.currentTimeMillis() + 900000;
        vTokenEntity.setExpiration(expirationTime);
        verificationRepository.save(vTokenEntity);
    }

    private void sendOtp(ForgetPasswordVerifyRequestDto requestData, String otpCode) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(requestData.getEmail());
        simpleMailMessage.setSentDate(Date.from(Instant.now()));
        simpleMailMessage.setSubject("Otp token to verify email");
        simpleMailMessage.setText("Email Verification\n\n" +
                "Hello,\n\n" +
                "Your email verification code is: " + otpCode + "\n\n" +
                "Please enter this code to verify your email address and reset your password.\n\n" +
                "This verification code will expire in 15 minutes. " +
                "For your security, please do not share this code with anyone.\n\n" +
                "If you did not request this verification code, you can safely ignore this email.\n\n" +
                "Thank you,\n" +
                "The Scentra Team");
        javaMailSender.send(simpleMailMessage);
    }


    private void checkEmail(String email) {
        if (email.equals(appMail)) {
            throw new CustomBadRequestException("Forbidden Mail");
        }


    }

    @Transactional
    public ForgetPasswordResponseDto forgetPassword(ForgetPasswordRequestDto requestData) {
        //verify email's not sender's
        if (requestData.getEmail().equals(appMail)) {
            throw new CustomBadRequestException("Email use is forbidden");
        }
        //verify user exists
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(requestData.getEmail());

        //verify user's acc status
        if (!(userDetails.isEnabled() && userDetails.isAccountNonExpired() && userDetails.isAccountNonLocked() && userDetails.isCredentialsNonExpired())) {
            throw new CustomBadRequestException("Acc disabled .Contact admin for help");
        }
        //check verification token in the database
        Optional<VerificationTokenEntity> vToken = verificationRepository.findByVerificationToken(requestData.getVerificationToken());
        if (vToken.isEmpty()) {
            throw new CustomBadRequestException("invalid verification Token");
        }
        //check otp code
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        boolean isVerified = encoder.matches(requestData.getOtpCode(), vToken.get().getOtpHash());
        if (!isVerified) {
            throw new CustomBadRequestException("Wrong otpCode");
        }
        //Hash new password
        String hashedPassword = encoder.encode(requestData.getNewPassword());
        //update new password
        authRepository.updateNewPassword(hashedPassword, requestData.getEmail());
        //return response to client
        ForgetPasswordResponseDto userResponse = new ForgetPasswordResponseDto(requestData.getEmail(), true, "Password successfully changed");
        return userResponse;

    }

    @Transactional
    public ResetPasswordResponseDto resetPassword(ResetPasswordRequestDto requestData) {
        //Extract all field values
        String email = requestData.getEmail();
        String newPassword = requestData.getNewPassword();
        String oldPassword = requestData.getOldPassword();
        //load user details
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        //check acc status
        if (!(userDetails.isEnabled() && userDetails.isAccountNonLocked() && userDetails.isCredentialsNonExpired() && userDetails.isAccountNonExpired())) {
            throw new CustomBadRequestException("Account is disabled.Contact admin for further assistance ");
        }
        //verify old password
        String databasePassword = authRepository.findByEmail(email).get().getPassword();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);
        boolean isTheSame = bCryptPasswordEncoder.matches(oldPassword, databasePassword);
        if (!isTheSame) {
            throw new CustomBadRequestException("incorrect old password");
        }
        //hash new password
        String hashedNewPassword = bCryptPasswordEncoder.encode(newPassword);
        //update new password
        authRepository.updateNewPassword(hashedNewPassword, email);
        //build response and return
        ResetPasswordResponseDto data = new ResetPasswordResponseDto();
        data.setEmail(email);
        data.setStatus(true);
        data.setMessage("Password changed successfully");
        return data;

    }
}
