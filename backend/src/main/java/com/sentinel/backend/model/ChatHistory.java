package com.sentinel.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_history")
public class ChatHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1. THE NEURAL LINK: Tying this message to a specific User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String userMessage;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String aiResponse;

    private LocalDateTime timestamp;

    // Standard empty constructor required by Spring JPA
    public ChatHistory() {}

    // 2. THE NEW CONSTRUCTOR: Notice it takes a 'User' now, not a 'String clientIp'
    public ChatHistory(User user, String userMessage, String aiResponse) {
        this.user = user;
        this.userMessage = userMessage;
        this.aiResponse = aiResponse;
        this.timestamp = LocalDateTime.now();
    }

    public ChatHistory(com.sentinel.backend.entity.User currentUser, String command, String aiResponse) {
    }

    // --- GETTERS & SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getUserMessage() { return userMessage; }
    public void setUserMessage(String userMessage) { this.userMessage = userMessage; }

    public String getAiResponse() { return aiResponse; }
    public void setAiResponse(String aiResponse) { this.aiResponse = aiResponse; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}