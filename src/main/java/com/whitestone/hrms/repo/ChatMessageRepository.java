package com.whitestone.hrms.repo;

import com.whitestone.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    
    // Find messages by user ID
    List<ChatMessage> findByUserId(String userId);
    
    // Find messages by user ID with limit
    @Query(value = "SELECT * FROM chat_messages WHERE user_id = :userId ORDER BY timestamp DESC LIMIT :limit", 
           nativeQuery = true)
    List<ChatMessage> findRecentByUserId(@Param("userId") String userId, @Param("limit") int limit);
    
    // Find messages by employee ID
    List<ChatMessage> findByEmployeeIdOrderByTimestampDesc(String employeeId);
    
    // Find messages by session ID
    List<ChatMessage> findBySessionIdOrderByTimestampAsc(String sessionId);
    
    // Find messages requiring human intervention
    List<ChatMessage> findByRequiresHumanTrueOrderByTimestampDesc();
    
    // Find messages by intent type
    List<ChatMessage> findByIntentTypeOrderByTimestampDesc(String intentType);
    
    // Find messages within date range
    List<ChatMessage> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime start, LocalDateTime end);
    
    // Get conversation count by user
    @Query("SELECT COUNT(c) FROM ChatMessage c WHERE c.userId = :userId")
    long countByUserId(@Param("userId") String userId);
    
    // Get average feedback rating by intent
    @Query("SELECT AVG(c.feedbackRating) FROM ChatMessage c WHERE c.intentType = :intentType")
    Double getAverageRatingByIntent(@Param("intentType") String intentType);
    
    // Find unresolved queries (no response or requires human)
    @Query("SELECT c FROM ChatMessage c WHERE c.assistantResponse IS NULL OR c.requiresHuman = true")
    List<ChatMessage> findUnresolvedQueries();
    
    // Search messages by content
    @Query("SELECT c FROM ChatMessage c WHERE LOWER(c.userMessage) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(c.assistantResponse) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ChatMessage> searchByKeyword(@Param("keyword") String keyword);
    
    // Get most common intents
    @Query("SELECT c.intentType, COUNT(c) FROM ChatMessage c GROUP BY c.intentType ORDER BY COUNT(c) DESC")
    List<Object[]> getIntentStatistics();
    
    // Delete old messages
    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMessage c WHERE c.timestamp < :cutoffDate")
    int deleteOldMessages(@Param("cutoffDate") LocalDateTime cutoffDate);
    
    // Get count of messages requiring human
    @Query("SELECT COUNT(c) FROM ChatMessage c WHERE c.requiresHuman = true")
    long countByRequiresHumanTrue();
}