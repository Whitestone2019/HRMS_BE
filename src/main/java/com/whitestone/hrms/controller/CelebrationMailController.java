package com.whitestone.hrms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSendException;

import com.whitestone.hrms.service.EmailService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;
import javax.mail.MessagingException;

@RestController
@RequestMapping("/api/celebration")
@CrossOrigin(origins = "*")
@EnableAsync
public class CelebrationMailController {

    @Autowired
    private EmailService emailService;
    
    @Value("${company.name:Whitestone Solutions}")
    private String companyName;
    
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    
    // Optimized thread pool for maximum throughput
    private final ExecutorService executorService = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors() * 2
    );
    
    // Pre-compiled email templates (loaded once at startup)
    private final Map<String, String> emailTemplates = new ConcurrentHashMap<>();
    
    // Pre-formatted dates and times
    private String currentDate;
    private String currentTime;
    
    public CelebrationMailController() {
        // Initialize templates at startup
        initializeTemplates();
        // Start date updater thread
        startDateUpdater();
    }
    
    private void initializeTemplates() {
        // Pre-compile all templates once at startup
        emailTemplates.put("birthday", buildBirthdayTemplate());
        emailTemplates.put("anniversary", buildAnniversaryTemplate());
        emailTemplates.put("both", buildBothTemplate());
    }
    
    private void startDateUpdater() {
        // Update date every minute instead of every request
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            currentDate = LocalDate.now().format(dateFormatter);
            currentTime = LocalTime.now().toString();
        }, 0, 1, TimeUnit.MINUTES);
    }
    
    // Get or update date efficiently
    private String getCurrentDate() {
        return currentDate != null ? currentDate : LocalDate.now().format(dateFormatter);
    }
    
    private String getCurrentTime() {
        return currentTime != null ? currentTime : LocalTime.now().toString();
    }

    @PostMapping("/send-bulk-emails")
    public ResponseEntity<?> sendBulkCelebrationEmails(@RequestBody BulkCelebrationEmailRequest request) {
        long startTime = System.nanoTime();
        
        // Quick validation
        if (request == null || request.getCelebrations() == null || request.getCelebrations().isEmpty()) {
            return ResponseEntity.badRequest().body(createErrorResponse("No celebrations data provided"));
        }
        
        if (request.getSenderEmail() == null || request.getSenderEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(createErrorResponse("Sender email is required"));
        }
        
        // Return immediate response while processing in background
        CompletableFuture.runAsync(() -> processBulkEmails(request), executorService);
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        
        // Immediate response to client (under 50ms)
        Map<String, Object> response = createSuccessResponse("Bulk email processing started");
        response.put("totalCount", request.getCelebrations().size());
        response.put("processingTimeMs", durationMs);
        response.put("message", "Emails are being sent in the background");
        
        return ResponseEntity.ok(response);
    }
    
    private void processBulkEmails(BulkCelebrationEmailRequest request) {
        System.out.println("\n🎉 === BACKGROUND BULK EMAIL PROCESSING ===");
        System.out.println("Processing " + request.getCelebrations().size() + " emails");
        
        // Use parallel stream for maximum speed
        request.getCelebrations().parallelStream().forEach(singleRequest -> {
            try {
                singleRequest.setSenderEmail(request.getSenderEmail());
                singleRequest.setSenderName(request.getSenderName());
                
                String emailType = singleRequest.getType().toLowerCase();
                
                switch (emailType) {
                    case "birthday":
                        sendBirthdayEmailFast(singleRequest);
                        break;
                    case "anniversary":
                        sendAnniversaryEmailFast(singleRequest);
                        break;
                    case "both":
                        sendBothCelebrationEmailFast(singleRequest);
                        break;
                    default:
                        System.err.println("❌ Invalid type: " + emailType);
                }
            } catch (Exception e) {
                System.err.println("❌ Error sending to " + singleRequest.getEmployeeName() + ": " + e.getMessage());
            }
        });
        
        System.out.println("✅ Background processing complete");
    }

    @PostMapping("/send-email")
    public ResponseEntity<?> sendCelebrationEmail(@RequestBody CelebrationEmailRequest request) {
        long startTime = System.nanoTime();
        
        // Quick validation
        if (request == null || !isValidRequest(request)) {
            return ResponseEntity.badRequest().body(createErrorResponse("Invalid request data"));
        }
        
        // Process asynchronously
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            try {
                String emailType = request.getType().toLowerCase();
                
                switch (emailType) {
                    case "birthday":
                        return sendBirthdayEmailFast(request);
                    case "anniversary":
                        return sendAnniversaryEmailFast(request);
                    case "both":
                        return sendBothCelebrationEmailFast(request);
                    default:
                        return false;
                }
            } catch (Exception e) {
                System.err.println("❌ Email sending exception: " + e.getMessage());
                return false;
            }
        }, executorService);
        
        long endTime = System.nanoTime();
        long durationMs = (endTime - startTime) / 1_000_000;
        
        // Return response immediately (don't wait for email to send)
        Map<String, Object> response = createSuccessResponse("Email queued for sending");
        response.put("from", request.getSenderEmail());
        response.put("to", request.getEmployeeEmail());
        response.put("type", request.getType());
        response.put("processingTimeMs", durationMs);
        response.put("message", "Email is being sent in the background");
        
        return ResponseEntity.ok(response);
    }
    
    // ==================== FIXED: ULTRA-FAST EMAIL METHODS WITH EMPLOYEE EMAIL ====================
    
    private boolean sendBirthdayEmailFast(CelebrationEmailRequest request) {
        try {
            String senderName = capitalizeFast(request.getSenderName());
            String employeeName = capitalizeFast(request.getEmployeeName());
            
            String subject = "🎂 " + senderName + " wishes you Happy Birthday!";
            String currentDate = getCurrentDate();
            
            // FIXED: Added {EMPLOYEE_EMAIL} and {DATE} replacements
            String htmlContent = emailTemplates.get("birthday")
                .replace("{EMPLOYEE_NAME}", request.getEmployeeName())
                .replace("{EMPLOYEE_EMAIL}", request.getEmployeeEmail())  // ADDED THIS LINE
                .replace("{SENDER_NAME}", senderName)
                .replace("{SENDER_EMAIL}", request.getSenderEmail())
                .replace("{DATE}", currentDate)  // ADDED THIS LINE
                .replace("{COMPANY_NAME}", companyName)
                .replace("{YEAR}", String.valueOf(LocalDate.now().getYear()));
            
            emailService.sendCelebrationEmail(
                request.getSenderEmail(),
                senderName + " via " + companyName,
                request.getEmployeeEmail(),
                subject,
                htmlContent
            );
            
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error sending birthday email: " + e.getMessage());
            return false;
        }
    }

    private boolean sendAnniversaryEmailFast(CelebrationEmailRequest request) {
        try {
            int years = request.getYears() != null ? request.getYears() : 1;
            String yearsText = years == 1 ? "1 year" : years + " years";
            
            String senderName = capitalizeFast(request.getSenderName());
            String employeeName = capitalizeFast(request.getEmployeeName());
            String currentDate = getCurrentDate();
            
            String subject = "🏆 " + senderName + " congratulates you on your " + yearsText + " Work Anniversary!";
            
            // FIXED: Added {EMPLOYEE_EMAIL} and {DATE} replacements
            String htmlContent = emailTemplates.get("anniversary")
                .replace("{EMPLOYEE_NAME}", request.getEmployeeName())
                .replace("{EMPLOYEE_EMAIL}", request.getEmployeeEmail())  // ADDED THIS LINE
                .replace("{SENDER_NAME}", senderName)
                .replace("{SENDER_EMAIL}", request.getSenderEmail())
                .replace("{DATE}", currentDate)  // ADDED THIS LINE
                .replace("{YEARS}", String.valueOf(years))
                .replace("{YEARS_TEXT}", yearsText)
                .replace("{YEAR_PLURAL}", years == 1 ? "year" : "years")
                .replace("{COMPANY_NAME}", companyName)
                .replace("{YEAR}", String.valueOf(LocalDate.now().getYear()));
            
            emailService.sendCelebrationEmail(
                request.getSenderEmail(),
                senderName + " via " + companyName,
                request.getEmployeeEmail(),
                subject,
                htmlContent
            );
            
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error sending anniversary email: " + e.getMessage());
            return false;
        }
    }

    private boolean sendBothCelebrationEmailFast(CelebrationEmailRequest request) {
        try {
            int anniversaryYears = request.getAnniversaryYears() != null ? request.getAnniversaryYears() : 
                                  (request.getYears() != null ? request.getYears() : 1);
            
            String anniversaryYearsText = anniversaryYears + " " + (anniversaryYears == 1 ? "year" : "years");
            
            String senderName = capitalizeFast(request.getSenderName());
            String employeeName = capitalizeFast(request.getEmployeeName());
            String currentDate = getCurrentDate();
            
            String subject = "🎂🎉 " + senderName + " wishes you Happy Birthday & " + anniversaryYearsText + " Work Anniversary!";
            
            // FIXED: Added {EMPLOYEE_EMAIL} and {DATE} replacements
            String htmlContent = emailTemplates.get("both")
                .replace("{EMPLOYEE_NAME}", request.getEmployeeName())
                .replace("{EMPLOYEE_EMAIL}", request.getEmployeeEmail())  // ADDED THIS LINE
                .replace("{SENDER_NAME}", senderName)
                .replace("{SENDER_EMAIL}", request.getSenderEmail())
                .replace("{DATE}", currentDate)  // ADDED THIS LINE
                .replace("{YEARS}", String.valueOf(anniversaryYears))
                .replace("{YEARS_TEXT}", anniversaryYearsText)
                .replace("{YEAR_PLURAL}", anniversaryYears == 1 ? "year" : "years")
                .replace("{COMPANY_NAME}", companyName)
                .replace("{YEAR}", String.valueOf(LocalDate.now().getYear()));
            
            emailService.sendCelebrationEmail(
                request.getSenderEmail(),
                senderName + " via " + companyName,
                request.getEmployeeEmail(),
                subject,
                htmlContent
            );
            
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error sending both celebration email: " + e.getMessage());
            return false;
        }
    }

    // ==================== PRE-COMPILED TEMPLATES (LOADED ONCE) ====================
    
    private String buildBirthdayTemplate() {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Birthday Wishes</title>"
            + "<style>body{font-family:Arial,sans-serif;background:#f7fafc;padding:20px;}</style>"
            + "</head><body>"
            + "<div style='max-width:550px;margin:0 auto;background:white;border-radius:12px;padding:20px;'>"
            + "<div style='background:#fff5f5;padding:20px;text-align:center;border-radius:8px;'>"
            + "<h1 style='color:#9b2c2c;margin:0;'>🎂 Happy Birthday! 🎉</h1>"
            + "</div>"
            + "<div style='padding:20px;'>"
            + "<h2 style='color:#2d3748;'>Dear {EMPLOYEE_NAME},</h2>"
            + "<div style='background:#fafbfc;padding:20px;border-left:4px solid #fc8181;'>"
            + "<p>I hope this email finds you well. On this special day, I wanted to take a moment to wish you a very <span style='color:#c53030;font-weight:bold;'>Happy Birthday!</span> 🎈</p>"
            + "<p>May your day be filled with joy, laughter, and wonderful moments with your loved ones.</p>"
            + "<p>Wishing you success, happiness, and all the best in the coming year!</p>"
            + "</div>"
            + "<div style='background:#f8fafc;padding:15px;margin-top:20px;text-align:center;border:1px solid #edf2f7;border-radius:8px;'>"
            + "<h3 style='color:#718096;margin:0 0 10px 0;font-size:14px;'>Warm wishes from:</h3>"
            + "<p style='margin:5px 0;'><strong>{SENDER_NAME}</strong></p>"
            + "<p style='margin:5px 0;color:#718096;'>{SENDER_EMAIL}</p>"
            + "<p style='margin:5px 0;color:#a0aec0;font-size:12px;'>{DATE} • {COMPANY_NAME} HRMS</p>"
            + "</div></div>"
            + "<div style='text-align:center;padding:15px;background:#fafbfc;border-top:1px solid #edf2f7;font-size:12px;color:#a0aec0;'>"
            + "<p style='margin:0;'>Sent via {COMPANY_NAME} HRMS</p>"
            + "<p style='margin:5px 0 0 0;'>To: {EMPLOYEE_NAME} • {EMPLOYEE_EMAIL}</p>"
            + "<p style='margin:5px 0 0 0;'>© {YEAR} {COMPANY_NAME}</p>"
            + "</div></div></body></html>";
    }

    private String buildAnniversaryTemplate() {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Work Anniversary</title>"
            + "<style>body{font-family:Arial,sans-serif;background:#f7fafc;padding:20px;}</style>"
            + "</head><body>"
            + "<div style='max-width:550px;margin:0 auto;background:white;border-radius:12px;padding:20px;'>"
            + "<div style='background:#e6fffa;padding:20px;text-align:center;border-radius:8px;'>"
            + "<h1 style='color:#234e52;margin:0;'>🏆 Work Anniversary</h1>"
            + "</div>"
            + "<div style='padding:20px;'>"
            + "<h2 style='color:#2d3748;'>Dear {EMPLOYEE_NAME},</h2>"
            + "<div style='background:#fafbfc;padding:20px;border-left:4px solid #4fd1c5;'>"
            + "<p>I hope this email finds you well. On this special day, I wanted to take a moment to congratulate you on your <span style='color:#319795;font-weight:bold;'>{YEARS} Year Work Anniversary!</span></p>"
            + "<p>Your dedication, hard work, and valuable contributions over the past <span style='color:#319795;font-weight:bold;'>{YEARS} {YEAR_PLURAL}</span> are truly appreciated.</p>"
            + "<p>Wishing you continued success, growth, and many more years of achievements!</p>"
            + "</div>"
            + "<div style='background:#f8fafc;padding:15px;margin-top:20px;text-align:center;border:1px solid #edf2f7;border-radius:8px;'>"
            + "<h3 style='color:#718096;margin:0 0 10px 0;font-size:14px;'>Congratulations from:</h3>"
            + "<p style='margin:5px 0;'><strong>{SENDER_NAME}</strong></p>"
            + "<p style='margin:5px 0;color:#718096;'>{SENDER_EMAIL}</p>"
            + "<p style='margin:5px 0;color:#a0aec0;font-size:12px;'>{DATE} • {COMPANY_NAME} HRMS</p>"
            + "</div></div>"
            + "<div style='text-align:center;padding:15px;background:#fafbfc;border-top:1px solid #edf2f7;font-size:12px;color:#a0aec0;'>"
            + "<p style='margin:0;'>Sent via {COMPANY_NAME} HRMS</p>"
            + "<p style='margin:5px 0 0 0;'>To: {EMPLOYEE_NAME} • {EMPLOYEE_EMAIL}</p>"
            + "<p style='margin:5px 0 0 0;'>© {YEAR} {COMPANY_NAME}</p>"
            + "</div></div></body></html>";
    }

    private String buildBothTemplate() {
        return "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Birthday & Work Anniversary</title>"
            + "<style>body{font-family:Arial,sans-serif;background:#f7fafc;padding:20px;}</style>"
            + "</head><body>"
            + "<div style='max-width:550px;margin:0 auto;background:white;border-radius:12px;padding:20px;'>"
            + "<div style='background:linear-gradient(135deg,#fff5f5,#e6fffa);padding:20px;text-align:center;border-radius:8px;'>"
            + "<h1 style='color:#234e52;margin:0;font-size:24px;'>🎂 Happy Birthday! & 🏆 Work Anniversary!</h1>"
            + "</div>"
            + "<div style='padding:20px;'>"
            + "<h2 style='color:#2d3748;'>Dear {EMPLOYEE_NAME},</h2>"
            + "<div style='background:#fafbfc;padding:20px;border-left:4px solid #fc8181;border-right:4px solid #4fd1c5;'>"
            + "<p>🎂 I hope this email finds you well. On this special day, I wanted to take a moment to wish you a very <span style='color:#c53030;font-weight:bold;'>Happy Birthday!</span> 🎈</p>"
            + "<p>🏆 And also congratulate you on your <span style='color:#319795;font-weight:bold;'>{YEARS} Year Work Anniversary!</span></p>"
            + "<p>Your dedication and contributions over the past {YEARS} {YEAR_PLURAL} are truly appreciated.</p>"
            + "<p>Wishing you continued success, happiness, and many more milestones!</p>"
            + "</div>"
            + "<div style='background:#f8fafc;padding:15px;margin-top:20px;text-align:center;border:1px solid #edf2f7;border-radius:8px;'>"
            + "<h3 style='color:#718096;margin:0 0 10px 0;font-size:14px;'>Warm wishes & Congratulations from:</h3>"
            + "<p style='margin:5px 0;'><strong>{SENDER_NAME}</strong></p>"
            + "<p style='margin:5px 0;color:#718096;'>{SENDER_EMAIL}</p>"
            + "<p style='margin:5px 0;color:#a0aec0;font-size:12px;'>{DATE} • {COMPANY_NAME} HRMS</p>"
            + "</div></div>"
            + "<div style='text-align:center;padding:15px;background:#fafbfc;border-top:1px solid #edf2f7;font-size:12px;color:#a0aec0;'>"
            + "<p style='margin:0;'>Sent via {COMPANY_NAME} HRMS</p>"
            + "<p style='margin:5px 0 0 0;'>To: {EMPLOYEE_NAME} • {EMPLOYEE_EMAIL}</p>"
            + "<p style='margin:5px 0 0 0;'>© {YEAR} {COMPANY_NAME}</p>"
            + "</div></div></body></html>";
    }

    // ==================== ULTRA-FAST UTILITY METHODS ====================
    
    private String capitalizeFast(String text) {
        if (text == null || text.isEmpty()) return "Colleague";
        
        // Single pass capitalization (much faster than split)
        char[] chars = text.toCharArray();
        boolean capitalize = true;
        
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                capitalize = true;
            } else if (capitalize) {
                chars[i] = Character.toUpperCase(chars[i]);
                capitalize = false;
            } else {
                chars[i] = Character.toLowerCase(chars[i]);
            }
        }
        
        return new String(chars);
    }

    private boolean isValidRequest(CelebrationEmailRequest request) {
        return request != null &&
               request.getEmployeeEmail() != null && !request.getEmployeeEmail().isEmpty() &&
               request.getSenderEmail() != null && !request.getSenderEmail().isEmpty() &&
               request.getEmployeeName() != null && !request.getEmployeeName().isEmpty() &&
               request.getSenderName() != null && !request.getSenderName().isEmpty() &&
               request.getType() != null && !request.getType().isEmpty();
    }

    private Map<String, Object> createSuccessResponse(String message) {
        Map<String, Object> response = new HashMap<>(8);
        response.put("status", "success");
        response.put("message", message);
        response.put("timestamp", getCurrentDate());
        response.put("time", getCurrentTime());
        return response;
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>(8);
        response.put("status", "error");
        response.put("message", message);
        response.put("timestamp", getCurrentDate());
        response.put("time", getCurrentTime());
        return response;
    }

    @PreDestroy
    public void cleanup() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    // ==================== DTO CLASSES ====================

    public static class BulkCelebrationEmailRequest {
        private String senderEmail;
        private String senderName;
        private List<CelebrationEmailRequest> celebrations;
        
        public String getSenderEmail() { return senderEmail; }
        public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }
        public String getSenderName() { return senderName; }
        public void setSenderName(String senderName) { this.senderName = senderName; }
        public List<CelebrationEmailRequest> getCelebrations() { return celebrations; }
        public void setCelebrations(List<CelebrationEmailRequest> celebrations) { this.celebrations = celebrations; }
    }

    public static class CelebrationEmailRequest {
        private String employeeEmail;
        private String employeeName;
        private String employeeId;
        private String senderEmail;
        private String senderName;
        private String type;
        private Integer years;
        private Integer anniversaryYears;
        
        public String getEmployeeEmail() { return employeeEmail; }
        public void setEmployeeEmail(String employeeEmail) { this.employeeEmail = employeeEmail; }
        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
        public String getEmployeeId() { return employeeId; }
        public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
        public String getSenderEmail() { return senderEmail; }
        public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }
        public String getSenderName() { return senderName; }
        public void setSenderName(String senderName) { this.senderName = senderName; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Integer getYears() { return years; }
        public void setYears(Integer years) { this.years = years; }
        public Integer getAnniversaryYears() { return anniversaryYears; }
        public void setAnniversaryYears(Integer anniversaryYears) { this.anniversaryYears = anniversaryYears; }
    }
}