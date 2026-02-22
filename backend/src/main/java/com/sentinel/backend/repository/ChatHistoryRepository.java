package com.sentinel.backend.repository;

import com.sentinel.backend.model.ChatHistory;
import com.sentinel.backend.model.User; // Import User entity
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    // This fetches the last 5 messages for a SPECIFIC user
    List<ChatHistory> findTop5ByUserOrderByTimestampDesc(User user);
}