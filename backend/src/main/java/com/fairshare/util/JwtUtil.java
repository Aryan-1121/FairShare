package com.fairshare.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtUtil {

    @Value("$jwt.secret")           // getting it from application properties file
    private String jwtSecret;



    public String generateToken(String userName){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

//this  method will take payload (subject) and will sigh it using the jwtSecret and will set an expiration time of the generated token(string)
//    subject is the payload(userName) which is going to be signed by the jwtSecret
    private String createToken (Map<String, Object> claims, String subject){
        return Jwts.builder()       // Starts building the JWT token
                .setClaims(claims)      // Setting the claims
                .setSubject(subject)        // Setting the subject (username)
                .setIssuedAt(new Date(System.currentTimeMillis()))          // Setting the issued date to the current time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))      // Setting the expiration date/time (10 hours from currentTime)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)          // Signs the token with HS256 algorithm and the secret key
                .compact();     // Builds and serializes the token to a compact, URL-safe string
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
//         return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        //for spring boot 3 ->

        return Jwts.parserBuilder()     // for parsing JWTs
                .setSigningKey(jwtSecret)           // Sets the signing key for verifying the token
                .build()            //Builds the parser
                .parseClaimsJws(token)          //Parses the JWT token
                .getBody();             //Gets the claims from the parsed token

    }





    // Validates the JWT token by checking the username and expiration date
    public Boolean validateToken(String token, String userName){
        final String extractedUserName = extractUserName(token);
        return (extractedUserName.equals(userName) && !isTokenExpired(token));
    }






}
