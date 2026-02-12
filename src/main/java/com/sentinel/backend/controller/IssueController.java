package com.sentinel.backend.controller;

import com.sentinel.backend.model.Issue;
import com.sentinel.backend.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "http://localhost:5173") // Allow React (Frontend)
public class IssueController {

    @Autowired
    private IssueService issueService;

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

    // GET: Get details of one bug
    @GetMapping("/{id}")
    public Issue getIssueById(@PathVariable Long id) {
        return issueService.getIssueById(id);
    }
}