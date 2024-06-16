package com.fairshare.services;

import com.fairshare.models.User;
import com.fairshare.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUserName(String email) throws UsernameNotFoundException{

        User user= userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("No User found with email : "+email));

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), new ArrayList<>());

    }
}
