package com.sentinel.backend.service;

import com.sentinel.backend.model.Issue;
import com.sentinel.backend.repository.IssueRepository;
import org.springframework.ai.chat.client.ChatClient; // 👈 OJO: El import cambió ligeramente en 1.1.0
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    private final ChatClient chatClient;

    // Inyectamos el cerebro de la IA 🧠
    public IssueService(ChatClient.Builder chatClientBuilder, IssueRepository issueRepository) {
        this.chatClient = chatClientBuilder.build();
        this.issueRepository = issueRepository;
    }

    public Issue createIssue(Issue issue) {
        // 1. Preguntar a Gemini antes de guardar
        try {
            String prompt = "Soy un Junior Developer. Explícame este error brevemente y dame una solución: "
                    + issue.getTitle() + " - " + issue.getDescription();

            String aiSolution = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            // 2. Guardar la sabiduría de la IA en la descripción
            issue.setDescription(issue.getDescription() + "\n\n🦁 **Sentinel AI Solution:**\n" + aiSolution);

        } catch (Exception e) {
            System.out.println("Error al conectar con Gemini: " + e.getMessage());
        }

        return issueRepository.save(issue);
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    public Issue getIssueById(Long id) {
        return issueRepository.findById(id).orElse(null);
    }
}