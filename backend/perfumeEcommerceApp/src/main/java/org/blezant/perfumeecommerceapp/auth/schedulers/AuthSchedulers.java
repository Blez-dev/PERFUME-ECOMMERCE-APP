package org.blezant.perfumeecommerceapp.auth.schedulers;



import org.blezant.perfumeecommerceapp.auth.repositories.RefreshTokenRepository;
import org.blezant.perfumeecommerceapp.auth.repositories.VerificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class AuthSchedulers {

    public AuthSchedulers(RefreshTokenRepository refreshTokenRepository, VerificationRepository verificationRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
      this.verificationRepository=verificationRepository;
    }

    private final VerificationRepository verificationRepository;
    private  final RefreshTokenRepository refreshTokenRepository;


    @Scheduled(initialDelay = 10000,fixedDelay = 86400000)
    public void deleteVerificationRecords(){
        Long timeInMills=System.currentTimeMillis();
        verificationRepository.deleteToken(timeInMills);
    }



    public void deleteRefreshTokenRecords(){
        BigInteger timeInMills= BigInteger.valueOf(System.currentTimeMillis());
        refreshTokenRepository.deleteToken(timeInMills);
    }
}
