package com.whitestone.hrms.service;
import java.math.BigDecimal;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.whitestone.entity.ExpenseDetailsMod;
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
			helper.setFrom("payroll@whitestones.co.in");
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



}
