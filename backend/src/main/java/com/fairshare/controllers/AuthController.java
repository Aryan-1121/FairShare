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



    @PostMapping("login")
    public ResponseEntity<?> generateAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword())
            );

        }catch (Exception e){
            throw new Exception("Incorrect username or password, try again !! ", e);
        }

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