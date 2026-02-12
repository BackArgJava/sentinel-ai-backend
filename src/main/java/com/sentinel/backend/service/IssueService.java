package com.sentinel.backend.service;

import com.sentinel.backend.model.Issue;
import com.sentinel.backend.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    // 1. Save a new Issue (Report a bug)
    public Issue createIssue(Issue issue) {
        // Later, we will ask AI here! 🤖
        return issueRepository.save(issue);
    }

    // 2. Get all Issues (For the Dashboard)
    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    // 3. Get one Issue (For details)
    public Issue getIssueById(Long id) {
        return issueRepository.findById(id).orElse(null);
    }
}