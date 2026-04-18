package com.whitestone.hrms.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.whitestone.entity.ExitFormMaster;
import com.whitestone.entity.ExpenseDetailsMod;
import com.whitestone.entity.PayrollAdjustment;
import com.whitestone.entity.usermaintenance;
import com.whitestone.hrms.repo.ExpenseDetailsRepository;
import com.whitestone.hrms.repo.usermaintenanceRepository;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private usermaintenanceRepository usermaintenanceRepository;

    @Autowired
    private ExpenseDetailsRepository expenseDetailsRepository;
    
    @Value("${spring.mail.username:career@whitestones.co.in}")
    private String defaultFromEmail;

    // Email addresses fetched from application.properties
    @Value("${email.sridharan:sridharan.ramar@whitestones.in}")
    private String sridharanEmail;
    
    @Value("${email.hr:hr@whitestones.in}")
    private String hrEmail;
    
    @Value("${email.accounts:accounts@whitestones.in}")
    private String accountsEmail;
    
    @Value("${email.systemadmin:systemadmin@whitestones.in}")
    private String systemAdminEmail;
    
    @Value("${email.payroll:payroll@whitestones.co.in}")
    private String payrollEmail;
    
    @Value("${email.career:career@whitestones.co.in}")
    private String careerEmail;
    
    @Value("${email.no-reply:no-reply@whitestones.in}")
    private String noReplyEmail;

    // ==================== ENUM FOR EXIT PROCESS STEPS ====================
    
    public enum ExitStep {
        CREATION,
        MANAGER_REVIEW,
        HR_ROUND1,
        ASSET_CLEARANCE,
        HR_ROUND2,
        PAYROLL_CLEARANCE,
        FINAL_HR,
        WITHDRAWAL
    }

    // ==================== BASIC EMAIL METHODS ====================

    public void sendVerificationEmail(String toEmail, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body);
            helper.setFrom("career@whitestones.co.in");
            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            e.printStackTrace();
        }
    }
    
    public void sendPayslipEmail(String email, ByteArrayResource payslip, String month) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("payroll@whitestones.co.in");
            helper.setTo(email);
            helper.setSubject("Payslip for " + month);
            helper.setText("Dear Employee, \n\nPlease find attached your payslip for " + month + ".");
            helper.addAttachment("Payslip_" + month + ".pdf", payslip);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send payslip email", e);
        }
    }
    
    // FIXED: sendLeaveEmail with duplicate protection
    public void sendLeaveEmail(String From, String email, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(From);
            
            // Use Set to avoid duplicate recipients
            Set<String> allRecipients = new HashSet<>();
            allRecipients.add(email);
            allRecipients.add("hr@whitestones.in");
            
            // Remove null or empty
            allRecipients.removeIf(e -> e == null || e.trim().isEmpty());
            
            // Set recipients (no separate CC to avoid duplicates)
            if (!allRecipients.isEmpty()) {
                helper.setTo(allRecipients.toArray(new String[0]));
            }
            
            helper.setSubject(subject);
            helper.setText(body);
            mailSender.send(message);
            
            System.out.println("✅ Leave email sent to: " + String.join(", ", allRecipients));
            
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send leave email", e);
        }
    }
    
    public void sendCelebrationEmail(String fromEmail, String fromName, String toEmail, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(new InternetAddress(fromEmail, fromName));
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            
            mailSender.send(message);
            System.out.println("🎉 Celebration email sent successfully!");
            System.out.println("From: " + fromEmail + " (" + fromName + ")");
            System.out.println("To: " + toEmail);
            System.out.println("Subject: " + subject);
        } catch (Exception e) {
            System.err.println("❌ Failed to send celebration email: " + e.getMessage());
            throw new RuntimeException("Failed to send celebration email", e);
        }
    }
    
    // FIXED: sendAdvanceEmail with duplicate protection
    public void sendAdvanceEmail(String From, String email, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(From);
            
            // Use Set to avoid duplicate recipients
            Set<String> allRecipients = new HashSet<>();
            allRecipients.add(email);
            allRecipients.add("accounts@whitestones.in");
            
            // Remove null or empty
            allRecipients.removeIf(e -> e == null || e.trim().isEmpty());
            
            // Set recipients (no separate CC to avoid duplicates)
            if (!allRecipients.isEmpty()) {
                helper.setTo(allRecipients.toArray(new String[0]));
            }
            
            helper.setSubject(subject);
            helper.setText(body);
            mailSender.send(message);
            
            System.out.println("✅ Advance email sent to: " + String.join(", ", allRecipients));
            
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send advance email", e);
        }
    }
    
    public void sendEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            helper.setFrom("no-reply@whitestones.in");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, false);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    // ==================== EXIT PROCESS EMAIL METHODS ====================

    /**
     * Unified method for ALL exit process email notifications
     */
    public void sendExitProcessEmail(ExitFormMaster exitForm, 
                                     usermaintenance employee,
                                     usermaintenance manager,
                                     ExitStep step,
                                     String action,
                                     String remarks) {
        try {
            // Log warning if manager and HR are same person
            String managerEmail = manager != null ? getEmployeeEmail(manager) : null;
            if (managerEmail != null && managerEmail.equalsIgnoreCase(hrEmail)) {
                System.out.println("⚠️ Warning: Manager email (" + managerEmail + 
                                 ") matches HR email. Duplicates will be prevented automatically.");
            }
            
            // 1. Determine recipients based on step
            String[] recipients = getRecipientsForStep(step, employee, manager);
            
            if (recipients.length == 0) {
                System.out.println("No recipients for " + step + " email");
                return;
            }
            
            // 2. Build subject and body
            EmailContent content = buildEmailContent(exitForm, employee, manager, step, action, remarks);
            
            // 3. Send email
            sendEmailToMultipleRecipients(recipients, content.subject, content.body);
            
            System.out.println("✅ " + step + " email sent to: " + String.join(", ", recipients));
            
        } catch (Exception e) {
            System.err.println("❌ Failed to send " + step + " email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get recipients based on the exit process step
     */
    private String[] getRecipientsForStep(ExitStep step, 
                                          usermaintenance employee, 
                                          usermaintenance manager) {
        String employeeEmail = getEmployeeEmail(employee);
        String managerEmail = manager != null ? manager.getEmailid() : null;
        
        switch(step) {
            case CREATION:
                // For creation: Send to Manager, Sridharan, and HR
                return prepareUniqueRecipients(managerEmail, sridharanEmail, hrEmail);
                
            case WITHDRAWAL:
                // For withdrawal: Send to same recipients as creation
                return prepareUniqueRecipients(managerEmail, sridharanEmail, hrEmail);
                
            case MANAGER_REVIEW:
                // Manager Review: Employee, HR, Sridharan
                return prepareUniqueRecipients(employeeEmail, hrEmail, sridharanEmail);
                
            case HR_ROUND1:
                // HR Round 1: Employee, Sridharan, System Admin
                return prepareUniqueRecipients(employeeEmail, sridharanEmail, systemAdminEmail);
                
            case ASSET_CLEARANCE:
                // Asset Clearance: HR, Sridharan
                return prepareUniqueRecipients(hrEmail, sridharanEmail);
                
            case HR_ROUND2:
                // HR Round 2: Sridharan, Accounts
                return prepareUniqueRecipients(sridharanEmail, accountsEmail);
                
            case PAYROLL_CLEARANCE:
                // Payroll Clearance: HR, Sridharan
                return prepareUniqueRecipients(hrEmail, sridharanEmail);
                
            case FINAL_HR:
                // Final HR: Sridharan, Employee, Manager
                return prepareUniqueRecipients(sridharanEmail, employeeEmail, managerEmail);
                
            default:
                return new String[0];
        }
    }

    /**
     * Build email content (subject and body) for the specific step
     */
    private EmailContent buildEmailContent(ExitFormMaster exitForm,
                                           usermaintenance employee,
                                           usermaintenance manager,
                                           ExitStep step,
                                           String action,
                                           String remarks) {
        
        String employeeName = getFullName(employee);
        String actionText = getActionText(action);
        
        String subject = getSubjectForStep(step, employeeName, actionText);
        String body = getBodyForStep(exitForm, employee, manager, step, actionText, remarks);
        
        return new EmailContent(subject, body);
    }

    /**
     * Get email subject based on the step
     */
    private String getSubjectForStep(ExitStep step, String employeeName, String actionText) {
        switch(step) {
            case CREATION:
                return "Resignation Submitted - " + employeeName;
            case WITHDRAWAL:
                return "Resignation WITHDRAWN - " + employeeName;
            case MANAGER_REVIEW:
                return "Resignation " + actionText + " by Manager - " + employeeName;
            case HR_ROUND1:
                return "Resignation " + actionText + " by HR (Round 1) - " + employeeName;
            case ASSET_CLEARANCE:
                return "Asset Clearance Completed - " + employeeName;
            case HR_ROUND2:
                return "Resignation " + actionText + " by HR (Round 2/Offboarding) - " + employeeName;
            case PAYROLL_CLEARANCE:
                return "Payroll Clearance Completed - " + employeeName;
            case FINAL_HR:
                return "Resignation Process COMPLETED - " + employeeName;
            default:
                return "Resignation Notification";
        }
    }

    /**
     * Get email body based on the step
     */
    private String getBodyForStep(ExitFormMaster exitForm,
                                  usermaintenance employee,
                                  usermaintenance manager,
                                  ExitStep step,
                                  String actionText,
                                  String remarks) {
        
        StringBuilder body = new StringBuilder();
        body.append("Dear Sir/Madam,\n\n");
        
        String employeeName = getFullName(employee);
        String managerName = manager != null ? manager.getFirstname() : "N/A";
        
        switch(step) {
            case CREATION:
                body.append("A new resignation has been submitted by:\n\n");
                appendCommonDetails(body, employeeName, employee, exitForm);
                body.append("Submitted On: ").append(exitForm.getUserSubmittedOn()).append("\n");
                body.append("Reason: ").append(exitForm.getReason() != null ? exitForm.getReason() : "Not specified").append("\n\n");
                body.append("Comments/Remarks: ").append(exitForm.getComments() != null ? exitForm.getComments().trim() : "None").append("\n");
                body.append("Please review the exit form at your earliest convenience.\n\n");
                break;
                
            case WITHDRAWAL:
                body.append("An resignation has been WITHDRAWN by the employee:\n\n");
                appendCommonDetails(body, employeeName, employee, exitForm);
                body.append("Submitted On: ").append(exitForm.getUserSubmittedOn()).append("\n");
                body.append("Withdrawn On: ").append(exitForm.getWithdrawDate() != null ? 
                           exitForm.getWithdrawDate() : LocalDate.now()).append("\n");
                body.append("Reason: ").append(exitForm.getReason() != null ? exitForm.getReason() : "Not specified").append("\n\n");
                
                String withdrawalRemarks = remarks != null ? remarks : 
                                          (exitForm.getWithdrawPurpose() != null ? 
                                          exitForm.getWithdrawPurpose() : "Employee has withdrawn the exit form");
                appendRemarks(body, withdrawalRemarks, "Withdrawal Purpose");
                
                body.append("\nThe resignation process has been cancelled and the form is no longer active.\n\n");
                break;
                
            case MANAGER_REVIEW:
                body.append("The resignation has been ").append(actionText.toLowerCase()).append(" by the manager.\n\n");
                appendCommonDetails(body, employeeName, employee, exitForm);
                body.append("Manager Name: ").append(managerName).append("\n");
                body.append("Manager Action: ").append(actionText).append("\n");
                
                String managerRemarks = remarks != null ? remarks : exitForm.getManagerRemarks();
                appendRemarks(body, managerRemarks, "Manager Remarks");
                
                body.append("\n").append(getNextStepMessage(step, actionText)).append("\n\n");
                break;
                
            case HR_ROUND1:
                body.append("The resignation has been ").append(actionText.toLowerCase()).append(" by HR in Round 1.\n\n");
                appendCommonDetails(body, employeeName, employee, exitForm);
                body.append("HR Action: ").append(actionText).append("\n");
                
                String hrRemarks = remarks != null ? remarks : exitForm.getHrGeneralComments();
                appendRemarks(body, hrRemarks, "HR Comments");
                
                body.append("\n").append(getNextStepMessage(step, actionText)).append("\n\n");
                break;
                
            case ASSET_CLEARANCE:
                body.append("Asset clearance has been completed for the following employee:\n\n");
                appendCommonDetails(body, employeeName, employee, exitForm);
                body.append("Asset Clearance By: ").append(exitForm.getAssetSubmittedBy() != null ? 
                           exitForm.getAssetSubmittedBy() : "System Admin").append("\n");
                body.append("Asset Clearance Date: ").append(exitForm.getAssetSubmittedOn()).append("\n\n");
                
                String assetClearance = exitForm.getAssetClearance();
                if (assetClearance != null && !assetClearance.trim().isEmpty()) {
                    body.append("Assets Returned:\n");
                    String[] assets = assetClearance.split(" # ");
                    for (String asset : assets) {
                        body.append("  • ").append(asset.trim()).append("\n");
                    }
                    body.append("\n");
                } else {
                    body.append("Assets Returned: None\n\n");
                }
                
                body.append("The form is now pending with HR Round 2.\n\n");
                break;
                
            case HR_ROUND2:
                body.append("The resignation has been ").append(actionText.toLowerCase()).append(" by HR in Round 2 (Offboarding).\n\n");
                appendCommonDetails(body, employeeName, employee, exitForm);
                body.append("HR Action: ").append(actionText).append("\n");
                
                String offboardingChecks = exitForm.getHrOffboardingChecks();
                if (offboardingChecks != null && !offboardingChecks.trim().isEmpty()) {
                    body.append("\n📋 Offboarding Checklist:\n");
                    body.append("------------------------\n");
                    
                    String[] checks = offboardingChecks.split(" # ");
                    for (String check : checks) {
                        body.append("• ").append(check.trim()).append("\n");
                    }
                    body.append("------------------------\n");
                } else {
                    body.append("\n📋 Offboarding Checklist: No items recorded\n");
                }
                
                body.append("\n").append(getNextStepMessage(step, actionText)).append("\n\n");
                break;
                
            case PAYROLL_CLEARANCE:
                body.append("Payroll clearance has been completed for the following employee:\n\n");
                appendCommonDetails(body, employeeName, employee, exitForm);
                body.append("Payroll Submitted By: ").append(exitForm.getPayrollSubmittedBy() != null ? 
                           exitForm.getPayrollSubmittedBy() : "Payroll Team").append("\n");
                body.append("Payroll Submitted On: ").append(exitForm.getPayrollSubmittedOn()).append("\n\n");
                
                String payrollChecks = exitForm.getPayrollChecks();
                if (payrollChecks != null && !payrollChecks.trim().isEmpty()) {
                    body.append("Payroll Items:\n");
                    String[] checks = payrollChecks.split(" # ");
                    for (String check : checks) {
                        body.append("  • ").append(check.trim()).append("\n");
                    }
                    body.append("\n");
                }
                
                body.append("The form is now pending with Final HR Approval.\n\n");
                break;
                
            case FINAL_HR:
                body.append("The resignation process has been successfully COMPLETED for the following employee:\n\n");
                appendCommonDetails(body, employeeName, employee, exitForm);
                body.append("Manager Name: ").append(managerName).append("\n");
                body.append("Final HR Approved By: ").append(exitForm.getFinalHrApprovedBy() != null ? 
                           exitForm.getFinalHrApprovedBy() : "HR").append("\n");
                body.append("Final HR Approved On: ").append(exitForm.getFinalHrApprovedOn()).append("\n\n");
                
                String finalChecklist = exitForm.getFinalChecklistData();
                if (finalChecklist != null && !finalChecklist.trim().isEmpty()) {
                    body.append("Final Clearance Items:\n");
                    String[] items = finalChecklist.split(" # ");
                    for (String item : items) {
                        body.append("  • ").append(item.trim()).append("\n");
                    }
                    body.append("\n");
                }
                
                String finalRemarks = remarks != null ? remarks : exitForm.getFinalHrRemarks();
                appendRemarks(body, finalRemarks, "Remarks");
                
                body.append("All clearances have been completed. The employee's exit process is now closed.\n\n");
                break;
        }
        
        body.append("Regards,\n");
        body.append("HRMS System");
        
        return body.toString();
    }

    /**
     * Get next step message based on action
     */
    private String getNextStepMessage(ExitStep step, String actionText) {
        if (!"APPROVED".equals(actionText)) {
            if ("REJECTED".equals(actionText)) {
                return "The exit form has been rejected and closed.";
            } else if ("ON HOLD".equals(actionText)) {
                return "The exit form has been put on hold.";
            }
            return "";
        }
        
        switch(step) {
            case MANAGER_REVIEW:
                return "The form is now pending with HR Round 1.";
            case HR_ROUND1:
                return "The form is now pending with System Admin for asset clearance.";
            case HR_ROUND2:
                return "The form is now pending with Payroll for final clearance.";
            default:
                return "";
        }
    }

    /**
     * Append common employee and exit form details
     */
    private void appendCommonDetails(StringBuilder body, String employeeName, 
                                     usermaintenance employee, ExitFormMaster exitForm) {
        body.append("Employee Name: ").append(employeeName).append("\n");
        body.append("Employee ID: ").append(employee.getEmpid()).append("\n");
        body.append("Exit Form ID: ").append(exitForm.getId()).append("\n");
    }

    /**
     * Append remarks if not empty
     */
    private void appendRemarks(StringBuilder body, String remarks, String label) {
        if (remarks != null && !remarks.trim().isEmpty()) {
            body.append(label).append(": ").append(remarks.trim()).append("\n");
        }
    }

    /**
     * Get full name from employee
     */
    private String getFullName(usermaintenance employee) {
        if (employee == null) return "Unknown";
        return employee.getFirstname() + 
               (employee.getLastname() != null && !employee.getLastname().trim().isEmpty() 
                ? " " + employee.getLastname().trim() : "");
    }

    /**
     * Simple inner class for email content
     */
    private static class EmailContent {
        String subject;
        String body;
        
        EmailContent(String subject, String body) {
            this.subject = subject;
            this.body = body;
        }
    }

    /**
     * Send email to multiple recipients (avoids duplicates)
     */
    private void sendEmailToMultipleRecipients(String[] recipients, String subject, String body) {
        if (recipients == null || recipients.length == 0) {
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setFrom(noReplyEmail);
            helper.setSubject(subject);
            helper.setText(body);
            
            // Set multiple TO recipients (all unique already)
            InternetAddress[] toAddresses = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                toAddresses[i] = new InternetAddress(recipients[i]);
            }
            message.setRecipients(MimeMessage.RecipientType.TO, toAddresses);
            
            mailSender.send(message);
            
            System.out.println("📧 Email sent to " + recipients.length + " recipient(s)");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to multiple recipients: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Prepare unique recipients list (removes duplicates, nulls, and handles case-insensitive comparison)
     */
    private String[] prepareUniqueRecipients(String... emails) {
        if (emails == null || emails.length == 0) {
            return new String[0];
        }

        // Use Set for automatic duplicate removal with case-insensitive comparison
        Set<String> uniqueEmails = new HashSet<>();
        
        for (String email : emails) {
            if (email != null && !email.trim().isEmpty()) {
                // Store in lowercase for case-insensitive uniqueness
                uniqueEmails.add(email.trim().toLowerCase());
            }
        }
        
        // Convert back to array (emails are now in lowercase)
        String[] result = uniqueEmails.toArray(new String[0]);
        
        // Log duplicate prevention if needed
        if (emails.length > result.length) {
            System.out.println("🔍 Duplicate prevention: Removed " + (emails.length - result.length) + " duplicate email(s)");
        }
        
        return result;
    }

    /**
     * Get employee email from database
     */
    private String getEmployeeEmail(usermaintenance employee) {
        if (employee == null) return null;
        
        // First try emailid field
        if (employee.getEmailid() != null && !employee.getEmailid().trim().isEmpty()) {
            return employee.getEmailid().trim();
        }
        
        // Try to find by empid if email not available
        usermaintenance userWithEmail = usermaintenanceRepository.findByEmpid(employee.getEmpid());
        if (userWithEmail != null && userWithEmail.getEmailid() != null && !userWithEmail.getEmailid().trim().isEmpty()) {
            return userWithEmail.getEmailid().trim();
        }
        
        return null;
    }

    /**
     * Format action text
     */
    private String getActionText(String action) {
        if (action == null) return "PROCESSED";
        
        switch (action.toUpperCase()) {
            case "APPROVE":
                return "APPROVED";
            case "REJECT":
                return "REJECTED";
            case "ON-HOLD":
                return "ON HOLD";
            default:
                return action.toUpperCase();
        }
    }

    // ==================== BACKWARD COMPATIBILITY EXIT METHODS ====================
    
    /**
     * @deprecated Use {@link #sendExitProcessEmail} instead
     */
    @Deprecated
    public void sendExitFormCreationEmail(ExitFormMaster exitForm, usermaintenance employee, 
                                          usermaintenance manager, String employeeEmail) {
        sendExitProcessEmail(exitForm, employee, manager, ExitStep.CREATION, null, null);
    }

    /**
     * @deprecated Use {@link #sendExitProcessEmail} instead
     */
    @Deprecated
    public void sendManagerReviewEmail(ExitFormMaster exitForm, usermaintenance employee, 
                                       usermaintenance manager, String action) {
        sendExitProcessEmail(exitForm, employee, manager, ExitStep.MANAGER_REVIEW, 
                            action, exitForm.getManagerRemarks());
    }

    /**
     * @deprecated Use {@link #sendExitProcessEmail} instead
     */
    @Deprecated
    public void sendHRRound1Email(ExitFormMaster exitForm, usermaintenance employee, String action) {
        sendExitProcessEmail(exitForm, employee, null, ExitStep.HR_ROUND1, 
                            action, exitForm.getHrGeneralComments());
    }

    /**
     * @deprecated Use {@link #sendExitProcessEmail} instead
     */
    @Deprecated
    public void sendAssetClearanceEmail(ExitFormMaster exitForm, usermaintenance employee) {
        sendExitProcessEmail(exitForm, employee, null, ExitStep.ASSET_CLEARANCE, null, null);
    }

    /**
     * @deprecated Use {@link #sendExitProcessEmail} instead
     */
    @Deprecated
    public void sendHRRound2Email(ExitFormMaster exitForm, usermaintenance employee, String action) {
        sendExitProcessEmail(exitForm, employee, null, ExitStep.HR_ROUND2, action, null);
    }

    /**
     * @deprecated Use {@link #sendExitProcessEmail} instead
     */
    @Deprecated
    public void sendPayrollEmail(ExitFormMaster exitForm, usermaintenance employee) {
        sendExitProcessEmail(exitForm, employee, null, ExitStep.PAYROLL_CLEARANCE, null, null);
    }

    /**
     * @deprecated Use {@link #sendExitProcessEmail} instead
     */
    @Deprecated
    public void sendFinalHREmail(ExitFormMaster exitForm, usermaintenance employee) {
        sendExitProcessEmail(exitForm, employee, null, ExitStep.FINAL_HR, null, 
                            exitForm.getFinalHrRemarks());
    }

    // ==================== FIXED NOTIFICATION METHODS ====================

    public void sendExpenseNotificationEmail(String empId, String action, String expenseId) {
        usermaintenance employee = usermaintenanceRepository.findByEmpid(empId);
        if (employee != null && employee.getRepoteTo() != null) {
            usermaintenance manager = usermaintenanceRepository.findByEmpid(employee.getRepoteTo());
            if (manager != null && manager.getEmailid() != null) {
                ExpenseDetailsMod expense = expenseDetailsRepository.findById(expenseId).orElse(null);
                if (expense != null) {
                    String subject = "Expense " + action + " Notification";
                    String body = "Dear " + manager.getFirstname() + ",\n\n" +
                            "An expense has been " + action + " by your reportee " + employee.getFirstname() +
                            ".\n\nExpense ID: " + expenseId +
                            "\nAmount: " + expense.getAmount() +
                            "\n\nPlease review it at your earliest convenience.\n\nRegards,\nHRMS System";

                    // Use the fixed sendAdvanceEmail which has duplicate protection
                    this.sendAdvanceEmail(employee.getEmailid(), manager.getEmailid(), subject, body);
                }
            }
        }
    }

    public void sendAdvanceNotificationEmail(String empId, String advanceId, String action, BigDecimal amount) {
        usermaintenance employee = usermaintenanceRepository.findByEmpid(empId);
        if (employee != null && employee.getRepoteTo() != null) {
            usermaintenance manager = usermaintenanceRepository.findByEmpid(employee.getRepoteTo());
            if (manager != null && manager.getEmailid() != null) {
                String actionFormatted = action.equalsIgnoreCase("add") ? "submitted" : "updated";
                String subject = "Advance Request " + actionFormatted;
                String body = "Dear " + manager.getFirstname() + ",\n\n" +
                        "An advance request has been " + actionFormatted + " by your reportee " + employee.getFirstname() +
                        ".\n\nAdvance ID: " + advanceId +
                        "\nAmount: " + amount +
                        "\n\nPlease review it at your earliest convenience.\n\nRegards,\nHRMS System";

                // Use the fixed sendAdvanceEmail which has duplicate protection
                this.sendAdvanceEmail(employee.getEmailid(), manager.getEmailid(), subject, body);
            }
        }
    }

    public void sendRejectionEmailToManager(PayrollAdjustment pa, String adminRemarks) {
        try {
            // Get Manager & Employee details
            usermaintenance manager = usermaintenanceRepository.findByEmpid1(pa.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found: " + pa.getManagerId()));

            usermaintenance employee = usermaintenanceRepository.findByEmpid1(pa.getEmpId())
                    .orElseThrow(() -> new RuntimeException("Employee not found: " + pa.getEmpId()));

            String fullName = employee.getFirstname() +
                    (employee.getLastname() != null ? " " + employee.getLastname() : "");

            String subject = "Payroll Adjustment REJECTED - " + fullName + " (" + pa.getEmpId() + ")";

            double deductionAmount = pa.getOtherDeductions();
            String deductionText = "₹" + String.format("%,.2f", deductionAmount);

            if (deductionAmount == 0.0) {
                deductionText = "₹0.00";
            }

            String body = "Dear " + manager.getFirstname() + ",\n\n" +
                          "A payroll adjustment request has been REJECTED by HR/Admin.\n\n" +
                          "Employee         : " + fullName + "\n" +
                          "Employee ID      : " + pa.getEmpId() + "\n" +
                          "Month            : " + formatMonth(pa.getMonth()) + "\n" +
                          "LOP Days         : " + pa.getLopDays() + "\n" +
                          "Other Deduction  : " + deductionText + "\n\n" +
                          "Rejection Reason:\n" +
                          (adminRemarks != null && !adminRemarks.trim().isEmpty()
                              ? adminRemarks.trim()
                              : "No remarks provided.") + "\n\n" +
                          "Please contact HR if you need clarification or wish to resubmit the request.\n\n" +
                          "Regards,\n" +
                          "HR & Payroll System\n" +
                          "Whitestones Solutions Pvt Ltd";

            // Use the fixed sendLeaveEmail which has duplicate protection
            sendLeaveEmail("payroll@whitestones.in", manager.getEmailid(), subject, body);
            System.out.println("✅ Rejection email sent to: " + manager.getEmailid());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Failed to send rejection email for adjustment ID: " + pa.getId());
        }
    }

    private String formatMonth(String monthStr) {
        if (monthStr == null) return "N/A";
        try {
            YearMonth ym = YearMonth.parse(monthStr.length() > 7 ? monthStr.substring(0, 7) : monthStr);
            return ym.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        } catch (Exception e) {
            return monthStr;
        }
    }
}