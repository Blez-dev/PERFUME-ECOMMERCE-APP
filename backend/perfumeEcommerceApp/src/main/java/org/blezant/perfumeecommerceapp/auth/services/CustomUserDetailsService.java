package org.blezant.perfumeecommerceapp.auth.services;

import org.blezant.perfumeecommerceapp.auth.entities.RegisterEntity;
import org.blezant.perfumeecommerceapp.auth.exceptions.CustomBadRequestException;
import org.blezant.perfumeecommerceapp.auth.models.UserPrincipal;
import org.blezant.perfumeecommerceapp.auth.repositories.AuthRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    public  CustomUserDetailsService(AuthRepository authRepository){
        this.authRepository=authRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<RegisterEntity> userData = authRepository.findByEmail(username);

        if(userData.isEmpty()){
            throw  new UsernameNotFoundException("Bad credentials");
        }
        return new UserPrincipal(userData.get());
    }
}
