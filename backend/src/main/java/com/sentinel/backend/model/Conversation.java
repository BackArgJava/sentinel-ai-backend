package com.sentinel.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This is the title that will appear on your Sidebar buttons!
    private String title;

    // Links this conversation to a specific Agent
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Empty constructor for Spring
    public Conversation() {}

    public Conversation(User user, String title) {
        this.user = user;
        this.title = title;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public User getUser() { return user; }
}