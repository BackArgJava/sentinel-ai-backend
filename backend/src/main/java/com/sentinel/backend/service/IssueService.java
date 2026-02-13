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

    // 👇 Eliminamos el ChatClient temporalmente para que compile
    // private final ChatClient chatClient;

    public Issue createIssue(Issue issue) {
        // Guardamos el bug directamente sin consultar a la IA por ahora
        return issueRepository.save(issue);
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    public Issue getIssueById(Long id) {
        return issueRepository.findById(id).orElse(null);
    }
}