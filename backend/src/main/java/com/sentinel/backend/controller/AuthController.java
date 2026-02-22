package com.sentinel.backend.controller;

import com.sentinel.backend.model.User;
import com.sentinel.backend.repository.UserRepository;
import com.sentinel.backend.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
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

        // NOTE: Plain-text comparison is NOT secure. Replace with BCryptPasswordEncoder later.
        if (request.password().equals(dbUser.getPassword())) {
            String token = jwtUtil.generateToken(dbUser.getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials."));
    }
}