package com.fairshare.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private String jwtSecret;


//this  method will take payload (subject) and will sigh it using the jwtSecret and will set an expiration time of the generated token(string)
//    subject is the payload(userName) which is going to be signed by the jwtSecret
    public String createToken (Map<String, Object> claims, String subject){
        return Jwts.builder().setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }



//    this will decode the token and extract whatever the payload was (username in this case)
    public String extractUserName(String token ){

    }







}
