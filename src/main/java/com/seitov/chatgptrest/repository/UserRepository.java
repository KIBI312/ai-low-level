package com.seitov.chatgptrest.repository;

import com.seitov.chatgptrest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}