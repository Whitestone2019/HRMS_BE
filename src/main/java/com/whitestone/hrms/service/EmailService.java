package com.whitestone.hrms.service;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

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


    public void sendVerificationEmail(String toEmail, String subject, String body) {
    	 try {
			MimeMessage message = mailSender.createMimeMessage();
			 MimeMessageHelper helper = new MimeMessageHelper(message, true);
			 helper.setTo(toEmail);
			 helper.setSubject(subject);
			 helper.setText(body);
			 helper.setFrom("career@whitestones.co.in"); // Replace with your sender email
			 mailSender.send(message);
		} catch (MailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
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
    
    public void sendLeaveEmail(String From,String email, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(From);
			helper.setCc("hr@whitestones.in");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send payslip email", e);
        }
    }
    
    
    public void sendAdvanceEmail(String From,String email, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(From);
			helper.setCc("accounts@whitestones.in");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(body);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send payslip email", e);
        }
    }
    
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

               this.sendAdvanceEmail(employee.getEmailid(), manager.getEmailid(), subject, body);
            }
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

            // THIS IS THE KEY FIX: Never compare primitive double with null
            double deductionAmount = pa.getOtherDeductions(); // It's double → always has a value
            String deductionText = "₹" + String.format("%,.2f", deductionAmount);

            // If you want to treat 0.0 as "no deduction", you can do:
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

            // Send Email
            sendLeaveEmail(
                "payroll@whitestones.in",
                manager.getEmailid(),
                subject,
                body
            );

            System.out.println("Rejection email sent to: " + manager.getEmailid());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to send rejection email for adjustment ID: " + pa.getId());
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
