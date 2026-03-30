package com.whitestone.hrms.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whitestone.entity.ExitFormMaster;
import com.whitestone.entity.TraineeMaster;
import com.whitestone.entity.UserRoleMaintenance;
import com.whitestone.entity.usermaintenance;
import com.whitestone.hrms.repo.ExitFormMasterRepository;
import com.whitestone.hrms.repo.TraineemasterRepository;
import com.whitestone.hrms.repo.UserRoleMaintenanceRepository;
import com.whitestone.hrms.repo.usermaintenanceRepository;
import com.whitestone.hrms.service.EmailService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ExitFormMasterController {

    @Autowired
    private ExitFormMasterRepository repo;

    @Autowired
    private usermaintenanceRepository userRepo;

    @Autowired
    private UserRoleMaintenanceRepository roleRepo;

    @Autowired
    private TraineemasterRepository traineemasterRepository;
    
    @Autowired
    private usermaintenanceRepository usermaintenanceRepository;
    
    @Autowired
    private EmailService emailService;

    // ----------------------- GENERATE EXIT ID --------------------------
    private String generateExitId() {
        try {
            List<ExitFormMaster> allForms = repo.findAll();
            
            if (allForms.isEmpty()) {
                return "RESFWSSL1000";
            }
            
            long maxNumber = 999;
            
            for (ExitFormMaster form : allForms) {
                String id = form.getId();
                if (id != null && id.startsWith("RESFWSSL")) {
                    try {
                        String numericPart = id.substring(8);
                        long number = Long.parseLong(numericPart);
                        if (number > maxNumber) {
                            maxNumber = number;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("⚠️ Invalid ID format: " + id);
                    }
                }
            }
            
            return "RESFWSSL" + (maxNumber + 1);
            
        } catch (Exception e) {
            System.out.println("⚠️ Error in generateExitId, using fallback: " + e.getMessage());
            long count = repo.count() + 1000;
            return "RESFWSSL" + count;
        }
    }

    // ----------------------- CREATE EXIT FORM WITH EMAIL --------------------------
    @PostMapping("/create/exit-form")
    public ResponseEntity<Map<String, Object>> createExitForm(@RequestBody ExitFormMaster form) {
        
        System.out.println("🔵 Creating new exit form for employee: " + form.getEmployeeId());
        
        try {
            // Check if employee already has active form
            List<ExitFormMaster> existingForms = repo.findByEmployeeIdAndDelFlag(form.getEmployeeId(), "N");
            if (!existingForms.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "You already have an active exit form. Please withdraw the existing form before creating a new one.");
                response.put("existingFormId", existingForms.get(0).getId());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            // Generate unique ID
            String newId = generateExitId();
            System.out.println("✅ Generated new ID: " + newId);
            
            // Set form details
            form.setId(newId);
            form.setCreatedOn(LocalDate.now().toString());
            form.setCreatedBy(form.getEmployeeName());
            form.setDelFlag("N");
            form.setStatus("0");
            form.setUserSubmittedOn(LocalDateTime.now());
            
            // Save the form
            ExitFormMaster savedForm = repo.save(form);
            System.out.println("✅ Exit form created successfully: " + savedForm.getId());
            
            // ========== SEND CREATION EMAIL ==========
            sendExitEmailForStep(savedForm, EmailService.ExitStep.CREATION, null, null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Exit form created successfully!");
            response.put("exitFormId", savedForm.getId());
            response.put("data", savedForm);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.out.println("❌ Error creating exit form: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error creating exit form: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ----------------------- ALL ACTIVE EXIT FORMS --------------------
    @GetMapping("/all/exit-form")
    public List<ExitFormMaster> getAllActive() {
        return repo.findAllActive();
    }

    // ----------------------- GET BY ID --------------------------------
    @GetMapping("/exit-form/{id}")
    public ExitFormMaster getActiveById(@PathVariable String id) {
        return repo.findActiveById(id);
    }

    // ----------------------- UPDATE EXIT FORM -------------------------
    @PutMapping("/update/exit-form/{id}")
    public ExitFormMaster updateExitForm(@PathVariable String id, @RequestBody ExitFormMaster newData) {

        ExitFormMaster existing = repo.findActiveById(id);

        if (existing == null) {
            return null;
        }

        existing.setEmployeeId(newData.getEmployeeId());
        existing.setEmployeeName(newData.getEmployeeName());
        existing.setNoticeStartDate(newData.getNoticeStartDate());
        existing.setReason(newData.getReason());
        existing.setComments(newData.getComments());
        existing.setNoticePeriod(newData.getNoticePeriod());
        existing.setNoticeEndDate(newData.getNoticeEndDate());
        existing.setAttachment(newData.getAttachment());

        existing.setUpdatedOn(LocalDate.now().toString());
        existing.setUpdatedBy(newData.getEmployeeName());

        return repo.save(existing);
    }

 // ----------------------- WITHDRAW EXIT FORM - SIMPLIFIED & GUARANTEED WORKING -----------------------
    @PutMapping("/exit-form/{formId}/withdraw")
    public ResponseEntity<Map<String, Object>> withdrawExitForm(
            @PathVariable String formId,
            @RequestBody(required = false) Map<String, String> requestBody) {
        
        System.out.println("🔵 ========== WITHDRAW API CALLED ==========");
        System.out.println("🔵 Form ID: " + formId);
        System.out.println("🔵 Request Body: " + requestBody);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Step 1: Find the form to get employee name
            ExitFormMaster form = repo.findById(formId).orElse(null);
            if (form == null) {
                response.put("success", false);
                response.put("message", "Exit form not found");
                return ResponseEntity.status(404).body(response);
            }

            // Step 2: Get withdraw purpose from request
            String withdrawPurpose = null;
            if (requestBody != null && requestBody.containsKey("withdrawPurpose")) {
                withdrawPurpose = requestBody.get("withdrawPurpose");
                System.out.println("✅ Withdraw purpose received: '" + withdrawPurpose + "'");
            } else {
                System.out.println("⚠️ No withdrawPurpose in request");
            }

            // Step 3: Execute simplified native query
            int updatedCount = repo.withdrawExitForm(
                formId,
                withdrawPurpose,
                form.getEmployeeName()  // withdrawBy
            );

            System.out.println("✅ Native query rows updated: " + updatedCount);

            // Step 4: Verify directly from database
            String savedPurpose = repo.getWithdrawPurposeDirect(formId);
            System.out.println("🔵 DIRECT DB CHECK - Saved purpose: '" + savedPurpose + "'");

            if (updatedCount > 0) {
                response.put("success", true);
                response.put("message", "Exit form withdrawn successfully");
                response.put("withdrawPurpose", savedPurpose);
                response.put("withdrawDate", new java.sql.Date(System.currentTimeMillis()).toString());
                response.put("withdrawBy", form.getEmployeeName());
                
                // Email sending (commented out)
                 sendExitEmailForStep(form, EmailService.ExitStep.WITHDRAWAL, null, withdrawPurpose);
                
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to withdraw exit form - no rows updated");
                return ResponseEntity.status(500).body(response);
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    // ----------------------- GET BY EMPLOYEE ID -----------------------
    @GetMapping("/exit-form/employee/{employeeId}")
    public List<ExitFormMaster> getByEmployeeId(@PathVariable String employeeId) {
        return repo.findByEmployeeIdAndDelFlag(employeeId, "N");
    }
    
    // ----------------------- GET EXIT FORMS BY EMP ID -----------------
    @GetMapping("/exitForms/get/{empId}")
    public ResponseEntity<Map<String, Object>> getExitFormsByEmpId(@PathVariable String empId) {
        
        System.out.println("Logged in employee: " + empId);

        try {
            List<String> empIds = new ArrayList<>();
            boolean isHR = false;

            if (empId == null || empId.trim().isEmpty() || "null".equals(empId) || "undefined".equals(empId)) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid employeeId received: " + empId
                ));
            }

            usermaintenance user = usermaintenanceRepository.findByEmpid(empId);
            if (user != null) {
                Optional<UserRoleMaintenance> role = roleRepo.findByRoleid(user.getRoleid());
                if (role.isPresent()) {
                    String roleId = role.get().getRoleid();
                    String roleName = role.get().getRolename();
                    
                    System.out.println("User role ID: " + roleId + ", Role Name: " + roleName);
                    
                    if ("R003".equals(roleId) || "HR".equalsIgnoreCase(roleName)) {
                        isHR = true;
                        System.out.println("✅ User is HR - will return all exit forms");
                    }
                }
            }

            if (isHR) {
                List<usermaintenance> allUsers = usermaintenanceRepository.findAll();
                for (usermaintenance u : allUsers) {
                    if (u.getEmpid() != null && !u.getEmpid().trim().isEmpty()) {
                        empIds.add(u.getEmpid().trim());
                    }
                }

                List<TraineeMaster> allTrainees = traineemasterRepository.findAll();
                for (TraineeMaster trainee : allTrainees) {
                    if (trainee.getTrngid() != null && !trainee.getTrngid().trim().isEmpty()) {
                        empIds.add(trainee.getTrngid().trim());
                    }
                }
                
                System.out.println("HR access - Total employee IDs: " + empIds.size());
                
            } else {
                empIds.add(empId.trim());
                
                List<String> directReports = getDirectReports(empId);
                if (directReports != null) {
                    empIds.addAll(directReports);
                }
                
                System.out.println("Non-HR access - Employee IDs: " + empIds);
            }

            List<ExitFormMaster> exitForms;
            if (empIds.isEmpty()) {
                exitForms = new ArrayList<>();
            } else {
                exitForms = repo.findByEmployeeIdInAndDelFlag(empIds, "N");
            }

            Map<String, String> empIdToName = new HashMap<>();

            if (!empIds.isEmpty()) {
                List<usermaintenance> users = usermaintenanceRepository.findByEmpidIn(empIds);
                for (usermaintenance u : users) {
                    if (u.getEmpid() != null && u.getFirstname() != null) {
                        empIdToName.put(u.getEmpid().trim(), u.getFirstname());
                    }
                }

                List<TraineeMaster> trainees = traineemasterRepository.findByTrngidIn(empIds);
                for (TraineeMaster trainee : trainees) {
                    if (trainee.getTrngid() != null && trainee.getFirstname() != null) {
                        empIdToName.put(trainee.getTrngid().trim(), trainee.getFirstname());
                    }
                }
            }

            List<Map<String, Object>> responseList = new ArrayList<>();

            for (ExitFormMaster form : exitForms) {
                Map<String, Object> row = new HashMap<>();

                row.put("id", form.getId());
                row.put("employeeId", form.getEmployeeId());

                String displayName = empIdToName.getOrDefault(
                    form.getEmployeeId(),
                    form.getEmployeeName() != null ? form.getEmployeeName() : "N/A"
                );

                row.put("employeeName", displayName);

                row.put("noticeStartDate", form.getNoticeStartDate());
                row.put("noticeEndDate", form.getNoticeEndDate());
                row.put("noticePeriod", form.getNoticePeriod());
                row.put("reason", form.getReason());
                row.put("comments", form.getComments());
                row.put("status", form.getStatus());
                row.put("attachment", form.getAttachment());
                row.put("createdOn", form.getCreatedOn());
                row.put("createdBy", form.getCreatedBy());

                row.put("performance", form.getPerformance());
                row.put("projectDependency", form.getProjectDependency());
                row.put("knowledgeTransfer", form.getKnowledgeTransfer());
                row.put("managerRemarks", form.getManagerRemarks());
                row.put("managerAction", form.getManagerAction());
                row.put("managerName", form.getManagerName());
                row.put("purposeOfChange", form.getPurposeOfChange());

                row.put("hasManagerReview", hasManagerReviewData(form));
                
                row.put("currentStage", getCurrentStage(form));
                row.put("requiresHRAction", requiresHRAction(form.getStatus()));
                row.put("managerReviewStatus", getManagerReviewStatus(form));
                
                row.put("userSubmittedOn", form.getUserSubmittedOn());
                row.put("managerSubmittedOn", form.getManagerSubmittedOn());
                row.put("hrRound1SubmittedOn", form.getHrRound1SubmittedOn());
                row.put("assetSubmittedOn", form.getAssetSubmittedOn());
                row.put("hrRound2SubmittedOn", form.getHrRound2SubmittedOn());
                row.put("payrollSubmittedOn", form.getPayrollSubmittedOn());
                row.put("finalHrSubmittedOn", form.getFinalHrSubmittedOn());

                responseList.add(row);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("data", responseList);
            response.put("message", responseList.isEmpty() ? "No exit forms found" : "Exit forms fetched successfully");
            response.put("success", true);
            response.put("isHR", isHR);
            response.put("totalCount", responseList.size());
            response.put("userRole", isHR ? "HR" : "Non-HR");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "message", "Server error: " + e.getMessage()
            ));
        }
    }

    // ==================== HELPER METHODS ====================

    private boolean hasManagerReviewData(ExitFormMaster form) {
        return (form.getPerformance() != null && !form.getPerformance().isEmpty())
            || (form.getProjectDependency() != null && !form.getProjectDependency().isEmpty())
            || (form.getKnowledgeTransfer() != null && !form.getKnowledgeTransfer().isEmpty())
            || (form.getManagerRemarks() != null && !form.getManagerRemarks().isEmpty())
            || (form.getManagerAction() != null && !form.getManagerAction().isEmpty());
    }

    private List<String> getDirectReports(String empId) {
        List<String> directReports = new ArrayList<>();

        try {
            List<usermaintenance> reports = usermaintenanceRepository.findByRepoteTo(empId);

            for (usermaintenance user : reports) {
                if (user.getEmpid() != null && !user.getEmpid().trim().isEmpty()) {
                    directReports.add(user.getEmpid().trim());
                }
            }

            System.out.println("Direct reports for " + empId + ": " + directReports);

        } catch (Exception e) {
            System.err.println("Error in getDirectReports: " + e.getMessage());
        }

        return directReports;
    }

    private String getCurrentStage(ExitFormMaster form) {
        if (form == null) {
            return "Unknown Status";
        }
        
        if ("Y".equalsIgnoreCase(form.getDelFlag())) {
            return "Rejected";
        }
        
        if ("ON-HOLD".equalsIgnoreCase(form.getManagerAction())) {
            return "On Hold by Manager";
        }
        if ("ON-HOLD".equalsIgnoreCase(form.getHrAction())) {
            return "On Hold by HR";
        }
        
        String status = form.getStatus();
        if (status == null) {
            return "Pending Review";
        }
        
        switch (status) {
            case "0":
                return "Pending with Manager";
            case "1":
                return "Pending with HR Round 1";
            case "2":
                return "Pending with System Admin";
            case "3":
                return "Pending with HR Round 2";
            case "4":
                return "Pending with Payroll";
            case "5":
                return "Pending Final HR Approval";
            case "6":
                return "Approved & Completed";
            default:
                return "Pending Review";
        }
    }

    private boolean requiresHRAction(String status) {
        if (status == null) return false;
        return "1".equals(status) || "3".equals(status);
    }

    private String getManagerReviewStatus(ExitFormMaster form) {
        if (form.getManagerAction() == null || form.getManagerAction().isEmpty()) {
            return "Pending";
        }
        return form.getManagerAction();
    }
    
    // ==================== MANAGER REVIEW ENDPOINTS WITH EMAIL ====================

    @PostMapping("/manager/create")
    public ResponseEntity<Map<String, Object>> submitReview(@RequestBody ExitFormMaster review,
                                      @RequestHeader("username") String currentUser) {
        try {
            List<ExitFormMaster> existingExitForms = repo.findByEmployeeIdAndDelFlag(review.getEmployeeId(), "N");
            if (existingExitForms.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "No active exit form found for employee: " + review.getEmployeeId());
                response.put("success", false);
                return ResponseEntity.status(404).body(response);
            }

            ExitFormMaster existingExitForm = existingExitForms.get(0);
            
            if (hasManagerReviewData(existingExitForm)) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Manager review already exists for this employee. Use update instead.");
                response.put("success", false);
                return ResponseEntity.status(400).body(response);
            }

            existingExitForm.setPerformance(review.getPerformance());
            existingExitForm.setProjectDependency(review.getProjectDependency());
            existingExitForm.setKnowledgeTransfer(review.getKnowledgeTransfer());
            existingExitForm.setManagerRemarks(review.getManagerRemarks());
            existingExitForm.setManagerAction(review.getManagerAction());
            existingExitForm.setManagerName(review.getManagerName());
            existingExitForm.setPurposeOfChange(review.getPurposeOfChange());
            existingExitForm.setManagerSubmittedOn(LocalDateTime.now());
            existingExitForm.setUpdatedOn(LocalDateTime.now().toString());
            existingExitForm.setUpdatedBy(currentUser);

            String action = review.getManagerAction();

            if ("REJECT".equalsIgnoreCase(action)) {
                existingExitForm.setDelFlag("Y");
                ExitFormMaster savedReview = repo.save(existingExitForm);
                
                // ========== SEND REJECTION EMAIL with remarks ==========
                sendExitEmailForStep(savedReview, EmailService.ExitStep.MANAGER_REVIEW, action, review.getManagerRemarks());
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form rejected successfully. Form has been deleted.");
                response.put("success", true);
                response.put("review", savedReview);
                return ResponseEntity.ok(response);
            }

            if ("ON-HOLD".equalsIgnoreCase(action)) {
                existingExitForm.setStatus("0");
                ExitFormMaster savedReview = repo.save(existingExitForm);
                
                // ========== SEND ON-HOLD EMAIL with remarks ==========
                sendExitEmailForStep(savedReview, EmailService.ExitStep.MANAGER_REVIEW, action, review.getManagerRemarks());
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form put on hold successfully. Status remains with Manager.");
                response.put("success", true);
                response.put("review", savedReview);
                return ResponseEntity.ok(response);
            }

            if ("APPROVE".equalsIgnoreCase(action)) {
                existingExitForm.setStatus("1");
            }

            ExitFormMaster savedReview = repo.save(existingExitForm);
            
            // ========== SEND APPROVAL EMAIL with remarks ==========
            sendExitEmailForStep(savedReview, EmailService.ExitStep.MANAGER_REVIEW, action, review.getManagerRemarks());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Manager review submitted successfully.");
            response.put("success", true);
            response.put("review", savedReview);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error submitting manager review: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    @PutMapping("/manager/update/{id}")
    public ResponseEntity<Map<String, Object>> updateReview(
            @PathVariable String id,
            @RequestBody ExitFormMaster updatedReview,
            @RequestHeader("username") String currentUser) {

        try {
            ExitFormMaster existingForm = repo.findById(id).orElse(null);
            if (existingForm == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Review not found: " + id);
                response.put("success", false);
                return ResponseEntity.status(404).body(response);
            }

            if (!hasManagerReviewData(existingForm)) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "No manager review data found to update. Please create a review first.");
                response.put("success", false);
                return ResponseEntity.status(400).body(response);
            }

            String oldAction = existingForm.getManagerAction();
            String newAction = updatedReview.getManagerAction();

            existingForm.setPerformance(updatedReview.getPerformance());
            existingForm.setProjectDependency(updatedReview.getProjectDependency());
            existingForm.setKnowledgeTransfer(updatedReview.getKnowledgeTransfer());
            existingForm.setManagerRemarks(updatedReview.getManagerRemarks());
            existingForm.setManagerAction(updatedReview.getManagerAction());
            existingForm.setManagerName(updatedReview.getManagerName());
            existingForm.setPurposeOfChange(updatedReview.getPurposeOfChange());
            existingForm.setUpdatedBy(currentUser);
            existingForm.setUpdatedOn(LocalDateTime.now().toString());
            existingForm.setManagerSubmittedOn(LocalDateTime.now());

            if ("APPROVE".equalsIgnoreCase(newAction)) {
                existingForm.setStatus("1");
            } else if ("ON-HOLD".equalsIgnoreCase(newAction)) {
                existingForm.setStatus("0");
            } else if ("REJECT".equalsIgnoreCase(newAction)) {
                existingForm.setDelFlag("Y");
            }

            ExitFormMaster savedReview = repo.save(existingForm);
            
            // ========== SEND UPDATE EMAIL if action changed ==========
            if (!newAction.equals(oldAction)) {
                sendExitEmailForStep(savedReview, EmailService.ExitStep.MANAGER_REVIEW, newAction, updatedReview.getManagerRemarks());
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Manager review updated successfully.");
            response.put("success", true);
            response.put("review", savedReview);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error updating manager review: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/getallmanager")
    public ResponseEntity<Map<String, Object>> getAllReviews() {
        try {
            List<ExitFormMaster> allActive = repo.findByDelFlag("N");
            List<ExitFormMaster> managerReviews = new ArrayList<>();
            
            for (ExitFormMaster form : allActive) {
                if (hasManagerReviewData(form)) {
                    managerReviews.add(form);
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("data", managerReviews);
            response.put("message", "Manager reviews fetched successfully");
            response.put("success", true);
            response.put("totalCount", managerReviews.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error fetching manager reviews: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @DeleteMapping("/deletemanager/{id}")
    public ResponseEntity<Map<String, Object>> deleteReview(@PathVariable String id,
                               @RequestHeader("username") String currentUser) {
        try {
            ExitFormMaster review = repo.findById(id).orElse(null);
            if (review == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Manager review not found");
                response.put("success", false);
                return ResponseEntity.status(404).body(response);
            }

            review.setPerformance(null);
            review.setProjectDependency(null);
            review.setKnowledgeTransfer(null);
            review.setManagerRemarks(null);
            review.setManagerAction(null);
            review.setManagerName(null);
            review.setPurposeOfChange(null);
            review.setUpdatedBy(currentUser);
            review.setUpdatedOn(LocalDateTime.now().toString());
            
            repo.save(review);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Manager review data cleared successfully");
            response.put("success", true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error deleting manager review: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/manager/employee/{employeeId}")
    public ResponseEntity<Map<String, Object>> getByEmployee(@PathVariable String employeeId) {
        try {
            List<ExitFormMaster> exitForms = repo.findByDelFlagAndEmployeeId("N", employeeId);
            List<ExitFormMaster> managerReviews = new ArrayList<>();
            
            for (ExitFormMaster form : exitForms) {
                if (hasManagerReviewData(form)) {
                    managerReviews.add(form);
                }
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("data", managerReviews);
            response.put("message", "Manager reviews fetched successfully");
            response.put("success", true);
            response.put("totalCount", managerReviews.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error fetching manager reviews: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // ==================== HR REVIEW ENDPOINTS WITH EMAIL ====================

    @PostMapping("/hr/review")
    public ResponseEntity<Map<String, Object>> createOrUpdateHRReview(@RequestBody ExitFormMaster hrReview,
                                        @RequestHeader("username") String currentUser) {
        try {
            System.out.println("🔵 HR Review Request Received - ID: " + hrReview.getId());
            
            if (hrReview.getId() == null || hrReview.getId().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form ID is required");
                response.put("success", false);
                return ResponseEntity.status(400).body(response);
            }

            ExitFormMaster existingForm = repo.findById(hrReview.getId()).orElse(null);
            if (existingForm == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form not found: " + hrReview.getId());
                response.put("success", false);
                return ResponseEntity.status(404).body(response);
            }

            System.out.println("✅ Current Status: " + existingForm.getStatus());
            System.out.println("✅ HR Action Requested: " + hrReview.getHrAction());

            existingForm.setHrNoticePeriod(hrReview.getHrNoticePeriod());
            existingForm.setHrLeaveBalances(hrReview.getHrLeaveBalances());
            existingForm.setHrPolicyCompliance(hrReview.getHrPolicyCompliance());
            existingForm.setHrExitEligibility(hrReview.getHrExitEligibility());
            existingForm.setHrNoticePeriodComments(hrReview.getHrNoticePeriodComments());
            existingForm.setHrLeaveBalancesComments(hrReview.getHrLeaveBalancesComments());
            existingForm.setHrPolicyComplianceComments(hrReview.getHrPolicyComplianceComments());
            existingForm.setHrExitEligibilityComments(hrReview.getHrExitEligibilityComments());
            existingForm.setHrGeneralComments(hrReview.getHrGeneralComments());
            existingForm.setHrAction(hrReview.getHrAction());
            existingForm.setHrReviewDate(LocalDateTime.now().toString());
            existingForm.setHrName(hrReview.getHrName());
            
            String currentstatus = existingForm.getStatus();
            if ("1".equals(currentstatus)) {
                existingForm.setHrRound1SubmittedOn(LocalDateTime.now());
            } else if ("3".equals(currentstatus)) {
                existingForm.setHrRound2SubmittedOn(LocalDateTime.now());
            } else if ("5".equals(currentstatus)) {
                existingForm.setFinalHrSubmittedOn(LocalDateTime.now());
            }

            String action = hrReview.getHrAction();
            EmailService.ExitStep step = determineHRStep(currentstatus);

            if ("REJECT".equalsIgnoreCase(action)) {
                existingForm.setDelFlag("Y");
                existingForm.setUpdatedOn(LocalDateTime.now().toString());
                existingForm.setUpdatedBy(currentUser);
                ExitFormMaster savedForm = repo.save(existingForm);
                
                // ========== SEND REJECTION EMAIL with remarks ==========
                sendExitEmailForStep(savedForm, step, action, hrReview.getHrGeneralComments());
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form rejected by HR successfully. Form has been deleted.");
                response.put("success", true);
                response.put("data", savedForm);
                return ResponseEntity.ok(response);
            }

            if ("ON-HOLD".equalsIgnoreCase(action)) {
                existingForm.setUpdatedOn(LocalDateTime.now().toString());
                existingForm.setUpdatedBy(currentUser);
                ExitFormMaster savedForm = repo.save(existingForm);
                
                // ========== SEND ON-HOLD EMAIL with remarks ==========
                sendExitEmailForStep(savedForm, step, action, hrReview.getHrGeneralComments());
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form put on hold by HR successfully. Status remains unchanged.");
                response.put("success", true);
                response.put("data", savedForm);
                return ResponseEntity.ok(response);
            }

            if ("APPROVE".equalsIgnoreCase(action)) {
                String currentStatus = existingForm.getStatus();
                String newStatus = mapHRActionToStatus("APPROVE", currentStatus);
                existingForm.setStatus(newStatus);
                System.out.println("🔄 Status updated from " + currentStatus + " to " + newStatus);
            }

            existingForm.setUpdatedOn(LocalDateTime.now().toString());
            existingForm.setUpdatedBy(currentUser);

            ExitFormMaster savedReview = repo.save(existingForm);
            
            // ========== SEND APPROVAL EMAIL with remarks ==========
            sendExitEmailForStep(savedReview, step, action, hrReview.getHrGeneralComments());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "HR review submitted successfully");
            response.put("success", true);
            response.put("data", savedReview);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.out.println("❌ Error in HR review: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error submitting HR review: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/hr/review/{exitFormId}")
    public ResponseEntity<Map<String, Object>> getHRReviewByExitFormId(@PathVariable String exitFormId) {
        try {
            System.out.println("🔵 Fetching HR review for exit form: " + exitFormId);
            
            ExitFormMaster form = repo.findById(exitFormId).orElse(null);
            if (form == null) {
                System.out.println("❌ Exit form not found: " + exitFormId);
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form not found: " + exitFormId);
                response.put("success", false);
                return ResponseEntity.status(404).body(response);
            }

            System.out.println("✅ Found form: " + form.getId() + " for employee: " + form.getEmployeeId());
            
            Map<String, Object> response = new HashMap<>();
            response.put("data", form);
            response.put("message", "HR review fetched successfully");
            response.put("success", true);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.out.println("❌ Error fetching HR review: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error fetching HR review: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @DeleteMapping("/hr/review/{id}")
    public ResponseEntity<Map<String, Object>> deleteHRReview(@PathVariable String id) {
        try {
            System.out.println("🔵 Deleting HR review data for: " + id);
            
            ExitFormMaster review = repo.findById(id).orElse(null);
            if (review == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "HR review not found");
                response.put("success", false);
                return ResponseEntity.status(404).body(response);
            }

            review.setHrNoticePeriod(null);
            review.setHrLeaveBalances(null);
            review.setHrPolicyCompliance(null);
            review.setHrExitEligibility(null);
            review.setHrNoticePeriodComments(null);
            review.setHrLeaveBalancesComments(null);
            review.setHrPolicyComplianceComments(null);
            review.setHrExitEligibilityComments(null);
            review.setHrGeneralComments(null);
            review.setHrAction(null);
            review.setHrReviewDate(null);
            review.setUpdatedOn(LocalDateTime.now().toString());
            review.setUpdatedBy("System-HR");
            
            repo.save(review);

            System.out.println("✅ HR review data cleared for: " + id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "HR review data cleared successfully");
            response.put("success", true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Error deleting HR review: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error deleting HR review: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    private String mapHRActionToStatus(String hrAction, String currentStatus) {
        if (hrAction == null || hrAction.isEmpty()) return currentStatus;
        
        switch (hrAction.toUpperCase()) {
            case "APPROVE":
                if ("1".equals(currentStatus)) return "2";
                if ("3".equals(currentStatus)) return "4";
                if ("5".equals(currentStatus)) return "6";
                break;
            case "REJECT":
                return currentStatus;
            case "ON-HOLD":
                return currentStatus;
            case "REVISE_LWD":
                return "3";
        }
        return currentStatus;
    }
    
    @PutMapping("/hr/review/update")
    public ResponseEntity<Map<String, Object>> updateHRReview(@RequestBody ExitFormMaster hrReview,
                                  @RequestHeader("username") String currentUser) {
        try {
            System.out.println("🔵 HR Review Update Request Received - ID: " + hrReview.getId());
            
            if (hrReview.getId() == null || hrReview.getId().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form ID is required");
                response.put("success", false);
                return ResponseEntity.status(400).body(response);
            }

            ExitFormMaster existingForm = repo.findById(hrReview.getId()).orElse(null);
            if (existingForm == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form not found: " + hrReview.getId());
                response.put("success", false);
                return ResponseEntity.status(404).body(response);
            }

            System.out.println("✅ Current HR Action: " + existingForm.getHrAction());
            System.out.println("✅ New HR Action in request: " + hrReview.getHrAction());

            String originalHRAction = existingForm.getHrAction();
            String newHRAction = hrReview.getHrAction();
            
            if (newHRAction != null && !newHRAction.isEmpty() && !newHRAction.equals(originalHRAction)) {
                existingForm.setHrAction(newHRAction);
            } else {
                existingForm.setHrAction(originalHRAction);
            }

            String currentStatus = existingForm.getStatus();
            if ("1".equals(currentStatus) || "2".equals(currentStatus)) {
                existingForm.setHrRound1SubmittedOn(LocalDateTime.now());
            } else if ("3".equals(currentStatus) || "4".equals(currentStatus)) {
                existingForm.setHrRound2SubmittedOn(LocalDateTime.now());
            }

            existingForm.setHrNoticePeriod(hrReview.getHrNoticePeriod());
            existingForm.setHrLeaveBalances(hrReview.getHrLeaveBalances());
            existingForm.setHrPolicyCompliance(hrReview.getHrPolicyCompliance());
            existingForm.setHrExitEligibility(hrReview.getHrExitEligibility());
            existingForm.setHrNoticePeriodComments(hrReview.getHrNoticePeriodComments());
            existingForm.setHrLeaveBalancesComments(hrReview.getHrLeaveBalancesComments());
            existingForm.setHrPolicyComplianceComments(hrReview.getHrPolicyComplianceComments());
            existingForm.setHrExitEligibilityComments(hrReview.getHrExitEligibilityComments());
            existingForm.setHrGeneralComments(hrReview.getHrGeneralComments());
            
            existingForm.setHrReviewDate(LocalDateTime.now().toString());
            existingForm.setUpdatedOn(LocalDateTime.now().toString());
            existingForm.setUpdatedBy(currentUser);

            ExitFormMaster savedReview = repo.save(existingForm);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "HR review updated successfully");
            response.put("success", true);
            response.put("data", savedReview);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.out.println("❌ Error updating HR review: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error updating HR review: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    // ==================== ASSET CLEARANCE ENDPOINTS WITH EMAIL ====================

    @PostMapping("/asset-clearance/{exitFormId}")
    public ResponseEntity<Map<String, Object>> submitAssetClearance(
        @PathVariable String exitFormId,
        @RequestBody Map<String, Object> assetData,
        @RequestHeader("username") String currentUser) {

        try {
            System.out.println("🔵 Asset Clearance Request Received - ID: " + exitFormId);
            System.out.println("🔵 Asset Data: " + assetData);
            System.out.println("🔵 Current User: " + currentUser);

            ExitFormMaster existingForm = repo.findById(exitFormId).orElse(null);
            if (existingForm == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form not found: " + exitFormId);
                response.put("success", false);
                return ResponseEntity.status(404).body(response);
            }

            String assetClearanceString = processAssetData(assetData);
            existingForm.setAssetClearance(assetClearanceString);
            existingForm.setAssetSubmittedOn(LocalDateTime.now());
            existingForm.setAssetSubmittedBy(currentUser);

            try {
                usermaintenance user = userRepo.findByEmpid(currentUser);
                if (user != null && user.getFirstname() != null) {
                    existingForm.setAssetSubmittedBy(user.getFirstname());
                } else {
                    TraineeMaster trainee = traineemasterRepository.findByTrngid(currentUser);
                    if (trainee != null && trainee.getFirstname() != null) {
                        existingForm.setAssetSubmittedBy(trainee.getFirstname());
                    }
                }
            } catch (Exception e) {
                System.out.println("⚠️ Could not fetch user details: " + e.getMessage());
            }

            String oldStatus = existingForm.getStatus();
            if ("2".equals(oldStatus)) {
                existingForm.setStatus("3");
                System.out.println("🔄 Status updated from " + oldStatus + " to 3 (HR Round 2) after asset clearance");
            } else {
                System.out.println("🟡 Status not updated - current status is " + oldStatus + ", expected 2");
            }

            existingForm.setUpdatedOn(LocalDateTime.now().toString());
            existingForm.setUpdatedBy(currentUser);

            ExitFormMaster savedForm = repo.save(existingForm);

            // ========== SEND ASSET CLEARANCE EMAIL ==========
            sendExitEmailForStep(savedForm, EmailService.ExitStep.ASSET_CLEARANCE, null, null);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Asset clearance submitted successfully");
            response.put("success", true);
            response.put("data", savedForm);
            response.put("submittedBy", existingForm.getAssetSubmittedBy());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Error in asset clearance: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error submitting asset clearance: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    private String processAssetData(Map<String, Object> assetData) {
        StringBuilder assetString = new StringBuilder();

        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> assets = (List<Map<String, Object>>) assetData.get("assets");
            
            if (assets != null && !assets.isEmpty()) {
                for (Map<String, Object> asset : assets) {
                    String name = (String) asset.get("name");
                    String condition = (String) asset.get("condition");
                    String comments = (String) asset.get("comments");
                    
                    if (name != null && condition != null) {
                        if (assetString.length() > 0) {
                            assetString.append(" # ");
                        }
                        
                        assetString.append(name).append(" : ").append(condition);
                        
                        if (comments != null && !comments.trim().isEmpty()) {
                            assetString.append(" || ").append(comments.trim());
                        } else {
                            assetString.append(" || null");
                        }
                    }
                }
            }
            
            return assetString.toString();
            
        } catch (Exception e) {
            System.out.println("❌ Error processing asset data: " + e.getMessage());
            return "Error processing assets";
        }
    }

    @GetMapping("/asset-clearance/{exitFormId}")
    public ResponseEntity<Map<String, Object>> getAssetClearance(@PathVariable String exitFormId) {
        try {
            System.out.println("🔵 Fetching asset clearance for exit form: " + exitFormId);
            
            ExitFormMaster form = repo.findById(exitFormId).orElse(null);
            if (form == null) {
                System.out.println("❌ Exit form not found: " + exitFormId);
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form not found: " + exitFormId);
                response.put("success", false);
                return ResponseEntity.status(404).body(response);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("assetClearance", form.getAssetClearance());
            response.put("assetSubmittedBy", form.getAssetSubmittedBy());
            response.put("assetSubmittedOn", form.getAssetSubmittedOn());
            response.put("message", "Asset clearance fetched successfully");
            response.put("success", true);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.out.println("❌ Error fetching asset clearance: " + e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error fetching asset clearance: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping("/asset-clearance/{exitFormId}")
    public ResponseEntity<Map<String, Object>> updateAssetClearance(
        @PathVariable String exitFormId,
        @RequestBody Map<String, Object> assetData,
        @RequestHeader("username") String currentUser) {

        try {
            System.out.println("🔄 UPDATE Asset Clearance Request - ID: " + exitFormId);
            System.out.println("🔄 Asset Data: " + assetData);
            System.out.println("🔄 Current User: " + currentUser);

            ExitFormMaster existingForm = repo.findById(exitFormId).orElse(null);
            if (existingForm == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form not found: " + exitFormId);
                response.put("success", false);
                return ResponseEntity.status(404).body(response);
            }

            if (existingForm.getAssetClearance() == null || existingForm.getAssetClearance().trim().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "No asset clearance found to update. Please submit first.");
                response.put("success", false);
                return ResponseEntity.status(400).body(response);
            }

            String assetClearanceString = processAssetData(assetData);
            existingForm.setAssetClearance(assetClearanceString);

            try {
                usermaintenance user = userRepo.findByEmpid(currentUser);
                if (user != null && user.getFirstname() != null) {
                    existingForm.setAssetSubmittedBy(user.getFirstname() + " (Updated)");
                } else {
                    TraineeMaster trainee = traineemasterRepository.findByTrngid(currentUser);
                    if (trainee != null && trainee.getFirstname() != null) {
                        existingForm.setAssetSubmittedBy(trainee.getFirstname() + " (Updated)");
                    } else {
                        existingForm.setAssetSubmittedBy(currentUser + " (Updated)");
                    }
                }
            } catch (Exception e) {
                System.out.println("⚠️ Could not fetch user details for update: " + e.getMessage());
                existingForm.setAssetSubmittedBy(currentUser + " (Updated)");
            }

            existingForm.setUpdatedOn(LocalDateTime.now().toString());
            existingForm.setUpdatedBy(currentUser);

            ExitFormMaster savedForm = repo.save(existingForm);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Asset clearance updated successfully");
            response.put("success", true);
            response.put("data", savedForm);
            response.put("submittedBy", existingForm.getAssetSubmittedBy());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("❌ Error updating asset clearance: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error updating asset clearance: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // ==================== PAYROLL ENDPOINTS WITH EMAIL ====================

    @PostMapping("/payroll/submit/{exitFormId}")
    public ResponseEntity<Map<String, Object>> submitPayroll(
        @PathVariable String exitFormId,
        @RequestBody Map<String, Object> payload,
        @RequestHeader("username") String currentUser) {

        try {
            ExitFormMaster form = repo.findById(exitFormId).orElse(null);
            if (form == null) {
                return ResponseEntity.status(404).body(Map.of("success", false, "message", "Form not found"));
            }

            String payrollString = (String) payload.get("payroll_checks");
            
            if (payrollString == null) {
                payrollString = (String) payload.get("payrollChecks");
            }
            
            if (payrollString == null) {
                payrollString = "";
            }
            
            form.setPayrollSubmittedBy(currentUser);
            form.setPayrollChecks(payrollString);
            form.setPayrollSubmittedOn(LocalDateTime.now());

            if ("4".equals(form.getStatus())) {
                form.setStatus("5");
                System.out.println("PAYROLL SUBMITTED → STATUS 4 → 5 (Final HR Approval)");
            }

            form.setUpdatedOn(LocalDateTime.now().toString());
            form.setUpdatedBy(currentUser);
            ExitFormMaster savedForm = repo.save(form);

            // ========== SEND PAYROLL EMAIL ==========
            sendExitEmailForStep(savedForm, EmailService.ExitStep.PAYROLL_CLEARANCE, null, null);

            System.out.println("✅ Payroll submitted by: " + currentUser);
            System.out.println("Saved payroll string: " + payrollString);
            System.out.println("Form saved with ID: " + form.getId());

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Payroll submitted! Now pending Final HR Approval.",
                "status", "5",
                "payrollString", payrollString,
                "payrollSubmittedBy", currentUser
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/payroll/get/{exitFormId}")
    public ResponseEntity<Map<String, Object>> getPayroll(@PathVariable String exitFormId) {
        try {
            ExitFormMaster form = repo.findById(exitFormId).orElse(null);
            if (form == null) {
                return ResponseEntity.status(404).body(Map.of("success", false));
            }

            String payrollChecks = form.getPayrollChecks();
            String payrollSubmittedBy = form.getPayrollSubmittedBy();
            LocalDateTime payrollSubmittedOn = form.getPayrollSubmittedOn();
            
            System.out.println("Retrieved payroll checks: " + payrollChecks);
            System.out.println("Retrieved payroll submitted by: " + payrollSubmittedBy);
            System.out.println("Retrieved payroll submitted on: " + payrollSubmittedOn);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "payrollChecks", payrollChecks != null ? payrollChecks : "",
                "payrollSubmittedBy", payrollSubmittedBy != null ? payrollSubmittedBy : "",
                "payrollSubmittedOn", payrollSubmittedOn != null ? payrollSubmittedOn.toString() : ""
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("success", false));
        }
    }

    @PutMapping("/payroll/update/{exitFormId}")
    public ResponseEntity<Map<String, Object>> updatePayrollChecks(
        @PathVariable String exitFormId,
        @RequestBody Map<String, Object> payload,
        @RequestHeader("username") String currentUser) {

        try {
            ExitFormMaster form = repo.findById(exitFormId)
                    .orElseThrow(() -> new RuntimeException("Exit form not found"));

            String payrollString = (String) payload.get("payroll_checks");
            
            if (payrollString == null) {
                payrollString = (String) payload.get("payrollChecks");
            }
            
            if (payrollString == null) {
                payrollString = "";
            }
            
            System.out.println("Updating payroll with string: " + payrollString);

            form.setPayrollChecks(payrollString);
            form.setPayrollSubmittedBy(currentUser);
            form.setPayrollSubmittedOn(LocalDateTime.now());
            form.setUpdatedOn(LocalDateTime.now().toString());
            form.setUpdatedBy(currentUser);
            repo.save(form);

            System.out.println("✅ Payroll updated by: " + currentUser);
            System.out.println("Update successful for form: " + exitFormId);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Payroll Checks Updated Successfully!",
                "status", form.getStatus(),
                "payrollString", payrollString,
                "payrollSubmittedBy", currentUser
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Update failed: " + e.getMessage()
            ));
        }
    }

    // ==================== FINAL HR ENDPOINTS WITH EMAIL ====================

    @PostMapping("/final-hr/approve/{exitFormId}")
    public ResponseEntity<Map<String, Object>> finalHrApprove(
        @PathVariable String exitFormId,
        @RequestBody Map<String, Object> payload,
        @RequestHeader("username") String currentUser) {

        try {
            System.out.println("Final HR Approval Request - ID: " + exitFormId);
            System.out.println("Payload: " + payload);
            System.out.println("Current User: " + currentUser);

            ExitFormMaster form = repo.findById(exitFormId).orElse(null);
            if (form == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "Exit form not found"
                ));
            }

            String finalRemarks = (String) payload.get("finalRemarks");
            String finalChecklistData = (String) payload.get("finalChecklistData");

            System.out.println("Final Remarks: " + finalRemarks);
            System.out.println("Final Checklist Data: " + finalChecklistData);

            form.setFinalHrRemarks(finalRemarks != null ? finalRemarks : "");
            form.setFinalChecklistData(finalChecklistData != null && !finalChecklistData.trim().isEmpty() 
                ? finalChecklistData 
                : null);

            form.setFinalHrApprovedBy(currentUser);
            form.setFinalHrApprovedOn(LocalDateTime.now().toString());
            form.setFinalHrSubmittedOn(LocalDateTime.now());

            if ("5".equals(form.getStatus())) {
                form.setStatus("6");
                System.out.println("Status updated to 6 - EXIT CLOSED");
            }

            form.setUpdatedOn(LocalDateTime.now().toString());
            form.setUpdatedBy(currentUser);

            ExitFormMaster savedForm = repo.save(form);
            System.out.println("Final HR approval saved! Checklist data stored.");

            // ========== SEND FINAL HR EMAIL with remarks ==========
            sendExitEmailForStep(savedForm, EmailService.ExitStep.FINAL_HR, null, finalRemarks);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "EXIT CLOSED SUCCESSFULLY!",
                "data", savedForm
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Server error: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/final-hr/get/{exitFormId}")
    public ResponseEntity<Map<String, Object>> getFinalHrData(@PathVariable String exitFormId) {
        try {
            ExitFormMaster form = repo.findById(exitFormId).orElse(null);
            if (form == null) {
                return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "message", "Form not found"
                ));
            }

            Map<String, Object> data = new HashMap<>();
            data.put("finalHrRemarks", form.getFinalHrRemarks() != null ? form.getFinalHrRemarks() : "");
            data.put("finalHrApprovedBy", form.getFinalHrApprovedBy() != null ? form.getFinalHrApprovedBy() : "");
            data.put("finalHrApprovedOn", form.getFinalHrApprovedOn() != null ? form.getFinalHrApprovedOn() : "");
            data.put("finalChecklistData", form.getFinalChecklistData() != null ? form.getFinalChecklistData() : null);
            data.put("isSubmitted", "6".equals(form.getStatus()));
            data.put("status", form.getStatus());

            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", data
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                "success", false,
                "message", "Error: " + e.getMessage()
            ));
        }
    }

    // ==================== HR OFFBOARDING ENDPOINTS WITH EMAIL ====================

    @PostMapping("/hr-offboarding/submit/{exitFormId}")
    public ResponseEntity<?> submitHrOffboarding(
            @PathVariable String exitFormId,
            @RequestBody Map<String, String> payload,
            @RequestHeader(value = "username", required = false) String user) {

        try {
            System.out.println("🔵 HR Offboarding Request Received - ID: " + exitFormId);
            System.out.println("🔵 Payload: " + payload);
            System.out.println("🔵 Current User: " + user);

            ExitFormMaster form = repo.findById(exitFormId).orElse(null);
            if (form == null) {
                return ResponseEntity.notFound().build();
            }

            String checks = payload.get("offboarding_checks");
            form.setHrOffboardingChecks(checks != null ? checks.trim() : "");
            form.setHrRound2SubmittedOn(LocalDateTime.now());
            form.setStatus("4");
            form.setUpdatedOn(LocalDateTime.now().toString());
            form.setUpdatedBy(user != null && !user.isEmpty() ? user : "HR");

            ExitFormMaster savedForm = repo.save(form);

            System.out.println("✅ HR Offboarding saved with timestamp: " + form.getHrRound2SubmittedOn());

            // ========== SEND HR ROUND 2 EMAIL ==========
            sendExitEmailForStep(savedForm, EmailService.ExitStep.HR_ROUND2, "APPROVE", null);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "HR Offboarding submitted successfully! Moving to Payroll Clearance.",
                "status", "4"
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/hr-offboarding/get/{exitFormId}")
    public ResponseEntity<?> getHrOffboarding(@PathVariable String exitFormId) {
        ExitFormMaster form = repo.findById(exitFormId).orElse(null);
        if (form == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(Map.of(
            "success", true,
            "offboardingData", form.getHrOffboardingChecks() != null ? form.getHrOffboardingChecks() : ""
        ));
    }

    // ==================== HELPER METHODS FOR EMAIL ====================

    /**
     * Helper method to send email for any exit process step
     */
    private void sendExitEmailForStep(ExitFormMaster form, 
                                       EmailService.ExitStep step, 
                                       String action, 
                                       String remarks) {
        try {
            System.out.println("🔍 SENDING EMAIL - Step: " + step + ", Action: " + action + ", Remarks: " + remarks);
            
            // Get employee details
            usermaintenance employee = userRepo.findByEmpid(form.getEmployeeId());
            
            // If not found in employee, try trainee
            if (employee == null) {
                TraineeMaster trainee = traineemasterRepository.findByTrngid(form.getEmployeeId());
                if (trainee != null) {
                    employee = new usermaintenance();
                    employee.setEmpid(trainee.getTrngid());
                    employee.setFirstname(trainee.getFirstname());
                    employee.setLastname(trainee.getLastname());
                    employee.setRepoteTo(trainee.getRepoteTo());
                    employee.setEmailid(trainee.getEmailid());
                }
            }
            
            if (employee == null) {
                System.out.println("⚠️ Employee not found for email: " + form.getEmployeeId());
                return;
            }
            
            // Get manager details
            usermaintenance manager = null;
            if (employee.getRepoteTo() != null) {
                manager = userRepo.findByEmpid(employee.getRepoteTo());
                if (manager == null) {
                    TraineeMaster traineeManager = traineemasterRepository.findByTrngid(employee.getRepoteTo());
                    if (traineeManager != null) {
                        manager = new usermaintenance();
                        manager.setEmpid(traineeManager.getTrngid());
                        manager.setFirstname(traineeManager.getFirstname());
                        manager.setLastname(traineeManager.getLastname());
                        manager.setEmailid(traineeManager.getEmailid());
                    }
                }
            }
            
            // Send email using unified service method
            emailService.sendExitProcessEmail(form, employee, manager, step, action, remarks);
            System.out.println("✅ Email sent successfully for step: " + step);
            
        } catch (Exception e) {
            System.err.println("❌ Failed to send " + step + " email: " + e.getMessage());
            e.printStackTrace();
            // Don't throw - email failure shouldn't break the main operation
        }
    }

    /**
     * Determine HR step based on current status
     */
    private EmailService.ExitStep determineHRStep(String status) {
        if (status == null) return EmailService.ExitStep.HR_ROUND1;
        
        switch (status) {
            case "1":
                return EmailService.ExitStep.HR_ROUND1;
            case "3":
                return EmailService.ExitStep.HR_ROUND2;
            case "5":
                return EmailService.ExitStep.FINAL_HR;
            default:
                return EmailService.ExitStep.HR_ROUND1;
        }
    }
}