package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // JpaRepository provides basic CRUD operations:
    // save(), findById(), findAll(), deleteById(), etc.
    

    // List<User> findByCreatedAtAfter(LocalDateTime date);
}
