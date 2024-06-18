package com.fairshare.config;

import com.fairshare.services.UserDetailsServiceImpl;
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


/*
                        Summary of Internal Working

SecurityContext: Holds the security information (authentication) of the currently logged-in user.
OncePerRequestFilter: Ensures the filter is executed only once per request.
JWT Token: A compact, URL-safe means of representing claims to be transferred between two parties.


            Authentication Process:
Extract token from header.
Decode token to get the username.
Load user details using the username.
Validate the token.
Create an authentication token if the JWT is valid.
Set the authentication token in the security context.
This filter helps ensure that each request is processed securely by validating the user's identity via a JWT token before proceeding with the request.


 */

@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;


//    This method processes every incoming HTTP request. It checks if the request has a valid JWT token and, if so, sets up the user's authentication in the security context.
//  this method will be called for every HTTP request that comes in
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //1st - need to get the Authorization header from the HTTP request
        final String authorizationHeader = request.getHeader("Authorization");

        String signedJwt = null;
        String extractedUserName= null;

//        if the Authorization header is present & starts with "Bearer" then extract the jwt token
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
//            removing the "Bearer" prefix to the actual token (signed jwt)
            signedJwt = authorizationHeader.substring(7);       //"Bearer " -> length = 7 after this main token starts

//      decoding the token will give us username and expiration date/time
            extractedUserName = jwtUtil.extractUserName(signedJwt);
        }


        // If the username is extracted and no authentication is set in the context, proceed to authenticate

//  TODO : THIS IS TAKING USERNAME AS AN ARGUMENT BUT, LOADBYUSERNAME METHOD INTERNALLY IS CALLING FINDBYEMAIL, NEED TO CHANGE ITS INTERNAL CALLING

//        Checks if the username is present and, the User is not already authenticated.
        if(extractedUserName != null && SecurityContextHolder.getContext().getAuthentication()== null){
            UserDetails userDetails = this.userDetailsService.loadUserByUserName(extractedUserName); // getting user details

//      Validates the token using jwtUtil.validateToken(). This checks if the token is not expired and the username matches.
//after getting userDetails validating the token
            if(jwtUtil.validateToken(signedJwt, userDetails.getUsername())){
                // if token is valid then -> Create a UsernamePasswordAuthenticationToken with the user details and authorities
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

//Sets additional details, like the user's IP address and session ID, into the authentication token.
                // Setting additional details for the authentication token
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

//  Setting above authentication token in the SecurityContext
//         Placing the authentication token in the security context, which means the user is now authenticated for this request.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }


        }
        // Continue with the filter chain (process the next filter or the request itself if no more filters)
//  Passes the request and response to the next filter in the chain, or processes the request if no more filters are defined.
        filterChain.doFilter(request, response);


    }
}
