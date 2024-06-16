package com.fairshare.config;

import com.fairshare.services.UserDetailsServiceImpl;
import com.fairshare.services.UserService;
import com.fairshare.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    @Autowired
    private UserDetailsServiceImpl userDetailsService;

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

//  TODO : THIS IS TAKING USERNAME AS AN ARGUMENT BUT, LOADBYUSERNAME METHOD INTERNALLY IS CALLING FINDBYEMAIL, NEED TO CHANGE ITS INTERNAL CALLING

//        Checks if the username is not null and the security context is not already authenticated.
        if(extractedUserName != null && SecurityContextHolder.getContext().getAuthentication()== null){
            UserDetails userDetails = this.userDetailsService.loadUserByUserName(extractedUserName); // getting user details

//after getting userDetails validated the token
            if(jwtUtil.validateToken(signedJwt, userDetails.getUsername())){
                // if token is valid then -> Create a UsernamePasswordAuthenticationToken with the user details and authorities
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Setting additional details for the authentication token
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

//  Setting above authentication token in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }


        }

//        if not authenticated, then authenticate
        filterChain.doFilter(request, response);


    }
}
