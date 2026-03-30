package com.whitestone.hrms.service;

import com.whitestone.entity.ChatMessage;
import com.whitestone.entity.EmployeeProfileMod;
import com.whitestone.entity.EmployeeLeaveMasterTbl;
import com.whitestone.entity.EmployeeLeaveSummary;
import com.whitestone.entity.UserMasterAttendanceMod;
import com.whitestone.entity.ExitFormMaster;
import com.whitestone.entity.EmployeeProjectHistory;
import com.whitestone.hrms.repo.ChatMessageRepository;
import com.whitestone.hrms.repo.EmployeeProfileModRepository;
import com.whitestone.hrms.repo.EmployeeLeaveMasterTblRepository;
import com.whitestone.hrms.repo.EmployeeLeaveSummaryRepository;
import com.whitestone.hrms.repo.UserMasterAttendanceModRepository;
import com.whitestone.hrms.repo.ExitFormMasterRepository;
import com.whitestone.hrms.repo.EmployeeProjectHistoryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotService.class);

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private EmployeeProfileModRepository employeeProfileModRepository;

    @Autowired
    private EmployeeLeaveMasterTblRepository employeeLeaveMasterTblRepository;

    @Autowired
    private EmployeeLeaveSummaryRepository employeeLeaveSummaryRepository;

    @Autowired
    private UserMasterAttendanceModRepository userMasterAttendanceModRepository;

    @Autowired
    private ExitFormMasterRepository exitFormMasterRepository;

    @Autowired
    private EmployeeProjectHistoryRepository employeeProjectHistoryRepository;

    @Autowired(required = false)
    private HttpServletRequest request;

    @Value("${hr.chatbot.system-prompt:You are an HR Assistant}")
    private String systemPrompt;

    @Value("${hr.chatbot.enable-actions:true}")
    private boolean enableActions;

    @Value("${hr.chatbot.max-history:10}")
    private int maxHistory;

    @Value("${gemini.api.key:YOUR_API_KEY}")
    private String geminiApiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent}")
    private String geminiApiUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter displayDateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private final DateTimeFormatter displayDateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");
    private final Random random = new Random();

    private final List<String> genericResponses = Arrays.asList(
            "i understand you're asking about",
            "i'm here to help with hr queries",
            "for detailed information, please contact hr",
            "could you be more specific",
            "i can help with leave, attendance",
            "i don't understand",
            "i cannot answer that",
            "i'm here to help with hr",
            "please contact hr"
    );

    private final List<String> hrKeywords = Arrays.asList(
            "leave", "attendance", "timesheet", "project", "profile",
            "salary", "holiday", "policy", "resignation", "employee",
            "hr", "benefit", "payroll", "training", "onboarding",
            "check in", "check out", "absent", "present", "overtime",
            "my space", "team", "organization", "summary", "request",
            "apply leave", "leave balance", "leave status", "leave history",
            "attendance approval", "miss punch", "week off",
            "designation", "department", "date of joining", "blood group",
            "exit form", "notice period", "full and final", "experience letter",
            "personal details", "id card", "photo", "upload", "document",
            "update profile", "edit details", "change password", "contact details"
    );

    private final List<String> inspirationalQuotes = Arrays.asList(
            "Success is liking yourself, liking what you do, and liking how you do it. - Maya Angelou",
            "The only way to do great work is to love what you do. - Steve Jobs",
            "Believe you can and you're halfway there. - Theodore Roosevelt",
            "Your attitude, not your aptitude, will determine your altitude. - Zig Ziglar",
            "The future depends on what you do today. - Mahatma Gandhi",
            "Don't watch the clock; do what it does. Keep going. - Sam Levenson",
            "Quality means doing it right when no one is looking. - Henry Ford",
            "The secret of getting ahead is getting started. - Mark Twain",
            "It always seems impossible until it's done. - Nelson Mandela",
            "Your work is going to fill a large part of your life. - Steve Jobs"
    );

    // ==================== DATE UTILITY METHODS ====================

    private LocalDate toLocalDate(Object date) {
        if (date == null) return null;
        try {
            if (date instanceof LocalDate) return (LocalDate) date;
            if (date instanceof java.sql.Date) return ((java.sql.Date) date).toLocalDate();
            if (date instanceof java.util.Date) return ((java.util.Date) date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if (date instanceof LocalDateTime) return ((LocalDateTime) date).toLocalDate();
            if (date instanceof String) {
                String str = date.toString();
                if (str.length() >= 10) {
                    return LocalDate.parse(str.substring(0, 10));
                }
            }
            return null;
        } catch (Exception e) {
            logger.debug("Date conversion error: {}", e.getMessage());
            return null;
        }
    }

    private String formatDate(Object date, DateTimeFormatter formatter) {
        if (date == null) return "N/A";
        try {
            LocalDate ld = toLocalDate(date);
            return ld != null ? ld.format(formatter) : "N/A";
        } catch (Exception e) {
            return "N/A";
        }
    }

    private String formatTime(Object dt) {
        if (dt == null) return "--:--:--";
        try {
            if (dt instanceof LocalDateTime) return ((LocalDateTime) dt).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            if (dt instanceof java.sql.Timestamp) return ((java.sql.Timestamp) dt).toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String s = dt.toString();
            return s.length() >= 19 ? s.substring(11, 19) : s;
        } catch (Exception e) {
            return "--:--:--";
        }
    }

    // ==================== USER INFO METHODS ====================

    /**
     * Get employee name from employee_profile_mod_tbl based on emp_id
     */
    private String getEmployeeNameFromDatabase(String employeeId) {
        try {
            if (employeeId == null || employeeId.trim().isEmpty()) {
                logger.warn("Employee ID is null or empty");
                return null;
            }

            Optional<EmployeeProfileMod> employeeOpt = employeeProfileModRepository.findByEmpid(employeeId);
            
            if (employeeOpt.isPresent()) {
                EmployeeProfileMod employee = employeeOpt.get();
                String name = employee.getEmployeename();
                
                logger.info("Database query successful for employee ID: {}", employeeId);
                logger.info("Retrieved name from database: '{}'", name);
                
                return name;
            } else {
                logger.warn("No employee found in database with ID: {}", employeeId);
                return null;
            }
        } catch (Exception e) {
            logger.error("Error fetching employee name from database for ID: {}", employeeId, e);
            return null;
        }
    }

    /**
     * Get full employee profile info for display
     */
    public Map<String, Object> getLoggedInUserInfo(String employeeId) {
        Map<String, Object> info = new HashMap<>();
        String displayName = "Employee";
        
        try {
            if (employeeId == null || employeeId.trim().isEmpty()) {
                info.put("name", "User");
                info.put("employeeId", "");
                info.put("designation", "");
                info.put("department", "");
                info.put("email", "");
                info.put("profileInitials", "U");
                return info;
            }

            Optional<EmployeeProfileMod> empOpt = employeeProfileModRepository.findByEmpid(employeeId);
            
            if (empOpt.isPresent()) {
                EmployeeProfileMod emp = empOpt.get();
                
                String dbName = emp.getEmployeename();
                
                if (dbName != null && !dbName.trim().isEmpty()) {
                    dbName = dbName.trim();
                    
                    if (isValidName(dbName)) {
                        displayName = dbName.replaceAll("\\s+", " ");
                        logger.info("Using valid name from database: '{}' for employee ID: {}", displayName, employeeId);
                    } else {
                        logger.warn("Invalid name format in database: '{}' for employee ID: {}. Using 'Employee' as fallback", dbName, employeeId);
                        displayName = "Employee";
                    }
                } else {
                    logger.warn("Empty name in database for employee ID: {}. Using 'Employee' as fallback", employeeId);
                    displayName = "Employee";
                }
                
                info.put("name", displayName);
                info.put("employeeId", emp.getEmpid() != null ? emp.getEmpid() : employeeId);
                info.put("designation", emp.getDesignation() != null ? emp.getDesignation() : "");
                info.put("department", emp.getDepartment() != null ? emp.getDepartment() : "");
                info.put("email", emp.getEmailid() != null ? emp.getEmailid() : "");
                info.put("mobile", emp.getMobilenumber() != null ? emp.getMobilenumber() : "");
                info.put("profileInitials", buildInitials(displayName));
                
                logger.info("Final display name for employee {}: '{}'", employeeId, displayName);
            } else {
                logger.warn("No employee profile found in database for ID: {}", employeeId);
                info.put("name", "Employee");
                info.put("employeeId", employeeId);
                info.put("designation", "");
                info.put("department", "");
                info.put("email", "");
                info.put("profileInitials", "E");
            }
        } catch (Exception e) {
            logger.error("Error fetching logged-in user info for ID: {}", employeeId, e);
            info.put("name", "Employee");
            info.put("employeeId", employeeId != null ? employeeId : "");
            info.put("profileInitials", "E");
        }
        
        return info;
    }

    private boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        if (name.matches("^\\d+$")) return false;
        if (name.length() < 2) return false;
        if (!name.matches(".*[a-zA-Z].*")) return false;
        long digitCount = name.chars().filter(Character::isDigit).count();
        if (digitCount > 0 && (digitCount * 100 / name.length()) > 30) return false;
        return true;
    }

    private String getEmployeeName(String employeeId) {
        String dbName = getEmployeeNameFromDatabase(employeeId);
        if (dbName != null && isValidName(dbName)) {
            return dbName.trim().replaceAll("\\s+", " ");
        }
        logger.info("Using fallback name 'Employee' for ID: {}", employeeId);
        return "Employee";
    }

    private String getFirstName(String fullName) {
        if (fullName == null || fullName.equals("Employee")) {
            return "you";
        }
        String[] parts = fullName.split("\\s+");
        if (parts.length > 0) {
            return parts[0];
        }
        return "you";
    }

    private String buildInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "U";
        if (name.equals("Employee")) return "E";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }

    // ==================== LOGOUT / SESSION CLEAR ====================

    @Transactional
    public Map<String, Object> clearChatOnLogout(String userId, String employeeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // DATABASE OPERATIONS COMMENTED OUT
            /*
            List<ChatMessage> userMessages = chatMessageRepository.findByUserId(userId);
            if (userMessages != null && !userMessages.isEmpty()) {
                chatMessageRepository.deleteAll(userMessages);
                logger.info("Cleared {} chat messages for user {} on logout", userMessages.size(), userId);
            }
            */
            
            result.put("success", true);
            result.put("message", "Chat history cleared successfully");
            result.put("clearedCount", 0);
        } catch (Exception e) {
            logger.error("Error clearing chat on logout", e);
            result.put("success", false);
            result.put("message", "Error clearing chat: " + e.getMessage());
        }
        return result;
    }

    public Map<String, Object> initializeChatSession(String userId, String employeeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> userInfo = getLoggedInUserInfo(employeeId);
            String welcomeMessage = getPersonalizedWelcome(employeeId);

            result.put("success", true);
            result.put("welcomeMessage", welcomeMessage);
            result.put("userInfo", userInfo);
            result.put("sessionId", "SESS-" + UUID.randomUUID().toString());
            result.put("timestamp", LocalDateTime.now().toString());
        } catch (Exception e) {
            logger.error("Error initializing chat session", e);
            result.put("success", false);
            result.put("welcomeMessage", "Welcome! How can I help you?");
        }
        return result;
    }

    // ==================== GREETING & WELCOME ====================

    private String getTimeBasedGreeting() {
        int hour = LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) return "Good Morning";
        if (hour >= 12 && hour < 17) return "Good Afternoon";
        return "Good Evening";
    }

    private String getRandomQuote() {
        return inspirationalQuotes.get(random.nextInt(inspirationalQuotes.size()));
    }

    private String getPersonalizedWelcome(String employeeId) {
        String employeeName = getEmployeeName(employeeId);
        String firstName = getFirstName(employeeName);
        String timeGreeting = getTimeBasedGreeting();
        String quote = getRandomQuote();
        String[] quoteParts = quote.split(" - ");
        String quoteText = quoteParts[0];
        String quoteAuthor = quoteParts.length > 1 ? " - " + quoteParts[1] : "";
        String formattedDate = LocalDate.now().format(displayDateTimeFormatter);

        return String.format(
                "🌟 **Welcome to Whitestone HRMS** 🌟\n\n" +
                "**%s, %s!**\n\n" +
                "📅 **%s**\n\n" +
                "💫 *\"%s\"*%s\n\n" +
                "I'm your HRMS Assistant, here to help you with:\n\n" +
                "✅ **Quick Actions:**\n" +
                "• 👤 View/Update Personal Details\n" +
                "• 📷 Upload ID Card Photo\n" +
                "• 📊 Check Timesheet & Attendance\n" +
                "• 📋 Leave Balance & Requests\n" +
                "• 📁 Project History\n" +
                "• 📝 Resignation Process\n\n" +
                "**How may I assist you today, %s?**\n\n" +
                "Try asking:\n" +
                "• 'How to update personal details?'\n" +
                "• 'How to upload ID card photo?'\n" +
                "• 'Show my profile'\n" +
                "• 'How to view timesheet?'",
                timeGreeting, employeeName, formattedDate, quoteText, quoteAuthor, firstName
        );
    }

    // ==================== PROFILE & DOCUMENT GUIDES ====================

    private String getPersonalDetailsGuide(String employeeId) {
        try {
            Optional<EmployeeProfileMod> opt = employeeProfileModRepository.findByEmpid(employeeId);
            if (!opt.isPresent()) return "I couldn't find your profile. Please contact HR for assistance.";
            EmployeeProfileMod emp = opt.get();
            return String.format(
                    "👤 **Personal Details Update Guide**\n\n" +
                    "**Current Information:**\n" +
                    "• Full Name: %s\n" +
                    "• Employee ID: %s\n" +
                    "• Date of Birth: %s\n" +
                    "• Blood Group: %s\n" +
                    "• Gender: %s\n" +
                    "• Marital Status: %s\n\n" +
                    "📍 **Path:** My Space → Profile → Edit Profile\n\n" +
                    "**Steps:**\n" +
                    "1. Click 'My Space' → 'Profile'\n" +
                    "2. Click 'Edit Profile' (top right)\n" +
                    "3. Update required fields with supporting docs\n" +
                    "4. Click 'Save Changes'\n" +
                    "5. HR will verify in 2-3 working days\n\n" +
                    "Need to update contact details? Ask: 'How to update contact details?'\n" +
                    "Want to upload ID card? Ask: 'How to upload ID card photo?'",
                    emp.getEmployeename() != null ? emp.getEmployeename() : "N/A",
                    emp.getEmpid() != null ? emp.getEmpid() : "N/A",
                    formatDate(emp.getDateofbirth(), displayDateFormatter),
                    emp.getBloodgroup() != null ? emp.getBloodgroup() : "N/A",
                    emp.getGender() != null ? emp.getGender() : "N/A",
                    emp.getMaritalstatus() != null ? emp.getMaritalstatus() : "N/A"
            );
        } catch (Exception e) {
            logger.error("Error in personal details guide", e);
            return "I'm having trouble fetching your profile. Please try again or contact HR.";
        }
    }

    private String getContactDetailsGuide(String employeeId) {
        try {
            Optional<EmployeeProfileMod> opt = employeeProfileModRepository.findByEmpid(employeeId);
            if (!opt.isPresent()) return "I couldn't find your profile. Please contact HR for assistance.";
            EmployeeProfileMod emp = opt.get();
            return String.format(
                    "📞 **Contact Details Update Guide**\n\n" +
                    "**Current Information:**\n" +
                    "• Email: %s\n" +
                    "• Mobile: %s\n" +
                    "• Alternate Phone: %s\n" +
                    "• Emergency Contact: %s\n" +
                    "• Emergency Contact Number: %s\n\n" +
                    "📍 **Path:** My Space → Profile → Contact Information\n\n" +
                    "**Steps to Update:**\n" +
                    "1. Go to 'My Space' → 'Profile'\n" +
                    "2. Click 'Contact Information' tab\n" +
                    "3. Click the 'Edit' pencil icon\n" +
                    "4. Mobile/Email changes require OTP/email verification\n" +
                    "5. Address & Emergency contact save immediately\n\n" +
                    "Need to change password? Ask: 'How to change my password?'",
                    emp.getEmailid() != null ? emp.getEmailid() : "N/A",
                    emp.getMobilenumber() != null ? emp.getMobilenumber() : "N/A",
                    emp.getAlternatemobilenumber() != null ? emp.getAlternatemobilenumber() : "N/A",
                    emp.getEmergencycontactname() != null ? emp.getEmergencycontactname() : "N/A",
                    emp.getEmergencycontactnumber() != null ? emp.getEmergencycontactnumber() : "N/A"
            );
        } catch (Exception e) {
            logger.error("Error in contact details guide", e);
            return "I'm having trouble fetching your contact details. Please try again or contact HR.";
        }
    }

    private String getPasswordChangeGuide(String employeeId) {
        return "🔐 **Password Change Guide**\n\n" +
               "📍 **Path:** My Space → Profile → Security → Change Password\n\n" +
               "**Steps:**\n" +
               "1. Go to 'My Space' → 'Profile' → 'Security'\n" +
               "2. Click 'Change Password'\n" +
               "3. Enter current password\n" +
               "4. Enter & confirm new password\n\n" +
               "**Requirements:** Min 8 chars, uppercase, lowercase, number, special char\n\n" +
               "**Forgot Password?** Click 'Forgot Password' on the login page.";
    }

    private String getIdCardUploadGuide(String employeeId) {
        return "🪪 **ID Card Photo Upload Guide**\n\n" +
               "📍 **Path:** My Space → Profile → Documents → ID Card\n\n" +
               "**Photo Requirements:** JPG/PNG, max 2MB, 300×300px min, white/light blue background\n\n" +
               "**Steps:**\n" +
               "1. Go to 'My Space' → 'Profile' → 'Documents' → 'ID Card'\n" +
               "2. Click 'Upload New'\n" +
               "3. Select & crop your photo\n" +
               "4. Click 'Upload'\n" +
               "5. HR verifies within 24 hours\n\n" +
               "**Common Issues:** File too large → Compress | Wrong format → Convert to JPG/PNG";
    }

    private String getProfilePhotoUploadGuide(String employeeId) {
        return "📸 **Profile Photo Upload Guide**\n\n" +
               "📍 **Path:** My Space → Profile → Profile Photo\n\n" +
               "**Steps:**\n" +
               "1. Go to 'My Space' → 'Profile'\n" +
               "2. Hover over profile picture → Click camera icon\n" +
               "3. Select your photo, crop & save\n\n" +
               "**Requirements:** JPG/PNG, max 1MB, professional attire";
    }

    private String getEnhancedProfileNavigationGuide() {
        return "👤 **My Profile Management**\n\n" +
               "📍 **Path:** My Space → Profile\n\n" +
               "🔹 View/Update Personal Details → Edit Profile\n" +
               "🔹 Upload Profile Photo → Hover on photo → Camera icon\n" +
               "🔹 Upload ID Card → Documents tab → ID Card\n" +
               "🔹 Update Contact Info → Contact Information tab\n" +
               "🔹 Change Password → Security tab\n\n" +
               "**Need Detailed Guide?**\n" +
               "• 'How to update personal details?'\n" +
               "• 'How to upload ID card photo?'\n" +
               "• 'How to change my password?'\n\n" +
               "© 2019 WHITESTONE";
    }

    // ==================== MAIN MESSAGE PROCESSING ====================

    @Transactional
    public Map<String, Object> processMessage(String userId, String employeeId, String userMessage) {
        logger.info("Processing message for user: {}, employee: {}", userId, employeeId);

        // DATABASE OPERATION COMMENTED OUT
        // ChatMessage chatMessage = createChatMessage(userId, employeeId, userMessage);
        
        Map<String, Object> response = new HashMap<>();

        try {
            String intent = detectIntent(userMessage);
            // DATABASE OPERATION COMMENTED OUT
            // chatMessage.setIntentType(intent);

            Map<String, Object> actionResult = null;
            String assistantResponse;
            boolean requiresHuman = false;
            boolean usedGemini = false;

            if (intent.equals("greeting") || intent.equals("first_visit")) {
                assistantResponse = getPersonalizedWelcome(employeeId);
            } else if (intent.startsWith("profile_") || intent.startsWith("document_") ||
                       intent.equals("personal_details") || intent.equals("id_card") ||
                       intent.equals("contact_details") || intent.equals("password_change")) {

                if (intent.equals("personal_details") || intent.equals("profile_update")) {
                    assistantResponse = getPersonalDetailsGuide(employeeId);
                } else if (intent.equals("contact_details")) {
                    assistantResponse = getContactDetailsGuide(employeeId);
                } else if (intent.equals("password_change")) {
                    assistantResponse = getPasswordChangeGuide(employeeId);
                } else if (intent.equals("id_card") || intent.equals("document_idcard")) {
                    assistantResponse = getIdCardUploadGuide(employeeId);
                } else if (intent.equals("profile_photo") || intent.equals("document_photo")) {
                    assistantResponse = getProfilePhotoUploadGuide(employeeId);
                } else {
                    assistantResponse = getEnhancedProfileNavigationGuide();
                }

            } else if (!isHrQuestion(userMessage) && isGeminiConfigured()) {
                String geminiResponse = callGeminiAPI(userMessage);
                if (geminiResponse != null) {
                    assistantResponse = geminiResponse;
                    actionResult = new HashMap<>();
                    actionResult.put("actionType", "GEMINI_AI");
                    actionResult.put("actionData", "AI generated response");
                    usedGemini = true;
                } else {
                    assistantResponse = "I'm having trouble with the AI service. Please try again.";
                    requiresHuman = true;
                }
            } else if (enableActions) {
                actionResult = handleAction(userMessage, employeeId, intent);

                if (actionResult != null && actionResult.containsKey("response")) {
                    assistantResponse = (String) actionResult.get("response");
                    requiresHuman = (boolean) actionResult.getOrDefault("requiresHuman", false);
                    
                    // DATABASE OPERATIONS COMMENTED OUT
                    // chatMessage.setActionTaken((String) actionResult.get("actionType"));
                    // chatMessage.setActionResult((String) actionResult.get("actionData"));

                    if (isGenericResponse(assistantResponse) && isGeminiConfigured()) {
                        String gr = callGeminiAPI(userMessage);
                        if (gr != null) {
                            assistantResponse = gr;
                            usedGemini = true;
                        }
                    }
                } else {
                    if (isGeminiConfigured()) {
                        String gr = callGeminiAPI(userMessage);
                        if (gr != null) {
                            assistantResponse = gr;
                            actionResult = new HashMap<>();
                            actionResult.put("actionType", "GEMINI_AI_FALLBACK");
                            usedGemini = true;
                        } else {
                            assistantResponse = getContextualResponse(employeeId, intent, userMessage);
                            requiresHuman = assistantResponse.toLowerCase().contains("contact hr");
                        }
                    } else {
                        assistantResponse = getContextualResponse(employeeId, intent, userMessage);
                        requiresHuman = assistantResponse.toLowerCase().contains("contact hr");
                    }
                }
            } else {
                assistantResponse = isNavigationQuestion(userMessage)
                        ? getNavigationResponse(userMessage)
                        : getContextualResponse(employeeId, intent, userMessage);
                requiresHuman = assistantResponse.toLowerCase().contains("contact hr");
            }

            // DATABASE OPERATIONS COMMENTED OUT
            // chatMessage.setAssistantResponse(assistantResponse);
            // chatMessage.setRequiresHuman(requiresHuman);
            // chatMessage.setConfidenceScore(calculateConfidence(intent, userMessage));
            // chatMessage.setMessageType(ChatMessage.MessageType.RESPONSE);
            // chatMessageRepository.save(chatMessage);

            Map<String, Object> userInfo = getLoggedInUserInfo(employeeId);

            response.put("success", true);
            response.put("message", assistantResponse);
            response.put("messageId", "msg-" + System.currentTimeMillis());
            response.put("timestamp", LocalDateTime.now().toString());
            response.put("requiresHuman", requiresHuman);
            response.put("intent", intent);
            response.put("suggestions", getContextualSuggestions(intent, employeeId));
            response.put("sessionId", "SESS-" + UUID.randomUUID().toString());
            response.put("usedGemini", usedGemini);
            response.put("userInfo", userInfo);

            if (actionResult != null) {
                response.put("actionType", actionResult.get("actionType"));
                response.put("actionData", actionResult.get("actionData"));
            }

        } catch (Exception e) {
            logger.error("Error processing message", e);
            // DATABASE OPERATION COMMENTED OUT
            // handleError(chatMessage, userId, response);
            
            response.put("success", false);
            response.put("message", "I'm having trouble processing your request. Please try again later.");
            response.put("requiresHuman", true);
            response.put("messageId", "error-" + System.currentTimeMillis());
        }
        return response;
    }

    // ==================== INTENT DETECTION ====================

    private String detectIntent(String message) {
        if (message == null || message.trim().isEmpty()) return "general";
        String lower = message.toLowerCase();

        if (lower.contains("personal details") || lower.contains("update personal") ||
            lower.contains("change name") || lower.contains("update name") ||
            lower.contains("change dob") || lower.contains("update dob") ||
            (lower.contains("blood group") && (lower.contains("update") || lower.contains("change")))) {
            return "personal_details";
        }

        if ((lower.contains("contact") || lower.contains("mobile") || lower.contains("phone") ||
             lower.contains("email") || lower.contains("address") || lower.contains("emergency contact")) &&
            (lower.contains("update") || lower.contains("change"))) {
            return "contact_details";
        }

        if (lower.contains("password") || lower.contains("change password") ||
            lower.contains("reset password") || lower.contains("forgot password")) {
            return "password_change";
        }

        if (lower.contains("id card") || lower.contains("id photo") ||
            lower.contains("id upload") || lower.contains("card photo")) {
            return "id_card";
        }

        if (lower.contains("profile photo") || lower.contains("profile pic") ||
            lower.contains("upload photo") || lower.contains("change photo") ||
            lower.contains("profile picture")) {
            return "profile_photo";
        }

        if (lower.contains("profile") || lower.contains("my details") || lower.contains("my information")) {
            if (lower.contains("update") || lower.contains("change") || lower.contains("edit")) {
                return "profile_update";
            }
            return "profile";
        }

        if (isNavigationQuestion(message)) {
            if (lower.contains("timesheet") || lower.contains("attendance")) return "navigation_timesheet";
            if (lower.contains("leave") && (lower.contains("request") || lower.contains("status"))) return "navigation_leave_request";
            if (lower.contains("leave") && lower.contains("balance")) return "navigation_leave_balance";
            if (lower.contains("project")) return "navigation_project";
            if (lower.contains("profile")) return "navigation_profile";
            if (lower.contains("resignation")) return "navigation_resignation";
            if (lower.contains("policy")) return "navigation_policy";
            return "navigation_general";
        }

        if (lower.contains("attendance") || lower.contains("present") || lower.contains("absent")) {
            if (lower.contains("yesterday") || lower.contains("previous day")) return "attendance_yesterday";
            if (lower.contains("today") || lower.contains("check in") || lower.contains("check out")) return "attendance_today";
            return "attendance";
        }

        if (lower.contains("timesheet") || lower.contains("time sheet") || lower.contains("weekly hours") ||
            lower.contains("this week") || lower.contains("work hours") || lower.contains("total hours")) {
            return "timesheet";
        }

        if (lower.contains("resign") || lower.contains("resignation") || lower.contains("quit") ||
            lower.contains("exit form") || lower.contains("leaving company") || lower.contains("notice period")) {
            return "resignation";
        }

        if ((lower.contains("project") || lower.contains("projects")) &&
            (lower.contains("history") || lower.contains("past") || lower.contains("previous") || lower.contains("current"))) {
            return "project_history";
        }

        if (lower.contains("policy") || lower.contains("policies") || lower.contains("company policy")) {
            return "policy";
        }

        if (lower.contains("leave") || lower.contains("vacation") || lower.contains("time off")) {
            if (lower.contains("balance") || lower.contains("left") || lower.contains("available")) return "leave_balance";
            if (lower.contains("apply") || lower.contains("request") || lower.contains("take")) return "leave_application";
            if (lower.contains("history") || lower.contains("previous") || lower.contains("past")) return "leave_history";
            return "leave_general";
        }

        if (lower.contains("hello") || lower.contains("hi") || lower.contains("hey") ||
            lower.contains("good morning") || lower.contains("good afternoon") || lower.contains("good evening")) {
            return "greeting";
        }

        if (lower.contains("help") || lower.contains("support") || lower.contains("what can you do")) {
            return "help";
        }

        return "general";
    }

    // ==================== CONTEXTUAL SUGGESTIONS ====================

    private List<String> getContextualSuggestions(String intent, String employeeId) {
        List<String> suggestions = new ArrayList<>();
        
        suggestions.add("Today's attendance");
        suggestions.add("Yesterday's attendance");
        suggestions.add("My timesheet");
        suggestions.add("Leave balance");
        suggestions.add("Update personal details");
        suggestions.add("Upload ID card photo");
        
        switch (intent) {
            case "personal_details":
            case "profile_update":
                suggestions.add("Show my profile");
                suggestions.add("How to upload ID card photo?");
                suggestions.add("How to change password?");
                suggestions.add("Update contact details");
                break;
            case "contact_details":
                suggestions.add("Update mobile number");
                suggestions.add("Change email address");
                suggestions.add("Update emergency contact");
                suggestions.add("Show my profile");
                break;
            case "password_change":
                suggestions.add("Forgot password");
                suggestions.add("Update personal details");
                suggestions.add("Show my profile");
                break;
            case "id_card":
            case "profile_photo":
                suggestions.add("How to upload ID card photo?");
                suggestions.add("How to upload profile photo?");
                suggestions.add("Update personal details");
                break;
            case "profile":
                suggestions.add("Update personal details");
                suggestions.add("Upload ID card photo");
                suggestions.add("Change password");
                suggestions.add("View projects");
                break;
            case "attendance_today":
            case "attendance_yesterday":
                suggestions.add("Show yesterday's attendance");
                suggestions.add("Show my timesheet");
                suggestions.add("Check leave balance");
                break;
            case "timesheet":
                suggestions.add("Show yesterday's attendance");
                suggestions.add("Check leave balance");
                suggestions.add("How to update personal details?");
                break;
            case "help":
            case "navigation_general":
                suggestions.add("Today's attendance");
                suggestions.add("My timesheet");
                suggestions.add("Leave balance");
                suggestions.add("Update personal details");
                suggestions.add("Upload ID card photo");
                break;
        }
        
        return suggestions.stream().distinct().limit(6).collect(Collectors.toList());
    }

    private String getContextualResponse(String employeeId, String intent, String userMessage) {
        switch (intent) {
            case "greeting":
            case "first_visit":
                return getPersonalizedWelcome(employeeId);
            case "farewell":
                String[] f = {"Goodbye! Feel free to ask if you need any HR help.", "Take care! Come back if you have more questions.", "Have a great day!"};
                return f[random.nextInt(f.length)];
            case "help":
                return "🤖 **I can help with:**\n\n" +
                       "**👤 Profile:** Update details, Upload ID card, Change password\n" +
                       "**📊 HR Ops:** Leave balance, Attendance, Timesheet, Projects, Resignation, Policies\n" +
                       "**🧭 Navigation:** Ask 'How to view timesheet?' for step-by-step guides";
            case "navigation_profile":
                return getEnhancedProfileNavigationGuide();
            default:
                String[] d = {
                    "I can help with HR queries. Ask about leave balance, timesheet, attendance, personal details, or ID card upload.",
                    "I'm here to help with HR. Try: 'Leave balance' or 'How to update personal details?'"
                };
                return d[random.nextInt(d.length)];
        }
    }

    // ==================== NAVIGATION ====================

    private boolean isNavigationQuestion(String message) {
        if (message == null) return false;
        String lower = message.toLowerCase();
        return lower.contains("how to") || lower.contains("where") || lower.contains("navigate") ||
               lower.contains("path") || lower.contains("go to") || lower.contains("find") ||
               lower.contains("locate") ||
               (lower.contains("timesheet") && lower.contains("view")) ||
               (lower.contains("leave") && lower.contains("status"));
    }

    private String getNavigationResponse(String message) {
        if (message == null) return getGeneralNavigationGuide();
        String lower = message.toLowerCase();
        if (lower.contains("timesheet") || lower.contains("attendance")) return getTimesheetNavigationGuide();
        if (lower.contains("leave")) {
            if (lower.contains("request") || lower.contains("status") || lower.contains("applied")) return getLeaveRequestNavigationGuide();
            if (lower.contains("balance")) return getLeaveBalanceNavigationGuide();
            return getLeaveNavigationGuide();
        }
        if (lower.contains("project")) return getProjectHistoryNavigationGuide();
        if (lower.contains("profile") || lower.contains("details")) return getEnhancedProfileNavigationGuide();
        if (lower.contains("resignation") || lower.contains("exit")) return getResignationNavigationGuide();
        if (lower.contains("policy")) return getPolicyNavigationGuide();
        return getGeneralNavigationGuide();
    }

    private String getTimesheetNavigationGuide() {
        return "📊 **Timesheet Access**\n📍 Home → Attendance → Timesheet\n\n" +
               "1. Click 'Attendance' in main menu\n2. View 'Timesheet' section\n3. Search by Employee ID/Name\n\n" +
               "✅ Present | ❌ Absent | ⚠️ Miss Punch | 🎉 Holiday | 📆 Week Off\n\n© 2019 WHITESTONE";
    }

    private String getLeaveRequestNavigationGuide() {
        return "📋 **Leave Request Status**\n📍 Home → Leave Tracker → Leave Request\n\n" +
               "• Tabs: Leave Requests | Permission Requests\n• Export to Excel available\n\n" +
               "⏳ Pending | ✅ Approved | ❌ Rejected | ↩️ Withdrawn\n\n© 2019 WHITESTONE";
    }

    private String getLeaveBalanceNavigationGuide() {
        return "📊 **Leave Balance**\n📍 My Space → Team → Organization → Leave Summary\n\n" +
               "Shows CL / SL / EL balances and leave taken this year.\n\n© 2019 WHITESTONE";
    }

    private String getLeaveNavigationGuide() {
        return "📅 **Leave Management**\n" +
               "• Balance: My Space → Team → Organization → Leave Summary\n" +
               "• Status: Home → Leave Tracker → Leave Request\n" +
               "• Apply: Home → Leave Tracker → Apply Leave\n\nWhat would you like to do?";
    }

    private String getProjectHistoryNavigationGuide() {
        return "📁 **Project History**\n📍 Home → Project History\n\n" +
               "Shows current & past projects with name, role, client, dates.\n\n© 2019 WHITESTONE";
    }

    private String getResignationNavigationGuide() {
        return "📝 **Resignation**\n📍 Home → Resignation\n\n" +
               "Fill form → Enter LWD → Provide reason → Submit\nNotice period: 90 days\n\n© 2019 WHITESTONE";
    }

    private String getPolicyNavigationGuide() {
        return "📚 **Company Policies**\n📍 Home → Policy\n\n" +
               "Available: Leave | Attendance | Resignation | WFH | Travel | Reimbursement\n\n© 2019 WHITESTONE";
    }

    private String getGeneralNavigationGuide() {
        return "🧭 **HRMS Navigation**\n\n" +
               "• Timesheet: Home → Attendance\n• Leave Status: Home → Leave Tracker → Leave Request\n" +
               "• Leave Balance: My Space → Team → Organization → Leave Summary\n" +
               "• Profile: My Space → Profile\n• Projects: Home → Project History\n\n" +
               "• Update Details: My Space → Profile → Edit Profile\n" +
               "• ID Card: My Space → Profile → Documents → ID Card\n" +
               "• Change Password: My Space → Profile → Security\n\nWhat would you like help with?";
    }

    // ==================== HR ACTION HANDLERS ====================

    private Map<String, Object> handleAction(String message, String employeeId, String intent) {
        if (message == null) return null;
        String lm = message.toLowerCase();
        Map<String, Object> result = new HashMap<>();
        try {
            if (isNavigationQuestion(message)) {
                result.put("response", getNavigationResponse(message));
                result.put("actionType", "NAVIGATION_GUIDE");
                result.put("requiresHuman", false);
                return result;
            }
            if (intent.equals("leave_balance") || Pattern.compile("leave balance|how many leaves|leave left|available leave").matcher(lm).find()) {
                return handleLeaveBalance(employeeId);
            }
            if (intent.equals("leave_application") || Pattern.compile("apply for leave|request leave|take leave|want leave").matcher(lm).find()) {
                return handleLeaveApplication(employeeId);
            }
            if (intent.equals("attendance_today") || Pattern.compile("today attendance|attendance today|check in|check out|punched in").matcher(lm).find()) {
                return handleAttendanceToday(employeeId);
            }
            if (intent.equals("attendance_yesterday") || Pattern.compile("yesterday attendance|yesterday check in|yesterday check out|previous day").matcher(lm).find()) {
                return handleAttendanceYesterday(employeeId);
            }
            if (intent.equals("timesheet") || Pattern.compile("timesheet|time sheet|weekly hours|work hours|this week").matcher(lm).find()) {
                return handleTimesheet(employeeId);
            }
            if (intent.equals("resignation") || Pattern.compile("resignation|resign|exit form|quit|leaving company").matcher(lm).find()) {
                return handleResignation(employeeId);
            }
            if (intent.equals("project_history") || Pattern.compile("project history|my projects|past projects|assigned projects|current projects").matcher(lm).find()) {
                return handleProjectHistory(employeeId);
            }
            if (intent.equals("profile") || lm.contains("show my profile") || lm.contains("my details")) {
                return handleProfile(employeeId);
            }
            if (intent.equals("leave_history") || lm.contains("leave history") || lm.contains("previous leaves")) {
                return handleLeaveHistory(employeeId);
            }
            if (intent.equals("policy") || lm.contains("policy") || lm.contains("company policy")) {
                return handlePolicy(lm);
            }
        } catch (Exception e) {
            logger.error("Error handling action", e);
        }
        return null;
    }

    private Map<String, Object> handleAttendanceToday(String employeeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            LocalDate today = LocalDate.now();
            UserMasterAttendanceMod att = findAttendanceForDate(employeeId, today);
            if (att != null) {
                result.put("response", String.format(
                        "Today's Attendance (%s):\n• Status: %s\n• Check In: %s\n• Check Out: %s\n• Total Hours: %s\n\n" +
                        "Ask: 'What was my attendance yesterday?' or 'How to view timesheet?'",
                        today.format(dateFormatter),
                        att.getStatus() != null ? att.getStatus() : "Unknown",
                        formatTime(att.getCheckintime()),
                        formatTime(att.getCheckouttime()),
                        att.getTotalhoursworked() != null ? att.getTotalhoursworked() : "N/A"
                ));
                result.put("actionData", att.getStatus());
            } else {
                result.put("response", "No attendance record found for today. Please check in.\n\nAsk: 'What was my attendance yesterday?'");
            }
            result.put("actionType", "ATTENDANCE_TODAY");
            result.put("requiresHuman", false);
        } catch (Exception e) {
            logger.error("Error handling today attendance", e);
            return null;
        }
        return result;
    }

    private Map<String, Object> handleAttendanceYesterday(String employeeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            UserMasterAttendanceMod att = findAttendanceForDate(employeeId, yesterday);
            if (att != null) {
                result.put("response", String.format(
                        "Yesterday's Attendance (%s):\n• Status: %s\n• Check In: %s\n• Check Out: %s\n• Total Hours: %s",
                        yesterday.format(dateFormatter),
                        att.getStatus() != null ? att.getStatus() : "Unknown",
                        formatTime(att.getCheckintime()),
                        formatTime(att.getCheckouttime()),
                        att.getTotalhoursworked() != null ? att.getTotalhoursworked() : "N/A"
                ));
                result.put("actionData", att.getStatus());
            } else {
                result.put("response", String.format("No attendance record found for yesterday (%s).", yesterday.format(dateFormatter)));
            }
            result.put("actionType", "ATTENDANCE_YESTERDAY");
            result.put("requiresHuman", false);
        } catch (Exception e) {
            logger.error("Error handling yesterday attendance", e);
            return null;
        }
        return result;
    }

    private UserMasterAttendanceMod findAttendanceForDate(String employeeId, LocalDate targetDate) {
        List<UserMasterAttendanceMod> list = userMasterAttendanceModRepository.findByUserid(employeeId);
        if (list == null) return null;
        return list.stream()
                .filter(a -> a.getAttendancedate() != null)
                .filter(a -> targetDate.equals(toLocalDate(a.getAttendancedate())))
                .findFirst().orElse(null);
    }

    private Map<String, Object> handleTimesheet(String employeeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            LocalDate today = LocalDate.now();
            LocalDate start = today.minusDays(today.getDayOfWeek().getValue() - 1);
            LocalDate end = start.plusDays(4);

            List<UserMasterAttendanceMod> all = userMasterAttendanceModRepository.findByUserid(employeeId);
            if (all == null || all.isEmpty()) {
                result.put("response", "No attendance records found.\n\nAsk: 'How to view timesheet?' for navigation help.");
                result.put("actionType", "TIMESHEET");
                result.put("requiresHuman", false);
                return result;
            }

            List<UserMasterAttendanceMod> week = all.stream()
                    .filter(a -> a.getAttendancedate() != null)
                    .filter(a -> {
                        LocalDate d = toLocalDate(a.getAttendancedate());
                        return d != null && !d.isBefore(start) && !d.isAfter(end);
                    })
                    .sorted((a, b) -> {
                        LocalDate da = toLocalDate(a.getAttendancedate());
                        LocalDate db = toLocalDate(b.getAttendancedate());
                        return (da == null || db == null) ? 0 : da.compareTo(db);
                    })
                    .collect(Collectors.toList());

            if (week.isEmpty()) {
                result.put("response", String.format("No records for this week (%s to %s).", start.format(dateFormatter), end.format(dateFormatter)));
                result.put("actionType", "TIMESHEET");
                result.put("requiresHuman", false);
                return result;
            }

            StringBuilder sb = new StringBuilder(String.format("📅 **Weekly Timesheet (%s to %s)**\n\n", start.format(dateFormatter), end.format(dateFormatter)));
            double total = 0;
            for (UserMasterAttendanceMod a : week) {
                LocalDate d = toLocalDate(a.getAttendancedate());
                if (d == null) continue;
                String hrs = a.getTotalhoursworked() != null ? a.getTotalhoursworked() : "0.0";
                try {
                    total += Double.parseDouble(hrs.replace("h", "").trim());
                } catch (Exception ignored) {
                }
                sb.append(String.format("%s (%s): %s - %s (%s hrs)\n",
                        d.getDayOfWeek().toString().substring(0, 3),
                        d.format(dateFormatter),
                        formatTime(a.getCheckintime()),
                        formatTime(a.getCheckouttime()),
                        hrs));
            }
            sb.append(String.format("\n📊 **Total: %.2f hrs**\n\nGo to: Home → Attendance for full view", total));

            result.put("response", sb.toString());
            result.put("actionType", "TIMESHEET");
            result.put("actionData", String.valueOf(total));
            result.put("requiresHuman", false);
        } catch (Exception e) {
            logger.error("Error handling timesheet", e);
            result.put("response", "Trouble fetching timesheet. Ask: 'How to view timesheet?'");
            result.put("actionType", "TIMESHEET");
            result.put("requiresHuman", true);
        }
        return result;
    }

    private Map<String, Object> handleProfile(String employeeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<EmployeeProfileMod> opt = employeeProfileModRepository.findByEmpid(employeeId);
            if (opt.isPresent()) {
                EmployeeProfileMod emp = opt.get();
                
                String name = emp.getEmployeename();
                String displayName = (name != null && isValidName(name)) ? name : "Employee";
                
                result.put("response", String.format(
                        "👤 **Your Profile**\n\n• Name: %s\n• ID: %s\n• Email: %s\n• Mobile: %s\n• DOB: %s\n• Blood Group: %s\n• Designation: %s\n• Department: %s\n• DOJ: %s\n\n" +
                        "**Want to update?**\n• 'How to update personal details?'\n• 'How to upload ID card photo?'\n• 'How to change password?'",
                        displayName,
                        emp.getEmpid() != null ? emp.getEmpid() : "N/A",
                        emp.getEmailid() != null ? emp.getEmailid() : "N/A",
                        emp.getMobilenumber() != null ? emp.getMobilenumber() : "N/A",
                        formatDate(emp.getDateofbirth(), displayDateFormatter),
                        emp.getBloodgroup() != null ? emp.getBloodgroup() : "N/A",
                        emp.getDesignation() != null ? emp.getDesignation() : "N/A",
                        emp.getDepartment() != null ? emp.getDepartment() : "N/A",
                        formatDate(emp.getDateofjoining(), displayDateFormatter)
                ));
                result.put("actionType", "PROFILE_INFO");
                result.put("requiresHuman", false);
            } else {
                result.put("response", "Profile not found. Please contact HR.");
                result.put("actionType", "PROFILE_INFO");
                result.put("requiresHuman", true);
            }
        } catch (Exception e) {
            logger.error("Error handling profile", e);
            return null;
        }
        return result;
    }

    private Map<String, Object> handleLeaveBalance(String employeeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<EmployeeLeaveSummary> opt = employeeLeaveSummaryRepository.findByEmpIdAndYear(employeeId, LocalDate.now().getYear());
            if (opt.isPresent()) {
                Float bal = opt.get().getCasualLeaveBalance() != null ? opt.get().getCasualLeaveBalance() : 0f;
                result.put("response", String.format("📊 **Leave Balance %d**\n\n• Casual Leave: %.1f days\n\nCheck status: Leave Tracker → Leave Request", LocalDate.now().getYear(), bal));
                result.put("actionData", String.format("%.1f", bal));
                result.put("requiresHuman", false);
            } else {
                result.put("response", "No leave records found. Please contact HR.");
                result.put("requiresHuman", true);
            }
            result.put("actionType", "LEAVE_BALANCE");
        } catch (Exception e) {
            logger.error("Error handling leave balance", e);
            return null;
        }
        return result;
    }

    private Map<String, Object> handleLeaveApplication(String employeeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Optional<EmployeeLeaveSummary> opt = employeeLeaveSummaryRepository.findByEmpIdAndYear(employeeId, LocalDate.now().getYear());
            Float bal = (opt.isPresent() && opt.get().getCasualLeaveBalance() != null) ? opt.get().getCasualLeaveBalance() : 0f;
            result.put("response", String.format("📝 **Leave Application**\n\nYour balance: **%.1f days**\n\nHow to Apply:\n1. Leave Tracker → Apply Leave\n2. Fill: Type, Dates, Reason\n3. Submit\n\nCheck status: Leave Tracker → Leave Request", bal));
            result.put("actionType", "LEAVE_APPLICATION");
            result.put("actionData", String.valueOf(bal));
            result.put("requiresHuman", false);
        } catch (Exception e) {
            result.put("response", "To apply for leave: Leave Tracker → Apply Leave");
            result.put("actionType", "LEAVE_APPLICATION");
            result.put("requiresHuman", false);
        }
        return result;
    }

    private Map<String, Object> handleLeaveHistory(String employeeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EmployeeLeaveMasterTbl> records = employeeLeaveMasterTblRepository.findByEmpid(employeeId);
            if (records != null && !records.isEmpty()) {
                records.sort((a, b) -> (a.getStartdate() == null || b.getStartdate() == null) ? 0 : b.getStartdate().compareTo(a.getStartdate()));
                StringBuilder sb = new StringBuilder("📋 **Recent Leave History**\n\n");
                int limit = Math.min(5, records.size());
                for (int i = 0; i < limit; i++) {
                    EmployeeLeaveMasterTbl lv = records.get(i);
                    sb.append(String.format("%d. %s to %s — %s (%.1f days) [%s]\n",
                            i + 1,
                            lv.getStartdate() != null ? lv.getStartdate().toString().substring(0, 10) : "N/A",
                            lv.getEnddate() != null ? lv.getEnddate().toString().substring(0, 10) : "N/A",
                            lv.getLeavetype() != null ? lv.getLeavetype() : "N/A",
                            lv.getNoofdays() != null ? lv.getNoofdays() : 0f,
                            lv.getStatus() != null ? lv.getStatus() : "N/A"));
                }
                sb.append("\nView all: Leave Tracker → History");
                result.put("response", sb.toString());
            } else {
                result.put("response", "No leave history found.");
            }
            result.put("actionType", "LEAVE_HISTORY");
            result.put("requiresHuman", false);
        } catch (Exception e) {
            logger.error("Error handling leave history", e);
            return null;
        }
        return result;
    }

    private Map<String, Object> handleResignation(String employeeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ExitFormMaster> list = exitFormMasterRepository.findByEmployeeIdAndDelFlag(employeeId, "N");
            Optional<ExitFormMaster> pending = list.stream().filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).findFirst();
            StringBuilder sb = new StringBuilder();
            if (pending.isPresent()) {
                ExitFormMaster r = pending.get();
                sb.append("📝 **You have a pending resignation request**\n\n");
                sb.append(String.format("• Applied on: %s\n• Last Working Day: %s\n• Status: %s\n\n", r.getUserSubmittedOn(), r.getNoticeEndDate(), r.getStatus()));
                sb.append("HR is processing your request. You'll be notified once approved.");
            } else {
                sb.append("📋 **Resignation Process**\n\n1. Home → Resignation\n2. Fill form (LWD, reason)\n3. Submit\n\n");
                sb.append("⚠️ Notice period: 90 days | Unused leave will be encashed");
            }
            result.put("response", sb.toString());
            result.put("actionType", "RESIGNATION_INFO");
            result.put("requiresHuman", false);
        } catch (Exception e) {
            logger.error("Error handling resignation", e);
            result.put("response", "Trouble fetching resignation info. Please contact HR.");
            result.put("actionType", "RESIGNATION_INFO");
            result.put("requiresHuman", true);
        }
        return result;
    }

    private Map<String, Object> handleProjectHistory(String employeeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<EmployeeProjectHistory> projects = employeeProjectHistoryRepository.findByEmpIdInAndEntityCreFlgIn(Arrays.asList(employeeId), Arrays.asList("Y", "N"));
            if (projects == null || projects.isEmpty()) {
                result.put("response", "No project history found.");
                result.put("actionType", "PROJECT_HISTORY");
                result.put("requiresHuman", false);
                return result;
            }
            projects.sort((a, b) -> (a.getProjectStartDate() == null || b.getProjectStartDate() == null) ? 0 : b.getProjectStartDate().compareTo(a.getProjectStartDate()));
            StringBuilder sb = new StringBuilder("📊 **Your Project History**\n\n");
            List<EmployeeProjectHistory> current = projects.stream().filter(p -> p.getProjectEndDate() == null).collect(Collectors.toList());
            if (!current.isEmpty()) {
                sb.append("**Current Projects:**\n");
                current.forEach(p -> sb.append(String.format("• %s (Since %s)\n",
                        p.getProjectName() != null ? p.getProjectName() : "Unnamed",
                        p.getProjectStartDate() != null ? p.getProjectStartDate().format(dateFormatter) : "N/A")));
            }
            List<EmployeeProjectHistory> past = projects.stream().filter(p -> p.getProjectEndDate() != null).limit(5).collect(Collectors.toList());
            if (!past.isEmpty()) {
                sb.append("\n**Recent Past Projects:**\n");
                past.forEach(p -> sb.append(String.format("• %s (%s → %s)\n",
                        p.getProjectName() != null ? p.getProjectName() : "Unnamed",
                        p.getProjectStartDate() != null ? p.getProjectStartDate().format(dateFormatter) : "N/A",
                        p.getProjectEndDate() != null ? p.getProjectEndDate().format(dateFormatter) : "Present")));
            }
            sb.append("\nFull history: Home → Project History");
            result.put("response", sb.toString());
            result.put("actionType", "PROJECT_HISTORY");
            result.put("requiresHuman", false);
        } catch (Exception e) {
            logger.error("Error handling project history", e);
            result.put("response", "Trouble fetching project history. Please try again.");
            result.put("actionType", "PROJECT_HISTORY");
            result.put("requiresHuman", true);
        }
        return result;
    }

    private Map<String, Object> handlePolicy(String lm) {
        String resp;
        if (lm.contains("leave") || lm.contains("vacation")) {
            resp = "📋 **Leave Policy**\n• CL: 18 days | SL: 12 days | EL: 15 days\n• Max at once: 15 days | Carry forward: 30 days\n\nFull policy: Home → Policy → Leave Policy";
        } else if (lm.contains("attendance") || lm.contains("timesheet")) {
            resp = "⏰ **Attendance Policy**\n• Hours: 9 AM–6 PM | Grace: 15 min | Half-day: <4 hrs\n• Timesheet due: Friday 5 PM\n\nHome → Attendance";
        } else if (lm.contains("resign") || lm.contains("exit")) {
            resp = "📝 **Resignation Policy**\n• Notice: 30 days | F&F: within 15 days\n\nHome → Resignation";
        } else {
            resp = "📚 **Company Policies**\nAvailable: Leave | Attendance | Resignation | WFH | Travel | Reimbursement\n\nAsk: 'Leave policy' or 'Attendance policy'";
        }
        Map<String, Object> result = new HashMap<>();
        result.put("response", resp);
        result.put("actionType", "POLICY_INFO");
        result.put("requiresHuman", false);
        return result;
    }

    // ==================== GEMINI ====================

    private boolean isGeminiConfigured() {
        return geminiApiKey != null && !geminiApiKey.equals("YOUR_API_KEY") && !geminiApiKey.isEmpty();
    }

    private String callGeminiAPI(String message) {
        try {
            String escaped = message.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
            String body = "{\"contents\":[{\"parts\":[{\"text\":\"" + escaped + "\"}]}],\"generationConfig\":{\"temperature\":1.0,\"maxOutputTokens\":800,\"topP\":0.95,\"topK\":64}}";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> resp = new RestTemplate().exchange(geminiApiUrl + "?key=" + geminiApiKey, HttpMethod.POST, new HttpEntity<>(body, headers), String.class);
            return objectMapper.readTree(resp.getBody()).path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
        } catch (Exception e) {
            logger.error("Gemini API error: {}", e.getMessage());
            return null;
        }
    }

    private boolean isHrQuestion(String message) {
        if (message == null) return false;
        String lower = message.toLowerCase();
        return hrKeywords.stream().anyMatch(lower::contains);
    }

    private boolean isGenericResponse(String response) {
        if (response == null) return true;
        String lower = response.toLowerCase();
        return genericResponses.stream().anyMatch(lower::contains);
    }

    // ==================== PUBLIC API METHODS (ALL DATABASE OPERATIONS COMMENTED OUT) ====================

    public List<ChatMessage> getUserHistory(String userId, int limit) {
        try {
            // DATABASE OPERATION COMMENTED OUT
            // return chatMessageRepository.findRecentByUserId(userId, limit);
            
            logger.info("getUserHistory called for userId: {} (returning empty list - database disabled)", userId);
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error getting user history", e);
            return new ArrayList<>();
        }
    }

    public List<ChatMessage> getSessionHistory(String sessionId) {
        try {
            // DATABASE OPERATION COMMENTED OUT
            // return chatMessageRepository.findBySessionIdOrderByTimestampAsc(sessionId);
            
            logger.info("getSessionHistory called for sessionId: {} (returning empty list - database disabled)", sessionId);
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Error getting session history", e);
            return new ArrayList<>();
        }
    }

    @Transactional
    public void addFeedback(String messageId, int rating, String comment) {
        try {
            // DATABASE OPERATIONS COMMENTED OUT
            /*
            Optional<ChatMessage> opt = chatMessageRepository.findById(messageId);
            if (opt.isPresent()) {
                ChatMessage msg = opt.get();
                msg.setFeedbackRating(rating);
                msg.setFeedbackComment(comment);
                msg.setModifiedBy(msg.getUserId());
                msg.setModifiedDate(LocalDateTime.now());
                chatMessageRepository.save(msg);
            }
            */
            logger.info("Feedback received for message: {} with rating: {} (not saved to database)", messageId, rating);
        } catch (Exception e) {
            logger.error("Error adding feedback", e);
        }
    }

    public List<ChatMessage> getQueriesNeedingHuman() {
        try {
            // DATABASE OPERATION COMMENTED OUT
            // return chatMessageRepository.findByRequiresHumanTrueOrderByTimestampDesc();
            
            logger.info("getQueriesNeedingHuman called (returning empty list - database disabled)");
            return new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Map<String, Object> getChatStatistics() {
        Map<String, Object> stats = new HashMap<>();
        try {
            // DATABASE OPERATIONS COMMENTED OUT
            // stats.put("totalMessages", chatMessageRepository.count());
            // stats.put("pendingHuman", chatMessageRepository.findByRequiresHumanTrueOrderByTimestampDesc().size());
            
            stats.put("totalMessages", 0);
            stats.put("pendingHuman", 0);
            stats.put("geminiStatus", getGeminiApiKeyStatus());
            stats.put("status", "available (database disabled)");
            
            stats.put("intentStats", new ArrayList<>());
            stats.put("averageRating", 0.0);
            stats.put("totalUsers", 0);
            
            stats.put("navigationGuidesAvailable", Arrays.asList(
                "Timesheet Navigation",
                "Leave Request Navigation",
                "Leave Balance Navigation",
                "Project History Navigation",
                "Profile Navigation (Enhanced)",
                "Personal Details Update Guide",
                "ID Card Upload Guide",
                "Resignation Navigation",
                "Policy Navigation"
            ));
            
        } catch (Exception e) {
            logger.error("Error getting chat statistics", e);
            stats.put("status", "error");
            stats.put("message", e.getMessage());
        }
        return stats;
    }

    public EmployeeProfileMod getEmployeeProfile(String employeeId) {
        try {
            return employeeProfileModRepository.findByEmpid(employeeId).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public List<UserMasterAttendanceMod> getEmployeeAttendanceForMonth(String employeeId, int month, int year) {
        try {
            if (employeeId == null || employeeId.isEmpty()) return new ArrayList<>();
            List<UserMasterAttendanceMod> all = userMasterAttendanceModRepository.findByUserid(employeeId);
            if (all == null) return new ArrayList<>();
            return all.stream()
                    .filter(a -> a.getAttendancedate() != null)
                    .filter(a -> {
                        LocalDate d = toLocalDate(a.getAttendancedate());
                        return d != null && d.getMonthValue() == month && d.getYear() == year;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<UserMasterAttendanceMod> getEmployeeTimesheet(String employeeId) {
        try {
            if (employeeId == null || employeeId.isEmpty()) return new ArrayList<>();
            LocalDate today = LocalDate.now();
            LocalDate start = today.minusDays(today.getDayOfWeek().getValue() - 1);
            LocalDate end = start.plusDays(6);
            List<UserMasterAttendanceMod> all = userMasterAttendanceModRepository.findByUserid(employeeId);
            if (all == null) return new ArrayList<>();
            return all.stream()
                    .filter(a -> a.getAttendancedate() != null)
                    .filter(a -> {
                        LocalDate d = toLocalDate(a.getAttendancedate());
                        return d != null && !d.isBefore(start) && !d.isAfter(end);
                    })
                    .sorted((a, b) -> {
                        LocalDate da = toLocalDate(a.getAttendancedate());
                        LocalDate db = toLocalDate(b.getAttendancedate());
                        return (da == null || db == null) ? 0 : da.compareTo(db);
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<EmployeeLeaveMasterTbl> getEmployeeLeaveRequests(String employeeId, String status) {
        try {
            if (employeeId == null || employeeId.isEmpty()) return new ArrayList<>();
            List<EmployeeLeaveMasterTbl> list = employeeLeaveMasterTblRepository.findByEmpid(employeeId);
            if (list == null) return new ArrayList<>();
            list.sort((a, b) -> (a.getStartdate() == null || b.getStartdate() == null) ? 0 : b.getStartdate().compareTo(a.getStartdate()));
            if (status != null && !status.isEmpty()) {
                return list.stream().filter(r -> status.equalsIgnoreCase(r.getStatus())).collect(Collectors.toList());
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Transactional
    public Map<String, Object> withdrawLeaveRequest(String leaveId, String employeeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // DATABASE OPERATIONS COMMENTED OUT
            /*
            Optional<EmployeeLeaveMasterTbl> opt = employeeLeaveMasterTblRepository.findById(Long.parseLong(leaveId));
            if (!opt.isPresent()) {
                result.put("success", false);
                result.put("message", "Leave request not found");
                return result;
            }
            EmployeeLeaveMasterTbl leave = opt.get();
            if (!employeeId.equals(leave.getEmpid())) {
                result.put("success", false);
                result.put("message", "Unauthorized");
                return result;
            }
            if (!"PENDING".equalsIgnoreCase(leave.getStatus())) {
                result.put("success", false);
                result.put("message", "Only pending requests can be withdrawn");
                return result;
            }
            if (leave.getStartdate() != null) {
                LocalDate sd = toLocalDate(leave.getStartdate());
                if (sd != null && sd.isBefore(LocalDate.now())) {
                    result.put("success", false);
                    result.put("message", "Cannot withdraw past leave");
                    return result;
                }
            }
            leave.setStatus("WITHDRAWN");
            employeeLeaveMasterTblRepository.save(leave);
            */
            
            result.put("success", true);
            result.put("message", "Leave request withdrawn successfully (mock - database disabled)");
            result.put("status", "WITHDRAWN");
        } catch (NumberFormatException e) {
            result.put("success", false);
            result.put("message", "Invalid leave request ID");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
        }
        return result;
    }

    public String getGeminiApiKeyStatus() {
        return (geminiApiKey == null || geminiApiKey.equals("YOUR_API_KEY") || geminiApiKey.isEmpty()) ? "Not Configured" : "Configured";
    }

    // ==================== PRIVATE UTILITY ====================

    private ChatMessage createChatMessage(String userId, String employeeId, String userMessage) {
        // DATABASE OPERATION COMMENTED OUT - RETURN NULL
        /*
        ChatMessage m = new ChatMessage();
        m.setUserId(userId);
        m.setUserMessage(userMessage);
        m.setEmployeeId(employeeId);
        m.setSessionId(generateSessionId());
        m.setIpAddress(getClientIp());
        m.setUserAgent(getUserAgent());
        m.setCreatedBy(userId);
        m.setCreatedDate(LocalDateTime.now());
        m.setRecordStatus("A");
        return m;
        */
        logger.info("Chat message creation skipped (database disabled)");
        return null;
    }

    private void handleError(ChatMessage chatMessage, String userId, Map<String, Object> response) {
        // DATABASE OPERATIONS COMMENTED OUT
        /*
        chatMessage.setAssistantResponse("I'm having trouble processing your request. Please try again later.");
        chatMessage.setRequiresHuman(true);
        chatMessage.setMessageType(ChatMessage.MessageType.ERROR);
        chatMessage.setModifiedBy(userId);
        chatMessage.setModifiedDate(LocalDateTime.now());
        chatMessageRepository.save(chatMessage);
        */
        
        response.put("success", false);
        response.put("message", "I'm having trouble processing your request. Please try again later.");
        response.put("requiresHuman", true);
        response.put("messageId", "error-" + System.currentTimeMillis());
    }

    private double calculateConfidence(String intent, String message) {
        if (intent.startsWith("navigation_")) return 0.95;
        if (intent.equals("personal_details") || intent.equals("id_card") || intent.equals("contact_details") || intent.equals("password_change")) {
            return 0.92;
        }
        if (intent.equals("general")) return 0.5 + random.nextDouble() * 0.3;
        if (intent.contains("_")) return 0.8 + random.nextDouble() * 0.2;
        return 0.6 + random.nextDouble() * 0.3;
    }

    private String generateSessionId() {
        return "SESS-" + UUID.randomUUID().toString();
    }

    private String getClientIp() {
        if (request == null) return "unknown";
        String ip = request.getHeader("X-Forwarded-For");
        return (ip == null || ip.isEmpty()) ? request.getRemoteAddr() : ip;
    }

    private String getUserAgent() {
        return request != null ? request.getHeader("User-Agent") : "unknown";
    }
}