package com.sentinel.backend.repository;

import com.sentinel.backend.model.Conversation;
import com.sentinel.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    // This custom command tells MySQL: "Give me all chat sessions for this specific Agent, newest first."
    List<Conversation> findByUserOrderByCreatedAtDesc(User user);
}