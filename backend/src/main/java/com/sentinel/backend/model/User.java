package com.sentinel.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;
    private String role;
    private int credits;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChatHistory> history;

    public User() {}

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public List<ChatHistory> getHistory() { return history; }
    public void setHistory(List<ChatHistory> history) { this.history = history; }
}