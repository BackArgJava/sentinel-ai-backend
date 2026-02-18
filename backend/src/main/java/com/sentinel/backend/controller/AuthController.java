package com.sentinel.backend.controller;

import com.sentinel.backend.entity.User;
import com.sentinel.backend.repository.UserRepository;
import com.sentinel.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth") // The URL for the Ticket Booth
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {

        // 1. Look for the user in the database
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        // 2. If the user exists and the password matches...
        if (userOptional.isPresent() && userOptional.get().getPassword().equals(loginRequest.getPassword())) {

            // 3. Print the VIP wristband!
            String token = jwtUtil.generateToken(loginRequest.getUsername());

            // 4. Give the token to the user
            return ResponseEntity.ok(Map.of("token", token));
        }

        // 5. If wrong password, kick them out
        return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
    }
}