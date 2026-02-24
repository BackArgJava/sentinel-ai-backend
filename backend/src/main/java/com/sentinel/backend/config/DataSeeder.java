package com.sentinel.backend.config;

import com.sentinel.backend.model.User;
import com.sentinel.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Look for a brand new user that definitely isn't in the database yet
        if (userRepository.findByUsername("SantiAdmin").isEmpty()) {
            User admin = new User();
            admin.setUsername("SantiAdmin"); // NEW USERNAME
            admin.setPassword(passwordEncoder.encode("1234"));

            userRepository.save(admin);
            System.out.println("[!] SYSTEM OVERRIDE: Agent 'SantiAdmin' injected into the Matrix.");
        }
    }
}