package org.blezant.perfumeecommerceapp.auth.services;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.blezant.perfumeecommerceapp.auth.exceptions.CustomAuthenticationEntryPoint;
import org.blezant.perfumeecommerceapp.auth.repositories.AuthRepository;
import org.blezant.perfumeecommerceapp.jwt.services.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthRepository authRepository;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public JwtFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService, AuthRepository authRepository, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.authRepository = authRepository;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authJwtToken = request.getHeader("Authorization");

        if (authJwtToken != null && authJwtToken.startsWith("Bearer ")) {
            //extract jwt token
            String jwtToken = authJwtToken.substring(7);
            //validate Token
            try {
                jwtService.validateToken(jwtToken);
            } catch (ExpiredJwtException e) {
                customAuthenticationEntryPoint.commence(request, response, new BadCredentialsException("JWT EXPIRED"));
                return;
            }catch (MalformedJwtException e){
                customAuthenticationEntryPoint.commence(request, response, new BadCredentialsException("MALFORMED JWT"));
                return;
            }catch (JwtException e){
                customAuthenticationEntryPoint.commence(request, response, new BadCredentialsException("INVALID JWT "));
                return;
            }


            //get userId from jwt
            String userId = jwtService.extractUserId(jwtToken);
            //extract username(email)
            String email = jwtService.getUserEmail(userId);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                //check if account is disabled
                boolean userAccStatus= isUserValid(userDetails);
                if(!userAccStatus){
                    customAuthenticationEntryPoint.commence(request, response, new BadCredentialsException("DISABLED ACCOUNT"));
                    return;
                }
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


            }
        }
        filterChain.doFilter(request, response);
    }


    boolean isUserValid(UserDetails userDetails){
        if(userDetails.isAccountNonExpired()&&userDetails.isAccountNonLocked()&&userDetails.isCredentialsNonExpired()&&userDetails.isEnabled()){
           return  true;
        }
        return false;
    }


}
