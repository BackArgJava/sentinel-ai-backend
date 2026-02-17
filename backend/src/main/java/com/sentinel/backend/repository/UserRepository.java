package com.sentinel.backend.repository;

import com.sentinel.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Believe it or not, this empty file gives you automatic access to:
    // .save(), .findAll(), .findById(), and .delete() without writing any code!
}