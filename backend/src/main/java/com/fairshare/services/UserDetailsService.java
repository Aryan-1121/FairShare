package com.fairshare.services;

import com.fairshare.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface UserDetailsService {
//    UserDetails class is provided by Spring core iteself

    public UserDetails loadUserByUserName(String email) throws UsernameNotFoundException;
}
