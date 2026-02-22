package com.sentinel.backend.controller;

import com.sentinel.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users") // This means all routes here start with /api/users
public class UserController {

    @Autowired
    private UserRepository userRepository; // This brings in your magic database tool!

    // 1. CREATE: This listens for a POST request to save a new user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user); // Magic! Saves to MySQL instantly.
    }

    // 2. READ: This listens for a GET request to show all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll(); // Magic! Grabs everyone from MySQL.
    }
}