package org.blezant.perfumeecommerceapp.auth.configurations;

import org.blezant.perfumeecommerceapp.auth.exceptions.CustomAuthenticationEntryPoint;
import org.blezant.perfumeecommerceapp.auth.services.CustomUserDetailsService;
import org.blezant.perfumeecommerceapp.auth.services.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtFilter jwtFilter;


    public  SecurityConfiguration(CustomUserDetailsService customUserDetailsService,CustomAuthenticationEntryPoint customAuthenticationEntryPoint,JwtFilter jwtFilter){
        this.customUserDetailsService=customUserDetailsService;
        this.customAuthenticationEntryPoint=customAuthenticationEntryPoint;
        this.jwtFilter =jwtFilter;
    }
    @Bean
    AuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(10));
        return  daoAuthenticationProvider;
    }

    @Bean
    BCryptPasswordEncoder encoder(){
      return new BCryptPasswordEncoder(10);
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

        return httpSecurity.csrf(csrf -> csrf.disable())
                .authenticationProvider(daoAuthenticationProvider())
                .exceptionHandling(exception->exception.authenticationEntryPoint(customAuthenticationEntryPoint))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/register", "/auth/register/verify", "/auth/login","/refresh/token","/auth/forget-password/verify","/auth/forget-password").permitAll().anyRequest().authenticated())
                .build();

    }
}
