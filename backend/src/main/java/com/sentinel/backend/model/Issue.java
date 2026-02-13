package com.sentinel.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "issues")
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT") // Allows long text for logs
    private String errorLog;

    @Enumerated(EnumType.STRING)
    private Severity severity; // LOW, MEDIUM, HIGH, CRITICAL

    @Enumerated(EnumType.STRING)
    private Status status; // OPEN, RESOLVED

    @Column(columnDefinition = "TEXT") // Allows long AI explanations
    private String aiAnalysis;

    private LocalDateTime createdAt;

    // Automatically set the date when we create the error
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) status = Status.OPEN;
        if (severity == null) severity = Severity.MEDIUM;
    }

    public void setDescription(String s) {

    }

    public String getDescription() {
        return null;
    }

    // Enums to restrict values (Validation!)
    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum Status {
        OPEN, IN_PROGRESS, RESOLVED
    }
}