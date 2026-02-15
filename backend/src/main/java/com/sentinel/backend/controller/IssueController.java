package com.sentinel.backend.controller;
import org.springframework.ai.chat.model.ChatModel;
import com.sentinel.backend.model.Issue;
import com.sentinel.backend.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "http://localhost:5173") // Allow React (Frontend)
public class IssueController {



    @Autowired
    private IssueService issueService;

    @Autowired
    private ChatModel chatModel; // <-- This brings Gemini into your Controller!
    // POST: Create a new bug report
    @PostMapping
    public Issue createIssue(@RequestBody Issue issue) {
        return issueService.createIssue(issue);
    }

    // GET: List all bugs
    @GetMapping
    public List<Issue> getAllIssues() {
        return issueService.getAllIssues();
    }
    //
    @GetMapping("/ai-status")
    public ResponseEntity<String> checkAiStatus() {
        try {
            // This calls Gemini using the ChatModel we just injected
            String response = chatModel.call("Say exactly: 'Sentinel AI is Online'");
            return ResponseEntity.ok("Success: " + response);
        } catch (Exception e) {
            // If the key is missing or wrong, this will catch the error
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Error: API Key not found or invalid. " + e.getMessage());
        }
    }
    @PostMapping("/analyze-error")
    public ResponseEntity<String> analyzeError(@RequestBody String errorMessage) {
        try {
            // We give Gemini a specific persona and the error to fix
            String prompt = "You are a Senior Java Developer. Explain this error simply and tell me how to fix it: " + errorMessage;

            String aiResponse = chatModel.call(prompt);
            return ResponseEntity.ok(aiResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Sentinel AI failed to analyze the error: " + e.getMessage());
        }
    }
    // GET: Get details of one bug
    @GetMapping("/{id}")
    public Issue getIssueById(@PathVariable Long id) {
        return issueService.getIssueById(id);
    }
}