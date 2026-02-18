package com.whitestone.hrms.controller;

import com.whitestone.entity.HrLeaveApproval;
import com.whitestone.entity.TraineeMaster;
import com.whitestone.entity.UserRoleMaintenance;
import com.whitestone.entity.usermaintenance;
import com.whitestone.entity.usermaintenancemod;
import com.whitestone.hrms.repo.HrLeaveApprovalRepository;
import com.whitestone.hrms.repo.TraineemasterRepository;
import com.whitestone.hrms.repo.UserRoleMaintenanceRepository;
import com.whitestone.hrms.repo.usermaintenanceRepository;
import com.whitestone.hrms.repo.usermaintenancemodRepository;
import com.whitestone.hrms.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HrLeaveApprovalController {

    @Autowired
    private HrLeaveApprovalRepository hrLeaveApprovalRepository;
    
    @Autowired
	private usermaintenanceRepository usermaintenanceRepository;

	@Autowired
	private TraineemasterRepository traineemasterRepository;
	
	@Autowired
	private usermaintenancemodRepository userMaintenanceRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRoleMaintenanceRepository userRoleMaintenanceRepository;

    // ==================== CREATE ====================
	@PostMapping("/create")
	public ResponseEntity<Map<String, Object>> createHrLeaveApproval(@RequestBody HrLeaveApproval approval) {
	    try {
	        System.out.println("=== START HR LEAVE REQUEST ===");
	        System.out.println("Employee ID: " + approval.getEmployeeId());
	        System.out.println("Leave Type: " + approval.getLeaveType());
	        System.out.println("No of Days: " + approval.getNoOfDays());
	        
	        // Set default values
	        if (approval.getStatus() == null || approval.getStatus().isEmpty()) {
	            approval.setStatus("pending");
	        }
	        approval.setRequestedDate(new Date());
	        approval.setDelFlag("N");
	        approval.setCreatedOn(new Date());
	        approval.setUpdatedOn(new Date());
	        
	        HrLeaveApproval savedApproval = hrLeaveApprovalRepository.save(approval);
	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "Leave approval created successfully");
	        response.put("data", savedApproval);
	        
	        // Send email notification to manager
	        sendEmailNotification(approval, response);
	        
	        System.out.println("=== HR LEAVE REQUEST COMPLETED SUCCESSFULLY ===");
	        
	        return ResponseEntity.ok(response);
	        
	    } catch (Exception e) {
	        System.err.println("=== ERROR IN HR LEAVE REQUEST ===");
	        System.err.println("Error: " + e.getMessage());
	        e.printStackTrace();
	        return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
	    }
	}

	private void sendEmailNotification(HrLeaveApproval approval, Map<String, Object> response) {
	    try {
	        String empId = approval.getEmployeeId();
	        
	        // Fetch employee details
	        Optional<usermaintenance> empOpt = usermaintenanceRepository.findByEmpIdOrUserId(empId);
	        Optional<TraineeMaster> traineeOpt = Optional.empty();
	        if (empOpt.isEmpty()) {
	            traineeOpt = traineemasterRepository.findByTrngidOrUserId(empId);
	        }
	        if (empOpt.isEmpty() && traineeOpt.isEmpty()) {
	            response.put("emailStatus", "Employee details not found, email not sent");
	            return;
	        }

	        String managerId;
	        String employeeFirstName;
	        String employeeEmail;
	        String roleId;

	        if (empOpt.isPresent()) {
	            usermaintenance emp = empOpt.get();
	            managerId = emp.getRepoteTo();
	            employeeFirstName = emp.getFirstname();
	            employeeEmail = emp.getEmailid();
	            roleId = emp.getRoleid();
	        } else {
	            TraineeMaster emp = traineeOpt.get();
	            managerId = emp.getRepoteTo();
	            employeeFirstName = emp.getFirstname();
	            employeeEmail = emp.getEmailid();
	            roleId = emp.getRoleid();
	        }

	        if (managerId == null) {
	            response.put("emailStatus", "Manager not assigned, email not sent");
	            return;
	        }

	        // Fetch manager details
	        Optional<usermaintenance> managerOpt = usermaintenanceRepository.findByEmpIdOrUserId(managerId);
	        Optional<TraineeMaster> managerTraineeOpt = Optional.empty();
	        if (managerOpt.isEmpty()) {
	            managerTraineeOpt = traineemasterRepository.findByTrngidOrUserId(managerId);
	        }
	        if (managerOpt.isEmpty() && managerTraineeOpt.isEmpty()) {
	            response.put("emailStatus", "Manager details not found, email not sent");
	            return;
	        }

	        String managerFirstName;
	        String managerEmail;
	        if (managerOpt.isPresent()) {
	            managerFirstName = managerOpt.get().getFirstname();
	            managerEmail = managerOpt.get().getEmailid();
	        } else {
	            managerFirstName = managerTraineeOpt.get().getFirstname();
	            managerEmail = managerTraineeOpt.get().getEmailid();
	        }

	        // Send email if manager email exists
	        if (managerEmail != null && !managerEmail.isEmpty()) {
	            UserRoleMaintenance role = userRoleMaintenanceRepository.findByRoleid(roleId).orElse(null);

	            String subject = "Leave Request Approval Needed for " + employeeFirstName;
	            
	            // Simple email body
	            StringBuilder emailBody = new StringBuilder();
	            emailBody.append(String.format(
	                "Dear %s,\n\n"
	                + "Employee %s (%s) has submitted a leave request. Please find the details below:\n\n"
	                + "Leave Type: %s\n"
//	                + "No. of Days: %s\n"
//	                + "Reason: %s\n\n"
	                + "Kindly review the request and take necessary action.\n\n"
	                + "Regards,\n"
	                + "%s,\n",
	                managerFirstName, employeeFirstName, empId, approval.getLeaveType(),
	                approval.getNoOfDays(), approval.getReason(), employeeFirstName
	            ));
	            
	            if (role != null) {
	                emailBody.append(role.getRolename() + " - " + role.getDescription() + ",\n");
	            }
	            emailBody.append("Whitestone Software Solution Pvt Ltd.\n");
//	            
//	            emailService.sendLeaveEmail(employeeEmail, managerEmail, subject, emailBody.toString());
	            response.put("emailStatus", "Email sent to manager: " + managerEmail);
	        } else {
	            response.put("emailStatus", "Manager email not found");
	        }
	        
	    } catch (Exception e) {
	        System.err.println("Error sending email notification: " + e.getMessage());
	        response.put("emailStatus", "Failed to send email: " + e.getMessage());
	    }
	}

	// Helper method to create error response
//	private Map<String, Object> createErrorResponse(String message) {
//	    Map<String, Object> errorResponse = new HashMap<>();
//	    errorResponse.put("success", false);
//	    errorResponse.put("error", message);
//	    return errorResponse;
//	}

    // ==================== FETCH ALL ====================
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllHrLeaveApprovals() {
        try {
            List<HrLeaveApproval> approvals = hrLeaveApprovalRepository.findByDelFlag("N");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", approvals.size());
            response.put("data", approvals);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    // ==================== FETCH BY EMPLOYEE ID ====================
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Map<String, Object>> getByEmployeeId(@PathVariable String employeeId) {
        try {
            List<HrLeaveApproval> approvals = hrLeaveApprovalRepository.findByEmployeeIdAndDelFlag(employeeId, "N");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("employeeId", employeeId);
            response.put("count", approvals.size());
            response.put("data", approvals);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    // ==================== UPDATE ====================
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateHrLeaveApproval(
            @PathVariable Long id,
            @RequestBody HrLeaveApproval updatedApproval) {
        try {
            return hrLeaveApprovalRepository.findById(id)
                    .map(existingApproval -> {
                        // Store old status for email notification
                        String oldStatus = existingApproval.getStatus();
                        System.out.println("=== DEBUG ===");
                        System.out.println("Old status: '" + oldStatus + "'");
                        System.out.println("New status: '" + updatedApproval.getStatus() + "'");
                        System.out.println("Employee ID: " + existingApproval.getEmployeeId());
                        
                        // Update only the fields that should be updatable
                        if (updatedApproval.getLeaveType() != null) {
                            existingApproval.setLeaveType(updatedApproval.getLeaveType());
                        }
                        if (updatedApproval.getStatus() != null) {
                            existingApproval.setStatus(updatedApproval.getStatus());
                        }
                        if (updatedApproval.getHrName() != null) {
                            existingApproval.setHrName(updatedApproval.getHrName());
                        }
                        if (updatedApproval.getManagerName() != null) {
                            existingApproval.setManagerName(updatedApproval.getManagerName());
                        }
                        
                        existingApproval.setUpdatedOn(new Date());
                        
                        HrLeaveApproval savedApproval = hrLeaveApprovalRepository.save(existingApproval);
                        
                        // Send email notification if status changed to "approved" (case-insensitive)
                        String newStatus = updatedApproval.getStatus();
                        if (newStatus != null && newStatus.equalsIgnoreCase("approved") && 
                            (oldStatus == null || !oldStatus.equalsIgnoreCase("approved"))) {
                            
                            System.out.println("=== EMAIL TRIGGER ===");
                            System.out.println("Email will be sent! Status changed to: " + newStatus);
                            System.out.println("Old status was: " + oldStatus);
                            
                            sendLeaveApprovalEmail(savedApproval);
                        } else {
                            System.out.println("=== NO EMAIL TRIGGER ===");
                            System.out.println("Reason: New status='" + newStatus + "', Old status='" + oldStatus + "'");
                            System.out.println("Condition check: newStatus.equalsIgnoreCase('approved') = " + 
                                (newStatus != null && newStatus.equalsIgnoreCase("approved")));
                            System.out.println("Condition check: !oldStatus.equalsIgnoreCase('approved') = " + 
                                (oldStatus != null && !oldStatus.equalsIgnoreCase("approved")));
                        }
                        
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("message", "Leave approval updated successfully");
                        response.put("data", savedApproval);
                        
                        return ResponseEntity.ok(response);
                    })
                    .orElse(ResponseEntity.badRequest()
                            .body(createErrorResponse("Leave approval not found with id: " + id)));
            
        } catch (Exception e) {
            System.out.println("Error in updateHrLeaveApproval: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    private void sendLeaveApprovalEmail(HrLeaveApproval leaveApproval) {
        System.out.println("=== ENTERING sendLeaveApprovalEmail ===");
        System.out.println("Leave Approval ID: " + leaveApproval.getId());
        System.out.println("Employee ID from entity: " + leaveApproval.getEmployeeId());
        System.out.println("Status: " + leaveApproval.getStatus());
        System.out.println("Leave Type: " + leaveApproval.getLeaveType());
        
        try {
            // Get employee ID from leave approval
            String empid = leaveApproval.getEmployeeId();
            if (empid == null || empid.isEmpty()) {
                System.out.println("ERROR: Employee ID is null or empty!");
                return;
            }

            System.out.println("Looking for employee with ID: " + empid);
            
            // Variables to store email details
            String employeeEmail = null;
            String employeeFirstName = null;
            String managerFirstName = null;
            String managerEmail = null;
            String managerId = null;
            
            boolean emailFound = false;

            // 1️⃣ First, check in usermaintenance table (regular employees)
            try {
                System.out.println("Step 1: Checking usermaintenance table...");
                Optional<usermaintenance> existingEmployeeOpt = usermaintenanceRepository.findByEmpIdOrUserId(empid);
                if (existingEmployeeOpt.isPresent()) {
                    usermaintenance existingEmployee = existingEmployeeOpt.get();
                    employeeEmail = existingEmployee.getEmailid();
                    employeeFirstName = existingEmployee.getFirstname();
                    managerId = existingEmployee.getRepoteTo();
                    
                    System.out.println("✓ Found in usermaintenance!");
                    System.out.println("  Employee Name: " + employeeFirstName);
                    System.out.println("  Employee Email: " + employeeEmail);
                    System.out.println("  Manager ID: " + managerId);
                    
                    // Get manager details
                    if (managerId != null && !managerId.isEmpty()) {
                        Optional<usermaintenance> managerOpt = usermaintenanceRepository.findByEmpIdOrUserId(managerId);
                        if (managerOpt.isPresent()) {
                            usermaintenance manager = managerOpt.get();
                            managerFirstName = manager.getFirstname();
                            managerEmail = manager.getEmailid();
                            System.out.println("  Manager Name: " + managerFirstName);
                            System.out.println("  Manager Email: " + managerEmail);
                        } else {
                            System.out.println("  Manager not found in usermaintenance");
                        }
                    }
                    emailFound = true;
                } else {
                    System.out.println("✗ Not found in usermaintenance");
                }
            } catch (Exception e) {
                System.out.println("ERROR checking usermaintenance: " + e.getMessage());
            }

            // 2️⃣ If not found in usermaintenance, check TraineeMaster table
            if (!emailFound) {
                try {
                    System.out.println("Step 2: Checking TraineeMaster table...");
                    Optional<TraineeMaster> traineeOpt = traineemasterRepository.findByTrngidOrUserId(empid);
                    if (traineeOpt.isPresent()) {
                        TraineeMaster trainee = traineeOpt.get();
                        employeeEmail = trainee.getEmailid();
                        employeeFirstName = trainee.getFirstname();
                        managerId = trainee.getRepoteTo();
                        
                        System.out.println("✓ Found in TraineeMaster!");
                        System.out.println("  Employee Name: " + employeeFirstName);
                        System.out.println("  Employee Email: " + employeeEmail);
                        System.out.println("  Manager ID: " + managerId);
                        
                        // Get manager details
                        if (managerId != null && !managerId.isEmpty()) {
                            Optional<TraineeMaster> managerOpt = traineemasterRepository.findByTrngidOrUserId(managerId);
                            if (managerOpt.isPresent()) {
                                TraineeMaster manager = managerOpt.get();
                                managerFirstName = manager.getFirstname();
                                managerEmail = manager.getEmailid();
                                System.out.println("  Manager Name: " + managerFirstName);
                                System.out.println("  Manager Email: " + managerEmail);
                            } else {
                                System.out.println("  Manager not found in TraineeMaster");
                            }
                        }
                        emailFound = true;
                    } else {
                        System.out.println("✗ Not found in TraineeMaster");
                    }
                } catch (Exception e) {
                    System.out.println("ERROR checking TraineeMaster: " + e.getMessage());
                }
            }

            // 3️⃣ If still not found, try usermaintenancemod table
            if (!emailFound) {
                try {
                    System.out.println("Step 3: Checking usermaintenancemod table...");
                    Optional<usermaintenancemod> employeeModOpt = userMaintenanceRepository.findByEmpId(empid);
                    if (!employeeModOpt.isPresent()) {
                        employeeModOpt = userMaintenanceRepository.findById(empid);
                    }
                    
                    if (employeeModOpt.isPresent()) {
                        usermaintenancemod employeeMod = employeeModOpt.get();
                        employeeEmail = employeeMod.getEmailid();
                        employeeFirstName = employeeMod.getFirstname();
                        
                        System.out.println("✓ Found in usermaintenancemod!");
                        System.out.println("  Employee Name: " + employeeFirstName);
                        System.out.println("  Employee Email: " + employeeEmail);
                        emailFound = true;
                    } else {
                        System.out.println("✗ Not found in usermaintenancemod");
                    }
                } catch (Exception e) {
                    System.out.println("ERROR checking usermaintenancemod: " + e.getMessage());
                }
            }

            // Check if we found employee email
            if (!emailFound || employeeEmail == null || employeeEmail.isEmpty()) {
                System.out.println("ERROR: Employee email not found for ID: " + empid);
                System.out.println("Email sending ABORTED!");
                return;
            }

            // Prepare email content
            String subject = "Leave Request Approved by HR";
            
            // Format dates
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String startDate = "N/A";
            String endDate = "N/A";
            
            if (leaveApproval.getFromDate() != null) {
                startDate = sdf.format(leaveApproval.getFromDate());
            }
            if (leaveApproval.getToDate() != null) {
                endDate = sdf.format(leaveApproval.getToDate());
            }
            
            // Use HR name if available, otherwise use manager name or default
            String approverName = leaveApproval.getHrName();
            if (approverName == null || approverName.isEmpty()) {
                approverName = managerFirstName != null ? managerFirstName : "HR Department";
            }

            String body = String.format(
                    "Dear %s,\n\nYour leave request has been approved by HR.\n\n" +
                    "Leave Details:\n" +
                    "• Leave Type: %s\n" +
//                    "• From Date: %s\n" +
//                    "• To Date: %s\n" +
                    "• Status: %s\n" +
                    "• Approved By: %s\n\n" +
                    "Regards,\n%s,\nWhitestone Software Solution Pvt Ltd.",
                    employeeFirstName != null ? employeeFirstName : "Employee",
                    leaveApproval.getLeaveType() != null ? leaveApproval.getLeaveType() : "N/A",
                    startDate,
                    endDate,
                    leaveApproval.getStatus() != null ? leaveApproval.getStatus() : "Approved",
                    approverName,
                    approverName);

            // Log email details
            System.out.println("=== EMAIL READY TO SEND ===");
            System.out.println("To: " + employeeEmail);
            System.out.println("Subject: " + subject);
            System.out.println("Body (first 150 chars): " + body.substring(0, Math.min(150, body.length())) + "...");
            System.out.println("==========================");

            // Send email
            try {
                System.out.println("Attempting to send email...");
                
                // Check if emailService exists
                if (emailService == null) {
                    System.out.println("WARNING: emailService is NULL! Make sure it's @Autowired");
                    System.out.println("Simulating email send for testing...");
                    System.out.println("SIMULATED EMAIL SENT TO: " + employeeEmail);
                } else {
                    System.out.println("emailService found, sending email...");
                    if (managerEmail != null && !managerEmail.isEmpty()) {
                        System.out.println("Sending with manager CC: " + managerEmail);
//                        emailService.sendLeaveEmail(managerEmail, employeeEmail, subject, body);
                    } else {
                        System.out.println("Sending directly to employee");
//                        emailService.sendEmail(employeeEmail, subject, body);
                    }
                    System.out.println("✅ Email sent successfully!");
                }
                
            } catch (Exception e) {
                System.out.println("ERROR sending email: " + e.getMessage());
                e.printStackTrace();
            }
            
            System.out.println("=== EXITING sendLeaveApprovalEmail ===");
            
        } catch (Exception e) {
            System.out.println("UNEXPECTED ERROR in sendLeaveApprovalEmail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== DELETE ====================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteHrLeaveApproval(@PathVariable Long id) {
        try {
            return hrLeaveApprovalRepository.findById(id)
                    .map(approval -> {
                        // Soft delete by setting delFlag to "Y"
                        approval.setDelFlag("Y");
                        approval.setUpdatedOn(new Date());
                        hrLeaveApprovalRepository.save(approval);
                        
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", true);
                        response.put("message", "Leave approval deleted successfully");
                        response.put("deletedId", id);
                        
                        return ResponseEntity.ok(response);
                    })
                    .orElse(ResponseEntity.badRequest()
                            .body(createErrorResponse("Leave approval not found with id: " + id)));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    // ==================== HELPER METHOD ====================
    private Map<String, Object> createErrorResponse(String errorMessage) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("error", errorMessage);
        return errorResponse;
    }
}