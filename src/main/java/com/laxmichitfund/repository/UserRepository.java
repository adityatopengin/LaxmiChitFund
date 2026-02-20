package com.laxmichitfund.repository;

import com.laxmichitfund.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring Boot automatically translates this into: 
    // SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);
    
    // Useful for login validation
    Optional<User> findByEmail(String email);
}

