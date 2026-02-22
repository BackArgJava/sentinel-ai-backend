package com.sentinel.backend.controller;

import com.sentinel.backend.model.ChatHistory;
import com.sentinel.backend.model.User;
import com.sentinel.backend.repository.ChatHistoryRepository;
import com.sentinel.backend.repository.UserRepository;
import com.sentinel.backend.service.RateLimiterService;
import io.github.bucket4j.Bucket;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/commands")
@CrossOrigin(origins = "http://localhost:5173")
public class CommandController {

    private final ChatClient chatClient;
    private final RateLimiterService rateLimiterService;
    private final ChatHistoryRepository chatHistoryRepository;
    private final UserRepository userRepository;

    public CommandController(ChatClient.Builder chatClientBuilder,
                             RateLimiterService rateLimiterService,
                             ChatHistoryRepository chatHistoryRepository,
                             UserRepository userRepository) {
        this.rateLimiterService = rateLimiterService;
        this.chatHistoryRepository = chatHistoryRepository;
        this.userRepository = userRepository;
        this.chatClient = chatClientBuilder.build(); // We build per-request now
    }

    // 1. Create a DTO for safe input validation (Point 4)
    public record CommandRequest(String command) {}

    @PostMapping("/execute")
    public ResponseEntity<Map<String, String>> executeCommand(@RequestBody CommandRequest requestBody, HttpServletRequest request) {

        // RATE LIMITING (Point 2: Simplified for now, but keeping the shield up)
        String clientIp = request.getRemoteAddr();
        Bucket bucket = rateLimiterService.resolveBucket(clientIp);
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("error", "ERROR: Mainframe cooling down. Try again in 60s."));
        }

        // VALIDATION (Point 4)
        if (requestBody.command() == null || requestBody.command().length() > 1000) {
            return ResponseEntity.badRequest().body(Map.of("error", "ERROR: Invalid command length."));
        }

        // USER IDENTIFICATION (Fixed: lowercase 'u' and correct User model)
        User currentUser = userRepository.findByUsername("Santi").orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername("Santi");
            newUser.setRole("ROLE_AGENT");
            return userRepository.save(newUser); // Lowercase 'u' here too!
        });

        // REVERSE CONTEXT ORDER (Point 6: Oldest -> Newest)
        List<ChatHistory> pastMessages = chatHistoryRepository.findTop5ByUserOrderByTimestampDesc(currentUser);
        Collections.reverse(pastMessages); // Flip it!

        String contextMemory = pastMessages.stream()
                .map(chat -> "Agent: " + chat.getUserMessage() + "\nSentinel: " + chat.getAiResponse())
                .reduce("", (a, b) -> a + "\n\n" + b);

        try {
            // PROMPT HARDENING (Point 3: One single system call)
            String aiResponse = chatClient.prompt()
                    .system("You are Sentinel AI. \nContext:\n" + contextMemory)
                    .user(requestBody.command())
                    .call()
                    .content();

            // PERSISTENCE (Point 9)
            chatHistoryRepository.save(new ChatHistory(currentUser, requestBody.command(), aiResponse));

            // Standardized JSON response (Point 10)
            return ResponseEntity.ok(Map.of("response", aiResponse != null ? aiResponse : ""));

        } catch (Exception e) {
            // LOG PRIVACY (Point 5: Never show e.getMessage() to the user!)
            return ResponseEntity.status(500).body(Map.of("error", "ERROR: Internal neural link failure."));
        }
    }
}