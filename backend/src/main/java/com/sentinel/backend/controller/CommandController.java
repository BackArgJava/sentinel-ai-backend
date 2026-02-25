package com.sentinel.backend.controller;

import com.sentinel.backend.model.ChatHistory;
import com.sentinel.backend.model.Conversation;
import com.sentinel.backend.model.User;
import com.sentinel.backend.repository.ChatHistoryRepository;
import com.sentinel.backend.repository.ConversationRepository;
import com.sentinel.backend.repository.UserRepository;
import com.sentinel.backend.service.RateLimiterService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/commands")
@CrossOrigin(origins = "http://localhost:5173")
public class CommandController {

    private final ChatClient chatClient;
    private final RateLimiterService rateLimiterService;
    private final ChatHistoryRepository chatHistoryRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    public CommandController(ChatClient.Builder chatClientBuilder,
                             RateLimiterService rateLimiterService,
                             ChatHistoryRepository chatHistoryRepository,
                             ConversationRepository conversationRepository,
                             UserRepository userRepository) {
        this.rateLimiterService = rateLimiterService;
        this.chatHistoryRepository = chatHistoryRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.chatClient = chatClientBuilder.build();
    }

    // Accepts an optional conversationId so we know where to save the message
    public record CommandRequest(String command, Long conversationId) {}

    // ==========================================
    // 1. MAIN EXECUTION ENDPOINT
    // ==========================================
    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> executeCommand(@RequestBody CommandRequest requestBody,
                                                              HttpServletRequest request) {

        // Rate limit per IP
        String clientIp = request.getRemoteAddr();
        Bucket bucket = rateLimiterService.resolveBucket(clientIp);
        if (!bucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("error", "ERROR: Mainframe cooling down. Try again in 60s."));
        }

        // Validate body
        if (requestBody == null || requestBody.command() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "ERROR: Missing command."));
        }
        String command = requestBody.command().trim();
        if (command.isEmpty() || command.length() > 1000) {
            return ResponseEntity.badRequest().body(Map.of("error", "ERROR: Invalid command length."));
        }

        // Auth guard (prevents anonymousUser crashes / null auth NPE)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "ERROR: Missing or invalid authentication token."));
        }

        // Load current user
        String authenticatedUsername = authentication.getName();
        User currentUser = userRepository.findByUsername(authenticatedUsername)
                .orElseThrow(() -> new RuntimeException("CRITICAL ERROR: Authenticated Agent not found."));

        // Conversation logic: find existing session, or create a new one
        Conversation activeConversation;
        if (requestBody.conversationId() != null) {
            activeConversation = conversationRepository.findById(requestBody.conversationId())
                    .orElseThrow(() -> new RuntimeException("Conversation not found"));

            // Security: prevent access to someone else's conversation
            if (activeConversation.getUser() == null
                    || activeConversation.getUser().getId() == null
                    || !activeConversation.getUser().getId().equals(currentUser.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Access Denied."));
            }
        } else {
            // Generate a title based on first chars of the command
            String title = command.length() > 25 ? command.substring(0, 25) + "..." : command;
            activeConversation = conversationRepository.save(new Conversation(currentUser, title));
        }

        // Fetch context for this specific conversation (oldest -> newest)
        List<ChatHistory> pastMessages = chatHistoryRepository.findByConversationOrderByTimestampAsc(activeConversation);

        String contextMemory = pastMessages.stream()
                .map(chat -> "Agent: " + safe(chat.getUserMessage()) + "\nSentinel: " + safe(chat.getAiResponse()))
                .collect(Collectors.joining("\n\n"));

        try {
            // Role fallback (null/blank-safe)
            String agentRole = (currentUser.getRole() == null || currentUser.getRole().isBlank())
                    ? "CLASS-1_AGENT"
                    : currentUser.getRole();

            // Dynamic system prompt
            String systemInstructions = String.format(
                    "You are Sentinel AI, an advanced mainframe assistant. " +
                            "Interfacing with Agent '%s', clearance level: '%s'. " +
                            "Maintain a professional, analytical, and slightly sci-fi tone. " +
                            "Acknowledge the user's clearance level if asked. " +
                            "Memory:\n%s",
                    currentUser.getUsername(),
                    agentRole,
                    contextMemory
            );

            String aiResponse = chatClient.prompt()
                    .system(systemInstructions)
                    .user(command)
                    .call()
                    .content();

            // Save message linked to the specific conversation
            chatHistoryRepository.save(new ChatHistory(currentUser, activeConversation, command, aiResponse));

            // Return response + conversationId
            Map<String, Object> payload = new HashMap<>();
            payload.put("response", aiResponse != null ? aiResponse : "");
            payload.put("conversationId", activeConversation.getId());
            return ResponseEntity.ok(payload);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "ERROR: Internal neural link failure."));
        }
    }

    // ==========================================
    // 2. FETCH ALL CONVERSATIONS (Sidebar)
    // ==========================================
    @GetMapping("/conversations")
    public ResponseEntity<List<Map<String, Object>>> getConversations() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username).orElseThrow();

        List<Conversation> convos = conversationRepository.findByUserOrderByCreatedAtDesc(currentUser);

        // IMPORTANT: Build Map<String, Object> explicitly to avoid generic inference issues
        List<Map<String, Object>> response = convos.stream()
                .map(c -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", c.getId());
                    m.put("title", c.getTitle());
                    return m;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ==========================================
    // 3. FETCH CHAT HISTORY (When clicking a Sidebar tab)
    // ==========================================
    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<List<Map<String, String>>> getMessages(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username).orElseThrow();

        Conversation conversation = conversationRepository.findById(id).orElseThrow();

        if (conversation.getUser() == null
                || conversation.getUser().getId() == null
                || !conversation.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<ChatHistory> history = chatHistoryRepository.findByConversationOrderByTimestampAsc(conversation);

        List<Map<String, String>> response = history.stream()
                .flatMap(h -> java.util.stream.Stream.of(
                        Map.of("sender", "user", "text", safe(h.getUserMessage())),
                        Map.of("sender", "system", "text", safe(h.getAiResponse()))
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}