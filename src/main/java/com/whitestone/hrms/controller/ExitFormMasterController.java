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

import java.time.format.DateTimeFormatter;

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

    // ----------------------- GENERATE EXIT ID --------------------------
    private String generateExitId() {
        try {
            // Get all exit IDs from database
            List<ExitFormMaster> allForms = repo.findAll();
            
            if (allForms.isEmpty()) {
                return "RESFWSSL1000"; // First ID
            }
            
            // Find the highest numeric ID
            long maxNumber = 999; // Start from 999 (so first will be 1000)
            
            for (ExitFormMaster form : allForms) {
                String id = form.getId();
                if (id != null && id.startsWith("RESFWSSL")) {
                    try {
                        // Extract numeric part after "RESFWSSL"
                        String numericPart = id.substring(8); // "RESFWSSL" is 8 characters
                        long number = Long.parseLong(numericPart);
                        if (number > maxNumber) {
                            maxNumber = number;
                        }
                    } catch (NumberFormatException e) {
                        // Skip if not a valid number after prefix
                        System.out.println("⚠️ Invalid ID format: " + id);
                    }
                }
            }
            
            // Return next ID: RESFWSSL + (maxNumber + 1)
            return "RESFWSSL" + (maxNumber + 1);
            
        } catch (Exception e) {
            System.out.println("⚠️ Error in generateExitId, using fallback: " + e.getMessage());
            // Fallback: count + 1000
            long count = repo.count() + 1000;
            return "RESFWSSL" + count;
        }
    }

    // ----------------------- CREATE EXIT FORM --------------------------
    @PostMapping("/create/exit-form")
    public ResponseEntity<Map<String, Object>> createExitForm(@RequestBody ExitFormMaster form) {
        
        System.out.println("🔵 Creating new exit form for employee: " + form.getEmployeeId());
        
        try {
            // ✅ FIX: Check if employee already has active form
            List<ExitFormMaster> existingForms = repo.findByEmployeeIdAndDelFlag(form.getEmployeeId(), "N");
            if (!existingForms.isEmpty()) {
                System.out.println("❌ Employee already has an active exit form!");
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "You already have an active exit form. Please withdraw the existing form before creating a new one.");
                response.put("existingFormId", existingForms.get(0).getId());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            // ✅ Generate unique ID
            String newId = generateExitId();
            System.out.println("✅ Generated new ID: " + newId);
            
            // Set form details
            form.setId(newId);
            form.setCreatedOn(LocalDate.now().toString());
            form.setCreatedBy(form.getEmployeeName());
            form.setDelFlag("N");
            form.setStatus("0"); // ✅ ADD THIS: Set initial status
            
            // Initialize manager review fields
            form.setPerformance(null);
            form.setProjectDependency(null);
            form.setKnowledgeTransfer(null);
            form.setManagerRemarks(null);
            form.setManagerAction(null);
            form.setManagerName(null);
            form.setPurposeOfChange(null);
            
            // ✅ Set submission timestamp
            form.setUserSubmittedOn(LocalDateTime.now());
            
            // Save the form
            ExitFormMaster savedForm = repo.save(form);
            
            System.out.println("✅ Exit form created successfully: " + savedForm.getId());
            
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

    @PutMapping("/exit-form/{formId}/withdraw")
    public ResponseEntity<Map<String, Object>> withdrawExitForm(
            @PathVariable String formId,
            @RequestBody(required = false) Map<String, String> requestBody) {
        
        try {
            ExitFormMaster form = repo.findById(formId).orElse(null);

            if (form == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form not found");
                response.put("success", false);
                return ResponseEntity.status(404).body(response);
            }

            // Extract withdraw purpose from request body
            String withdrawPurpose = null;
            if (requestBody != null && requestBody.containsKey("withdrawPurpose")) {
                withdrawPurpose = requestBody.get("withdrawPurpose");
            }

            form.setDelFlag("Y");
            form.setWithdrawPurpose(withdrawPurpose);
            form.setWithdrawDate(LocalDate.now());
            form.setWithdrawBy(form.getEmployeeName());
            form.setUpdatedOn(LocalDate.now().toString());
            form.setUpdatedBy(form.getEmployeeName());

            repo.save(form);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Exit form withdrawn successfully");
            response.put("success", true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("success", false);
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    // Get Exit Form by Employee ID (active only)
    @GetMapping("/exit-form/employee/{employeeId}")
    public List<ExitFormMaster> getByEmployeeId(@PathVariable String employeeId) {
        return repo.findByEmployeeIdAndDelFlag(employeeId, "N");
    }
    
    
    @GetMapping("/exitForms/get/{empId}")
    public ResponseEntity<Map<String, Object>> getExitFormsByEmpId(@PathVariable String empId) {
        
        System.out.println("Logged in employee: " + empId);

        try {
            List<String> empIds = new ArrayList<>();
            boolean isHR = false;

            // 1️⃣ Validate employeeId
            if (empId == null || empId.trim().isEmpty() || "null".equals(empId) || "undefined".equals(empId)) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid employeeId received: " + empId
                ));
            }

            // 2️⃣ Check if user is HR (Role R003)
            usermaintenance user = usermaintenanceRepository.findByEmpid(empId);
            if (user != null) {
                // Get user role
                Optional<UserRoleMaintenance> role = roleRepo.findByRoleid(user.getRoleid());
                if (role.isPresent()) {
                    String roleId = role.get().getRoleid();
                    String roleName = role.get().getRolename();
                    
                    System.out.println("User role ID: " + roleId + ", Role Name: " + roleName);
                    
                    // Check if role is HR (R003)
                    if ("R003".equals(roleId) || "HR".equalsIgnoreCase(roleName)) {
                        isHR = true;
                        System.out.println("✅ User is HR - will return all exit forms");
                    }
                }
            }

            // 3️⃣ Build employee IDs list based on role
            if (isHR) {
                // HR can see ALL employees and trainees
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
                // Regular employee/manager - can see own forms and direct reports only
                empIds.add(empId.trim());
                
                List<String> directReports = getDirectReports(empId);
                if (directReports != null) {
                    empIds.addAll(directReports);
                }
                
                System.out.println("Non-HR access - Employee IDs: " + empIds);
            }

            // 4️⃣ Fetch exit forms for the collected employee IDs
            List<ExitFormMaster> exitForms;
            if (empIds.isEmpty()) {
                exitForms = new ArrayList<>();
            } else {
                exitForms = repo.findByEmployeeIdInAndDelFlag(empIds, "N");
            }

            // 5️⃣ Prepare name map
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

            // 6️⃣ Build response list with enhanced information for HR
            List<Map<String, Object>> responseList = new ArrayList<>();

            for (ExitFormMaster form : exitForms) {
                Map<String, Object> row = new HashMap<>();

                // Basic form information
                row.put("id", form.getId());
                row.put("employeeId", form.getEmployeeId());

                String displayName = empIdToName.getOrDefault(
                    form.getEmployeeId(),
                    form.getEmployeeName() != null ? form.getEmployeeName() : "N/A"
                );

                row.put("employeeName", displayName);

                // Exit form details
                row.put("noticeStartDate", form.getNoticeStartDate());
                row.put("noticeEndDate", form.getNoticeEndDate());
                row.put("noticePeriod", form.getNoticePeriod());
                row.put("reason", form.getReason());
                row.put("comments", form.getComments());
                row.put("status", form.getStatus());
                row.put("attachment", form.getAttachment());
                row.put("createdOn", form.getCreatedOn());
                row.put("createdBy", form.getCreatedBy());

                // Manager review details
                row.put("performance", form.getPerformance());
                row.put("projectDependency", form.getProjectDependency());
                row.put("knowledgeTransfer", form.getKnowledgeTransfer());
                row.put("managerRemarks", form.getManagerRemarks());
                row.put("managerAction", form.getManagerAction());
                row.put("managerName", form.getManagerName());
                row.put("purposeOfChange", form.getPurposeOfChange());

                // Add manager review flag
                row.put("hasManagerReview", hasManagerReviewData(form));
                
                // Additional fields for HR view
                row.put("currentStage", getCurrentStage(form));
                row.put("requiresHRAction", requiresHRAction(form.getStatus()));
                row.put("managerReviewStatus", getManagerReviewStatus(form));
                
             // In the section where you build response list, add these lines:
                row.put("userSubmittedOn", form.getUserSubmittedOn());
                row.put("managerSubmittedOn", form.getManagerSubmittedOn());
                row.put("hrRound1SubmittedOn", form.getHrRound1SubmittedOn());
                row.put("assetSubmittedOn", form.getAssetSubmittedOn());
                row.put("hrRound2SubmittedOn", form.getHrRound2SubmittedOn());
                row.put("payrollSubmittedOn", form.getPayrollSubmittedOn());
                row.put("finalHrSubmittedOn", form.getFinalHrSubmittedOn());

                responseList.add(row);
            }

            // 7️⃣ Final response
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

    // ✔ Helper to check if manager review exists
    private boolean hasManagerReviewData(ExitFormMaster form) {
        return (form.getPerformance() != null && !form.getPerformance().isEmpty())
            || (form.getProjectDependency() != null && !form.getProjectDependency().isEmpty())
            || (form.getKnowledgeTransfer() != null && !form.getKnowledgeTransfer().isEmpty())
            || (form.getManagerRemarks() != null && !form.getManagerRemarks().isEmpty())
            || (form.getManagerAction() != null && !form.getManagerAction().isEmpty());
    }

    // ✔ Fixed direct reports
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
        
        // First check if form is rejected (delFlag = "Y")
        if ("Y".equalsIgnoreCase(form.getDelFlag())) {
            return "Rejected";
        }
        
        // Check for on-hold status based on managerAction or hrAction
        if ("ON-HOLD".equalsIgnoreCase(form.getManagerAction())) {
            return "On Hold by Manager";
        }
        if ("ON-HOLD".equalsIgnoreCase(form.getHrAction())) {
            return "On Hold by HR";
        }
        
        // Normal status flow
        String status = form.getStatus();
        if (status == null) {
            return "Pending Review";
        }
        
        // Traditional switch statement (Java 8 compatible)
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
        // HR action required for status 1 (HR Round 1) and 3 (HR Round 2)
        return "1".equals(status) || "3".equals(status);
    }

    private String getManagerReviewStatus(ExitFormMaster form) {
        if (form.getManagerAction() == null || form.getManagerAction().isEmpty()) {
            return "Pending";
        }
        return form.getManagerAction();
    }
    
    
    // ==================== MANAGER REVIEW ENDPOINTS ====================

 // Create Manager Review - ONLY for existing exit forms
    @PostMapping("/manager/create")
    public ResponseEntity<Map<String, Object>> submitReview(@RequestBody ExitFormMaster review,
                                      @RequestHeader("username") String currentUser) {
        try {
            // Check if exit form exists for this employee
            List<ExitFormMaster> existingExitForms = repo.findByEmployeeIdAndDelFlag(review.getEmployeeId(), "N");
            if (existingExitForms.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "No active exit form found for employee: " + review.getEmployeeId());
                response.put("success", false);
                return ResponseEntity.status(404).body(response);
            }

            // Get the existing exit form
            ExitFormMaster existingExitForm = existingExitForms.get(0);
            
            // Check if manager review already exists
            if (hasManagerReviewData(existingExitForm)) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Manager review already exists for this employee. Use update instead.");
                response.put("success", false);
                return ResponseEntity.status(400).body(response);
            }

            // ✅ IMPORTANT FIX: Update the EXISTING form, don't create a new one
            // Only update manager-specific fields
            existingExitForm.setPerformance(review.getPerformance());
            existingExitForm.setProjectDependency(review.getProjectDependency());
            existingExitForm.setKnowledgeTransfer(review.getKnowledgeTransfer());
            existingExitForm.setManagerRemarks(review.getManagerRemarks());
            existingExitForm.setManagerAction(review.getManagerAction());
            existingExitForm.setManagerName(review.getManagerName());
            existingExitForm.setPurposeOfChange(review.getPurposeOfChange());
            
            // ✅ Set manager submission timestamp
            existingExitForm.setManagerSubmittedOn(LocalDateTime.now());
            
            // ✅ Audit fields
            existingExitForm.setUpdatedOn(LocalDateTime.now().toString());
            existingExitForm.setUpdatedBy(currentUser);

            // ✅ HANDLE REJECTION: Set delFlag to Y and keep status 0
            if ("REJECT".equalsIgnoreCase(review.getManagerAction())) {
                existingExitForm.setDelFlag("Y"); // ❌ REJECTION: Logical delete
                ExitFormMaster savedReview = repo.save(existingExitForm);
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form rejected successfully. Form has been deleted.");
                response.put("success", true);
                response.put("review", savedReview);
                return ResponseEntity.ok(response);
            }

            // ✅ HANDLE ON-HOLD: Keep status 0, don't change status
            if ("ON-HOLD".equalsIgnoreCase(review.getManagerAction())) {
                existingExitForm.setStatus("0"); // Keep status 0 for manager on-hold
                ExitFormMaster savedReview = repo.save(existingExitForm);
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form put on hold successfully. Status remains with Manager.");
                response.put("success", true);
                response.put("review", savedReview);
                return ResponseEntity.ok(response);
            }

            // ✅ HANDLE APPROVE: Move to next status (1)
            if ("APPROVE".equalsIgnoreCase(review.getManagerAction())) {
                existingExitForm.setStatus("1"); // Move to HR Round 1
            }

            // Save updated form
            ExitFormMaster savedReview = repo.save(existingExitForm);
            
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
    
    

 // Update Manager Review - ONLY if manager review data exists
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

            // Check if manager review data exists before allowing update
            if (!hasManagerReviewData(existingForm)) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "No manager review data found to update. Please create a review first.");
                response.put("success", false);
                return ResponseEntity.status(400).body(response);
            }

            // ✅ FIX: Update only manager review fields (preserve all other data)
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

            // ✅ FIX: Preserve original status and update based on action
            if ("APPROVE".equalsIgnoreCase(updatedReview.getManagerAction())) {
                existingForm.setStatus("1"); // Move to HR Round 1
            } else if ("ON-HOLD".equalsIgnoreCase(updatedReview.getManagerAction())) {
                existingForm.setStatus("0"); // Keep at Manager
            } else if ("REJECT".equalsIgnoreCase(updatedReview.getManagerAction())) {
                existingForm.setDelFlag("Y"); // Reject - logical delete
            }

            ExitFormMaster savedReview = repo.save(existingForm);

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

    // Fetch all manager reviews (only those with manager review data)
    @GetMapping("/getallmanager")
    public ResponseEntity<Map<String, Object>> getAllReviews() {
        try {
            List<ExitFormMaster> allActive = repo.findByDelFlag("N");
            List<ExitFormMaster> managerReviews = new ArrayList<>();
            
            // Filter only records that have manager review data
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

    // Logical delete for manager review (only removes manager review data, not the entire record)
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

            // Only clear manager review data, don't delete the entire record
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

    // Fetch manager reviews by employeeId (only those with manager review data)
    @GetMapping("/manager/employee/{employeeId}")
    public ResponseEntity<Map<String, Object>> getByEmployee(@PathVariable String employeeId) {
        try {
            List<ExitFormMaster> exitForms = repo.findByDelFlagAndEmployeeId("N", employeeId);
            List<ExitFormMaster> managerReviews = new ArrayList<>();
            
            // Filter only records that have manager review data
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

 // REPLACE THIS ENTIRE METHOD
    private String updateExitFormStatus(String employeeId, String action) {
        try {
            List<ExitFormMaster> forms = repo.findByEmployeeIdAndDelFlag(employeeId, "N");
            if (forms.isEmpty()) return "No active form";

            ExitFormMaster form = forms.get(0);

            if ("REJECTED".equalsIgnoreCase(action)) {
                // REJECT = LOGICAL DELETE
                form.setDelFlag("Y");
                form.setUpdatedOn(LocalDateTime.now().toString());
                form.setUpdatedBy("Manager-Rejected");
                repo.save(form);
                return "Form REJECTED and removed.";
            }

            // Only APPROVE moves forward
            if ("APPROVE".equalsIgnoreCase(action)) {
                form.setStatus("1");  // → HR Round 1
            }
            // ON-HOLD → do nothing (status stays 0)

            form.setUpdatedOn(LocalDateTime.now().toString());
            form.setUpdatedBy("Manager");
            repo.save(form);

            return "Action processed successfully";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

 // ✅ CORRECTED: Map manager action to exit form status
    private String mapActionToStatus(String action) {
        if (action == null) return "0";
       
        switch (action.toUpperCase()) {
            case "APPROVE":
                return "1"; // Manager Approved → Pending HR Round 1
            case "REJECT":
                return "0"; // ❌ KEEP STATUS 0 but will update delFlag to Y
            case "ON-HOLD":
                return "0"; // ❌ KEEP STATUS 0 (No status change for on-hold)
            default:
                return "0"; // Pending
        }
    }

    private String getStatusText(String statusCode) {
        if (statusCode == null) {
            return "Submitted";
        }
        switch (statusCode) {
            case "0": return "Pending with Manager";
            case "1": return "Pending with HR Round 1";
            case "2": return "Pending with System Admin (Asset Clearance)";
            case "3": return "Pending with HR Round 2";
            case "4": return "Pending with Payroll";
            case "5": return "Pending Final HR Approval";
            case "6": return "Completed";
            // Remove rejected and on-hold statuses since we handle them differently
            default: return "Unknown Status";
        }
    }
//
//    private String getCurrentStage1(String status) {
//        return getStatusText(status);
//    }
    
 // ==================== HR REVIEW ENDPOINTS ====================

    @PostMapping("/hr/review")
    public ResponseEntity<Map<String, Object>> createOrUpdateHRReview(@RequestBody ExitFormMaster hrReview,
                                        @RequestHeader("username") String currentUser) {
        try {
            System.out.println("🔵 HR Review Request Received - ID: " + hrReview.getId());
            
            // Check if exit form ID is provided
            if (hrReview.getId() == null || hrReview.getId().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form ID is required");
                response.put("success", false);
                return ResponseEntity.status(400).body(response);
            }

            // Find existing exit form
            ExitFormMaster existingForm = repo.findById(hrReview.getId()).orElse(null);
            if (existingForm == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form not found: " + hrReview.getId());
                response.put("success", false);
                return ResponseEntity.status(404).body(response);
            }

            System.out.println("✅ Current Status: " + existingForm.getStatus());
            System.out.println("✅ HR Action Requested: " + hrReview.getHrAction());

            // ✅ UPDATE ONLY HR REVIEW FIELDS
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
            existingForm.setHrName(hrReview.getHrName());  // This saves to HR_NAME column
            
         // ✅ ADD THESE LINES - Set HR submission dates based on status
            String currentstatus = existingForm.getStatus();
            if ("1".equals(currentstatus)) {
                existingForm.setHrRound1SubmittedOn(LocalDateTime.now());
            } else if ("3".equals(currentstatus)) {
                existingForm.setHrRound2SubmittedOn(LocalDateTime.now());
            } else if ("5".equals(currentstatus)) {
                existingForm.setFinalHrSubmittedOn(LocalDateTime.now());
            }

            // ✅ HANDLE HR REJECTION: Set delFlag to Y and keep current status
            if ("REJECT".equalsIgnoreCase(hrReview.getHrAction())) {
                existingForm.setDelFlag("Y"); // ❌ REJECTION: Logical delete
                
                existingForm.setUpdatedOn(LocalDateTime.now().toString());
                existingForm.setUpdatedBy(currentUser);
                repo.save(existingForm);
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form rejected by HR successfully. Form has been deleted.");
                response.put("success", true);
                response.put("data", existingForm);
                return ResponseEntity.ok(response);
            }

            // ✅ HANDLE HR ON-HOLD: Keep current status, don't change
            if ("ON-HOLD".equalsIgnoreCase(hrReview.getHrAction())) {
                // Status remains the same (1 for HR Round 1, 3 for HR Round 2, 5 for Final HR)
                existingForm.setUpdatedOn(LocalDateTime.now().toString());
                existingForm.setUpdatedBy(currentUser);
                repo.save(existingForm);
                
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Exit form put on hold by HR successfully. Status remains unchanged.");
                response.put("success", true);
                response.put("data", existingForm);
                return ResponseEntity.ok(response);
            }

            // ✅ HANDLE HR APPROVE: Move to next status based on current status
            if ("APPROVE".equalsIgnoreCase(hrReview.getHrAction())) {
                String currentStatus = existingForm.getStatus();
                String newStatus = mapHRActionToStatus("APPROVE", currentStatus);
                existingForm.setStatus(newStatus);
                System.out.println("🔄 Status updated from " + currentStatus + " to " + newStatus);
            }

            // Audit fields
            existingForm.setUpdatedOn(LocalDateTime.now().toString());
            existingForm.setUpdatedBy(currentUser);

            ExitFormMaster savedReview = repo.save(existingForm);
            
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
 // Get HR Review by Exit Form ID
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

 // Delete HR Review (only removes HR review data, not the entire record)
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

         // Only clear HR review data, don't delete the entire record
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
	            if ("1".equals(currentStatus)) return "2"; // HR Round 1 → System Admin
	            if ("3".equals(currentStatus)) return "4"; // HR Round 2 → Payroll
	            if ("5".equals(currentStatus)) return "6"; // Final HR → Completed
	            break;
	        case "REJECT":
	            return currentStatus; // ❌ KEEP SAME STATUS but will update delFlag to Y
	        case "ON-HOLD":
	            return currentStatus; // ❌ KEEP SAME STATUS (No status change for on-hold)
	        case "REVISE_LWD":
	            return "3"; // Send back to HR Round 2
	    }
	    return currentStatus;
	}
 
//Update HR Review (for edits after initial submission)
@PutMapping("/hr/review/update")
public ResponseEntity<Map<String, Object>> updateHRReview(@RequestBody ExitFormMaster hrReview,
                                  @RequestHeader("username") String currentUser) {
  try {
      System.out.println("🔵 HR Review Update Request Received - ID: " + hrReview.getId());
      
      // Check if exit form ID is provided
      if (hrReview.getId() == null || hrReview.getId().isEmpty()) {
          Map<String, Object> response = new HashMap<>();
          response.put("message", "Exit form ID is required");
          response.put("success", false);
          return ResponseEntity.status(400).body(response);
      }

      // Find existing exit form
      ExitFormMaster existingForm = repo.findById(hrReview.getId()).orElse(null);
      if (existingForm == null) {
          Map<String, Object> response = new HashMap<>();
          response.put("message", "Exit form not found: " + hrReview.getId());
          response.put("success", false);
          return ResponseEntity.status(404).body(response);
      }

      System.out.println("✅ Current HR Action: " + existingForm.getHrAction());
      System.out.println("✅ New HR Action in request: " + hrReview.getHrAction());

      // For updates, preserve the original HR action unless it's empty
      String originalHRAction = existingForm.getHrAction();
      String newHRAction = hrReview.getHrAction();
      
      // Only update HR action if it's provided AND different from current
      if (newHRAction != null && !newHRAction.isEmpty() && !newHRAction.equals(originalHRAction)) {
          existingForm.setHrAction(newHRAction);
      } else {
          // Keep the original HR action for updates
          existingForm.setHrAction(originalHRAction);
      }

      
      String currentStatus = existingForm.getStatus();
      if ("1".equals(currentStatus) || "2".equals(currentStatus)) {
          existingForm.setHrRound1SubmittedOn(LocalDateTime.now());
      } else if ("3".equals(currentStatus) || "4".equals(currentStatus)) {
          existingForm.setHrRound2SubmittedOn(LocalDateTime.now());
      }
      // ✅ UPDATE HR REVIEW FIELDS
      existingForm.setHrNoticePeriod(hrReview.getHrNoticePeriod());
      existingForm.setHrLeaveBalances(hrReview.getHrLeaveBalances());
      existingForm.setHrPolicyCompliance(hrReview.getHrPolicyCompliance());
      existingForm.setHrExitEligibility(hrReview.getHrExitEligibility());
      existingForm.setHrNoticePeriodComments(hrReview.getHrNoticePeriodComments());
      existingForm.setHrLeaveBalancesComments(hrReview.getHrLeaveBalancesComments());
      existingForm.setHrPolicyComplianceComments(hrReview.getHrPolicyComplianceComments());
      existingForm.setHrExitEligibilityComments(hrReview.getHrExitEligibilityComments());
      existingForm.setHrGeneralComments(hrReview.getHrGeneralComments());
      
      // Update review date for the edit
      existingForm.setHrReviewDate(LocalDateTime.now().toString());

      // Audit fields
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
 
//----------------------- ASSET CLEARANCE ENDPOINTS --------------------------

@PostMapping("/asset-clearance/{exitFormId}")
public ResponseEntity<Map<String, Object>> submitAssetClearance(
    @PathVariable String exitFormId,
    @RequestBody Map<String, Object> assetData,
    @RequestHeader("username") String currentUser) {

try {
    System.out.println("🔵 Asset Clearance Request Received - ID: " + exitFormId);
    System.out.println("🔵 Asset Data: " + assetData);
    System.out.println("🔵 Current User: " + currentUser);

    // Find existing exit form
    ExitFormMaster existingForm = repo.findById(exitFormId).orElse(null);
    if (existingForm == null) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Exit form not found: " + exitFormId);
        response.put("success", false);
        return ResponseEntity.status(404).body(response);
    }

    // Process asset data and convert to single string format
    String assetClearanceString = processAssetData(assetData);
    existingForm.setAssetClearance(assetClearanceString);
    
    // Set asset submission timestamp
    existingForm.setAssetSubmittedOn(LocalDateTime.now());
    
    // ✅ Set who submitted the asset clearance
    existingForm.setAssetSubmittedBy(currentUser); // Store the employee ID
    
    // Try to get user name from database
    try {
        usermaintenance user = userRepo.findByEmpid(currentUser);
        if (user != null && user.getFirstname() != null) {
            // If found in usermaintenance, store the name
            existingForm.setAssetSubmittedBy(user.getFirstname());
        } else {
            // If not found in usermaintenance, check trainee
            TraineeMaster trainee = traineemasterRepository.findByTrngid(currentUser);
            if (trainee != null && trainee.getFirstname() != null) {
                existingForm.setAssetSubmittedBy(trainee.getFirstname());
            }
        }
    } catch (Exception e) {
        System.out.println("⚠️ Could not fetch user details: " + e.getMessage());
        // Keep the employee ID if name not found
    }

    // ✅ CORRECTED: Update status to 3 only if coming from status 2 (System Admin completed work)
    String oldStatus = existingForm.getStatus();
    if ("2".equals(oldStatus)) {
        existingForm.setStatus("3"); // Move to HR round 2 after asset clearance
        System.out.println("🔄 Status updated from " + oldStatus + " to 3 (HR Round 2) after asset clearance");
    } else {
        System.out.println("🟡 Status not updated - current status is " + oldStatus + ", expected 2");
    }

    // Update audit fields
    existingForm.setUpdatedOn(LocalDateTime.now().toString());
    existingForm.setUpdatedBy(currentUser);

    // Save the form
    ExitFormMaster savedForm = repo.save(existingForm);

    Map<String, Object> response = new HashMap<>();
    response.put("message", "Asset clearance submitted successfully");
    response.put("success", true);
    response.put("data", savedForm);
    
    // ✅ Include who submitted in response
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


//Helper method to process asset data into single string format
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
           
           // Format: AssetName : Condition || Remarks
           if (name != null && condition != null) {
               if (assetString.length() > 0) {
                   assetString.append(" # ");
               }
               
               assetString.append(name).append(" : ").append(condition);
               
               // Add comments if present and not empty
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

//Get Asset Clearance by Exit Form ID
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
   response.put("assetSubmittedBy", form.getAssetSubmittedBy()); // ✅ ADDED FIELD
   response.put("assetSubmittedOn", form.getAssetSubmittedOn()); // ✅ Also include timestamp
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


//Update Asset Clearance
@PutMapping("/asset-clearance/{exitFormId}")
public ResponseEntity<Map<String, Object>> updateAssetClearance(
@PathVariable String exitFormId,
@RequestBody Map<String, Object> assetData,
@RequestHeader("username") String currentUser) {

try {
System.out.println("🔄 UPDATE Asset Clearance Request - ID: " + exitFormId);
System.out.println("🔄 Asset Data: " + assetData);
System.out.println("🔄 Current User: " + currentUser);

// Find existing exit form
ExitFormMaster existingForm = repo.findById(exitFormId).orElse(null);
if (existingForm == null) {
    Map<String, Object> response = new HashMap<>();
    response.put("message", "Exit form not found: " + exitFormId);
    response.put("success", false);
    return ResponseEntity.status(404).body(response);
}

// Check if asset clearance already exists
if (existingForm.getAssetClearance() == null || existingForm.getAssetClearance().trim().isEmpty()) {
    Map<String, Object> response = new HashMap<>();
    response.put("message", "No asset clearance found to update. Please submit first.");
    response.put("success", false);
    return ResponseEntity.status(400).body(response);
}

// Process and update asset data
String assetClearanceString = processAssetData(assetData);
existingForm.setAssetClearance(assetClearanceString);

// ✅ Update who submitted the asset clearance (for updates)
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

// Update audit fields (don't change status on update)
existingForm.setUpdatedOn(LocalDateTime.now().toString());
existingForm.setUpdatedBy(currentUser);

// Save the form
ExitFormMaster savedForm = repo.save(existingForm);

Map<String, Object> response = new HashMap<>();
response.put("message", "Asset clearance updated successfully");
response.put("success", true);
response.put("data", savedForm);

// ✅ Include who submitted in response
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



//1. SUBMIT PAYROLL → GO TO FINAL HR (4 → 5)
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

 // CHANGE HERE: Get the pre-built string directly
 String payrollString = (String) payload.get("payroll_checks");
 
 // If payroll_checks not found, try payrollChecks
 if (payrollString == null) {
     payrollString = (String) payload.get("payrollChecks");
 }
 
 // If still null, use empty string
 if (payrollString == null) {
     payrollString = "";
 }
 
 // ✅ ADD THIS: Store who submitted the payroll
 form.setPayrollSubmittedBy(currentUser);
 form.setPayrollChecks(payrollString);
 form.setPayrollSubmittedOn(LocalDateTime.now());

 // CRITICAL: Move to Final HR Approval
 if ("4".equals(form.getStatus())) {
     form.setStatus("5");  // ← PENDING FINAL HR
     System.out.println("PAYROLL SUBMITTED → STATUS 4 → 5 (Final HR Approval)");
 }

 form.setUpdatedOn(LocalDateTime.now().toString());
 form.setUpdatedBy(currentUser);
 repo.save(form);

 System.out.println("✅ Payroll submitted by: " + currentUser);
 System.out.println("Saved payroll string: " + payrollString);
 System.out.println("Form saved with ID: " + form.getId());

 return ResponseEntity.ok(Map.of(
     "success", true,
     "message", "Payroll submitted! Now pending Final HR Approval.",
     "status", "5",
     "payrollString", payrollString,
     "payrollSubmittedBy", currentUser // ✅ Return who submitted it
 ));

} catch (Exception e) {
 e.printStackTrace();
 return ResponseEntity.internalServerError().body(Map.of("success", false, "message", e.getMessage()));
}
}

//2. GET PAYROLL DATA (for loading in form) - UPDATED
@GetMapping("/payroll/get/{exitFormId}")
public ResponseEntity<Map<String, Object>> getPayroll(@PathVariable String exitFormId) {
try {
 ExitFormMaster form = repo.findById(exitFormId).orElse(null);
 if (form == null) {
     return ResponseEntity.status(404).body(Map.of("success", false));
 }

 String payrollChecks = form.getPayrollChecks();
 String payrollSubmittedBy = form.getPayrollSubmittedBy(); // ✅ Get submitted by
 LocalDateTime payrollSubmittedOn = form.getPayrollSubmittedOn(); // ✅ Get submission date
 
 System.out.println("Retrieved payroll checks: " + payrollChecks);
 System.out.println("Retrieved payroll submitted by: " + payrollSubmittedBy);
 System.out.println("Retrieved payroll submitted on: " + payrollSubmittedOn);
 
 return ResponseEntity.ok(Map.of(
     "success", true,
     "payrollChecks", payrollChecks != null ? payrollChecks : "",
     "payrollSubmittedBy", payrollSubmittedBy != null ? payrollSubmittedBy : "", // ✅ Include in response
     "payrollSubmittedOn", payrollSubmittedOn != null ? payrollSubmittedOn.toString() : "" // ✅ Include in response
 ));
} catch (Exception e) {
 return ResponseEntity.internalServerError().body(Map.of("success", false));
}
}

//3. UPDATE PAYROLL CHECKS (PUT) - UPDATED
@PutMapping("/payroll/update/{exitFormId}")
public ResponseEntity<Map<String, Object>> updatePayrollChecks(
 @PathVariable String exitFormId,
 @RequestBody Map<String, Object> payload,
 @RequestHeader("username") String currentUser) {

try {
 ExitFormMaster form = repo.findById(exitFormId)
         .orElseThrow(() -> new RuntimeException("Exit form not found"));

 // Get the pre-built string directly
 String payrollString = (String) payload.get("payroll_checks");
 
 // If payroll_checks not found, try payrollChecks
 if (payrollString == null) {
     payrollString = (String) payload.get("payrollChecks");
 }
 
 if (payrollString == null) {
     payrollString = "";
 }
 
 System.out.println("Updating payroll with string: " + payrollString);

 form.setPayrollChecks(payrollString);
 form.setPayrollSubmittedBy(currentUser); // ✅ Update who submitted it
 form.setPayrollSubmittedOn(LocalDateTime.now()); // ✅ Update submission date
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
     "payrollSubmittedBy", currentUser // ✅ Return who submitted it
 ));

} catch (Exception e) {
 e.printStackTrace();
 return ResponseEntity.status(500).body(Map.of(
     "success", false,
     "message", "Update failed: " + e.getMessage()
 ));
}
}

//3. FINAL HR APPROVAL → EXIT CLOSED (5 → 6)
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

     // EXTRACT VALUES FROM PAYLOAD
     String finalRemarks = (String) payload.get("finalRemarks");
     String finalChecklistData = (String) payload.get("finalChecklistData"); // THIS IS WHAT WE WANT!

     System.out.println("Final Remarks: " + finalRemarks);
     System.out.println("Final Checklist Data: " + finalChecklistData);

     // SAVE TO CORRECT COLUMNS
     form.setFinalHrRemarks(finalRemarks != null ? finalRemarks : "");
     form.setFinalChecklistData(finalChecklistData != null && !finalChecklistData.trim().isEmpty() 
         ? finalChecklistData 
         : null); // Save exactly as received

     form.setFinalHrApprovedBy(currentUser);
     form.setFinalHrApprovedOn(LocalDateTime.now().toString());
     form.setFinalHrSubmittedOn(LocalDateTime.now());

     // CHANGE STATUS TO 6 → EXIT CLOSED
     if ("5".equals(form.getStatus())) {
         form.setStatus("6");
         System.out.println("Status updated to 6 - EXIT CLOSED");
     }

     // Audit fields
     form.setUpdatedOn(LocalDateTime.now().toString());
     form.setUpdatedBy(currentUser);

     ExitFormMaster savedForm = repo.save(form);
     System.out.println("Final HR approval saved! Checklist data stored.");

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

//GET FINAL HR DATA (for loading already submitted form)
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

        // ✅ ADD THIS LINE: Set HR Round 2 submission timestamp
        form.setHrRound2SubmittedOn(LocalDateTime.now());

        // CORRECT STATUS AFTER HR OFFBOARDING = 4 (Pending Payroll)
        form.setStatus("4");

        // ✅ Use LocalDateTime instead of formatted string
        form.setUpdatedOn(LocalDateTime.now().toString());
        form.setUpdatedBy(user != null && !user.isEmpty() ? user : "HR");

        repo.save(form);

        System.out.println("✅ HR Offboarding saved with timestamp: " + form.getHrRound2SubmittedOn());

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
    
}