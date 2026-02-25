package com.sentinel.backend.repository;

import com.sentinel.backend.model.ChatHistory;
import com.sentinel.backend.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    // This command tells MySQL: "Give me all messages inside this specific chat folder, in chronological order."
    List<ChatHistory> findByConversationOrderByTimestampAsc(Conversation conversation);
}