package com.fairshare.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtUtil {

    private String jwtSecret;



    public String generateToken(String userName){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

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
        return extractClaim(token, Claims::getSubject);
    }

//    similarly we will decode and get the expiration time/date
    public Date extractExpirationDateTime(String token ){
        return extractClaim(token, Claims::getExpiration);
    }


//    now we also need to make a function which check if the token is expired or not
    private Boolean isTokenExpired(String token ){
        return extractExpirationDateTime(token).before(new Date());
    }




    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String token){
         return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

    }






    public Boolean validateToken(String token, String userName){
        final String extractedUserName = extractUserName(token);
        return (extractedUserName.equals(userName) && !isTokenExpired(token));
    }






}
