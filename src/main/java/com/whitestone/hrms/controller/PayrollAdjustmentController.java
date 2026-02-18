package com.whitestone.hrms.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitestone.entity.EmployeeLeaveSummary;
import com.whitestone.entity.EmployeeSalaryTbl;
import com.whitestone.entity.PayrollAdjustment;
import com.whitestone.entity.TraineeMaster;
import com.whitestone.entity.UserMasterAttendanceMod;
import com.whitestone.entity.WsslCalendarMod;
import com.whitestone.entity.usermaintenance;
import com.whitestone.hrms.repo.EmployeeLeaveSummaryRepository;
import com.whitestone.hrms.repo.EmployeeSalaryTblRepository;
import com.whitestone.hrms.repo.PayrollAdjustmentRepository;
import com.whitestone.hrms.repo.TraineemasterRepository;
import com.whitestone.hrms.repo.UserMasterAttendanceModRepository;
import com.whitestone.hrms.repo.WsslCalendarModRepository;
import com.whitestone.hrms.service.EmailService;

@RestController
@RequestMapping("/payroll-adjustments")
public class PayrollAdjustmentController {

    @Autowired
    private PayrollAdjustmentRepository payrollAdjustmentRepository;

    @Autowired
    private com.whitestone.hrms.repo.usermaintenanceRepository usermaintenanceRepository; // Assuming this exists

    @Autowired
    private EmployeeSalaryTblRepository employeeSalaryTblRepository; // Assuming this exists

    @Autowired
    private EmployeeLeaveSummaryRepository employeeLeaveSummaryRepository; // Assuming this exists

    @Autowired
    private WsslCalendarModRepository wsslCalendarModRepository; // Assuming this exists

    // Add other autowired repositories as needed from the sample code
    
    @Autowired
    private AppController appController;
    
	@Autowired
	UserMasterAttendanceModRepository usermasterattendancemodrepository;
	
	@Autowired
	TraineemasterRepository traineemasterRepository;

	@Autowired
	private EmailService emailService;

    // Endpoint to generate draft adjustments for the current month
	@PostMapping("/generate")
	public ResponseEntity<Map<String, Object>> generateAdjustments() {
	    Map<String, Object> response = new HashMap<>();
	    YearMonth currentMonth = YearMonth.now();
	    List<PayrollAdjustment> adjustments = new ArrayList<>();

	    try {
	        // ========== 1. GET ALL EMPLOYEE IDs FROM USERMAINTENANCE ==========
	        List<String> employeeIds = usermaintenanceRepository.findAll()
	                .stream()
	                .map(usermaintenance::getEmpid)
	                .collect(Collectors.toList());
	        
	        System.out.println("Total employees found in usermaintenance: " + employeeIds.size());

	        // ========== 2. GET ALL TRAINEE IDs (trng_id) FROM TRAINEE MASTER ==========
	        List<String> traineeIds = new ArrayList<>();
	        try {
	            // Fetch all active trainees using trng_id
	            List<TraineeMaster> activeTrainees = traineemasterRepository.findAllActiveTrainees();
	            traineeIds = activeTrainees.stream()
	                    .map(TraineeMaster::getTrngid)  // Using trng_id as employee ID
	                    .filter(id -> id != null && !id.isEmpty())
	                    .collect(Collectors.toList());
	            
	            System.out.println("Total active trainees found (using trng_id): " + traineeIds.size());
	        } catch (Exception e) {
	            System.out.println("Error fetching trainees: " + e.getMessage());
	            // Continue with employees only if trainee fetch fails
	        }

	        // ========== 3. COMBINE BOTH LISTS (EMPLOYEES + TRAINEES) ==========
	        Set<String> allUniqueIds = new HashSet<>();
	        allUniqueIds.addAll(employeeIds);
	        allUniqueIds.addAll(traineeIds);
	        
	        List<String> allIds = new ArrayList<>(allUniqueIds);
	        System.out.println("Total unique users to process (Employees + Trainees): " + allIds.size());

	        LocalDate periodStart = LocalDate.of(
	                currentMonth.minusMonths(1).getYear(),
	                currentMonth.minusMonths(1).getMonth(),
	                27
	        );
	        LocalDate periodEnd = LocalDate.of(
	                currentMonth.getYear(),
	                currentMonth.getMonth(),
	                26
	        );

	        long totalPeriodDays = ChronoUnit.DAYS.between(periodStart, periodEnd) + 1;
	        System.out.println("Pay period: " + periodStart + " to " + periodEnd + ", Total days: " + totalPeriodDays);

	        // Counter variables for statistics
	        int employeeCount = 0;
	        int traineeCount = 0;
	        int skippedCount = 0;
	        int noSalaryCount = 0;

	        // ========== 4. PROCESS ALL IDs (BOTH EMPLOYEES AND TRAINEES) ==========
	        for (String empId : allIds) {
	            try {
	                System.out.println("\nProcessing ID: " + empId);
	                
	                // Determine if this is an employee or trainee
	                boolean isEmployee = employeeIds.contains(empId);
	                boolean isTrainee = traineeIds.contains(empId);
	                
	                String userType = isEmployee ? "EMPLOYEE" : "TRAINEE";
	                System.out.println("User type: " + userType);

	                // Get user details based on type
	                String employeeName = "";
	                String managerId = "";
	                
	                if (isEmployee) {
	                    // Fetch from usermaintenance for employees
	                    usermaintenance user = usermaintenanceRepository.findByEmpid(empId);
	                    if (user == null) {
	                        System.out.println("User not found for empId: " + empId);
	                        continue;
	                    }
	                    employeeName = (user.getFirstname() + " " + 
	                                 (user.getLastname() != null ? user.getLastname() : "")).trim();
	                    managerId = user.getRepoteTo();
	                    employeeCount++;
	                } else if (isTrainee) {
	                    // Fetch from trainee master using trng_id for trainees
	                    TraineeMaster trainee = traineemasterRepository.findByTrngid(empId);
	                    if (trainee == null) {
	                        System.out.println("Trainee not found for trng_id: " + empId);
	                        continue;
	                    }
	                    employeeName = (trainee.getFirstname() + " " + 
	                                  (trainee.getLastname() != null ? trainee.getLastname() : "")).trim();
	                    managerId = trainee.getRepoteTo();
	                    traineeCount++;
	                } else {
	                    System.out.println("Unknown user type for ID: " + empId);
	                    continue;
	                }

	                // Get salary details (same table for both)
	                EmployeeSalaryTbl salary = employeeSalaryTblRepository.findByEmpid(empId);
	                if (salary == null) {
	                    System.out.println("Salary not found for ID: " + empId);
	                    noSalaryCount++;
	                    continue;
	                }

	                // Check if employee has "Per Day Allowance" in earnings
	                boolean hasPerDayAllowance = false;
	                try {
	                    String earningsJson = appController.cleanJson(salary.getEarnings());
	                    if (earningsJson != null && !earningsJson.isBlank()) {
	                        JsonNode earningsNode = new ObjectMapper().readTree(earningsJson);
	                        for (JsonNode node : earningsNode) {
	                            String name = node.has("name") ? node.get("name").asText("").trim() : "";
	                            if ("Per Day Allowance".equalsIgnoreCase(name)) {
	                                hasPerDayAllowance = true;
	                                break;
	                            }
	                        }
	                    }
	                } catch (Exception ignored) {
	                    // JSON invalid or empty → no per-day allowance
	                }

	                long allowanceDays = hasPerDayAllowance ? totalPeriodDays : 0L;
	                
	                List<UserMasterAttendanceMod> attendanceRecords =
	                        usermasterattendancemodrepository.findByAttendanceidAndDateRange(
	                                empId, java.sql.Date.valueOf(periodStart), java.sql.Date.valueOf(periodEnd));

	                // Calculate Working Days Based on Status
	                double effectiveWorkingDays = attendanceRecords.stream()
	                        .mapToDouble(rec -> {
	                            if (rec.getStatus() == null) return 0d;
	                            String status = rec.getStatus().trim().toUpperCase();
	                            switch (status) {
	                            case "PRESENT":
	                            case "EARLY LEAVE":
	                            case "COMP OFF":
	                            case "OD":
	                            case "ABSENT":
	                            case "MISS PUNCH":
	                            case "FORGOT":
	                                return 1.0;
	                            case "HALF DAY":
	                                return 0.5;
	                            default:
	                                return 0.0;
	                        }
	                        })
	                        .sum();

	                // Get LOP days
	                EmployeeLeaveSummary leaveSummary = employeeLeaveSummaryRepository.findByEmpId_AndYear(empId, Year.now().getValue());
	                float lopDays = 0f;
	                if (leaveSummary != null) {
	                    switch (currentMonth.getMonth()) {
	                        case JANUARY: lopDays = leaveSummary.getLopJan(); break;
	                        case FEBRUARY: lopDays = leaveSummary.getLopFeb(); break;
	                        case MARCH: lopDays = leaveSummary.getLopMar(); break;
	                        case APRIL: lopDays = leaveSummary.getLopApr(); break;
	                        case MAY: lopDays = leaveSummary.getLopMay(); break;
	                        case JUNE: lopDays = leaveSummary.getLopJun(); break;
	                        case JULY: lopDays = leaveSummary.getLopJul(); break;
	                        case AUGUST: lopDays = leaveSummary.getLopAug(); break;
	                        case SEPTEMBER: lopDays = leaveSummary.getLopSep(); break;
	                        case OCTOBER: lopDays = leaveSummary.getLopOct(); break;
	                        case NOVEMBER: lopDays = leaveSummary.getLopNov(); break;
	                        case DECEMBER: lopDays = leaveSummary.getLopDec(); break;
	                    }
	                }

	                // Prevent duplicate generation
	                if (payrollAdjustmentRepository.findByEmpIdAndMonth(empId, currentMonth.toString()) != null) {
	                    System.out.println("Duplicate found for " + empId + " - skipping");
	                    skippedCount++;
	                    continue;
	                }

	                PayrollAdjustment adj = new PayrollAdjustment();
	                adj.setEmpId(empId);
	                adj.setEmployeeName(employeeName);
	                adj.setMonth(currentMonth.toString());
	                adj.setAllowanceDays(allowanceDays);
	                adj.setLopDays(lopDays);
	                adj.setOtherDeductions(0.0);
	                adj.setEffectiveWorkingDays(effectiveWorkingDays);
	                adj.setManagerId(managerId);
	                adj.setApprovalStatus("PENDING");
	                adj.setCreatedDate(LocalDateTime.now());
	                adj.setUpdatedDate(LocalDateTime.now());
	                
	                // Optional: You can add a field to distinguish between employee and trainee
	                // adj.setUserType(userType); // If you add this field to your entity

	                adjustments.add(adj);
	                System.out.println("Added adjustment for " + userType + ": " + empId);
	                
	            } catch (Exception e) {
	                System.err.println("Error processing ID " + empId + ": " + e.getMessage());
	                e.printStackTrace();
	            }
	        }

	        // ========== 5. SAVE ALL ADJUSTMENTS ==========
	        if (!adjustments.isEmpty()) {
	            payrollAdjustmentRepository.saveAllAndFlush(adjustments);
	            
	            // Prepare response
	            String statusMessage = String.format(
	                "Adjustments generated successfully for %s. Employees: %d, Trainees: %d, Total: %d",
	                currentMonth, employeeCount, traineeCount, adjustments.size()
	            );
	            
	            response.put("status", statusMessage);
	            response.put("generated_count", adjustments.size());
	            response.put("employee_count", employeeCount);
	            response.put("trainee_count", traineeCount);
	            response.put("skipped_count", skippedCount);
	            response.put("no_salary_count", noSalaryCount);
	            response.put("month", currentMonth.toString());
	            
	            // Print summary to console
	            System.out.println("\n=== FINAL SUMMARY ===");
	            System.out.println("Total employees processed: " + employeeCount);
	            System.out.println("Total trainees processed: " + traineeCount);
	            System.out.println("Total adjustments saved: " + adjustments.size());
	            System.out.println("Skipped (duplicates): " + skippedCount);
	            System.out.println("No salary found: " + noSalaryCount);
	            
	        } else {
	            response.put("status", "No new adjustments generated — already exists or no eligible employees/trainees");
	            System.out.println("No adjustments generated");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("error", "Failed to generate adjustments: " + e.getMessage());
	    }

	    return ResponseEntity.ok(response);
	}


    // Get adjustments for a manager's reportees
    @GetMapping("/manager/{managerId}/{month}")
    public List<PayrollAdjustment> getForManager(@PathVariable String managerId, @PathVariable String month) {
        return payrollAdjustmentRepository.findByManagerIdAndMonth(managerId, month);
    }

    // Update adjustment (edit allowance days, LOP, other deductions)
    @PutMapping("/{id}")
    public PayrollAdjustment update(@PathVariable Long id, @RequestBody PayrollAdjustment update) {
        PayrollAdjustment existing = payrollAdjustmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Adjustment not found"));
        existing.setAllowanceDays(update.getAllowanceDays());
        existing.setLopDays(update.getLopDays());
        existing.setOtherDeductions(update.getOtherDeductions());
        existing.setOtherDeductionsRemarks(update.getOtherDeductionsRemarks());
        existing.setEffectiveWorkingDays(update.getEffectiveWorkingDays());
        existing.setUpdatedDate(LocalDateTime.now());
        return payrollAdjustmentRepository.save(existing);
    }

    // Approve adjustment
    @PostMapping("/approve/{id}")
    public PayrollAdjustment approve(@PathVariable Long id) {
        PayrollAdjustment pa = payrollAdjustmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Adjustment not found"));
        pa.setApprovalStatus("APPROVED");
        pa.setUpdatedDate(LocalDateTime.now());
        return payrollAdjustmentRepository.save(pa);
    }

    // Reject adjustment (optional)
    @PostMapping("/reject/{id}")
    public PayrollAdjustment reject(@PathVariable Long id) {
        PayrollAdjustment pa = payrollAdjustmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Adjustment not found"));
        pa.setApprovalStatus("REJECTED");
        pa.setUpdatedDate(LocalDateTime.now());
        return payrollAdjustmentRepository.save(pa);
    }
    @GetMapping("/all")
    public List<PayrollAdjustment> getAllAdjustments() {
        YearMonth current = YearMonth.now();
        return payrollAdjustmentRepository.findByMonth(current.toString());
    }

    @PostMapping("/admin-approve/{id}")
    public ResponseEntity<Map<String, String>> adminApprove(@PathVariable Long id) {
        PayrollAdjustment pa = payrollAdjustmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Not found"));
        pa.setApprovalStatus("APPROVED");
        pa.setUpdatedDate(LocalDateTime.now());
        payrollAdjustmentRepository.save(pa);

        // Optional: Send email to manager
        // sendEmail(pa.getManagerId(), "Adjustment Approved", pa.getEmployeeName() + " payroll adjustment approved by Admin");

        return ResponseEntity.ok(Map.of("status", "Approved by Admin"));
    }

    @PostMapping("/admin-reject/{id}")
    public ResponseEntity<Map<String, String>> adminReject(@PathVariable Long id, @RequestBody Map<String, String> body) {
        PayrollAdjustment pa = payrollAdjustmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Not found"));
        
        String remarks = body.get("remarks");
        pa.setApprovalStatus("REJECTED");
        pa.setPayrollRejectRemarks("REJECTED BY HR/ADMIN: " + remarks);
        pa.setUpdatedDate(LocalDateTime.now());
        payrollAdjustmentRepository.save(pa);

        // Send Email to Manager
        emailService.sendRejectionEmailToManager(pa, remarks);

        return ResponseEntity.ok(Map.of("status", "Rejected and notified"));
    }
   
}