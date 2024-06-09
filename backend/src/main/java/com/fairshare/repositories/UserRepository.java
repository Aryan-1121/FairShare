package com.fairshare.repositories;

import com.fairshare.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    added custom method to search via email
    Optional<User> findByEmail(String email);

    Optional<User> findByUserName(String userName);

}
