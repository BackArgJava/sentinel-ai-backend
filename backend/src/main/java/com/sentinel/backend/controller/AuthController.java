package com.sentinel.backend.controller;

import com.sentinel.backend.model.User;
import com.sentinel.backend.repository.UserRepository;
import com.sentinel.backend.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- ADDED
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder; // <-- ADDED

    // Updated constructor to include the PasswordEncoder
    public AuthController(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public record LoginRequest(String username, String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        if (request.username() == null || request.password() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password required."));
        }

        Optional<User> userOptional = userRepository.findByUsername(request.username());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials."));
        }

        User dbUser = userOptional.get();

        // 👇 THE FIX: We use matches() instead of equals() 👇
        // It securely compares the raw "1234" against the encrypted database string
        if (passwordEncoder.matches(request.password(), dbUser.getPassword())) {
            String token = jwtUtil.generateToken(dbUser.getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials."));
    }
}