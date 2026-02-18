package com.sentinel.backend.repository;

import com.sentinel.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    // Believe it or not, this empty file gives you automatic access to:
    // .save(), .findAll(), .findById(), and .delete() without writing any code!
}