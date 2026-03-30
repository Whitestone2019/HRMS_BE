package com.whitestone.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;
    
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Column(name = "employee_id")
    private String employeeId;
    
    @Column(name = "user_message", length = 2000)
    private String userMessage;
    
    @Column(name = "assistant_response", length = 4000)
    private String assistantResponse;
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @Column(name = "message_type")
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    
    @Column(name = "session_id")
    private String sessionId;
    
    @Column(name = "requires_human")
    private boolean requiresHuman;
    
    @Column(name = "intent_type")
    private String intentType;
    
    @Column(name = "confidence_score")
    private Double confidenceScore;
    
    @Column(name = "action_taken")
    private String actionTaken;
    
    @Column(name = "action_result", length = 2000)
    private String actionResult;
    
    @Column(name = "feedback_rating")
    private Integer feedbackRating;
    
    @Column(name = "feedback_comment", length = 1000)
    private String feedbackComment;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    
    @Column(name = "Created_By")
    private String createdBy;
    
    @Column(name = "Created_Date")
    private LocalDateTime CreatedDate;
    
    
    @Column(name = "Modified_By")
    private String ModifiedBy;
    
    @Column(name = "Modified_Date")
    private LocalDateTime ModifiedDate;
    
    @Column(name = "Record_Status")
    private String RecordStatus;
    
    public enum MessageType {
        QUERY, RESPONSE, ACTION, ERROR, SYSTEM
    }
    
    // Constructors
    public ChatMessage() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }
    
    // Constructor for user query
    public ChatMessage(String userId, String userMessage, String employeeId) {
        this();
        this.userId = userId;
        this.userMessage = userMessage;
        this.employeeId = employeeId;
        this.messageType = MessageType.QUERY;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public String getAssistantResponse() {
		return assistantResponse;
	}

	public void setAssistantResponse(String assistantResponse) {
		this.assistantResponse = assistantResponse;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getModifiedBy() {
		return ModifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		ModifiedBy = modifiedBy;
	}

	public LocalDateTime getModifiedDate() {
		return ModifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		ModifiedDate = modifiedDate;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public boolean isRequiresHuman() {
		return requiresHuman;
	}

	public void setRequiresHuman(boolean requiresHuman) {
		this.requiresHuman = requiresHuman;
	}

	public String getIntentType() {
		return intentType;
	}

	public void setIntentType(String intentType) {
		this.intentType = intentType;
	}

	public Double getConfidenceScore() {
		return confidenceScore;
	}

	public void setConfidenceScore(Double confidenceScore) {
		this.confidenceScore = confidenceScore;
	}

	public String getActionTaken() {
		return actionTaken;
	}

	public void setActionTaken(String actionTaken) {
		this.actionTaken = actionTaken;
	}

	public String getActionResult() {
		return actionResult;
	}

	public void setActionResult(String actionResult) {
		this.actionResult = actionResult;
	}

	public Integer getFeedbackRating() {
		return feedbackRating;
	}

	public void setFeedbackRating(Integer feedbackRating) {
		this.feedbackRating = feedbackRating;
	}

	public String getFeedbackComment() {
		return feedbackComment;
	}

	public void setFeedbackComment(String feedbackComment) {
		this.feedbackComment = feedbackComment;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	

	public LocalDateTime getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		CreatedDate = createdDate;
	}

	public String getRecordStatus() {
		return RecordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		RecordStatus = recordStatus;
	}
    
    // Getters and Setters
   
}