package com.fairshare.controllers;

import com.fairshare.models.User;
import com.fairshare.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.saveUser(user);
    }


    @GetMapping
    public ResponseEntity<User> getUserByEmail(@RequestParam String email){
        return userService.findByEmail(email)
                .map(ResponseEntity :: ok)
                .orElse(ResponseEntity.notFound()
                        .build());
    }








}
