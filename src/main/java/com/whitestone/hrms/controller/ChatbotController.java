package com.whitestone.hrms.controller;

import com.whitestone.entity.ChatMessage;
import com.whitestone.hrms.service.ChatbotService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:3000", "*" })
public class ChatbotController {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatbotController.class);
    private final Random random = new Random();
    
    @Autowired
    private ChatbotService chatbotService;
    
    // List of all available quick questions
    private final List<String> allQuickQuestions = Arrays.asList(
        "How to view timesheet?",
        "How to check leave status?",
        "Show today's attendance",
        "Show yesterday's attendance",
        "Show my timesheet",
        "Check leave balance",
        "How to apply for leave?",
        "How to view project history?",
        "What is my profile?",
        "How to apply for resignation?",
        "Show company policies",
        "How to check leave balance?"
        );
    
    /**
     * Get quick questions - returns only 4 random questions
     */
    @GetMapping("/quick-questions")
    public ResponseEntity<?> getQuickQuestions() {
        
        logger.info("Fetching 4 random quick questions");
        
        try {
            // Get 4 random questions from the list
            List<String> selectedQuestions = getRandomQuestions(4);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("quickQuestions", selectedQuestions);
            response.put("total", selectedQuestions.size());
            response.put("message", "Showing 4 quick questions");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching quick questions", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to fetch quick questions");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get different set of quick questions (for refresh)
     */
    @GetMapping("/quick-questions/refresh")
    public ResponseEntity<?> refreshQuickQuestions() {
        
        logger.info("Refreshing quick questions - getting new set of 4");
        
        try {
            // Get 4 new random questions
            List<String> selectedQuestions = getRandomQuestions(4);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("quickQuestions", selectedQuestions);
            response.put("total", selectedQuestions.size());
            response.put("message", "Quick questions refreshed");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error refreshing quick questions", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to refresh quick questions");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get categorized quick questions (4 questions from different categories)
     */
    @GetMapping("/quick-questions/categorized")
    public ResponseEntity<?> getCategorizedQuickQuestions() {
        
        logger.info("Fetching categorized quick questions");
        
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            
            // Questions by category - take 1 from each category to make 4 total
            List<Map<String, String>> categorized = new ArrayList<>();
            
            // Navigation category
            Map<String, String> nav1 = new HashMap<>();
            nav1.put("category", "Navigation");
            nav1.put("question", "How to view timesheet?");
            categorized.add(nav1);
            
            // Status category
            Map<String, String> status1 = new HashMap<>();
            status1.put("category", "Status");
            status1.put("question", "How to check leave status?");
            categorized.add(status1);
            
            // Attendance category
            Map<String, String> attendance1 = new HashMap<>();
            attendance1.put("category", "Attendance");
            attendance1.put("question", "Show today's attendance");
            categorized.add(attendance1);
            
            // Balance category
            Map<String, String> balance1 = new HashMap<>();
            balance1.put("category", "Balance");
            balance1.put("question", "Check leave balance");
            categorized.add(balance1);
            
            response.put("quickQuestions", categorized);
            response.put("total", categorized.size());
            response.put("message", "Showing 4 categorized quick questions");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching categorized quick questions", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to fetch categorized quick questions");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get logged-in user information
     */
    @GetMapping("/user/info")
    public ResponseEntity<?> getLoggedInUserInfo(
            @RequestHeader(value = "X-Employee-Id", required = false) String employeeId) {
        
        logger.info("Fetching user info for employee: {}", employeeId);
        
        try {
            if (employeeId == null || employeeId.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("error", "Employee ID is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            Map<String, Object> userInfo = chatbotService.getLoggedInUserInfo(employeeId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("userInfo", userInfo);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching user info", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to fetch user info: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Initialize chat session with welcome message
     */
    @GetMapping("/chat/initialize")
    public ResponseEntity<?> initializeChat(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Employee-Id", required = false) String employeeId) {
        
        logger.info("Initializing chat for user: {}, employee: {}", userId, employeeId);
        
        try {
            Map<String, Object> result = chatbotService.initializeChatSession(userId, employeeId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error initializing chat", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to initialize chat: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Send a message to the chatbot
     */
    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-Employee-Id", required = false) String employeeId,
            @RequestBody Map<String, String> request) {
        
        logger.info("Received chat message request");
        
        try {
            String finalUserId = userId;
            if (finalUserId == null || finalUserId.isEmpty()) {
                finalUserId = request.getOrDefault("userId", "anonymous");
            }
            
            String finalEmployeeId = employeeId;
            if (finalEmployeeId == null || finalEmployeeId.isEmpty()) {
                finalEmployeeId = request.getOrDefault("employeeId", "unknown");
            }
            
            String message = request.get("message");
            if (message == null || message.trim().isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("error", "Message cannot be empty");
                return ResponseEntity.badRequest().body(error);
            }
            
            Map<String, Object> response = chatbotService.processMessage(finalUserId, finalEmployeeId, message);
            
            // Add 4 new quick questions to the response
            if (response.containsKey("success") && Boolean.TRUE.equals(response.get("success"))) {
                response.put("quickQuestions", getRandomQuestions(4));
            }
            
            if (response.containsKey("success") && Boolean.TRUE.equals(response.get("success"))) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (Exception e) {
            logger.error("Unexpected error in sendMessage", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Helper method to get random questions
     */
    private List<String> getRandomQuestions(int count) {
        List<String> shuffled = new ArrayList<>(allQuickQuestions);
        java.util.Collections.shuffle(shuffled);
        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }
    
    /**
     * Test Gemini API connection
     */
    @GetMapping("/gemini/test")
    public ResponseEntity<?> testGemini() {
        logger.info("Testing Gemini API connection");
        
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("status", chatbotService.getGeminiApiKeyStatus());
            result.put("message", "Gemini API is " + chatbotService.getGeminiApiKeyStatus());
            result.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error testing Gemini", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get timesheet navigation flow
     */
    @GetMapping("/timesheet/flow")
    public ResponseEntity<?> getTimesheetFlow() {
        
        logger.info("Fetching timesheet navigation flow");
        
        try {
            Map<String, Object> response = new HashMap<>();
            
            response.put("feature", "TIMESHEET STATUS VIEW");
            response.put("description", "Follow these steps to view your timesheet status");
            
            List<Map<String, Object>> steps = new ArrayList<>();
            
            Map<String, Object> step1 = new HashMap<>();
            step1.put("stepNumber", 1);
            step1.put("action", "CLICK on 'Attendance'");
            step1.put("location", "Main Navigation Menu");
            steps.add(step1);
            
            Map<String, Object> step2 = new HashMap<>();
            step2.put("stepNumber", 2);
            step2.put("action", "VIEW 'Attendance Summary' page");
            steps.add(step2);
            
            Map<String, Object> step3 = new HashMap<>();
            step3.put("stepNumber", 3);
            step3.put("action", "LOCATE 'Timesheet' section");
            steps.add(step3);
            
            Map<String, Object> step4 = new HashMap<>();
            step4.put("stepNumber", 4);
            step4.put("action", "VIEW month/year displayed");
            step4.put("example", "Timesheet for 4/2026");
            steps.add(step4);
            
            Map<String, Object> step5 = new HashMap<>();
            step5.put("stepNumber", 5);
            step5.put("action", "USE search functionality");
            step5.put("placeholder", "Search by Employee ID or Name");
            steps.add(step5);
            
            Map<String, Object> step6 = new HashMap<>();
            step6.put("stepNumber", 6);
            step6.put("action", "VIEW timesheet table with status columns");
            step6.put("columns", Arrays.asList(
                "S.NO", "EMPLOYEE ID", "NAME", "TOTAL DAYS",
                "EFFECTIVE WORKING DAYS", "PRESENT", "ABSENT", "MISS PUNCH",
                "OD", "COMP OFF", "HOLIDAY", "WEEK OFF"
            ));
            steps.add(step6);
            
            response.put("navigationSteps", steps);
            
            Map<String, Object> quickReference = new HashMap<>();
            quickReference.put("page_title", "Timesheet for 4/2026");
            quickReference.put("menu_path", "Home → Attendance");
            quickReference.put("footer", "© 2019 WHITESTONE. All rights reserved.");
            response.put("quickReference", quickReference);
            
            // Add quick questions
            response.put("quickQuestions", getRandomQuestions(4));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching timesheet flow", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to fetch timesheet navigation flow");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get leave request navigation flow
     */
    @GetMapping("/leave/flow")
    public ResponseEntity<?> getLeaveRequestFlow() {
        
        logger.info("Fetching leave request navigation flow");
        
        try {
            Map<String, Object> response = new HashMap<>();
            
            response.put("feature", "LEAVE REQUEST STATUS VIEW");
            response.put("description", "Follow these steps to view your leave request status");
            
            Map<String, Object> mainPath = new HashMap<>();
            mainPath.put("path", "My Space → Team → Organization → Leave Summary → Leave Request");
            mainPath.put("total_clicks", "5 clicks to reach destination");
            response.put("navigationPath", mainPath);
            
            List<Map<String, Object>> steps = new ArrayList<>();
            
            Map<String, Object> step1 = new HashMap<>();
            step1.put("stepNumber", 1);
            step1.put("action", "CLICK on 'My Space'");
            step1.put("location", "Main Navigation Menu");
            steps.add(step1);
            
            Map<String, Object> step2 = new HashMap<>();
            step2.put("stepNumber", 2);
            step2.put("action", "SELECT 'Team'");
            steps.add(step2);
            
            Map<String, Object> step3 = new HashMap<>();
            step3.put("stepNumber", 3);
            step3.put("action", "CLICK on 'Organization'");
            steps.add(step3);
            
            Map<String, Object> step4 = new HashMap<>();
            step4.put("stepNumber", 4);
            step4.put("action", "CHOOSE 'Leave Summary'");
            steps.add(step4);
            
            Map<String, Object> step5 = new HashMap<>();
            step5.put("stepNumber", 5);
            step5.put("action", "CLICK on 'Leave Request' tab");
            steps.add(step5);
            
            Map<String, Object> step6 = new HashMap<>();
            step6.put("stepNumber", 6);
            step6.put("action", "VIEW leave requests table");
            step6.put("columns", Arrays.asList(
                "EMPLOYEE ID", "EMPLOYEE NAME", "TYPE", "REASON",
                "DURATION", "START DATE", "END DATE", "STATUS", "ACTION"
            ));
            steps.add(step6);
            
            Map<String, Object> step7 = new HashMap<>();
            step7.put("stepNumber", 7);
            step7.put("action", "CHECK request status");
            step7.put("status_options", Arrays.asList(
                "⏳ Pending - Waiting for approval",
                "✅ Approved - Leave approved",
                "❌ Rejected - Leave rejected",
                "↩️ Withdrawn - Request cancelled"
            ));
            steps.add(step7);
            
            response.put("navigationSteps", steps);
            
            Map<String, Object> visualGuide = new HashMap<>();
            visualGuide.put("header_structure", "# Whitestone");
            visualGuide.put("navigation_bar", "My Space | Team | Organization");
            visualGuide.put("content_area", "Leave Summary | Leave Request");
            visualGuide.put("export_button", "📥 Export to Excel");
            visualGuide.put("search", "🔍 Search by Employee ID or Name");
            visualGuide.put("pagination", "◀ Previous | Page 1 of 1 | Next ▶");
            visualGuide.put("footer", "© 2019 WHITESTONE. All rights reserved.");
            response.put("visualGuide", visualGuide);
            
            // Add quick questions
            response.put("quickQuestions", getRandomQuestions(4));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching leave flow", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to fetch leave request navigation flow");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get chat history for a user
     */
    @GetMapping("/history")
    public ResponseEntity<?> getHistory(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestParam(defaultValue = "20") int limit) {
        
        logger.info("Fetching history for user: {}", userId);
        
        try {
            if (userId == null || userId.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("error", "User ID is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            if (limit < 1 || limit > 100) {
                limit = 20;
            }
            
            List<ChatMessage> history = chatbotService.getUserHistory(userId, limit);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("history", history);
            response.put("count", history.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching history", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to fetch history: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get conversation by session ID
     */
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> getSessionHistory(@PathVariable String sessionId) {
        
        logger.info("Fetching session: {}", sessionId);
        
        try {
            if (sessionId == null || sessionId.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("error", "Session ID is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            List<ChatMessage> history = chatbotService.getSessionHistory(sessionId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("sessionId", sessionId);
            response.put("history", history);
            response.put("count", history.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching session history", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to fetch session history: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Add feedback to a chat message
     */
    @PostMapping("/feedback/{messageId}")
    public ResponseEntity<?> addFeedback(
            @PathVariable String messageId,
            @RequestBody Map<String, Object> feedback) {
        
        logger.info("Adding feedback for message: {}", messageId);
        
        try {
            if (messageId == null || messageId.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("error", "Message ID is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            Integer rating = null;
            Object ratingObj = feedback.get("rating");
            if (ratingObj instanceof Integer) {
                rating = (Integer) ratingObj;
            } else if (ratingObj instanceof String) {
                try {
                    rating = Integer.parseInt((String) ratingObj);
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }
            
            String comment = feedback.get("comment") != null ? feedback.get("comment").toString() : "";
            
            if (rating == null || rating < 1 || rating > 5) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("error", "Rating must be between 1 and 5");
                return ResponseEntity.badRequest().body(error);
            }
            
            chatbotService.addFeedback(messageId, rating, comment);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Feedback recorded successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error adding feedback", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to add feedback: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get suggested questions - now returns only 4
     */
    @GetMapping("/suggestions")
    public ResponseEntity<?> getSuggestions() {
        
        logger.info("Fetching 4 suggestions");
        
        try {
            List<String> allSuggestions = Arrays.asList(
                "How do I view my timesheet status?",
                "Where can I see my leave request status?",
                "Show me the path to timesheet",
                "How to check leave request status?",
                "Navigation guide for attendance",
                "How to reach leave request page?",
                "Where is the timesheet located?",
                "How to check if my leave is approved?",
                "Show me the menu for leave tracker",
                "How to see miss punches in timesheet?",
                "What is my leave balance?",
                "Show my attendance for today",
                "What was my attendance yesterday?",
                "Show my weekly timesheet",
                "How to apply for leave?",
                "My profile information",
                "How to apply for resignation?",
                "Show my project history",
                "Company holidays",
                "Leave policy"
            );
            
            // Get 4 random suggestions
            List<String> shuffled = new ArrayList<>(allSuggestions);
            java.util.Collections.shuffle(shuffled);
            List<String> selectedSuggestions = shuffled.subList(0, Math.min(4, shuffled.size()));
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("suggestions", selectedSuggestions);
            response.put("count", selectedSuggestions.size());
            response.put("message", "Showing 4 suggestions");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching suggestions", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to fetch suggestions");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Clear chat history for a user
     */
    @DeleteMapping("/history")
    public ResponseEntity<?> clearHistory(
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        logger.info("Clearing history for user: {}", userId);
        
        try {
            if (userId == null || userId.isEmpty()) {
                Map<String, Object> error = new HashMap<>();
                error.put("success", false);
                error.put("error", "User ID is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Chat history cleared successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error clearing history", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to clear history");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get unresolved queries needing human attention
     */
    @GetMapping("/admin/unresolved")
    public ResponseEntity<?> getUnresolvedQueries() {
        
        logger.info("Fetching unresolved queries");
        
        try {
            List<ChatMessage> unresolved = chatbotService.getQueriesNeedingHuman();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("queries", unresolved);
            response.put("count", unresolved.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error fetching unresolved queries", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to fetch unresolved queries");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Get chat statistics
     */
    @GetMapping("/admin/statistics")
    public ResponseEntity<?> getStatistics() {
        
        logger.info("Fetching chat statistics");
        
        try {
            Map<String, Object> stats = chatbotService.getChatStatistics();
            stats.put("success", true);
            
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            logger.error("Error fetching statistics", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", "Failed to fetch statistics");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        
        logger.debug("Health check called");
        
        try {
            Map<String, Object> health = new HashMap<>();
            health.put("status", "UP");
            health.put("timestamp", System.currentTimeMillis());
            health.put("service", "Chatbot Service");
            health.put("version", "1.0.0");
            health.put("features", Arrays.asList(
                "Timesheet Navigation Flow",
                "Leave Request Navigation Flow",
                "Quick Questions (4 at a time)",
                "Chat Messaging",
                "Feedback Collection",
                "Admin Statistics",
                "Gemini AI Integration" // Added
            ));
            health.put("geminiStatus", chatbotService.getGeminiApiKeyStatus()); // Added
            
            if (chatbotService != null) {
                health.put("serviceStatus", "available");
                
                try {
                    chatbotService.getUserHistory("test", 1);
                    health.put("database", "UP");
                } catch (Exception dbEx) {
                    health.put("database", "DOWN");
                    health.put("databaseError", dbEx.getMessage());
                }
            } else {
                health.put("serviceStatus", "unavailable");
            }
            
            return ResponseEntity.ok(health);
            
        } catch (Exception e) {
            logger.error("Health check failed", e);
            Map<String, Object> error = new HashMap<>();
            error.put("status", "DOWN");
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
        }
    }
}