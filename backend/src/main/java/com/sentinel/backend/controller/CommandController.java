package com.sentinel.backend.controller; // Ensure this matches your package structure!

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/commands")
@CrossOrigin(origins = "http://localhost:5173")
public class CommandController {

    private final ChatClient chatClient;

    // We inject the ChatClient builder and give Sentinel its personality!
    public CommandController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("You are Sentinel AI, a highly advanced, secure mainframe system. " +
                        "Keep your answers concise, professional, and slightly futuristic or terminal-themed. " +
                        "Always address the user as 'Agent'.")
                .build();
    }

    @PostMapping("/execute")
    public ResponseEntity<String> executeCommand(@RequestBody Map<String, String> payload) {
        String userCommand = payload.get("command");

        // Failsafe in case of empty input
        if (userCommand == null || userCommand.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("ERROR: Blank command sequence detected.");
        }

        try {
            // 1. Send the Agent's command to the Gemini model
            // 2. Wait for the intelligent response
            String aiResponse = chatClient.prompt()
                    .user(userCommand)
                    .call()
                    .content();

            // 3. Send the AI's response back to the React terminal
            return ResponseEntity.ok(aiResponse);

        } catch (Exception e) {
            // If the connection to Google fails, show a secure error message
            return ResponseEntity.status(500).body("ERROR: Mainframe neural link severed. " + e.getMessage());
        }
    }
}