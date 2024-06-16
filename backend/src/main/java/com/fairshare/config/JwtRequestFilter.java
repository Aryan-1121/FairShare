package com.fairshare.config;

import com.fairshare.services.UserService;
import com.fairshare.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;





    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //1st need to get the Authorization header from the request
        final String authorizationHeader = request.getHeader("Authorization");

        String signedJwt = null;
        String extractedUserName= null;
//        if the Authorization header is present & starts with "Bearer" then extract the jwt token

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            signedJwt = authorizationHeader.substring(7);       //"Bearer " -> length = 7 after this main token starts

//      decoding the token will give us username and expiration date/time
            extractedUserName = jwtUtil.extractUserName(signedJwt);
        }


        // If the username is extracted and no authentication is set in the context, proceed to authenticate



    }
}
