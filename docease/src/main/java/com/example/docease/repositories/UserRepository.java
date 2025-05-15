package com.example.docease.repositories;

import com.example.docease.entities.Role;
import com.example.docease.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Integer>{

    Optional<User> findByUsernameAndRole(String username, String role);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
