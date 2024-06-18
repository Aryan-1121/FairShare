package com.fairshare.controllers;

import com.fairshare.models.User;
import com.fairshare.services.UserDetailsService;
import com.fairshare.services.UserService;
import com.fairshare.util.JwtUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/*
                        This class's purpose ->
Client Request:
A client (e.g., web or mobile app) sends a POST request to /login with a JSON payload containing a username and password.

Parsing the Request:
Spring converts the JSON payload into an AuthenticationRequest object, which holds the username and password.

Authenticating the User:
The method tries to authenticate the user by checking the provided username and password against stored data.
If authentication fails (e.g., wrong password), an error is thrown.

Loading User Details:
If authentication succeeds, the method fetches additional user details (like roles) from the database.

Generating a Token:
The method generates a JWT token using the username. This token includes information like the username and an expiration time.

Returning the Token:
The method sends back a response containing the JWT token. The client stores this token and includes it in the headers of subsequent requests to prove their identity.



            IN SHORT ->
AuthenticationManager: Authenticates users by comparing provided credentials with stored data.
UsernamePasswordAuthenticationToken: Holds the user's credentials for authentication.
UserDetailsService: Loads user details from the database.
JwtUtil: Generates and validates JWT tokens.
ResponseEntity: Wraps HTTP responses.
AuthenticationRequest: A custom class representing the login request payload.
AuthenticationResponse: A custom class representing the login response payload containing the JWT token.


This class will ensure that only users with valid credentials receives a JWT token, which they can use to authenticate subsequent requests.
 */



@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;




    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        userService.saveUser(user);
        return ResponseEntity.ok("User registered Successfully - \n"+user);
    }



    @PostMapping("/login")
    public ResponseEntity<?> generateAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try{
//            authenticationManager: A Spring Security component that performs authentication.
//authenticate: This method attempts to authenticate the user with the provided credentials.
            authenticationManager.authenticate(
//      This will create an UsernamePasswordAuthenticationToken object, which is a token containing the username and password.
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword())
//UsernamePasswordAuthenticationToken: A class that implements Authentication and contains the principal (username), credentials (password), and optionally the authorities (roles).
            );

        }catch (Exception e){
            throw new Exception("Incorrect username or password, try again !! ", e);
        }
//UserDetails: it is an interface provided by Spring Security that holds user information.
        final UserDetails userDetails = userDetailsService.loadUserByUserName(authenticationRequest.getUserName());

        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }


}


// User login will be via userName and password , not with email and password
// TODO: (email will used for password reset kind of things and will be added later on )

@Data
class AuthenticationRequest{
    private String userName;
    private String password;
}



class AuthenticationResponse {
    private final String jwtToken;

    public AuthenticationResponse(String jwtToken){
        this.jwtToken = jwtToken;
    }

    public String getJwtToken(){
        return jwtToken;
    }
}