package com.whitestone.hrms.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.crypto.SecretKey;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.PropertyValueException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitestone.entity.AdvancesDetailsMod;
import com.whitestone.entity.AttendanceChangeRequest;
import com.whitestone.entity.CompanyLocation;
import com.whitestone.entity.Department;
import com.whitestone.entity.Designation;
import com.whitestone.entity.EmployeeAddressMod;
import com.whitestone.entity.EmployeeEducationDetailsMod;
import com.whitestone.entity.EmployeeFingerprint;
import com.whitestone.entity.EmployeeLeaveMasterTbl;
import com.whitestone.entity.EmployeeLeaveModTbl;
import com.whitestone.entity.EmployeeLeaveSummary;
import com.whitestone.entity.EmployeePermissionMasterTbl;
import com.whitestone.entity.EmployeePhoto;
import com.whitestone.entity.EmployeeProfessionalDetailsMod;
import com.whitestone.entity.EmployeeProfile;
import com.whitestone.entity.EmployeeProfileMod;
import com.whitestone.entity.EmployeeProjectHistory;
import com.whitestone.entity.EmployeeSalaryHistory;
import com.whitestone.entity.EmployeeSalaryTbl;
import com.whitestone.entity.EmployeeSkillMod;
import com.whitestone.entity.ExpenseDetailsMod;
import com.whitestone.entity.LocationAllowance;
import com.whitestone.entity.LocationAllowance.AllowanceType;
import com.whitestone.entity.Organization;
import com.whitestone.entity.PTSlab;
import com.whitestone.entity.Payroll;
import com.whitestone.entity.PayrollAdjustment;
import com.whitestone.entity.PayrollHistory;
import com.whitestone.entity.SalaryComponent;
import com.whitestone.entity.SalaryTemplate;
import com.whitestone.entity.SalaryTemplateComponent;
import com.whitestone.entity.StatutorySettings;
import com.whitestone.entity.TraineeMaster;
import com.whitestone.entity.TravelEntityMod;
import com.whitestone.entity.UserMasterAttendanceMod;
import com.whitestone.entity.UserRoleMaintenance;
import com.whitestone.entity.WsslCalendarMod;
import com.whitestone.entity.usermaintenance;
import com.whitestone.entity.usermaintenancemod;
import com.whitestone.hrms.repo.AdvancesDetailsModRepository;
import com.whitestone.hrms.repo.AttendanceChangeRequestRepository;
import com.whitestone.hrms.repo.CompanyLocationRepository;
import com.whitestone.hrms.repo.DepartmentRepository;
import com.whitestone.hrms.repo.DesignationRepository;
import com.whitestone.hrms.repo.EmployeeAddressModRepository;
import com.whitestone.hrms.repo.EmployeeEducationDetailsModRepository;
import com.whitestone.hrms.repo.EmployeeFingerprintRepository;
import com.whitestone.hrms.repo.EmployeeLeaveMasterTblRepository;
import com.whitestone.hrms.repo.EmployeeLeaveModTblRepository;
import com.whitestone.hrms.repo.EmployeeLeaveSummaryRepository;
import com.whitestone.hrms.repo.EmployeePermissionMasterRepository;
import com.whitestone.hrms.repo.EmployeePhotoRepository;
import com.whitestone.hrms.repo.EmployeeProfessionalDetailsModRepository;
import com.whitestone.hrms.repo.EmployeeProfileModRepository;
import com.whitestone.hrms.repo.EmployeeProfileRepository;
import com.whitestone.hrms.repo.EmployeeProjectHistoryRepository;
import com.whitestone.hrms.repo.EmployeeSalaryHistoryTblRepository;
import com.whitestone.hrms.repo.EmployeeSalaryTblRepository;
import com.whitestone.hrms.repo.EmployeeSkillModRepository;
import com.whitestone.hrms.repo.ExpenseDetailsModRepository;
import com.whitestone.hrms.repo.ExpenseDetailsRepository;
import com.whitestone.hrms.repo.LocationAllowanceRepository;
import com.whitestone.hrms.repo.OrganizationRepository;
import com.whitestone.hrms.repo.PTSlabRepository;
import com.whitestone.hrms.repo.PayrollAdjustmentRepository;
import com.whitestone.hrms.repo.PayrollHistoryRepository;
import com.whitestone.hrms.repo.PayrollRepository;
import com.whitestone.hrms.repo.SalaryComponentRepository;
import com.whitestone.hrms.repo.SalaryTemplateComponentRepository;
import com.whitestone.hrms.repo.SalaryTemplatePayload;
import com.whitestone.hrms.repo.SalaryTemplateRepository;
import com.whitestone.hrms.repo.StatutorySettingsRepository;
import com.whitestone.hrms.repo.TraineemasterRepository;
import com.whitestone.hrms.repo.TravelModRepository;
import com.whitestone.hrms.repo.UserMasterAttendanceModRepository;
import com.whitestone.hrms.repo.UserMasterAttendanceRepository;
import com.whitestone.hrms.repo.UserRoleMaintenanceRepository;
import com.whitestone.hrms.repo.WsslCalendarModRepository;
import com.whitestone.hrms.repo.usermaintenanceRepository;
import com.whitestone.hrms.repo.usermaintenancemodRepository;
import com.whitestone.hrms.service.AdvanceService;
import com.whitestone.hrms.service.EmailService;
import com.whitestone.hrms.service.ErrorMessageService;
import com.whitestone.hrms.service.ExpenseDetailsService;
import com.whitestone.hrms.service.PayslipService;
import com.whitestone.hrms.service.ResourceNotFoundException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@RestController
@Controller
@Service
public class AppController {

	@Value("${file.upload.dir}")
    private String docUploadDir;

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	private ErrorMessageService errorMessageService;

	@Autowired
	private usermaintenanceRepository usermaintenanceRepository;

	@Autowired
	private TraineemasterRepository traineemasterRepository;

	@Autowired
	private usermaintenancemodRepository userMaintenanceRepository;

	@Autowired
	private EmployeeProfileRepository employeeProfileRepository2;

	@Autowired
	private EmployeeProfileModRepository employeeProfilemodRepository;

	@Autowired
	private EmployeeAddressModRepository employeeAddressModRepository;

	@Autowired
	private EmployeeEducationDetailsModRepository employeeEducationDetailsModRepository;

	@Autowired
	private EmployeeProfessionalDetailsModRepository employeeProfessionalDetailsModRepository;

	@Autowired
	private EmployeeSkillModRepository employeeSkillModRepository;

	@Autowired
	UserMasterAttendanceModRepository usermasterattendancemodrepository;

	@Autowired
	private TravelModRepository travelModRepository;

	@Autowired
	private UserRoleMaintenanceRepository userRoleMaintenanceRepository;

	@Autowired
	private AdvanceService advanceService;

	@Autowired
	private EmployeePermissionMasterRepository employeePermissionMasterRepository;

	// User login endpoint
	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> login(@RequestBody usermaintenance employeeDetails) {
		System.out.println("TEST");
		System.out.println("sedfg" + employeeDetails);
		try {
			String employeeid = employeeDetails.getUsername();
			String rawPassword = employeeDetails.getPassword();

			Optional<usermaintenance> employeeOpt = Optional.empty();
			Optional<TraineeMaster> employeeOpt1 = Optional.empty();

			if (employeeid.toUpperCase().startsWith("WS")) {
				employeeOpt1 = traineemasterRepository.findByTrngidOrUserId(employeeid.toUpperCase());
				System.out.println("employeeid::::   " + employeeOpt1.get().getFirstname());
			} else {
				employeeOpt = usermaintenanceRepository.findByEmpIdOrUserId(employeeid);
			}

			if ((employeeOpt.isPresent() && passwordEncoder.matches(rawPassword, employeeOpt.get().getPassword()))
					|| (employeeOpt1.isPresent()
							&& passwordEncoder.matches(rawPassword, employeeOpt1.get().getPassword()))) {

				Map<String, Object> response = new HashMap<>();

				if (employeeOpt.isPresent()) {
					usermaintenance employee = employeeOpt.get();
					String role = userRoleMaintenanceRepository.findByRoleid(employee.getRoleid())
							.map(UserRoleMaintenance::getRolename).orElse("Unknown Role");

					String employeeId = employee.getEmpid();
					String employeeName = employee.getFirstname();
					String employeeEmail = employee.getEmailid();
					String reportTo = employee.getRepoteTo();

					// 🔹 Fetch Manager Name from same table (usermaintenance)
					String managerName = null;
					if (reportTo != null && !reportTo.isEmpty()) {
						usermaintenance manager = usermaintenanceRepository.findByEmpid(reportTo);
						if (manager != null) {
							managerName = manager.getFirstname()
									+ (manager.getLastname() != null ? " " + manager.getLastname() : "");
						}
					}

					String token = generateToken(employeeId, role);
					String successMessage = errorMessageService.getErrorMessage("VALID_USR_CREDENTIALS", "en");

					response.put("message", successMessage);
					response.put("role", role);
					response.put("employeeId", employeeId);
					response.put("username", employeeName);
					response.put("email", employeeEmail);
					response.put("token", token);
					response.put("reportTo", reportTo);
					response.put("managerName", managerName); // ✅ Added Manager Name
				}

				if (employeeOpt1.isPresent()) {
					TraineeMaster employee = employeeOpt1.get();
					String role = userRoleMaintenanceRepository.findByRoleid(employee.getRoleid())
							.map(UserRoleMaintenance::getRolename).orElse("Unknown Role");

					String employeeId = employee.getTrngid();
					String employeeName = employee.getFirstname();
					String employeeEmail = employee.getEmailid();
					String reportTo = employee.getRepoteTo();

					// 🔹 Fetch Manager Name from same table (TraineeMaster)
					String managerName = null;
					if (reportTo != null && !reportTo.isEmpty()) {
						usermaintenance manager = usermaintenanceRepository.findByEmpid(reportTo);
						if (manager != null) {
							managerName = manager.getFirstname()
									+ (manager.getLastname() != null ? " " + manager.getLastname() : "");
						}
					}

					String token = generateToken(employeeId, role);
					String successMessage = errorMessageService.getErrorMessage("VALID_USR_CREDENTIALS", "en");

					response.put("message", successMessage);
					response.put("role", role);
					response.put("employeeId", employeeId);
					response.put("username", employeeName);
					response.put("email", employeeEmail);
					response.put("token", token);
					response.put("reportTo", reportTo);
					response.put("managerName", managerName); // ✅ Added Manager Name
				}

				return ResponseEntity.ok(response);

			} else {
				String errorMessage = errorMessageService.getErrorMessage("INVALID_USR_CREDENTIALS", "en");
				Map<String, Object> errorResponse = new HashMap<>();
				errorResponse.put("error", errorMessage);
				return ResponseEntity.badRequest().body(errorResponse);
			}

		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = errorMessageService.getErrorMessage("INTERNAL_SERVER_ERROR", "en");
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", errorMessage);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}
	
	
	
	public String generateToken(String employeeId, String role) {
		// Set expiration date for token (1 hour from now)
		long expirationTime = 1000 * 60 * 60; // 1 hour in milliseconds
		Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

		// Generate a secure signing key for HS256 algorithm (256 bits)
		SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Secure key for HS256

		// Generate JWT token using the employeeId and role
		return Jwts.builder().setSubject(employeeId).claim("role", role) // You can add more claims as needed
				.setIssuedAt(new Date()).setExpiration(expirationDate).signWith(secretKey) // Use the generated secret
																							// key
				.compact();
	}

	@PostMapping("/checkIn")
	public ResponseEntity<?> checkIn(@RequestBody UserMasterAttendanceMod attendancePayload) {
		return handleAttendanceRecord(attendancePayload.getAttendanceid(), true, attendancePayload,
				attendancePayload.getSrlnum());
	}

	@PostMapping("/checkOut")
	public ResponseEntity<?> checkOut(@RequestBody UserMasterAttendanceMod attendancePayload) {
		if (attendancePayload.getAttendanceid() == null || attendancePayload.getSrlnum() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("{\"error\": \"Invalid attendance ID or serial number\"}");
		}
		return handleAttendanceRecord(attendancePayload.getAttendanceid(), false, attendancePayload,
				attendancePayload.getSrlnum());
	}

	private ResponseEntity<?> handleAttendanceRecord(String employeeId, boolean isCheckIn,
			UserMasterAttendanceMod attendancePayload, Long srlNum) {
		try {
			if (employeeId == null || employeeId.trim().isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Invalid employee ID\"}");
			}

			// Initialize employee and trainee optional objects
			Optional<usermaintenance> employeeOpt = Optional.empty();
			Optional<TraineeMaster> traineeOpt = Optional.empty();
			String userIdForAttendance = null;

			// Fetch employee or trainee based on ID prefix
			if (employeeId.toUpperCase().startsWith("WS")) {
				traineeOpt = traineemasterRepository.findByTrngidOrUserId(employeeId.toUpperCase());
				if (!traineeOpt.isPresent()) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Trainee not found\"}");
				}
				userIdForAttendance = traineeOpt.get().getUserid();
			} else {
				employeeOpt = usermaintenanceRepository.findByEmpIdOrUserId(employeeId);
				if (!employeeOpt.isPresent()) {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Employee not found\"}");
				}
				userIdForAttendance = employeeOpt.get().getUserid();
			}

			if (isCheckIn) {
				// Check-in logic
				Optional<UserMasterAttendanceMod> existingAttendanceToday = usermasterattendancemodrepository
						.findByAttendanceidAndAttendancedate(employeeId, java.sql.Date.valueOf(LocalDate.now()));

				if (existingAttendanceToday.isPresent()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("{\"error\": \"Already checked in for today\"}");
				}

				// Create new attendance record
				UserMasterAttendanceMod attendance = new UserMasterAttendanceMod();
				attendance.setUserid(userIdForAttendance);
				attendance.setAttendanceid(attendancePayload.getAttendanceid());
				attendance.setStatus("Present");
				attendance.setCheckinstatus(attendancePayload.getStatus());
				attendance.setCheckinlocation(attendancePayload.getCheckinlocation());
				attendance.setRcreuserid(attendancePayload.getAttendanceid());
				attendance.setAttendancedate(new java.sql.Date(System.currentTimeMillis()));
				attendance.setCheckintime(new java.sql.Timestamp(System.currentTimeMillis()));
				attendance.setRcretime(new java.sql.Timestamp(System.currentTimeMillis()));
				attendance.setRcreuserid(employeeId);
				attendance.setRmodtime(new java.sql.Timestamp(System.currentTimeMillis()));
				attendance.setRmoduserid(employeeId);

				// Save the record
				usermasterattendancemodrepository.save(attendance);

				Map<String, Object> response = new HashMap<>();
				response.put("message", "Check-in success");
				return ResponseEntity.ok(response);
			} else {
				// Checkout logic
				Optional<UserMasterAttendanceMod> latestRecordOpt = usermasterattendancemodrepository
						.findByAttendanceidAndSrlnum(employeeId, srlNum);

				if (!latestRecordOpt.isPresent()) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("{\"error\": \"No check-in found. Cannot check out.\"}");
				}

				UserMasterAttendanceMod attendance = latestRecordOpt.get();

				if (attendance.getCheckouttime() != null) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("{\"error\": \"Already checked out for this day\"}");
				}

				attendance.setCheckoutlocation(attendancePayload.getCheckinlocation());
				Date checkinTime = attendance.getCheckintime();
				Date checkoutTime = new Date();

				long durationMillis = checkoutTime.getTime() - checkinTime.getTime();
				long durationMinutes = durationMillis / 60000;
				long durationHours = durationMinutes / 60;
				long remainingMinutes = durationMinutes % 60;
				String totalWorkedTime = String.format("%02d:%02d", durationHours, remainingMinutes);
				attendance.setTotalhoursworked(totalWorkedTime);

				// Calculate status using the provided method
				attendance.setStatus(calculateStatus(durationHours));

				// Calculate overtime for logging
				String overtime = "None";
				if (durationHours >= 8) {
					long overtimeMinutes = durationMinutes - (8 * 60);
					long overtimeHours = overtimeMinutes / 60;
					long overtimeRemainingMinutes = overtimeMinutes % 60;
					overtime = String.format("%02d:%02d", overtimeHours, overtimeRemainingMinutes);
				}

				// Set checkout details
				attendance.setCheckouttime(checkoutTime);
				attendance.setCheckoutstatus(attendancePayload.getStatus());
				attendance.setRcretime(checkoutTime);
				attendance.setRcreuserid(employeeId);
				attendance.setRmodtime(checkoutTime);
				attendance.setRmoduserid(employeeId);

				// Log details
				System.out.println("Total Worked Time: " + totalWorkedTime);
				System.out.println("Overtime: " + overtime);

				// Save updated record
				usermasterattendancemodrepository.save(attendance);

				Map<String, Object> response = new HashMap<>();
				response.put("message", "Check-out success");
				response.put("totalWorkedTime", totalWorkedTime);
				response.put("overtime", overtime);
				return ResponseEntity.ok(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\": \"Failed to process attendance: " + e.getMessage() + "\"}");
		}
	}

	private String calculateStatus(long durationHours) {
		DayOfWeek day = LocalDate.now().getDayOfWeek();

		// ✅ Full day logic
		if (durationHours >= 8) {
			return "Present";
		}
		// ✅ Early leave logic (between 6 and 8 hours)
		else if (durationHours >= 6 && durationHours < 8) {
			return "Early Leave";
		}
		// ✅ Half-day logic (between 4 and 6 hours, except Saturday)
		else if (durationHours >= 4 && durationHours < 6) {
			return day == DayOfWeek.SATURDAY ? "Present" : "Half-Day";
		}
		// ✅ Less than 4 hours → Absent
		else {
			return "Absent";
		}
	}

	private ResponseEntity<?> successResponse(String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", message);
		return ResponseEntity.ok(response);
	}

	private ResponseEntity<?> errorResponse(HttpStatus status, String error) {
		Map<String, Object> response = new HashMap<>();
		response.put("success", false);
		response.put("error", error);
		return ResponseEntity.status(status).body(response);
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> resetData) {
		try {
			String employeeId = resetData.get("employeeId");
			String oldPassword = resetData.get("oldPassword");
			String newPassword = resetData.get("newPassword");

			if (employeeId == null || oldPassword == null || newPassword == null) {
				return ResponseEntity.badRequest().body(Map.of("error", "All fields are required"));
			}

			Optional<usermaintenance> userOpt = usermaintenanceRepository.findByEmpIdOrUserId(employeeId);
			if (userOpt.isPresent()) {
				usermaintenance user = userOpt.get();
				if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
					return ResponseEntity.badRequest().body(Map.of("error", "Old password is incorrect"));
				}

				user.setPassword(passwordEncoder.encode(newPassword));
				usermaintenanceRepository.save(user);
				return ResponseEntity.ok(Map.of("message", "Password reset successfully for employee"));
			}

			TraineeMaster trainee = traineemasterRepository.findByTrngid(employeeId);
			if (trainee != null) {
				if (!passwordEncoder.matches(oldPassword, trainee.getPassword())) {
					return ResponseEntity.badRequest().body(Map.of("error", "Old password is incorrect"));
				}

				trainee.setPassword(passwordEncoder.encode(newPassword));
				trainee.setRmodtime(new Date());
				traineemasterRepository.save(trainee);
				return ResponseEntity.ok(Map.of("message", "Password reset successfully for trainee"));
			}

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to reset password"));
		}
	}

	private final Map<String, String> otpCache = new HashMap<>();

	/**
	 * Send OTP for password reset
	 */
	@GetMapping("/sendOtp")
	public ResponseEntity<?> sendOtp(@RequestParam String employeeId) {
		try {
			String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

			// Check both tables
			String email = usermaintenanceRepository.findEmailByEmpId(employeeId);
			if (email == null) {
				email = traineemasterRepository.findEmailByTrngid(employeeId);
			}

			if (email == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Map.of("error", "User not found for ID: " + employeeId));
			}

			otpCache.put(employeeId, otp);

			String subject = "Whitestone HRMS - Password Reset OTP";
			String body = "Dear Employee,\n\n" + "Your OTP for password reset is: " + otp + "\n\n"
					+ "This OTP will expire soon. Please do not share it with anyone.\n\n" + "Regards,\n"
					+ "Whitestone HRMS";

			// Using MIME Message with CC
			emailService.sendEmail(email, subject, body);

			return ResponseEntity.ok(Map.of("message", "OTP sent to registered email ID."));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Failed to send OTP: " + e.getMessage()));
		}
	}

	// ✅ 2. CHANGE PASSWORD WITH OTP
	@PostMapping("/changePasswordWithOtp")
	public ResponseEntity<?> changePasswordWithOtp(@RequestBody Map<String, String> payload) {
		String employeeId = payload.get("employeeId");
		String otp = payload.get("otp");
		String newPassword = payload.get("newPassword");

		if (employeeId == null || otp == null || newPassword == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Missing required fields."));
		}

		String cachedOtp = otpCache.get(employeeId);
		if (cachedOtp == null || !cachedOtp.equals(otp)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid or expired OTP."));
		}

		// Update password in usermaintenance or trainee master
		int updatedRows = usermaintenanceRepository.updatePasswordByEmpId(passwordEncoder.encode(newPassword),
				employeeId);

		if (updatedRows == 0) {
			updatedRows = traineemasterRepository.updatePasswordByTrngid(passwordEncoder.encode(newPassword),
					employeeId);
		}

		if (updatedRows > 0) {
			otpCache.remove(employeeId);
			return ResponseEntity.ok(Map.of("message", "Password updated successfully."));
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found."));
	}

	@GetMapping("/attendance/status/{employeeId}")
	public ResponseEntity<?> getCheckInStatus(@PathVariable String employeeId) {
		try {
			// Fetch the most recent attendance record for the employee based on SRL_NUM
			Date date = new Date();
			Optional<UserMasterAttendanceMod> latestRecord = usermasterattendancemodrepository
					.findLatestRecordBySrlNo(employeeId, date);
			if (latestRecord.isEmpty()) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.add(Calendar.DAY_OF_MONTH, -1); // subtract 1 day
				Date previousDate = cal.getTime();
				latestRecord = usermasterattendancemodrepository.findLatestRecordBySrlNo(employeeId, previousDate);
			}
			Map<String, Object> response = new HashMap<>();

			if (latestRecord.isPresent()) {
				UserMasterAttendanceMod attendance = latestRecord.get();

				// Check if the employee is checked in but not checked out
				System.out.println("Checkin status  -   " + attendance.getCheckinstatus() + "  Checkout status  -    "
						+ attendance.getCheckoutstatus());
				if (attendance.getCheckinstatus() != null && attendance.getCheckoutstatus() == null) {
					response.put("isCheckedIn", true);
					// response.put("checkInTime", attendance.getCheckintime());
					response.put("checkInTime", attendance.getCheckintime().toInstant().toString()); // Converts to ISO
																										// 8601 format
					System.out.println("INTIME>>>>" + attendance.getCheckintime());
					response.put("checkInLocation", attendance.getCheckinlocation());
					response.put("attendanceDate", attendance.getAttendancedate());
					response.put("srlNum", attendance.getSrlnum());
				} else {
					response.put("isCheckedIn", false);
					response.put("message", "Employee is not currently checked in.");
				}
			} else {
				response.put("isCheckedIn", false);
				response.put("message", "No attendance record found.");
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", "Failed to fetch attendance status");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@GetMapping("/attendance/data")
	public ResponseEntity<?> getAttendanceData(@RequestParam(required = false) String employeeId,
			@RequestParam String startDate, @RequestParam String endDate) {
		try {
			// Convert date strings to Date objects
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date start = dateFormat.parse(startDate);
			Date end = dateFormat.parse(endDate);
			List<UserMasterAttendanceMod> attendanceRecords;
			if (employeeId != null && !employeeId.isEmpty()) {
				attendanceRecords = usermasterattendancemodrepository.findAttendanceByEmployeeIdAndDateRange(employeeId,
						start, end);
			} else {
				attendanceRecords = usermasterattendancemodrepository.findAttendanceByDateRange(start, end);
			}
			if (attendanceRecords.isEmpty()) {
				Map<String, Object> response = new HashMap<>();
				response.put("message", "No attendance records found for the given period.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
			SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			isoFormat.setTimeZone(TimeZone.getTimeZone("IST"));
			// Prepare response data
			List<Map<String, Object>> attendanceData = new ArrayList<>();
			for (UserMasterAttendanceMod record : attendanceRecords) {
				Map<String, Object> recordData = new HashMap<>();
				Optional<usermaintenance> employeeOpt = usermaintenanceRepository
						.findByEmpIdOrUserId(record.getAttendanceid());
				String employeeName = employeeOpt.map(usermaintenance::getUsername).orElse("Unknown");

				recordData.put("employeeName", employeeName != null ? employeeName : "N/A");
				recordData.put("employeeId", record.getAttendanceid() != null ? record.getAttendanceid() : "N/A");
				recordData.put("status", record.getStatus() != null ? record.getStatus() : "Unknown");
				recordData.put("date",
						record.getAttendancedate() != null ? isoFormat.format(record.getAttendancedate()) : "N/A");
				recordData.put("checkIn",
						record.getCheckintime() != null ? isoFormat.format(record.getCheckintime()) : "N/A");
				recordData.put("checkOut",
						record.getCheckouttime() != null ? isoFormat.format(record.getCheckouttime()) : "N/A");
				recordData.put("hoursWorked",
						record.getTotalhoursworked() != null ? record.getTotalhoursworked() : "N/A");
				recordData.put("checkinLocation", record.getCheckinlocation());
				recordData.put("checkoutLocation", record.getCheckoutlocation());
				attendanceData.add(recordData);
			}
			return ResponseEntity.ok(attendanceData);

		} catch (ParseException e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", "Invalid date format. Expected yyyy-MM-dd.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", "Failed to fetch attendance data");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@RestController
	@RequestMapping("/onboard")
	public class EmployeeController {

		@Autowired
		private EmployeeProfileModRepository employeeProfileModRepository; // ← Fixed name!

		@Autowired
		private EmployeeAddressModRepository employeeAddressModRepository;

		@Autowired
		private EmployeeEducationDetailsModRepository employeeEducationDetailsModRepository;

		@Autowired
		private EmployeeProfessionalDetailsModRepository employeeProfessionalDetailsModRepository;

		@Autowired
		private EmployeeSkillModRepository employeeSkillModRepository;

		@Autowired
		private ErrorMessageService errorMessageService;


		@Value("${file.upload.dir}")
	    private String docUploadDir;
		
		@PostMapping(consumes = "multipart/form-data")
		@Transactional(rollbackFor = Exception.class)
		public ResponseEntity<String> addOrUpdateEmployeeWithDocuments(@RequestParam("data") String dataJson,
				@RequestPart(value = "photo", required = false) MultipartFile photo,
				@RequestPart(value = "aadharDoc", required = false) MultipartFile aadharDoc,
				@RequestPart(value = "panDoc", required = false) MultipartFile panDoc,
				@RequestPart(value = "tenthMarksheet", required = false) MultipartFile tenthMarksheet,
				@RequestPart(value = "twelfthOrDiploma", required = false) MultipartFile twelfthOrDiploma,
				@RequestPart(value = "degreeCertificate", required = false) MultipartFile degreeCertificate) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			Path empDirectory = null;
			List<Path> savedFiles = new ArrayList<>();

			try {
				Map<String, Object> requestData = mapper.readValue(dataJson, Map.class);
				String empId = (String) requestData.get("empid");

				if (empId == null || empId.trim().isEmpty()) {
					return ResponseEntity.badRequest().body("{\"error\": \"Employee ID is required\"}");
				}

				// Fixed: Use correct repository variable name
				boolean isUpdate = employeeProfileModRepository.findByEmpid(empId).isPresent();
				EmployeeProfileMod employee;

				if (isUpdate) {
					employee = employeeProfileModRepository.findByEmpid(empId)
							.orElseThrow(() -> new RuntimeException("Employee not found"));
				} else {
					employee = new EmployeeProfileMod();
					employee.setEmpid(empId);
				}

				// Map JSON to entity (safe update)
				mapper.readerForUpdating(employee).readValue(dataJson);

				Date now = new Date();
				if (!isUpdate) {
					employee.setRcretime(now);
					employee.setDelflg("N");
					employee.setEntitycreflg("N");
				}
				employee.setRmodtime(now);

				// Auto full name
				employee.setEmployeename((employee.getFirstname() != null ? employee.getFirstname() : "")
						+ (employee.getLastname() != null ? " " + employee.getLastname() : ""));

				employee.setGender(safeString(requestData, "gender"));
				employee.setMaritalstatus(safeString(requestData, "maritalstatus"));
				employee.setNationality(safeString(requestData, "nationality", "Indian"));
				employee.setUannumber(safeString(requestData, "uannumber"));
				employee.setPassportnumber(safeString(requestData, "passportnumber"));
				employee.setDrivinglicense(safeString(requestData, "drivinglicense"));
				employee.setEsinumber(safeString(requestData, "esinumber"));
				employee.setEmergencycontactname(safeString(requestData, "emergencycontactname"));
				employee.setEmergencycontactnumber(safeString(requestData, "emergencycontactnumber"));
				employee.setEmergencycontactrelation(safeString(requestData, "emergencycontactrelation"));
				employee.setAlternatemobilenumber(safeString(requestData, "alternatemobilenumber"));
				employee.setDateofjoining(toDate(requestData.get("dateofjoining")));
				employee.setDesignation(safeString(requestData, "designation"));
				employee.setDepartment(safeString(requestData, "department"));
				employee.setWorklocation(safeString(requestData, "worklocation"));
				employee.setReportingmanager(safeString(requestData, "reportingmanager"));

				// Save to generate userid (for new records)
				employeeProfileModRepository.save(employee);

				// Create employee folder
				System.out.println("empDirectorydocUploadDir::::::::   "+docUploadDir);
				empDirectory = Paths.get(docUploadDir +"/emp_doc/"+ empId);
				System.out.println("empDirectory::::::::   "+empDirectory);
				Files.createDirectories(empDirectory);

				// Save files only if uploaded
				if (photo != null && !photo.isEmpty())
					employee.setPhotoPath(saveFile(photo, empId, "photo", empDirectory, savedFiles));
				if (aadharDoc != null && !aadharDoc.isEmpty())
					employee.setAadharPath(saveFile(aadharDoc, empId, "aadhar", empDirectory, savedFiles));
				if (panDoc != null && !panDoc.isEmpty())
					employee.setPanPath(saveFile(panDoc, empId, "pan", empDirectory, savedFiles));
				if (tenthMarksheet != null && !tenthMarksheet.isEmpty())
					employee.setTenthPath(saveFile(tenthMarksheet, empId, "10th", empDirectory, savedFiles));
				if (twelfthOrDiploma != null && !twelfthOrDiploma.isEmpty())
					employee.setTwelfthPath(saveFile(twelfthOrDiploma, empId, "12th", empDirectory, savedFiles));
				if (degreeCertificate != null && !degreeCertificate.isEmpty())
					employee.setDegreePath(saveFile(degreeCertificate, empId, "degree", empDirectory, savedFiles));

				employeeProfileModRepository.save(employee);

				// Delete old related records on update
				if (isUpdate && employee.getUserid() != null) {
					employeeAddressModRepository.deleteByUserid(employee.getUserid());
					employeeEducationDetailsModRepository.deleteByUserid(employee.getUserid());
					employeeSkillModRepository.deleteByUserid(employee.getUserid());

					// Professional uses String userid
					if (employee.getUserid() != null) {
						employeeProfessionalDetailsModRepository.deleteByUserid(employee.getUserid().toString());
					}
				}

				// Save new related data
				saveAddress(requestData.get("address"), employee);
				saveEducationDetails(requestData.get("education"), employee);
				saveProfessionalDetails(requestData.get("professional"), employee);
				saveSkills(requestData.get("skillSet"), employee);

				String message = isUpdate ? "Employee updated successfully!" : "Employee added successfully!";
				return ResponseEntity.ok("{\"message\": \"" + message + "\"}");

			} catch (Exception e) {
				e.printStackTrace(); // For debugging

				// Cleanup uploaded files
				savedFiles.forEach(path -> {
					try {
						if (Files.exists(path))
							Files.delete(path);
					} catch (Exception ignored) {
					}
				});

				String errorMsg = errorMessageService.getErrorMessage("ADD_EMP_ERROR", "en");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("{\"error\": \"" + errorMsg + " - " + e.getMessage() + "\"}");
			}
		}

		// Helper methods (no logging)
		private String safeString(Map<String, Object> map, String key) {
			Object val = map.get(key);
			return (val != null && !val.toString().trim().isEmpty()) ? val.toString().trim() : null;
		}

		private String safeString(Map<String, Object> map, String key, String defaultVal) {
			Object val = map.get(key);
			return (val != null && !val.toString().trim().isEmpty()) ? val.toString().trim() : defaultVal;
		}

		private Date toDate(Object obj) {
			if (obj instanceof String && !((String) obj).trim().isEmpty()) {
				try {
					return new SimpleDateFormat("yyyy-MM-dd").parse((String) obj);
				} catch (Exception e) {
					return null;
				}
			}
			return null;
		}

		private String saveFile(MultipartFile file, String empId, String type, Path dir, List<Path> track)
				throws IOException {
			if (file == null || file.isEmpty())
				return null;

			String orig = file.getOriginalFilename();
			if (orig == null)
				orig = "document";
			orig = new File(orig).getName();

			String ext = "";
			int i = orig.lastIndexOf('.');
			if (i > 0)
				ext = orig.substring(i);

			Path path = dir.resolve(empId + "_" + type + ext);
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			track.add(path);
			return path.toString();
		}

		// Save methods (unchanged)
		private void saveAddress(Object obj, EmployeeProfileMod emp) {
			if (obj instanceof Map) {
				EmployeeAddressMod addr = new ObjectMapper().convertValue(obj, EmployeeAddressMod.class);
				addr.setUserid(emp.getUserid());
				addr.setDelflg("N");
				addr.setRcreuserid(emp.getUserid().toString());
				addr.setRcretime(emp.getRcretime());
				addr.setRmoduserid(emp.getRmoduserid());
				addr.setRmodtime(emp.getRmodtime());
				employeeAddressModRepository.save(addr);
			}
		}

		private void saveEducationDetails(Object obj, EmployeeProfileMod emp) {
			if (obj instanceof List) {
				List<?> list = (List<?>) obj;
				for (Object item : list) {
					if (item instanceof Map) {
						EmployeeEducationDetailsMod edu = new ObjectMapper().convertValue(item,
								EmployeeEducationDetailsMod.class);
						Long max = employeeEducationDetailsModRepository.findMaxSerialNumber(emp.getUserid());
						edu.setSrlnum(max == null ? 1L : max + 1);
						edu.setUserid(emp.getUserid());
						edu.setDelflg("N");
						edu.setRcreuserid(emp.getUserid().toString());
						edu.setRcretime(emp.getRcretime());
						edu.setRmoduserid(emp.getRmoduserid());
						edu.setRmodtime(emp.getRmodtime());
						employeeEducationDetailsModRepository.save(edu);
					}
				}
			}
		}

		private void saveProfessionalDetails(Object obj, EmployeeProfileMod emp) {
			if (obj instanceof List) {
				List<?> list = (List<?>) obj;
				for (Object item : list) {
					if (item instanceof Map) {
						EmployeeProfessionalDetailsMod prof = new ObjectMapper().convertValue(item,
								EmployeeProfessionalDetailsMod.class);
						Long max = employeeProfessionalDetailsModRepository.findProfMaxSerialNumber(emp.getUserid());
						prof.setSrlnum(max == null ? 1L : max + 1);
						prof.setUserid(emp.getUserid().toString());
						prof.setDelflg("N");
						prof.setRcreuserid(emp.getUserid().toString());
						prof.setRcretime(emp.getRcretime());
						prof.setRmoduserid(emp.getRmoduserid());
						prof.setRmodtime(emp.getRmodtime());
						prof.setOfferletter("SUBMITTED");
						employeeProfessionalDetailsModRepository.save(prof);
					}
				}
			}
		}

		private void saveSkills(Object obj, EmployeeProfileMod emp) {
			if (obj instanceof List) {
				List<?> list = (List<?>) obj;
				for (Object item : list) {
					if (item instanceof Map) {
						EmployeeSkillMod skill = new ObjectMapper().convertValue(item, EmployeeSkillMod.class);
						Long max = employeeSkillModRepository.findSkillMaxSerialNumber(emp.getUserid());
						skill.setSrlnum(max == null ? 1L : max + 1);
						skill.setUserid(emp.getUserid());
						skill.setYearsofexp("2year");
						skill.setDelflg("N");
						skill.setRcreuserid(emp.getUserid().toString());
						skill.setRcretime(emp.getRcretime());
						skill.setRmoduserid(emp.getRmoduserid());
						skill.setRmodtime(emp.getRmodtime());
						employeeSkillModRepository.save(skill);
					}
				}
			}
		}
	}

	@RestController
	@RequestMapping("/travel")
	public class TravelController {

		@Autowired
		private TravelModRepository travelModRepository;

		@PostMapping("/addTravelRequest")
		public ResponseEntity<?> addTravelRequest(@RequestBody Map<String, Object> requestData) {
			try {
				// Create a new TravelEntityMod instance and populate fields from requestData
				TravelEntityMod travelEntity = new TravelEntityMod();
				travelEntity.setUserId((String) requestData.get("userId"));
				travelEntity.setEmpId((String) requestData.get("empId"));
				travelEntity.setTravelId(UUID.randomUUID().toString().substring(0, 20)); // Generate a unique ID
				travelEntity.setEmployeeDepartment((String) requestData.get("employeeDepartment"));
				travelEntity.setPlaceOfVisit((String) requestData.get("placeOfVisit"));
				travelEntity.setCustomerName((String) requestData.get("customerName"));

				// Parse dates using SimpleDateFormat
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String departureDate = (String) requestData.get("expectedDateOfDeparture");
				String arrivalDate = (String) requestData.get("expectedDateOfArrival");

				if (departureDate != null && !departureDate.isEmpty()) {
					travelEntity.setExpectedDateOfDeparture(dateFormat.parse(departureDate));
				}

				if (arrivalDate != null && !arrivalDate.isEmpty()) {
					travelEntity.setExpectedDateOfArrival(dateFormat.parse(arrivalDate));
				}

				travelEntity.setPurposeOfVisit((String) requestData.get("purposeOfVisit"));
				travelEntity.setExpectedDurationInDays((String) requestData.get("expectedDurationInDays"));
				travelEntity.setIsBillableToCustomer((String) requestData.get("isBillableToCustomer"));
				travelEntity.setCustomerName((String) requestData.get("customerName"));
				travelEntity.setEntityCreFlg("Y"); // Default to 'Y'
				travelEntity.setDelFlg("N"); // Default to 'N'

				// Set metadata fields
				travelEntity.setRcreUserId((String) requestData.get("rcreUserId"));
				travelEntity.setRcreTime(new Date()); // Current timestamp
				travelEntity.setRmodUserId((String) requestData.get("rmodUserId"));
				travelEntity.setRmodTime(new Date()); // Current timestamp
				travelEntity.setRvfyUserId((String) requestData.get("rvfyUserId"));
				travelEntity.setRvfyTime(new Date()); // Current timestamp

				// Save the entity into the database
				TravelEntityMod savedEntity = travelModRepository.save(travelEntity);

				// Return a success response
				return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);

			} catch (Exception e) {
				// Handle any exceptions and return an error response
				return new ResponseEntity<>("Error saving travel request: " + e.getMessage(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

	}

	@Autowired
	@GetMapping(value = { "/travel-records" })
	public ResponseEntity<?> getTravelRecords() {
		System.out.println("getTravelRecords : START");

		try {
			// Fetch all travel records without filtering by DEL_FLG
			List<TravelEntityMod> travelRecords = travelModRepository.findAll(); // Updated line

			if (travelRecords.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"No travel records found.\"}");
			}

			// Initialize a list to store filtered travel details
			List<Map<String, Object>> travelDetails = new ArrayList<>();

			// Loop through each travel record and fetch only required details
			for (TravelEntityMod travelRecord : travelRecords) {
				Map<String, Object> response = new HashMap<>();
				response.put("employeeId", travelRecord.getEmpId());
				response.put("travelId", travelRecord.getTravelId());
				response.put("department", travelRecord.getEmployeeDepartment());
				response.put("placeOfVisit", travelRecord.getPlaceOfVisit());
				response.put("departureDate", travelRecord.getExpectedDateOfDeparture());
				response.put("arrivalDate", travelRecord.getExpectedDateOfArrival());
				response.put("purpose", travelRecord.getPurposeOfVisit());
				response.put("expdur", travelRecord.getExpectedDurationInDays());
				response.put("billcust", travelRecord.getIsBillableToCustomer());
				response.put("custname", travelRecord.getCustomerName());
				response.put("status", travelRecord.getEntityCreFlg()); // Retaining status based on DEL_FLG value
				// Format the Date object directly to a human-readable date
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); // Desired format
				String formattedDate = dateFormat.format(travelRecord.getRcreTime());
				response.put("addedTime", formattedDate);

				// Add this travel record's details to the list
				travelDetails.add(response);
			}

			// Return filtered travel details
			return ResponseEntity.ok(travelDetails);
		} catch (Exception e) {
			System.out.println("getTravelRecords : ERR");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\": \"Error fetching travel records: " + e.getMessage() + "\"}");
		}
	}

	@PatchMapping("/delete/{empid}")
	@Transactional
	public ResponseEntity<?> deleteEmployeeById(@PathVariable String empid) {
		System.out.println("deleteEmployeeById : " + empid);
		Map<String, String> response = new HashMap<>();
		try {
			// Fetch the employee profile using the provided empid
			Optional<EmployeeProfileMod> employeeProfileModOptional = employeeProfilemodRepository.findByEmpid(empid);

			if (!employeeProfileModOptional.isPresent()) {
				// If employee not found, return 404 with error message
				String errorDescription = "Employee not found with empid: " + empid;

				response.put("error", errorDescription);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

			}

			// Extract the employee profile details
			EmployeeProfileMod employeeProfile = employeeProfileModOptional.get();

			// Check if the DEL_FLG is already 'Y'
			if ("Y".equals(employeeProfile.getDelflg())) {
				// If already deleted, return message
				response.put("message", "Employee already marked as deleted");
				// Add more entries if needed
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}

			// Update DEL_FLG column to 'Y'
			employeeProfile.setDelflg("Y");
			employeeProfilemodRepository.save(employeeProfile);
			System.out.println("Employee profile marked as deleted");

			// Update related entities if necessary (e.g., address, education, skills, etc.)
			Optional<EmployeeAddressMod> address = employeeAddressModRepository
					.findByUserid(employeeProfile.getUserid());
			address.ifPresent(employeeAddressMod -> {
				employeeAddressMod.setDelflg("Y");
				employeeAddressModRepository.save(employeeAddressMod);
				System.out.println("Employee address marked as deleted");
			});

			List<EmployeeEducationDetailsMod> educationDetails = employeeEducationDetailsModRepository
					.findByUserid(employeeProfile.getUserid());
			for (EmployeeEducationDetailsMod edu : educationDetails) {
				edu.setDelflg("Y");
				employeeEducationDetailsModRepository.save(edu);
				System.out.println("Employee education details marked as deleted");
			}

			List<EmployeeProfessionalDetailsMod> professionalDetails = employeeProfessionalDetailsModRepository
					.findByUserid(String.valueOf(employeeProfile.getUserid()));
			for (EmployeeProfessionalDetailsMod professionalDetail : professionalDetails) {
				professionalDetail.setDelflg("Y");
				employeeProfessionalDetailsModRepository.save(professionalDetail);
				System.out.println("Employee professional details marked as deleted");
			}

			List<EmployeeSkillMod> skills = employeeSkillModRepository.findByUserid(employeeProfile.getUserid());
			for (EmployeeSkillMod skill : skills) {
				skill.setDelflg("Y");
				employeeSkillModRepository.save(skill);
				System.out.println("Employee skill marked as deleted");
			}
			response.put("message", "Employee marked as deleted successfully");
			// Return success response with custom message
			return ResponseEntity.status(HttpStatus.OK).body(response);

		} catch (Exception e) {
			// Log the exception
			System.out.println("deleteEmployeeById : ERR - " + e.getMessage());
			e.printStackTrace();

			// Prepare error message and return as response
			response.put("message ", "An error occurred while marking the employee as deleted");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@Autowired
	private EmailService emailService;

	@RestController
	@RequestMapping("/employees/email")
	public class EmployeeController1 {
		@Autowired
		private EmailService emailService;

		private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		@Autowired
		private usermaintenancemodRepository userMaintenanceRepository;

		@PostMapping
		@Transactional
		public ResponseEntity<Map<String, Object>> addEmployee(@RequestBody Map<String, Object> requestData) {
			Map<String, Object> response = new HashMap<>();
			try {
				System.out.println("request test " + requestData);

				usermaintenancemod user = new ObjectMapper().convertValue(requestData, usermaintenancemod.class);

				// Encode password
				String rawPassword = "Whitestone@#123";
				System.out.println("rawPassword>>>>>>>" + rawPassword);
				user.setPassword(passwordEncoder.encode(rawPassword));

				userMaintenanceRepository.save(user);

				String toEmail = user.getEmailid();
				String subject = "Welcome to Our Service!" + user.getUserid();
				String body = String.format("Hi %s,\n\nThank you for registering with us!\n\nUser ID: %s\n"
						+ "Password: Whitestone@#123\n\nSet your password using:\n"
						+ "http://localhost:4200/reset-password\n\nBest regards,\nWHITESTONE SOFTWARE SOLUTION PVT LTD.",
						user.getFirstname(), user.getemp_id());

				try {
					emailService.sendVerificationEmail(toEmail, subject, body);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				response.put("success", true);
				response.put("message", "Employee added successfully.");
				response.put("emailSentTo", toEmail);

				return ResponseEntity.ok(response);

			} catch (Exception e) {
				e.printStackTrace();

				response.put("success", false);
				response.put("message", "Error occurred while adding employee.");
				response.put("errorDetails", e.getMessage());

				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		}
	}

	@GetMapping(value = { "/employees" })
	public ResponseEntity<?> getAllEmployees() {

		System.out.println("fetchAllEmployee : START");

		try {
			// Fetch all employee profiles with required fields
			// List<EmployeeProfileMod> employeeProfiles =
			// employeeProfileModRepository.findAll();

			// Fetch all employee profiles where DEL_FLG is not null
			List<EmployeeProfileMod> employeeProfiles = employeeProfilemodRepository.findAllWhereDelFlgIsNotNull();

			if (employeeProfiles.isEmpty()) {
				String errorDescription = errorMessageService.getErrorMessage("FETCH_EMP_NOTFOUND", "en");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"" + errorDescription + "\"}");
			}

			// Initialize a list to store filtered employee details
			List<Map<String, Object>> employeesDetails = new ArrayList<>();

			// Loop through each employee profile and fetch only required details
			for (EmployeeProfileMod employeeProfile : employeeProfiles) {
				Map<String, Object> response = new HashMap<>();
				response.put("empid", employeeProfile.getEmpid());
				response.put("firstName", employeeProfile.getFirstname());
				response.put("email", employeeProfile.getOfficialemail());
				response.put("department", employeeProfile.getDepartment()); // Assuming `getDepartment()` exists
				response.put("lastName", employeeProfile.getLastname());
				response.put("officialEmail", employeeProfile.getOfficialemail());
				response.put("onboardingStatus", employeeProfile.getDelflg());

				// Add this employee's details to the list
				employeesDetails.add(response);
			}

			// Return filtered employee details
			return ResponseEntity.ok(employeesDetails);
		} catch (Exception e) {
			System.out.println("fetchAllEmployee : ERR");
			String errorDescription = errorMessageService.getErrorMessage("ERR_NO_RECORD_FOUND", "en");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\": \"" + errorDescription + " - " + e.getMessage() + "\"}");
		}
	}

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@GetMapping("/employees/{empid}")
	public ResponseEntity<Map<String, Object>> fetchEmployeeById(@PathVariable String empid) {
		Map<String, Object> response = new HashMap<>();

		try {
			Optional<EmployeeProfileMod> empOpt = employeeProfilemodRepository.findByEmpid(empid);
			if (!empOpt.isPresent()) {
				response.put("error", "Employee not found with ID: " + empid);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			EmployeeProfileMod emp = empOpt.get();
			Long userid = emp.getUserid();
			if (userid == null) {
				response.put("error", "Invalid employee data: USER_ID is missing");
				return ResponseEntity.badRequest().body(response);
			}

			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

			// === BASIC PERSONAL DETAILS ===
			response.put("empid", nullToEmpty(emp.getEmpid()));
			response.put("firstName", nullToEmpty(emp.getFirstname()));
			response.put("lastName", nullToEmpty(emp.getLastname()));
			response.put("emailid", nullToEmpty(emp.getEmailid()));
			response.put("mobileNumber", nullToEmpty(emp.getMobilenumber()));
			response.put("officialemail", nullToEmpty(emp.getOfficialemail()));
			response.put("dateofbirth", emp.getDateofbirth() != null ? DATE_FORMAT.format(emp.getDateofbirth()) : "");
			response.put("bloodgroup", nullToEmpty(emp.getBloodgroup()));
			response.put("aadhaarnumber", nullToEmpty(emp.getAadhaarnumber()));
			response.put("pannumber", nullToEmpty(emp.getPannumber()));
			response.put("uannumber", nullToEmpty(emp.getUannumber()));

			// === NEW PERSONAL FIELDS ===
			response.put("gender", nullToEmpty(emp.getGender()));
			response.put("maritalstatus", nullToEmpty(emp.getMaritalstatus()));
			response.put("nationality", nullToEmpty(emp.getNationality()));
			response.put("alternatemobilenumber", nullToEmpty(emp.getAlternatemobilenumber()));

			// === EMERGENCY CONTACT ===
			response.put("emergencycontactname", nullToEmpty(emp.getEmergencycontactname()));
			response.put("emergencycontactnumber", nullToEmpty(emp.getEmergencycontactnumber()));
			response.put("emergencycontactrelation", nullToEmpty(emp.getEmergencycontactrelation()));

			// === EMPLOYMENT DETAILS ===
			response.put("dateofjoining",
					emp.getDateofjoining() != null ? DATE_FORMAT.format(emp.getDateofjoining()) : "");
			response.put("designation", nullToEmpty(emp.getDesignation()));
			response.put("department", (emp.getDepartment()));
			response.put("worklocation", nullToEmpty(emp.getWorklocation()));
			response.put("reportingmanager", nullToEmpty(emp.getReportingmanager()));

			// === OTHER IDs ===
			response.put("passportnumber", nullToEmpty(emp.getPassportnumber()));
			response.put("drivinglicense", nullToEmpty(emp.getDrivinglicense()));
			response.put("esinumber", nullToEmpty(emp.getEsinumber()));

			// === ADDRESS ===
			employeeAddressModRepository.findByUserid(userid).ifPresent(addr -> {
				response.put("presentaddressline1", nullToEmpty(addr.getPresentaddressline1()));
				response.put("presentaddressline2", nullToEmpty(addr.getPresentaddressline2()));
				response.put("presentcity", nullToEmpty(addr.getPresentcity()));
				response.put("presentstate", nullToEmpty(addr.getPresentstate()));
				response.put("presentpostalcode", nullToEmpty(addr.getPresentpostalcode()));
				response.put("presentcountry", nullToEmpty(addr.getPresentcountry()));

				response.put("permanentaddressline1", nullToEmpty(addr.getPermanentaddressline1()));
				response.put("permanentaddressline2", nullToEmpty(addr.getPermanentaddressline2()));
				response.put("permanentcity", nullToEmpty(addr.getPermanentcity()));
				response.put("permanentstate", nullToEmpty(addr.getPermanentstate()));
				response.put("permanentpostalcode", nullToEmpty(addr.getPermanentpostalcode()));
				response.put("permanentcountry", nullToEmpty(addr.getPermanentcountry()));
			});

			// === EDUCATION ===
			response.put("education", employeeEducationDetailsModRepository.findByUserid(userid).stream()
					.map(this::mapEducation).collect(Collectors.toList()));

			// === PROFESSIONAL EXPERIENCE ===
			response.put("professional", employeeProfessionalDetailsModRepository.findByUserid(String.valueOf(userid))
					.stream().map(this::mapProfessional).collect(Collectors.toList()));

			// === SKILL SET ===
			response.put("skillSet", employeeSkillModRepository.findByUserid(userid).stream().map(this::mapSkill)
					.collect(Collectors.toList()));

			// === DOCUMENTS AS BASE64 WITH METADATA ===
			Map<String, Object> documents = new HashMap<>();
			addDocumentBlob(documents, "photo", emp.getPhotoPath(), "image/jpeg");
			addDocumentBlob(documents, "aadhar", emp.getAadharPath(), "application/pdf");
			addDocumentBlob(documents, "pan", emp.getPanPath(), "application/pdf");
			addDocumentBlob(documents, "tenth", emp.getTenthPath(), "application/pdf");
			addDocumentBlob(documents, "twelfth", emp.getTwelfthPath(), "application/pdf");
			addDocumentBlob(documents, "degree", emp.getDegreePath(), "application/pdf");

			response.put("documents", documents);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("error", "Failed to fetch employee details: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	private String nullToEmpty(String s) {
		return s != null ? s : "";
	}

	private void addDocumentBlob(Map<String, Object> docs, String key, String filePath, String defaultMime) {
		if (filePath != null && !filePath.trim().isEmpty() && Files.exists(Paths.get(filePath))) {
			try {
				byte[] bytes = Files.readAllBytes(Paths.get(filePath));
				String base64 = Base64.getEncoder().encodeToString(bytes);
				String mime = Files.probeContentType(Paths.get(filePath));
				if (mime == null)
					mime = defaultMime;

				Map<String, String> doc = new HashMap<>();
				doc.put("data", base64);
				doc.put("mime", mime);
				doc.put("name", Paths.get(filePath).getFileName().toString());
				docs.put(key, doc);
			} catch (Exception e) {
				docs.put(key, null);
			}
		} else {
			docs.put(key, null);
		}
	}

	private Map<String, Object> mapEducation(EmployeeEducationDetailsMod e) {
		Map<String, Object> m = new HashMap<>();
		m.put("qualification", nullToEmpty(e.getQualification()));
		m.put("institution", nullToEmpty(e.getInstitution()));
		m.put("regnum", nullToEmpty(e.getRegnum()));
		m.put("percentage", nullToEmpty(e.getPercentage()));
		m.put("duration", (e.getDuration()));
		m.put("fieldofstudy", nullToEmpty(e.getFieldofstudy()));
		m.put("yearofgraduation", nullToEmpty(e.getYearofgraduation()));
		m.put("additionalnotes", nullToEmpty(e.getAdditionalnotes()));
		return m;
	}

	private Map<String, Object> mapProfessional(EmployeeProfessionalDetailsMod p) {
		Map<String, Object> m = new HashMap<>();
		m.put("organisation", nullToEmpty(p.getOrganisation()));
		m.put("location", nullToEmpty(p.getLocation()));
		m.put("orgempid", nullToEmpty(p.getOrgempid()));
		m.put("orgdept", nullToEmpty(p.getOrgdept()));
		m.put("orgrole", nullToEmpty(p.getOrgrole()));
		m.put("joiningdate", p.getJoiningdate() != null ? DATE_FORMAT.format(p.getJoiningdate()) : "");
		m.put("relievingdate", p.getRelievingdate() != null ? DATE_FORMAT.format(p.getRelievingdate()) : "");
		m.put("ctc", nullToEmpty(p.getCtc()));
		m.put("additionalinformation", nullToEmpty(p.getAdditionalinformation()));
		return m;
	}

	private Map<String, Object> mapSkill(EmployeeSkillMod s) {
		Map<String, Object> m = new HashMap<>();
		m.put("skill", nullToEmpty(s.getSkill()));
		m.put("proficiencylevel", nullToEmpty(s.getProficiencylevel()));
		m.put("certification", nullToEmpty(s.getCertification()));
		m.put("yearsExperience", nullToEmpty(s.getYearsofexp()));
		m.put("lastupdated", s.getLastupdated());
		return m;
	}

	@Autowired
	private OrganizationRepository organizationRepository;

	@PostMapping({ "/saveProfile", "/saveProfile/{id}" })
	public ResponseEntity<?> saveProfile(@RequestBody Organization organization,
			@PathVariable(value = "id", required = false) Long id) {

		try {
			// Basic validation
			if (organization.getOrganizationName() == null || organization.getLegalBusinessName() == null
					|| organization.getTaxId() == null) {

				Map<String, String> error = new HashMap<>();
				error.put("message", "Missing required fields: organizationName, legalBusinessName, or taxId");
				return ResponseEntity.badRequest().body(error);
			}

			if (id != null) {
				// Update logic
				Optional<Organization> existingOrgOpt = organizationRepository.findById(id);
				if (existingOrgOpt.isPresent()) {
					Organization existing = existingOrgOpt.get();

					if (!existing.getBusinessEmail().equals(organization.getBusinessEmail()) && organizationRepository
							.existsByBusinessEmailAndDelFlg(organization.getBusinessEmail(), "N")) {
						Map<String, String> error = new HashMap<>();
						error.put("message", "Business email already in use.");
						return ResponseEntity.badRequest().body(error);
					}

					if (!existing.getTaxId().equals(organization.getTaxId())
							&& organizationRepository.existsByTaxIdAndDelFlg(organization.getTaxId(), "N")) {
						Map<String, String> error = new HashMap<>();
						error.put("message", "Tax ID already in use.");
						return ResponseEntity.badRequest().body(error);
					}

					existing.setOrganizationName(organization.getOrganizationName());
					existing.setLegalBusinessName(organization.getLegalBusinessName());
					existing.setTaxId(organization.getTaxId());
					existing.setRmodTime(new Date());
					existing.setDelFlg("N");
					organization = existing;
				} else {
					Map<String, String> error = new HashMap<>();
					error.put("message", "Invalid organization ID: " + id);
					return ResponseEntity.badRequest().body(error);
				}
			} else {
				// Insert logic
				if (organizationRepository.existsByBusinessEmailAndDelFlg(organization.getBusinessEmail(), "N")) {
					Map<String, String> error = new HashMap<>();
					error.put("message", "Organization with this business email already exists.");
					return ResponseEntity.badRequest().body(error);
				}

				if (organizationRepository.existsByTaxIdAndDelFlg(organization.getTaxId(), "N")) {
					Map<String, String> error = new HashMap<>();
					error.put("message", "Organization with this Tax ID already exists.");
					return ResponseEntity.badRequest().body(error);
				}

				organization.setId(null);
				organization.setRcreTime(new Date());
				organization.setEntityCreFlg("Y");
				organization.setDelFlg("N");
			}

			Organization saved = organizationRepository.save(organization);
			return ResponseEntity.status(HttpStatus.CREATED).body(saved);

		} catch (org.hibernate.exception.ConstraintViolationException e) {
			String errorMessage = "A unique constraint was violated.";
			if (e.getMessage().contains("SYS_C008154")) {
				errorMessage += " The 'taxId' or 'organizationName' already exists.";
			}
			Map<String, String> error = new HashMap<>();
			error.put("message", errorMessage);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

		} catch (Exception e) {
			Map<String, String> error = new HashMap<>();
			error.put("message", "An error occurred: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

	// Fetch all organizations (excluding deleted)
	@GetMapping(value = { "/orgProfile" })
	public ResponseEntity<?> getAllOrganizations() {
		try {
			List<Organization> organizations = organizationRepository.findByDelFlgNot("Y");
			if (organizations.isEmpty()) {
				return new ResponseEntity<>("No organizations found", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(organizations, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error retrieving organizations", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch an organization by ID
	@GetMapping("/orgProfile/{id}")
	public ResponseEntity<?> getOrganizationById(@PathVariable("id") Long id) {
		Optional<Organization> organization = organizationRepository.findById(id);
		if (organization.isPresent()) {
			return new ResponseEntity<>(organization.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Organization not found", HttpStatus.NOT_FOUND);
		}
	}

	// Delete an organization (mark as deleted by setting delFlg = 'Y')
	@DeleteMapping("/orgProfile/{id}")
	public ResponseEntity<?> deleteProfile(@PathVariable("id") Long id) {
		try {
			Optional<Organization> organization = organizationRepository.findById(id);
			if (!organization.isPresent()) {
				return new ResponseEntity<>("Organization not found", HttpStatus.NOT_FOUND);
			}

			// Set delFlg to 'Y' instead of deleting the record
			Organization existingOrg = organization.get();
			existingOrg.setDelFlg("Y");
			organizationRepository.save(existingOrg);

			return new ResponseEntity<>("Organization marked as deleted", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error deleting organization", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Autowired
	private CompanyLocationRepository locationRepository;

	// Get all locations
	@GetMapping("/getLocation")
	public ResponseEntity<?> getAllLocations() {
		try {
			List<CompanyLocation> locations = locationRepository.findAll();
			if (locations.isEmpty()) {
				return new ResponseEntity<>("No locations found", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(locations, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error retrieving locations", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Add new location
	@PostMapping("/addLocation")
	public ResponseEntity<?> addLocation(@RequestBody CompanyLocation location) {
		try {
			CompanyLocation newLocation = locationRepository.save(location);
			return new ResponseEntity<>(newLocation, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Error adding location", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Update an existing location
	@PutMapping("/updateLocation/{id}")
	public ResponseEntity<?> updateLocation(@PathVariable Long id, @RequestBody CompanyLocation location) {
		try {
			Optional<CompanyLocation> existingLocation = locationRepository.findById(id);
			if (!existingLocation.isPresent()) {
				return new ResponseEntity<>("Location not found", HttpStatus.NOT_FOUND);
			}
			location.setId(id); // Set the existing ID to update the record
			CompanyLocation updatedLocation = locationRepository.save(location);
			return new ResponseEntity<>(updatedLocation, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error updating location", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Delete a location
	@DeleteMapping("/deleteLocation/{id}")
	public ResponseEntity<?> deleteLocation(@PathVariable Long id) {
		try {
			Optional<CompanyLocation> location = locationRepository.findById(id);
			if (!location.isPresent()) {
				return new ResponseEntity<>("Location not found", HttpStatus.NOT_FOUND);
			}
			locationRepository.deleteById(id);
			return new ResponseEntity<>("Location deleted successfully", HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>("Error deleting location", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// desegination

	@Autowired
	private DesignationRepository repository;

	@GetMapping("/getdesignations")
	public ResponseEntity<?> getAllDesignations() {
		try {
			List<Designation> designations = repository.findByDelFlgNot("Y");
			if (designations.isEmpty()) {
				return new ResponseEntity<>("No designations found", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(designations, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error fetching designations", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Fetch a designation by ID
	@GetMapping("/getdesignations/{id}")
	public ResponseEntity<?> getDesignationById(@PathVariable Long id) {
		try {
			Optional<Designation> designation = repository.findById(id);
			if (!designation.isPresent()) {
				return new ResponseEntity<>("Designation not found", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(designation.get(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error fetching designation", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Add a new designation
	@PostMapping("/adddesignations")
	public ResponseEntity<?> addDesignation(@RequestBody Designation designation) {
		try {
			designation.setrCreTime(new Date());
			designation.setEntityCreFlg("Y");
			designation.setDelFlg("N");

			// Debugging log: Check the fields of the designation object before saving
			System.out.println("Saving designation: " + designation);

			Designation savedDesignation = repository.save(designation);
			return new ResponseEntity<>(savedDesignation, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace(); // This will print the exception details to the server logs
			return new ResponseEntity<>("Error saving designation", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Update an existing designation
	@PutMapping("/updatedesignations/{id}")
	public ResponseEntity<?> updateDesignation(@PathVariable Long id, @RequestBody Designation designation) {
		try {
			Optional<Designation> existingDesignation = repository.findById(id);
			if (!existingDesignation.isPresent()) {
				return new ResponseEntity<>("Designation not found", HttpStatus.NOT_FOUND);
			}
			designation.setId(id);
			Designation updatedDesignation = repository.save(designation);
			return new ResponseEntity<>(updatedDesignation, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error updating designation", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Delete a designation (mark as deleted by setting delFlg = 'Y')
	@DeleteMapping("/deldesignations/{id}")
	public ResponseEntity<Void> deleteDesignation(@PathVariable Long id) {
		try {
			Optional<Designation> designation = repository.findById(id);
			if (!designation.isPresent()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			Designation existingDesignation = designation.get();
			existingDesignation.setDelFlg("Y");
			repository.save(existingDesignation);

			return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No content on success
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // No content on error
		}
	}

	@Autowired
	private DepartmentRepository departmentRepository;

	// Fetch all departments
	@GetMapping("/getDepartment")
	public ResponseEntity<List<Department>> getAllDepartments() {
		try {
			List<Department> departments = departmentRepository.findAll();
			return new ResponseEntity<>(departments, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Create a new department
	@PostMapping("/addDepartment")
	public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
		try {
			// Log the input data for debugging
			System.out.println("Received Department: " + department);
			department.setrCreTime(new Date());
			department.setEntityCreFlg("Y");
			department.setDelFlg("N");

			// Save to the database
			Department savedDepartment = departmentRepository.save(department);

			// Return success response
			return new ResponseEntity<>(savedDepartment, HttpStatus.CREATED);
		} catch (Exception e) {
			// Log the exception for debugging
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Update an existing department
	@PutMapping("/updateDepartment/{id}")
	public ResponseEntity<Department> updateDepartment(@PathVariable Long id,
			@RequestBody Department departmentDetails) {
		try {
			Optional<Department> optionalDepartment = departmentRepository.findById(id);
			if (optionalDepartment.isPresent()) {

				Department department = optionalDepartment.get();
				department.setrModTime(new Date());
				department.setEntityCreFlg("Y");
				department.setDelFlg("N");
				department.setName(departmentDetails.getName());
				department.setCode(departmentDetails.getCode());
				department.setDescription(departmentDetails.getDescription());
				department.setStatus(departmentDetails.getStatus());
				Department updatedDepartment = departmentRepository.save(department);
				return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Delete a department by ID
	@DeleteMapping("/deleteDepartment/{id}")
	public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
		try {
			if (departmentRepository.existsById(id)) {
				departmentRepository.deleteById(id);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Autowired
	private StatutorySettingsRepository statutorySettingsRepository;

	@Autowired
	private PTSlabRepository ptSlabRepository;

	// Get PF & ESI Settings
	@GetMapping("/getsettings")
	public ResponseEntity<StatutorySettings> getSettings() {
		try {
			StatutorySettings settings = statutorySettingsRepository.findAll().stream().findFirst().orElse(null);
			return settings != null ? new ResponseEntity<>(settings, HttpStatus.OK)
					: new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Update or Save PF & ESI Settings
	@PostMapping("/addsettings")
	public ResponseEntity<StatutorySettings> saveSettings(@RequestBody StatutorySettings settings) {
		try {
			// Check if there are existing records (only one record should exist)
			List<StatutorySettings> allSettings = statutorySettingsRepository.findAll();
			if (allSettings.isEmpty()) {
				// Create the first record
				settings.setrCreTime(new Date());
				settings.setEntityCreFlg("Y");
				settings.setDelFlg("N");
				StatutorySettings savedSettings = statutorySettingsRepository.save(settings);
				return new ResponseEntity<>(savedSettings, HttpStatus.CREATED);
			} else {
				// Only one record should exist, so update it
				StatutorySettings currentSettings = allSettings.get(0);
				currentSettings.setrModTime(new Date());
				currentSettings.setPfRate(settings.getPfRate()); // Set other fields
				currentSettings.setEsiRate(settings.getEsiRate()); // Example: Update fields
				currentSettings.setPfMaxLimit(settings.getPfMaxLimit()); // Example: Update fields
				currentSettings.setEsiMaxLimit(settings.getEsiMaxLimit());
				currentSettings.setEsiEnabled(settings.getEsiEnabled());
				currentSettings.setPfEnabled(settings.getPfEnabled());

				// Update other fields as needed...
				StatutorySettings savedSettings = statutorySettingsRepository.save(currentSettings);
				return new ResponseEntity<>(savedSettings, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Get PT Slabs
	@GetMapping("/getpt-slabs")
	public ResponseEntity<List<PTSlab>> getPTSlabs() {
		try {
			List<PTSlab> slabs = ptSlabRepository.findAll();
			return new ResponseEntity<>(slabs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Add or Update PT Slab
	@PostMapping("/addpt-slabs")
	public ResponseEntity<PTSlab> savePTSlab(@RequestBody PTSlab slab) {
		try {
			slab.setrCreTime(new Date());
			slab.setrModTime(new Date());
			slab.setEntityCreFlg("Y");
			slab.setDelFlg("N");
			PTSlab savedSlab = ptSlabRepository.save(slab);
			return new ResponseEntity<>(savedSlab, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Delete PT Slab
	@DeleteMapping("/delpt-slabs/{id}")
	public ResponseEntity<Void> deletePTSlab(@PathVariable Long id) {
		try {
			if (ptSlabRepository.existsById(id)) {
				ptSlabRepository.deleteById(id);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Autowired
	private SalaryComponentRepository salaryComponentRepository;

	// Get all salary components
	@GetMapping("/getcomponent")
	public ResponseEntity<?> getAllComponents() {
		try {
			List<SalaryComponent> components = salaryComponentRepository.findAll();
			return new ResponseEntity<>(components, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error fetching salary components", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Get a specific salary component by id
	@GetMapping("/getcomponent/{id}")
	public ResponseEntity<?> getComponentById(@PathVariable Long id) {
		try {
			Optional<SalaryComponent> component = salaryComponentRepository.findById(id);
			if (component.isPresent()) {
				return new ResponseEntity<>(component.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Salary component not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error fetching salary component", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Add or update a salary component
	@PostMapping("/addcomponent")
	public ResponseEntity<?> saveComponent(@RequestBody SalaryComponent component) {
		try {
			component.setrCreTime(new Date());
			component.setrModTime(new Date());
			component.setEntityCreFlg("Y");
			component.setDelFlg("N");
			SalaryComponent savedComponent = salaryComponentRepository.save(component);
			return new ResponseEntity<>(savedComponent, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Error saving salary component", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Update a salary component (handle both add and edit)
	@PutMapping("/updatecomponent/{id}")
	public ResponseEntity<?> updateComponent(@PathVariable Long id, @RequestBody SalaryComponent component) {
		try {
			Optional<SalaryComponent> existingComponent = salaryComponentRepository.findById(id);
			if (existingComponent.isPresent()) {
				component.setId(id); // Ensure the ID is set to the correct value for updating
				component.setrCreTime(new Date());
				component.setrModTime(new Date());
				component.setEntityCreFlg("Y");
				component.setDelFlg("N");
				SalaryComponent updatedComponent = salaryComponentRepository.save(component);
				return new ResponseEntity<>(updatedComponent, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Salary component not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error updating salary component", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Delete a salary component by id
	@DeleteMapping("/deletecomponent/{id}")
	public ResponseEntity<?> deleteComponent(@PathVariable Long id) {
		try {
			Optional<SalaryComponent> existingComponent = salaryComponentRepository.findById(id);
			if (existingComponent.isPresent()) {
				salaryComponentRepository.deleteById(id);
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>("Salary component not found", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error deleting salary component", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// vasanth

	@Autowired
	private UserMasterAttendanceRepository attendanceRepository;

	@GetMapping("/getAttendanceDetails/{month}")
	public ResponseEntity<?> getAttendanceDetails(@PathVariable String month) {
		try {
			List<Object[]> results = attendanceRepository.getAttendanceDetailsForMonth(month);

			List<Map<String, Object>> response = new ArrayList<>();
			for (Object[] row : results) {
				Map<String, Object> record = new HashMap<>();
				record.put("attendanceId", row[0]);
				record.put("employeeName", row[1]);
				record.put("distinctAttendanceCount", row[2]);
				record.put("leavesTaken", row[3]);
				response.add(record);
			}

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
		}
	}

	@GetMapping("/getEmployeeReport/{attendanceId}/{month}")
	public ResponseEntity<?> getEmployeeReportForMonth(@PathVariable String attendanceId, @PathVariable String month) {
		try {
			// Fetch employee attendance records for the specified month
			List<Object[]> results = attendanceRepository.getEmployeeDetailedReportForMonth(attendanceId, month);

			// Date format for attendance date
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

			List<Map<String, Object>> report = new ArrayList<>();
			for (Object[] row : results) {
				Map<String, Object> record = new HashMap<>();

				// Format the attendance date
				Date attendanceDate = (Date) row[0];
				String formattedAttendanceDate = dateFormat.format(attendanceDate);

				// Handle checkin and checkout as String or Timestamp
				String checkin = row[2] != null ? row[2].toString() : null;
				String checkout = row[3] != null ? row[3].toString() : null;

				// Add formatted values to the record
				record.put("attendanceDate", formattedAttendanceDate);
				record.put("status", row[1]);
				record.put("checkin", checkin);
				record.put("checkout", checkout);
				record.put("totalHoursWorked", row[4]);
				record.put("attendanceId", row[5]);

				report.add(record);
			}

			// Print the report to the console
			System.out.println("Employee Report Response: " + report);

			return ResponseEntity.ok(report);
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
		}
	}

	@GetMapping("/generateLeaveReport")
	public ResponseEntity<?> generateLeaveReport() {
		try {
			// Fetch employee leave and attendance summary for all months
			List<Object[]> results = attendanceRepository.getEmployeeLeaveSummaryForAllEmployees();

			// Prepare the response
			List<Map<String, Object>> response = new ArrayList<>();
			for (Object[] row : results) {
				Map<String, Object> record = new HashMap<>();
				record.put("attendanceId", row[0]); // Employee attendance ID
				record.put("month", row[1]); // Month-Year (e.g., "01-2024")
				record.put("totalDaysWorked", row[2]); // Total worked days in the month
				record.put("totalHoursWorked", row[3]); // Total hours worked in the month
				record.put("remainingDays", row[4]); // Remaining days in the month
				response.add(record);
			}

			// Return the response
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			// Log error for debugging
			System.err.println("Error: " + e.getMessage());
			return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
		}
	}

	private List<String> getDirectReports(String empId) {
		List<String> empIds = new ArrayList<>();
		empIds.add(empId); // Include the given empId

		// Fetch direct reports

		List<usermaintenance> reportingEmployees = usermaintenanceRepository.findByRepoteTo(empId);

		List<TraineeMaster> reportingtrainees = traineemasterRepository.findByRepoteTo(empId);

		for (TraineeMaster traineeMaster : reportingtrainees) {
			empIds.add(traineeMaster.getTrngid());
		}

		for (usermaintenance emp : reportingEmployees) {
			empIds.add(emp.getEmpid());
		}

		return empIds;
	}

	// leave
	@GetMapping("/leaveRequests/get/{empId}")
	public ResponseEntity<Map<String, Object>> getLeaveRequestsByEmpId(@PathVariable(required = false) String empId) {

		try {
			List<String> empIds = new ArrayList<>();
			boolean isHR = false;

			// Determine if the user is HR
			if (empId != null && !empId.isEmpty()) {
				usermaintenance user = usermaintenanceRepository.findByEmpid(empId);
				TraineeMaster trng = traineemasterRepository.findByTrngid(empId);
				if (user != null) {
					Optional<UserRoleMaintenance> role = userRoleMaintenanceRepository.findByRoleid(user.getRoleid());
					if (role.isPresent() && "HR".equalsIgnoreCase(role.get().getRolename())) {
						isHR = true;
					}
				}
			} else {
				// If empId is null, assume HR (frontend request from HR user)
				isHR = true;
			}

			if (isHR) {
				// Fetch all employees + trainees
				empIds.addAll(usermaintenanceRepository.findAll().stream().map(usermaintenance::getEmpid).toList());
				empIds.addAll(traineemasterRepository.findAll().stream().map(TraineeMaster::getTrngid).toList());
			} else {
				// Non-HR, fetch only direct reports
				empIds = getDirectReports(empId);
			}

			// Fetch Master and Mod leave requests
			List<EmployeeLeaveMasterTbl> masterLeaveRequests = employeeLeaveMasterRepository
					.findByEmpidInAndEntitycreflgIn(empIds, Arrays.asList("N", "Y"));
			List<EmployeeLeaveModTbl> modLeaveRequests = employeeLeaveModRepository.findByEmpidIn(empIds);

			// Build empId -> name mapping
			Map<String, String> empIdToName = new HashMap<>();
			usermaintenanceRepository.findByEmpidIn(empIds)
					.forEach(u -> empIdToName.put(u.getEmpid(), u.getFirstname()));
			traineemasterRepository.findByTrngidIn(empIds)
					.forEach(t -> empIdToName.put(t.getTrngid(), t.getFirstname()));

			// Combine results
			List<Map<String, Object>> combinedLeaveRequests = new ArrayList<>();

			masterLeaveRequests.forEach(request -> {
				Map<String, Object> leaveData = new HashMap<>();
				leaveData.put("name", empIdToName.getOrDefault(request.getEmpid(), "N/A"));
				leaveData.put("srlnum", request.getSrlnum());
				leaveData.put("empid", request.getEmpid());
				leaveData.put("leavetype", request.getLeavetype());
				leaveData.put("startdate", request.getStartdate());
				leaveData.put("enddate", request.getEnddate());
				leaveData.put("leavereason", request.getLeavereason());
				leaveData.put("status", request.getStatus());
				leaveData.put("entitycreflg", request.getEntitycreflg());
				leaveData.put("noofdays", request.getNoofdays());
				combinedLeaveRequests.add(leaveData);
			});

			modLeaveRequests.forEach(request -> {
				Map<String, Object> leaveData = new HashMap<>();
				leaveData.put("name", empIdToName.getOrDefault(request.getEmpid(), "N/A"));
				leaveData.put("srlnum", request.getSrlnum());
				leaveData.put("empid", request.getEmpid());
				leaveData.put("leavetype", request.getLeavetype());
				leaveData.put("startdate", request.getStartdate());
				leaveData.put("enddate", request.getEnddate());
				leaveData.put("leavereason", request.getLeavereason());
				leaveData.put("status", request.getStatus());
				leaveData.put("entitycreflg", request.getEntitycreflg());
				leaveData.put("noofdays", request.getNoofdays());
				combinedLeaveRequests.add(leaveData);
			});

			Map<String, Object> response = new HashMap<>();
			response.put("data", combinedLeaveRequests);
			response.put("message", combinedLeaveRequests.isEmpty() ? "No data found" : "Data fetched successfully");

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("status", "error");
			errorResponse.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@Autowired
	private EmployeeLeaveModTblRepository employeeLeaveModRepository;
	@Autowired
	private EmployeeLeaveMasterTblRepository employeeLeaveMasterRepository;

	@PutMapping("/updateEntityFlag")
	public ResponseEntity<?> updateEntityFlag(@RequestParam(name = "empid", required = false) String empid,
			@RequestParam(name = "srlnum", required = false) Long srlnum) {
		try {
			System.out.println("Approved: " + srlnum);

			// Retrieve entity by empid and srlnum
			EmployeeLeaveMasterTbl entity = employeeLeaveMasterRepository.findByEmpidAndSrlnum(empid, srlnum);
			if (entity == null) {
				Map<String, String> errorResponse = new HashMap<>();
				errorResponse.put("status", "failure");
				errorResponse.put("message", "Employee not found");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
			}

			updateConsolidatedLeave(entity);

			// Update flag and status
			entity.setEntitycreflg("Y");
			entity.setStatus("Approved");
			employeeLeaveMasterRepository.save(entity);

			// ✅ Insert "Absent" attendance records during leave period
			Date startDate = entity.getStartdate();
			Date endDate = entity.getEnddate();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);

			while (!calendar.getTime().after(endDate)) {
				Date currentDate = calendar.getTime();

				Optional<UserMasterAttendanceMod> existingAttendance = usermasterattendancemodrepository
						.findByAttendanceidAndAttendancedate(empid, currentDate);

				Date defaultCheckin = combineDateTime(currentDate, 0, 0);
				Date defaultCheckout = combineDateTime(currentDate, 0, 0);

				if (existingAttendance.isPresent()) {
					UserMasterAttendanceMod attendance = existingAttendance.get();
					attendance.setStatus("Absent");
					attendance.setRmoduserid("System");
					attendance.setRmodtime(new Date());
					usermasterattendancemodrepository.save(attendance);
				} else {
					UserMasterAttendanceMod newAttendance = new UserMasterAttendanceMod();
					newAttendance.setAttendanceid(empid);
					newAttendance.setUserid("2019" + empid);
					newAttendance.setAttendancedate(currentDate);
					newAttendance.setCheckintime(defaultCheckin);
					newAttendance.setCheckouttime(defaultCheckout);
					newAttendance.setStatus("Absent");
					newAttendance.setRcreuserid("System");
					newAttendance.setRcretime(new Date());
					usermasterattendancemodrepository.save(newAttendance);
				}

				calendar.add(Calendar.DATE, 1); // next day
			}

			// Copy entity to mod table
			EmployeeLeaveModTbl modEntity = new EmployeeLeaveModTbl();
			modEntity.setSrlnum(entity.getSrlnum());
			modEntity.setEmpid(entity.getEmpid());
			modEntity.setLeavetype(entity.getLeavetype());
			modEntity.setStartdate(entity.getStartdate());
			modEntity.setEnddate(entity.getEnddate());
			modEntity.setTeamEmail(entity.getTeamemail());
			modEntity.setLeavereason(entity.getLeavereason());
			modEntity.setStatus(entity.getStatus());
			modEntity.setNoofdays(entity.getNoofdays());
			modEntity.setEntitycreflg(entity.getEntitycreflg());
			modEntity.setDelflg(entity.getDelflg());
			modEntity.setRcreuserid(entity.getRcreuserid());
			modEntity.setRcretime(entity.getRcretime());
			modEntity.setRmoduserid(entity.getRmoduserid());
			modEntity.setRmodtime(entity.getRmodtime());
			modEntity.setRvfyuserid(entity.getRvfyuserid());
			modEntity.setRvfytime(entity.getRvfytime());
			modEntity.setNoofbooked(entity.getNoofdays());

			employeeLeaveModRepository.save(modEntity);

			// Delete record from master table
			employeeLeaveMasterRepository.delete(entity);

			// Send email to employee (regular or trainee)
			try {
				boolean emailSent = false;

				// 1️⃣ Check usermaintenance
				Optional<usermaintenance> existingEmployeeOpt = usermaintenanceRepository.findByEmpIdOrUserId(empid);
				if (existingEmployeeOpt.isPresent()) {
					usermaintenance existingEmployee = existingEmployeeOpt.get();
					String employeeEmail = existingEmployee.getEmailid();
					String managerId = existingEmployee.getRepoteTo();
					usermaintenance manager = usermaintenanceRepository.findByEmpIdOrUserId(managerId)
							.orElseThrow(() -> new RuntimeException("Manager not found"));

					if (employeeEmail != null && !employeeEmail.isEmpty()) {
						sendLeaveApprovalEmail(existingEmployee.getFirstname(), employeeEmail, manager.getFirstname(),
								manager.getEmailid(), entity.getLeavetype(), entity.getStartdate(),
								entity.getEnddate());
						emailSent = true;
					}
				}

				// 2️⃣ If not found in usermaintenance, check TraineeMaster
				if (!emailSent) {
					Optional<TraineeMaster> traineeOpt = traineemasterRepository.findByTrngidOrUserId(empid);
					if (traineeOpt.isPresent()) {
						TraineeMaster trainee = traineeOpt.get();
						String traineeEmail = trainee.getEmailid();
						String managerId = trainee.getRepoteTo();
						TraineeMaster manager = traineemasterRepository.findByTrngidOrUserId(managerId)
								.orElseThrow(() -> new RuntimeException("Manager not found"));

						if (traineeEmail != null && !traineeEmail.isEmpty()) {
							sendLeaveApprovalEmail(trainee.getFirstname(), traineeEmail, manager.getFirstname(),
									manager.getEmailid(), entity.getLeavetype(), entity.getStartdate(),
									entity.getEnddate());
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace(); // log error
			}

			// Return success response
			Map<String, Object> successResponse = new HashMap<>();
			successResponse.put("status", "success");
			successResponse.put("message",
					"Entity flag updated, Absent marked in attendance, record copied to mod table, and deleted from master");
			successResponse.put("empid", empid);
			return ResponseEntity.ok(successResponse);

		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("status", "error");
			errorResponse.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	// Helper method to send email
	private void sendLeaveApprovalEmail(String employeeName, String employeeEmail, String managerName,
			String managerEmail, String leaveType, Date startDate, Date endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String subject = "Leave Request Approved";
		String body = String.format(
				"Dear %s,\n\nYour leave request for %s from %s to %s has been approved by your manager, %s.\n\nRegards,\n%s,\nWhitestone Software Solution Pvt Ltd",
				employeeName, leaveType, sdf.format(startDate), sdf.format(endDate), managerName, managerName);
		emailService.sendLeaveEmail(managerEmail, employeeEmail, subject, body);
	}



	@Autowired
	private WsslCalendarModRepository wsslCalendarModRepository;

	@GetMapping("/leaveRequests/upcomingHolidays")
	public ResponseEntity<Map<String, Object>> getUpcomingHolidays() {
		LocalDate currentDate = LocalDate.now();
		List<WsslCalendarMod> upcomingHolidays = wsslCalendarModRepository
				.findByEventDateAfter(java.sql.Date.valueOf(currentDate));
		Map<String, Object> response = new HashMap<>();
		response.put("data", upcomingHolidays);
		response.put("message",
				upcomingHolidays.isEmpty() ? "No upcoming holidays found" : "Upcoming holidays fetched successfully");
		return ResponseEntity.ok(response);
	}

	@PostMapping("/rejectLeaveRequest")
	public ResponseEntity<Map<String, String>> rejectLeaveRequest(@RequestBody Map<String, String> requestData) {
		String empid = requestData.get("empid");
		String leavereason = requestData.get("leavereason");
		System.out.println("Received empid: " + empid);
		EmployeeLeaveMasterTbl leaveRequest = employeeLeaveMasterRepository.findByEmpidAndLeavereason(empid,
				leavereason);
		Map<String, String> response = new HashMap<>();
		if (leaveRequest != null) {
			leaveRequest.setDelflg("Y");
			leaveRequest.setStatus("Rejected");
			employeeLeaveMasterRepository.save(leaveRequest);
			try {
				usermaintenance existingEmployee = usermaintenanceRepository.findByEmpIdOrUserId(empid)
						.orElseThrow(() -> new RuntimeException("Employee not found"));
				String employeeEmail = existingEmployee.getEmailid();
				String managerId = existingEmployee.getRepoteTo();
				usermaintenance manager = usermaintenanceRepository.findByEmpIdOrUserId(managerId)
						.orElseThrow(() -> new RuntimeException("Manager not found"));

				if (employeeEmail != null && !employeeEmail.isEmpty()) {
					String subject = "Leave Request Rejected";
					String body = String.format(
							"Dear %s,\n\nYour leave request for %s has been rejected. Please contact your manager, %s, for further details.\n\nRegards,\n"
									+ manager.getFirstname() + ",\nWhitestone Software Solution Pvt Ltd.",
							existingEmployee.getFirstname(), leaveRequest.getLeavetype(), manager.getFirstname());
					emailService.sendLeaveEmail(manager.getEmailid(), employeeEmail, subject, body);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			response.put("status", "success");
			response.put("message", "Leave request rejected successfully.");
			return ResponseEntity.ok(response);
		} else {
			System.out.println("Leave request not found for empid: " + empid);
			response.put("status", "failure");
			response.put("message", "Leave request not found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}

	@Autowired
	private EmployeeLeaveModTblRepository leaveRepository;

	@GetMapping("/leave/count")
	public ResponseEntity<Map<String, Object>> getLeaveCounts(@RequestParam("empId") String empId) {
	    try {
	        int year = LocalDate.now().getYear();

	        // 1. Fetch Approved + Pending leaves
			/*
			 * List<EmployeeLeaveMasterTbl> leaves = employeeLeaveMasterRepository
			 * .findByEmpidAndStartdateBetweenAndStatusInIgnoreCase( empId,
			 * Date.from(LocalDate.of(year, 1,
			 * 1).atStartOfDay(ZoneId.systemDefault()).toInstant()),
			 * Date.from(LocalDate.of(year, 12,
			 * 31).atStartOfDay(ZoneId.systemDefault()).toInstant()),
			 * Arrays.asList("Approved", "Pending") );
			 */
	        
//	        Date start = Date.from(LocalDate.of(year, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
//	        Date end   = Date.from(LocalDate.of(year, 12, 31).atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
//
//	        List<EmployeeLeaveMasterTbl> leaves = employeeLeaveMasterRepository
//	            .findApprovedAndPendingLeaves(empId, start, end);
//
//	        // 2. Sum total leave days (FIXED: use mapToFloat correctly)
//	        float totalTaken = (float) leaves.stream()
//	                .filter(l -> l.getNoofdays() != null)
//	                .mapToDouble(l -> l.getNoofdays())  // Double stream
//	                .sum();

	        // 3. Get Leave Summary from DB (Source of Truth)
	        Optional<EmployeeLeaveSummary> summaryOpt = employeeLeaveSummaryRepository
	                .findByEmpIdAndYear(empId, year);

	        float casualBalance = 12.0f;
	        float totalLop = 0f;

	        if (summaryOpt.isPresent()) {
	            EmployeeLeaveSummary s = summaryOpt.get();
	           // casualBalance = safe(s.getCasualLeaveBalance());

	            totalLop = safe(s.getLopJan()) + safe(s.getLopFeb()) + safe(s.getLopMar())
	                     + safe(s.getLopApr()) + safe(s.getLopMay()) + safe(s.getLopJun())
	                     + safe(s.getLopJul()) + safe(s.getLopAug()) + safe(s.getLopSep())
	                     + safe(s.getLopOct()) + safe(s.getLopNov()) + safe(s.getLopDec())
	                     + safe(s.getLop());
	        }

	        // 4. Calculate CL Used & Remaining
	        float clUsed = Math.max(summaryOpt.get().getLeaveTaken() - summaryOpt.get().getLop(), 0f);
	        float clRemaining = Math.max(casualBalance - clUsed, 0f);

	        // 5. LWP = recorded LOP + extra days beyond CL
	       // float lwp = totalLop + Math.max(totalTaken - (casualBalance + totalLop), 0f);

	        // 6. Response
	       // System.out.println("clsuse::"+clUsed +"totalTaken:::"+totalTaken);
	        Map<String, Object> resp = new HashMap<>();
	        resp.put("empId", empId);
	        resp.put("year", year);
	       // resp.put("totalTakenLeave", Math.round(totalTaken));
	        resp.put("casualUsed", clUsed);           // Booked
	        resp.put("casualRemaining", Math.round(clRemaining)); // Available
	       // resp.put("leavewithoutpay", Math.round(lwp));
	        resp.put("rawCasualBalance", Math.round(casualBalance));

	        return ResponseEntity.ok(resp);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("error", "Failed to fetch leave counts", "message", e.getMessage()));
	    }
	}

	// Safe null check for Float
	private float safe(Float value) {
	    return value == null ? 0f : value;
	}
	// Controller
	@PostMapping("/leaveRequest")
	public ResponseEntity<?> leaveRequest(@RequestBody EmployeeLeaveMasterTbl employeeLeaveMasterTbl) {
		try {
			// Step 1: Normalize start date to remove time part
			LocalDate onlyDate = employeeLeaveMasterTbl.getStartdate().toLocalDateTime().toLocalDate();
			Timestamp dateOnlyTimestamp = Timestamp.valueOf(onlyDate.atStartOfDay());

			// Step 2: Check if leave already exists for that date
			boolean existingLeave = employeeLeaveMasterRepository
					.countByEmpidAndStartDate(employeeLeaveMasterTbl.getEmpid(), dateOnlyTimestamp) > 0;

			if (existingLeave) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\": \"Leave request already exists for the selected date.\"}");
			}

			// Step 3: Save the leave request
			employeeLeaveMasterTbl.setNoofbooked(1.0f);
			employeeLeaveMasterTbl.setDelflg("N");
			employeeLeaveMasterTbl.setEntitycreflg("N");
			employeeLeaveMasterTbl.setStatus("Pending");
			EmployeeLeaveMasterTbl savedRequest = employeeLeaveMasterRepository.save(employeeLeaveMasterTbl);

			// Step 4: Update consolidated leave
			updateConsolidatedLeave(employeeLeaveMasterTbl);

			Map<String, Object> response = new HashMap<>();
			response.put("message", "Leave Request sent successfully");
			response.put("data", savedRequest);

			// Step 5: Fetch employee details (from either table)
			String empId = employeeLeaveMasterTbl.getEmpid();
			Optional<usermaintenance> empOpt = usermaintenanceRepository.findByEmpIdOrUserId(empId);
			Optional<TraineeMaster> traineeOpt = Optional.empty();
			if (empOpt.isEmpty()) {
				traineeOpt = traineemasterRepository.findByTrngidOrUserId(empId);
			}
			if (empOpt.isEmpty() && traineeOpt.isEmpty()) {
				throw new RuntimeException("Employee not found in both employee and trainee tables");
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
				throw new RuntimeException("Manager not assigned to this employee");
			}

			// Step 6: Fetch manager details (from either table)
			Optional<usermaintenance> managerOpt = usermaintenanceRepository.findByEmpIdOrUserId(managerId);
			Optional<TraineeMaster> managerTraineeOpt = Optional.empty();
			if (managerOpt.isEmpty()) {
				managerTraineeOpt = traineemasterRepository.findByTrngidOrUserId(managerId);
			}
			if (managerOpt.isEmpty() && managerTraineeOpt.isEmpty()) {
				throw new RuntimeException("Manager not found in both tables");
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

			// Step 7: Send email if manager email exists
			if (managerEmail != null && !managerEmail.isEmpty()) {
				UserRoleMaintenance role = userRoleMaintenanceRepository.findByRoleid(roleId)
						.orElseThrow(() -> new RuntimeException("Role not found"));

				String subject = "Leave Request Approval Needed for " + employeeFirstName;
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

				String startDateFormatted = sdf.format(employeeLeaveMasterTbl.getStartdate());
				String endDateFormatted = sdf.format(employeeLeaveMasterTbl.getEnddate());

				String body = String.format(
						"Dear %s,\n\n"
								+ "Employee %s (%s) has submitted a leave request. Please find the details below:\n\n"
								+ "Leave Type: %s\n" + "From Date: %s\n" + "To Date: %s\n" + "No. of Days: %s\n"
								+ "Reason: %s\n\n" + "Kindly review the request and take necessary action.\n\n"
								+ "Regards,\n" + "%s,\n" + "%s - %s,\n" + "Whitestone Software Solution Pvt Ltd.\n",
						managerFirstName, employeeFirstName, empId, employeeLeaveMasterTbl.getLeavetype(),
						startDateFormatted, endDateFormatted, employeeLeaveMasterTbl.getNoofdays(),
						employeeLeaveMasterTbl.getLeavereason(), employeeFirstName, role.getRolename(),
						role.getDescription());

				emailService.sendLeaveEmail(employeeEmail, managerEmail, subject, body);
				response.put("emailStatus", "Email sent to manager: " + managerEmail);
			} else {
				response.put("emailStatus", "Manager email not found");
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\": \"Failed to send Leave Request\"}");
		}
	}

	@Autowired
	private EmployeeLeaveSummaryRepository employeeLeaveSummaryRepository;

	public void updateConsolidatedLeave(EmployeeLeaveMasterTbl leaveRequest) {
		String empId = leaveRequest.getEmpid();
		LocalDate requestDate = leaveRequest.getStartdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int year = requestDate.getYear();
		int currentMonth = requestDate.getMonthValue();
		Float requestedDays = leaveRequest.getNoofdays();

		// Fetch leave summary or initialize if not present
		EmployeeLeaveSummary leaveSummary = employeeLeaveSummaryRepository.findByEmpIdAndYear(empId, year)
				.orElseGet(() -> {
					EmployeeLeaveSummary newSummary = new EmployeeLeaveSummary();
					newSummary.setEmpId(empId);
					newSummary.setYear(year);
					return newSummary;
				});

		// Fetch previous month’s unutilized CL and carry it forward
		Float previousCarryForwardCL = (currentMonth > 1) ? getPreviousMonthCL(leaveSummary, currentMonth) : 0;
		Float carryForwardCL = leaveSummary.getCasualLeaveBalance() + previousCarryForwardCL;

		// Max CL accrued up to the current month
		long maxAccruedCL = currentMonth;
		Float availableCL = Math.min(carryForwardCL + 1, maxAccruedCL);

		// Apply CL first, then LOP
		Float clUsed = Math.min(requestedDays, availableCL);
		Float lopDays = requestedDays - clUsed;

		// ✅ Add any remaining LOP from the previous month
		Float previousLopBalance = getPreviousMonthLop(leaveSummary, currentMonth);
		lopDays += previousLopBalance;

		// Update leave summary
		leaveSummary.setCasualLeaveBalance(availableCL - clUsed);
		leaveSummary.setLeaveTaken(leaveSummary.getLeaveTaken() + requestedDays);
		leaveSummary.setLop(leaveSummary.getLop() + lopDays);

		// ✅ Update Monthly LOP with carry-forward logic
		updateMonthlyLop(leaveSummary, currentMonth, lopDays);

		// Update timestamp
		leaveSummary.setUpdatedAt(LocalDateTime.now());

		// Save updated record
		employeeLeaveSummaryRepository.save(leaveSummary);

		System.out.println("Month: " + currentMonth + " | Applied Leave: " + requestedDays + " | CL Used: " + clUsed
				+ " | LOP (With Carry Forward): " + lopDays);
	}

	// ✅ Helper method to get CL balance from the previous month
	private Float getPreviousMonthCL(EmployeeLeaveSummary leaveSummary, int currentMonth) {
		switch (currentMonth - 1) { // Get last month's CL
		case 1:
			return leaveSummary.getCasualLeaveBalance();
		case 2:
			return leaveSummary.getCasualLeaveBalance();
		case 3:
			return leaveSummary.getCasualLeaveBalance();
		case 4:
			return leaveSummary.getCasualLeaveBalance();
		case 5:
			return leaveSummary.getCasualLeaveBalance();
		case 6:
			return leaveSummary.getCasualLeaveBalance();
		case 7:
			return leaveSummary.getCasualLeaveBalance();
		case 8:
			return leaveSummary.getCasualLeaveBalance();
		case 9:
			return leaveSummary.getCasualLeaveBalance();
		case 10:
			return leaveSummary.getCasualLeaveBalance();
		case 11:
			return leaveSummary.getCasualLeaveBalance();
		case 12:
			return leaveSummary.getCasualLeaveBalance();
		default:
			return 0.0f; // No previous month for January
		}
	}

	// ✅ Helper method to update LOP for the correct month
	private void updateMonthlyLop(EmployeeLeaveSummary leaveSummary, int month, Float lopDays) {
		switch (month) {
		case 1:
			leaveSummary.setLopJan(leaveSummary.getLopJan() + lopDays);
			break;
		case 2:
			leaveSummary.setLopFeb(leaveSummary.getLopFeb() + lopDays);
			break;
		case 3:
			leaveSummary.setLopMar(leaveSummary.getLopMar() + lopDays);
			break;
		case 4:
			leaveSummary.setLopApr(leaveSummary.getLopApr() + lopDays);
			break;
		case 5:
			leaveSummary.setLopMay(leaveSummary.getLopMay() + lopDays);
			break;
		case 6:
			leaveSummary.setLopJun(leaveSummary.getLopJun() + lopDays);
			break;
		case 7:
			leaveSummary.setLopJul(leaveSummary.getLopJul() + lopDays);
			break;
		case 8:
			leaveSummary.setLopAug(leaveSummary.getLopAug() + lopDays);
			break;
		case 9:
			leaveSummary.setLopSep(leaveSummary.getLopSep() + lopDays);
			break;
		case 10:
			leaveSummary.setLopOct(leaveSummary.getLopOct() + lopDays);
			break;
		case 11:
			leaveSummary.setLopNov(leaveSummary.getLopNov() + lopDays);
			break;
		case 12:
			leaveSummary.setLopDec(leaveSummary.getLopDec() + lopDays);
			break;
		default:
			throw new IllegalArgumentException("Invalid month: " + month);
		}
	}

	// ✅ Helper method to get LOP balance from the previous month
	private Float getPreviousMonthLop(EmployeeLeaveSummary leaveSummary, int currentMonth) {
		switch (currentMonth - 1) {
		case 1:
			return leaveSummary.getLopJan();
		case 2:
			return leaveSummary.getLopFeb();
		case 3:
			return leaveSummary.getLopMar();
		case 4:
			return leaveSummary.getLopApr();
		case 5:
			return leaveSummary.getLopMay();
		case 6:
			return leaveSummary.getLopJun();
		case 7:
			return leaveSummary.getLopJul();
		case 8:
			return leaveSummary.getLopAug();
		case 9:
			return leaveSummary.getLopSep();
		case 10:
			return leaveSummary.getLopOct();
		case 11:
			return leaveSummary.getLopNov();
		case 12:
			return leaveSummary.getLopDec();
		default:
			return 0.0f;
		}
	}

	@Autowired
	private ExpenseDetailsModRepository expenseDetailsRepository;

	@GetMapping("/expenses/receipt/{expenseId}")
	public ResponseEntity<?> getReceiptImage(@PathVariable String expenseId) {
		try {
			System.out.println("imagePath" + expenseId);
			// Fetch the expense by ID
			ExpenseDetailsMod expense = expenseDetailsRepository.findById(expenseId)
					.orElseThrow(() -> new RuntimeException("Expense not found with ID: " + expenseId));

			// Retrieve the file path from the expense
			String filePath = expense.getRecipt();
			Path imagePath = Paths.get(filePath);
			System.out.println("imagePath" + imagePath);
			// Ensure the file exists
			if (!Files.exists(imagePath)) {
				Map<String, String> errorResponse = new HashMap<>(); // ✅ Use HashMap for multiple key-value pairs
				errorResponse.put("message", "Receipt image not found for expense ID: " + expenseId);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
			}

			// Read the image as a byte array
			byte[] imageBytes = Files.readAllBytes(imagePath);
			String fileName = imagePath.getFileName().toString();
			ByteArrayResource resource = new ByteArrayResource(imageBytes);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
					.body(resource);

		} catch (Exception e) {
			e.printStackTrace();

			// Using HashMap to return multiple error details
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("message", "Error retrieving receipt image.");
			errorResponse.put("errorDetails", e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	// Endpoint to upload a receipt for a specific employee
	@PostMapping("/uploadReceipt/{Empid}")
	public ResponseEntity<String> uploadReceipt(@PathVariable String Empid,
			@RequestParam("receipt") MultipartFile receipt) {
		try {
			System.out.println("File>>>Hiiii");
			if (receipt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file uploaded");
			}
			String uploadDir = docUploadDir + "/expense_bills";
			// Create the directory if it doesn't exist
			Path path = Paths.get(uploadDir);
			System.out.println("File>>>" + path);
			if (!Files.exists(path)) {
				System.out.println("File>>>" + path);
				Files.createDirectories(path);
			}

			// Save the file to the server
			String filename = Empid + "_" + receipt.getOriginalFilename();
			Path filePath = path.resolve(filename);
			((MultipartFile) receipt).transferTo(filePath);

			// Optionally, save the receipt file path to the database
			ExpenseDetailsMod expenseDetails = new ExpenseDetailsMod();
			expenseDetails.setEmpId(Empid);
			expenseDetails.setRecipt(filePath.toString()); // Assuming your database has a field for file paths
			expenseDetailsRepository.save(expenseDetails);

			return ResponseEntity.ok("File uploaded successfully: " + filename);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the file");
		}
	}

	@GetMapping("/expensesbyid/{Empid}")
	public ResponseEntity<List<ExpenseDetailsMod>> getExpenseByEmpid(@PathVariable String Empid) {
		try {
			// Fetch expenses where Empid matches and delflg = 'N'
			List<ExpenseDetailsMod> expenses = expenseDetailsRepository.findByEmpIdAndDelflg(Empid, "N");
			return ResponseEntity.ok(expenses);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/app/expensesbyid/all")
	public ResponseEntity<List<ExpenseDetailsMod>> getAdminAllExpenses() {
		try {
			List<ExpenseDetailsMod> expenses = expenseDetailsRepository.findByDelflg("N");
			return ResponseEntity.ok(expenses);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/app/expensesbyid/{Empid}")
	public ResponseEntity<List<ExpenseDetailsMod>> getExpensesByEmpid(@PathVariable String Empid) {
		try {
			// Fetch expenses where Empid matches and delflg = 'N'
			List<ExpenseDetailsMod> expenses = expenseDetailsRepository.findByEmpIdAndDelflg(Empid, "N");
			return ResponseEntity.ok(expenses);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/app/expensesbyreportingmanager/{Empid}")
	public ResponseEntity<List<ExpenseDetailsMod>> getExpensesByReportingManager(@PathVariable String Empid) {
		try {
			Set<String> allEmpIds = new HashSet<>();
			Deque<String> queue = new ArrayDeque<>();
			queue.add(Empid); // Start with the manager

			while (!queue.isEmpty()) {
				String currentEmpId = queue.poll();
				List<usermaintenance> reportingEmployees = usermaintenanceRepository.findByRepoteToCustom(currentEmpId);

				for (usermaintenance emp : reportingEmployees) {
					String empId = emp.getEmpid();
					if (allEmpIds.add(empId)) {
						queue.add(empId); // Add to queue for further traversal
					}
				}
			}

			if (allEmpIds.isEmpty()) {
				return ResponseEntity.ok(Collections.emptyList());
			}

			List<ExpenseDetailsMod> expenses = expenseDetailsRepository
					.findByEmpIdInAndDelflg(new ArrayList<>(allEmpIds), "N");

			return ResponseEntity.ok(expenses);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/advances/advancebyreportingmancger/{Empid}")
	public ResponseEntity<List<AdvancesDetailsMod>> getAdvanceByReportingManager(@PathVariable String Empid) {
		try {
			Set<String> allEmpIds = new HashSet<>();
			Deque<String> queue = new ArrayDeque<>();
			queue.add(Empid); // Start with the manager

			while (!queue.isEmpty()) {
				String currentEmpId = queue.poll();
				List<usermaintenance> reportingEmployees = usermaintenanceRepository.findByRepoteToCustom(currentEmpId);

				for (usermaintenance emp : reportingEmployees) {
					String empId = emp.getEmpid();
					if (allEmpIds.add(empId)) {
						queue.add(empId); // Add to queue for further traversal
					}
				}
			}

			if (allEmpIds.isEmpty()) {
				return ResponseEntity.ok(Collections.emptyList());
			}

			List<AdvancesDetailsMod> expenses = advancesDetailsModRepository
					.findByEmpIdInAndDelflg(new ArrayList<>(allEmpIds), "N");

			return ResponseEntity.ok(expenses);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

//	@GetMapping("/app/expensesbyreportingmanager/{Empid}")
//	public ResponseEntity<List<ExpenseDetailsMod>> getExpensesByReportingManager(@PathVariable String Empid) {
//		try {
//			List<usermaintenance> reportingEmployees = usermaintenanceRepository.findByRepoteToCustom(Empid);
//			if (reportingEmployees.isEmpty()) {
//				return ResponseEntity.ok(Collections.emptyList());
//			}
//
//			List<String> empIds = reportingEmployees.stream().map(usermaintenance::getEmpid)
//					.collect(Collectors.toList());
//			List<ExpenseDetailsMod> expenses = expenseDetailsRepository.findByEmpIdInAndDelflg(empIds, "N");
//			return ResponseEntity.ok(expenses);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//		}
//	}

//	@GetMapping("/advances/advancebyreportingmancger/{Empid}")
//	public ResponseEntity<List<AdvancesDetailsMod>> getAdvanceByReportingManager(@PathVariable String Empid) {
//		try {
//			List<usermaintenance> reportingEmployees = usermaintenanceRepository.findByRepoteToCustom(Empid);
//			if (reportingEmployees.isEmpty()) {
//				return ResponseEntity.ok(Collections.emptyList());
//			}
//			System.out.println("reportingEmployees>>>>" + reportingEmployees);
//			List<String> empIds = reportingEmployees.stream().map(usermaintenance::getEmpid)
//					.collect(Collectors.toList());
//			List<AdvancesDetailsMod> expenses = advancesDetailsModRepository.findByEmpIdInAndDelflg(empIds, "N");
//			return ResponseEntity.ok(expenses);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//		}
//	}

	@GetMapping("/app/approvedexpenses/all")
	public ResponseEntity<List<ExpenseDetailsMod>> getAllApprovedExpenses() {
		try {
			List<ExpenseDetailsMod> expenses = expenseDetailsRepository.findByDelflg("N");
			List<ExpenseDetailsMod> approvedExpenses = expenses.stream().filter(e -> e.getPaymentStatus() != null)
					.collect(Collectors.toList());
			return ResponseEntity.ok(approvedExpenses != null ? approvedExpenses : new ArrayList<>());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
		}
	}

	@GetMapping("/app/approvedexpenses/{Empid}")
	public ResponseEntity<List<ExpenseDetailsMod>> getApprovedExpensesByEmpId(@PathVariable String Empid) {
		try {
			// Collect all employee IDs in the reporting hierarchy
			Set<String> allEmpIds = new HashSet<>();
			Deque<String> queue = new ArrayDeque<>();
			queue.add(Empid); // Start with the given employee

			while (!queue.isEmpty()) {
				String currentEmpId = queue.poll();
				List<usermaintenance> reportingEmployees = usermaintenanceRepository.findByRepoteToCustom(currentEmpId);

				for (usermaintenance emp : reportingEmployees) {
					String empId = emp.getEmpid();
					if (allEmpIds.add(empId)) {
						queue.add(empId); // Add to queue for further traversal
					}
				}
			}

			// Include the original employee ID
			allEmpIds.add(Empid);

			if (allEmpIds.isEmpty()) {
				return ResponseEntity.ok(Collections.emptyList());
			}

			// Fetch expenses for all employee IDs in the hierarchy
			List<ExpenseDetailsMod> expenses = expenseDetailsRepository
					.findByEmpIdInAndDelflg(new ArrayList<>(allEmpIds), "N");

			// Filter expenses with non-null payment status
			List<ExpenseDetailsMod> approvedExpenses = expenses.stream().filter(e -> e.getPaymentStatus() != null)
					.collect(Collectors.toList());

			return ResponseEntity.ok(approvedExpenses != null ? approvedExpenses : new ArrayList<>());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
		}
	}

	@PutMapping("/update/expenses/{expenseId}")
	@Transactional
	public ResponseEntity<Map<String, Object>> updateExpense(@PathVariable String expenseId,
			@RequestBody Map<String, Object> requestBody) {

		Map<String, Object> response = new HashMap<>();
		String reason = (String) requestBody.get("reason");
		BigDecimal approvedAmount = new BigDecimal(requestBody.get("approvedAmount").toString());

		try {
			Optional<ExpenseDetailsMod> existingExpenseOpt = expenseDetailsRepository.findById(expenseId);
			if (!existingExpenseOpt.isPresent()) {
				response.put("success", false);
				response.put("message", "Expense not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			ExpenseDetailsMod existingExpense = existingExpenseOpt.get();
			existingExpense.setEntitycreflg("Y");
			existingExpense.setPaymentStatus(0);
			existingExpense.setRejectreason(reason);
			existingExpense.setApprovedAmount(approvedAmount);
			existingExpense.setRmodtime(new Date());

			String approver = existingExpense.getApprover();
			usermaintenance currentApprover = usermaintenanceRepository.findByEmpid(approver);

			if (!approver.equals("10029")) {
				if (currentApprover != null && currentApprover.getRepoteTo() != null) {
					usermaintenance nextApprover = usermaintenanceRepository.findByEmpid(currentApprover.getRepoteTo());

					if (nextApprover != null && nextApprover.getRoleid() != null) {
						Optional<UserRoleMaintenance> roleOpt = userRoleMaintenanceRepository
								.findByRoleid(nextApprover.getRoleid());

						if (roleOpt.isPresent()) {
							existingExpense.setApprover(nextApprover.getEmpid());
							existingExpense.setStatus("Pending for " + roleOpt.get().getRolename() + " approval");
						} else {
							existingExpense.setStatus("Payment to be Initiate");
							existingExpense.setApprover("10029");
						}
					} else {
						existingExpense.setStatus("Payment to be Initiate");
						existingExpense.setApprover("10029");
					}
				} else {
					existingExpense.setStatus("Payment to be Initiate");
					existingExpense.setApprover("10029");
				}
			}

			expenseDetailsRepository.save(existingExpense);

			// Email notification
			String empId = existingExpense.getEmpId();
			usermaintenance employee = usermaintenanceRepository.findByEmpid(empId);
			if (employee == null) {
				response.put("success", false);
				response.put("message", "Employee not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			// Format currency (optional)
			BigDecimal appliedAmount = existingExpense.getAmount(); // assuming you have this field
			String subjectToEmployee = "Your Expense Request Has Been Approved";

			String bodyToEmployee = "Dear " + employee.getFirstname() + ",\n\n" + "Your expense request (ID: "
					+ expenseId + ") has been approved.\n\n" + "Applied Amount : ₹" + appliedAmount + "\n"
					+ "Approved Amount : ₹" + approvedAmount + "\n\n" + "Regards,\nHRMS System";

			usermaintenance manager = usermaintenanceRepository.findByEmpid(employee.getRepoteTo());
			if (employee.getEmailid() != null) {
				emailService.sendLeaveEmail(manager.getEmailid(), employee.getEmailid(), subjectToEmployee,
						bodyToEmployee);
			} else {
				System.out.println("No email found for employee: " + empId);
			}

			response.put("success", true);
			response.put("message", "Expense updated successfully and email sent.");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Error occurred while updating expense.");
			response.put("errorDetails", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PutMapping("/update/advance/approval/{advanceId}")
	@Transactional
	public ResponseEntity<Map<String, Object>> updateAdvance(@PathVariable String advanceId,
			@RequestBody Map<String, Object> requestBody) {

		Map<String, Object> response = new HashMap<>();
		String reason = (String) requestBody.get("reason");
		BigDecimal approvedAmount = new BigDecimal(requestBody.get("approvedAmount").toString());

		try {
			Optional<AdvancesDetailsMod> existingAdvanceOpt = advancesDetailsModRepository.findById(advanceId);
			if (!existingAdvanceOpt.isPresent()) {
				response.put("success", false);
				response.put("message", "Advance not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			AdvancesDetailsMod existingAdvance = existingAdvanceOpt.get();
			existingAdvance.setEntityCreFlg("Y");
			existingAdvance.setPaymentStatus(0);
			existingAdvance.setApprovedAmount(approvedAmount);
			existingAdvance.setRejectreason(reason);
			existingAdvance.setRmodTime(new Date());

			String currentApproverId = existingAdvance.getApprover();
			usermaintenance currentApprover = usermaintenanceRepository.findByEmpid(currentApproverId);

			if (currentApprover != null && currentApprover.getRepoteTo() != null) {
				usermaintenance nextApprover = usermaintenanceRepository.findByEmpid(currentApprover.getRepoteTo());

				if (nextApprover != null && nextApprover.getRoleid() != null) {
					Optional<UserRoleMaintenance> roleOpt = userRoleMaintenanceRepository
							.findByRoleid(nextApprover.getRoleid());

					if (roleOpt.isPresent()) {
						String nextRoleName = roleOpt.get().getRolename();

						if ("Accountant".equalsIgnoreCase(nextRoleName) || "ACC".equalsIgnoreCase(nextRoleName)) {
							existingAdvance.setStatus("Approved");
							existingAdvance.setApprover(nextApprover.getEmpid());
						} else {
							existingAdvance.setApprover(nextApprover.getEmpid());
							existingAdvance.setStatus("Pending for " + nextRoleName + " approval");
						}
					} else {
						existingAdvance.setStatus("Payment to be Initiate");
					}
				} else {
					existingAdvance.setStatus("Payment to be Initiate");
				}
			} else {
				existingAdvance.setStatus("Payment to be Initiate");
			}

			advancesDetailsModRepository.save(existingAdvance);

			// Email notification
			String empId = existingAdvance.getEmpId();
			usermaintenance employee = usermaintenanceRepository.findByEmpid(empId);
			if (employee == null) {
				response.put("success", false);
				response.put("message", "Employee not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			BigDecimal appliedAmount = existingAdvance.getAmount(); // assuming this field exists
			String subjectToEmployee = "Your Advance Request Has Been Approved";
			String bodyToEmployee = "Dear " + employee.getFirstname() + ",\n\n" + "Your advance request (ID: "
					+ advanceId + ") has been approved.\n\n" + "Applied Amount : ₹" + appliedAmount + "\n"
					+ "Approved Amount : ₹" + approvedAmount + "\n\n" + "Regards,\nHRMS System";

			usermaintenance manager = usermaintenanceRepository.findByEmpid(employee.getRepoteTo());
			if (employee.getEmailid() != null) {
				emailService.sendLeaveEmail(manager.getEmailid(), employee.getEmailid(), subjectToEmployee,
						bodyToEmployee);
			}

			response.put("success", true);
			response.put("message", "Advance updated successfully and email sent.");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Error occurred while updating advance.");
			response.put("errorDetails", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

//	@PutMapping("/update/advance/approval/{advanceId}")
//	@Transactional
//	public ResponseEntity<Map<String, Object>> updateAdvance(@PathVariable String advanceId,
//			@RequestBody Map<String, Object> requestBody) {
//
//		Map<String, Object> response = new HashMap<>();
//		String Reason = (String) requestBody.get("reason");
//		try {
//			// Fetch the existing advance record by ID
//			Optional<AdvancesDetailsMod> existingAdvanceOpt = advancesDetailsModRepository.findById(advanceId);
//			if (!existingAdvanceOpt.isPresent()) {
//				response.put("success", false);
//				response.put("message", "Advance not found.");
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//			}
//
//			// Update the relevant fields
//			AdvancesDetailsMod existingAdvance = existingAdvanceOpt.get();
//			existingAdvance.setEntityCreFlg("Y");
//			existingAdvance.setPaymentStatus(0);
//			existingAdvance.setRejectreason(Reason);
//			existingAdvance.setRmodTime(new Date());
//			String approver = existingAdvance.getApprover();
//
//			usermaintenance currentApprover = usermaintenanceRepository.findByEmpid(approver);
//
//			if (!currentApprover.equals("10029")) {
//
//				if (currentApprover != null && currentApprover.getRepoteTo() != null) {
//					usermaintenance nextApprover = usermaintenanceRepository.findByEmpid(currentApprover.getRepoteTo());
//
//					if (nextApprover != null && nextApprover.getRoleid() != null) {
//						Optional<UserRoleMaintenance> roleOpt = userRoleMaintenanceRepository
//								.findByRoleid(nextApprover.getRoleid());
//
//						if (roleOpt.isPresent()) {
//							existingAdvance.setApprover(nextApprover.getEmpid());
//							existingAdvance.setStatus("Pending for " + roleOpt.get().getRolename() + " approval");
//						} else {
//							// Role not found
//							existingAdvance.setStatus("Payment to be Initiate");
//							existingAdvance.setApprover("10029");
//						}
//					} else {
//						// nextApprover is null
//						existingAdvance.setStatus("Payment to be Initiate");
//						existingAdvance.setApprover("10029");
//					}
//				} else {
//					existingAdvance.setStatus("Payment to be Initiate");
//					existingAdvance.setApprover("10029");
//				}
//			} else {
//				existingAdvance.setStatus("Approved");
//				existingAdvance.setApprover("10029");
//			}
//
//			// Save the updated advance
//			advancesDetailsModRepository.save(existingAdvance);
//
//			// Get employee info related to the advance
//			String empId = existingAdvance.getEmpId();
//			usermaintenance employee = usermaintenanceRepository.findByEmpid(empId);
//			if (employee == null) {
//				response.put("success", false);
//				response.put("message", "Employee not found.");
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//			}
//
//			// Email notification to employee
//			String subjectToEmployee = "Your Advance Request Has Been Approved";
//			String bodyToEmployee = "Dear " + employee.getFirstname() + ",\n\n" + "Your advance request (ID: "
//					+ advanceId + ") has been approved by the admin.\n\n" + "Regards,\nHRMS System";
//
//			usermaintenance manager = usermaintenanceRepository.findByEmpid(employee.getRepoteTo());
//
//			if (employee.getEmailid() != null) {
//				emailService.sendLeaveEmail(manager.getEmailid(), employee.getEmailid(), subjectToEmployee,
//						bodyToEmployee);
//			} else {
//				System.out.println("No email found for employee: " + employee.getEmpid());
//			}
//
//			// Success response
//			response.put("success", true);
//			response.put("message", "Advance updated successfully and email sent.");
//			return ResponseEntity.ok(response);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			response.put("success", false);
//			response.put("message", "Error occurred while updating advance.");
//			response.put("errorDetails", e.getMessage());
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//		}
//	}

	@PutMapping("/update/expenses/{expenseId}/payment-status")
	@Transactional
	public ResponseEntity<Map<String, Object>> updatePaymentStatus(@PathVariable String expenseId,
			@RequestBody Map<String, Object> requestBody) {

		Map<String, Object> response = new HashMap<>();

		System.out.println("GJ:: response" + requestBody);
		try {
			Optional<ExpenseDetailsMod> existingExpenseOpt = expenseDetailsRepository.findById(expenseId);

			// Check if the expense exists
			if (!existingExpenseOpt.isPresent()) {
				response.put("success", false);
				response.put("message", "Expense not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			ExpenseDetailsMod expense = existingExpenseOpt.get();
			String empId = expense.getEmpId();

			usermaintenance employee = usermaintenanceRepository.findByEmpid(empId);
			if (employee == null || employee.getRepoteTo() == null) {
				response.put("success", false);
				response.put("message", "Employee or reporting manager not found.");
				return ResponseEntity.badRequest().body(response);
			}

			usermaintenance manager = usermaintenanceRepository.findByEmpid(employee.getRepoteTo());

			// Example: Check if the user calling the API is the manager (replace with
			// actual authentication)
//				String requesterEmpId = (String) requestBody.get("requesterEmpId"); // Simulate authenticated manager ID
//				System.out.println("requesterEmpId>>>>>>>>>>>_____"+requesterEmpId);
//				if (!manager.getEmpid().equalsIgnoreCase(requesterEmpId)) {
//					response.put("success", false);
//					response.put("message", "Unauthorized. Only the manager can update the payment status.");
//					return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
//				}
			Integer paymentStatus = null;
			try {

				paymentStatus = Integer.parseInt(requestBody.get("paymentStatus").toString());
				System.out.println("paymentStatus>>>>>>>>>>>_____" + paymentStatus);
				if (paymentStatus == null) {
					response.put("success", false);
					response.put("message", "Missing paymentStatus.");
					return ResponseEntity.badRequest().body(response);
				}

				// Update the payment status
				expense.setPaymentStatus(paymentStatus);

				if (paymentStatus == 0) {
					expense.setStatus("Payment To Be Initiate");
				} else if (paymentStatus == 1) {
					expense.setStatus("Payment Initiated");
				} else if (paymentStatus == 2) {
					expense.setStatus("Payment Done");
				}
				expense.setRmodtime(new Date());

				expenseDetailsRepository.save(expense);
			} catch (Exception e) {
				System.out.println("Catch block ::   " + e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Send email to employee
			String subjectToEmployee = "Payment Status Updated";
			String bodyToEmployee = "Dear " + employee.getFirstname() + ",\n\n" + "Your expense (ID: " + expenseId
					+ ") has been updated to status: " + expense.getStatus() + " by your manager.\n\n"
					+ "Regards,\nHRMS System";
			emailService.sendLeaveEmail(manager.getEmailid(), employee.getEmailid(), subjectToEmployee, bodyToEmployee);

			// If the manager has a manager, notify them as well
			if (manager.getRepoteTo() != null) {
				usermaintenance managersManager = usermaintenanceRepository.findByEmpid(manager.getRepoteTo());
				if (managersManager != null && managersManager.getEmailid() != null) {
					String subjectToSuperManager = "Your Reportee Performed an Action";
					String bodyToSuperManager = "Dear " + managersManager.getFirstname() + ",\n\n" + "Your reportee "
							+ manager.getFirstname() + " has updated the payment status of an expense submitted by "
							+ employee.getFirstname() + ".\n\n" + "Expense ID: " + expenseId + "\nUpdated Status: "
							+ paymentStatus + "\n\nRegards,\nHRMS System";
					emailService.sendLeaveEmail(manager.getEmailid(), managersManager.getEmailid(),
							subjectToSuperManager, bodyToSuperManager);
				}
			}

			response.put("success", true);
			response.put("message", "Payment status updated and notifications sent.");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "Error updating payment status.");
			response.put("errorDetails", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PutMapping("/del/update/expenses/{expenseId}")
	@Transactional
	public ResponseEntity<Map<String, Object>> updateExpense2(@PathVariable String expenseId,
			@RequestParam("status") String status) {

		Map<String, Object> response = new HashMap<>();

		try {
			// Fetch the existing expense record by ID
			Optional<ExpenseDetailsMod> existingExpenseOpt = expenseDetailsRepository.findById(expenseId);
			if (!existingExpenseOpt.isPresent()) {
				response.put("success", false);
				response.put("message", "Expense not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			// Update the relevant fields
			ExpenseDetailsMod existingExpense = existingExpenseOpt.get();
			if ("deleted".equalsIgnoreCase(status)) {
				existingExpense.setDelflg("Y"); // Set delete flag
			}
			existingExpense.setRmodtime(new Date()); // Update modification timestamp

			// Save the updated entity
			expenseDetailsRepository.save(existingExpense);

			// Prepare success response
			response.put("success", true);
			response.put("message", "Expense updated successfully.");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();

			// Prepare error response
			response.put("success", false);
			response.put("message", "Error occurred while updating expense.");
			response.put("errorDetails", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PutMapping("/rej/expenses/{expenseId}")
	@Transactional
	public ResponseEntity<Map<String, Object>> updateExpense1(@PathVariable String expenseId,
			@RequestBody Map<String, Object> requestBody) {

		Map<String, Object> response = new HashMap<>();

		try {
			Optional<ExpenseDetailsMod> existingExpenseOpt = expenseDetailsRepository.findById(expenseId);
			if (!existingExpenseOpt.isPresent()) {
				response.put("success", false);
				response.put("message", "Expense not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			String rejectReason = (String) requestBody.get("reason");
			String status = (String) requestBody.get("status");

			if (rejectReason == null || rejectReason.trim().isEmpty()) {
				response.put("success", false);
				response.put("message", "Reject reason is required.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			ExpenseDetailsMod existingExpense = existingExpenseOpt.get();
			existingExpense.setEntitycreflg("N");
			existingExpense.setStatus(status); // e.g., "rejected"
			existingExpense.setRmodtime(new Date());
			existingExpense.setRejectreason(rejectReason);
			expenseDetailsRepository.save(existingExpense);

			String empId = existingExpense.getEmpId();
			usermaintenance employee = usermaintenanceRepository.findByEmpid(empId);

			if (employee != null && employee.getEmailid() != null) {
				// Send rejection email to employee
				String to = employee.getEmailid();
				String subject = "Expense Rejected - ID: " + expenseId;
				String body = "Dear " + employee.getFirstname() + ",\n\n" + "Your expense request with ID: " + expenseId
						+ " has been rejected by your manager.\n" + "Reason: " + rejectReason + "\n\n"
						+ "Please contact your manager for further details.\n\n" + "Regards,\nHRMS Team";

				// Send from manager's email if available
				String fromEmail = "manager@yourcompany.com";
				if (employee.getRepoteTo() != null) {
					usermaintenance manager = usermaintenanceRepository.findByEmpid(employee.getRepoteTo());
					if (manager != null && manager.getEmailid() != null) {
						fromEmail = manager.getEmailid();

						// Notify manager's manager (optional)
						if (manager.getRepoteTo() != null) {
							usermaintenance managersManager = usermaintenanceRepository
									.findByEmpid(manager.getRepoteTo());
							if (managersManager != null && managersManager.getEmailid() != null) {
								String ccSubject = "Expense Rejected Notification";
								String ccBody = "Dear " + managersManager.getFirstname() + ",\n\n" + "Your reportee "
										+ manager.getFirstname() + " has rejected the expense request from "
										+ employee.getFirstname() + ".\n\n" + "Expense ID: " + expenseId + "\nReason: "
										+ rejectReason + "\n\nRegards,\nHRMS System";

								emailService.sendLeaveEmail(fromEmail, managersManager.getEmailid(), ccSubject, ccBody);
							}
						}
					}
				}

				emailService.sendLeaveEmail(fromEmail, to, subject, body);
			} else {
				System.out.println("Employee email not found for ID: " + empId);
			}

			response.put("success", true);
			response.put("message", "Expense updated and rejection email sent.");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Error occurred while updating expense.");
			response.put("errorDetails", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PutMapping("/rej/advance/{advanceId}")
	@Transactional
	public ResponseEntity<Map<String, Object>> rejectAdvance(@PathVariable String advanceId,
			@RequestBody Map<String, Object> requestBody) {

		Map<String, Object> response = new HashMap<>();

		try {
			Optional<AdvancesDetailsMod> existingAdvanceOpt = advancesDetailsModRepository.findById(advanceId);
			if (!existingAdvanceOpt.isPresent()) {
				response.put("success", false);
				response.put("message", "Advance not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			String rejectReason = (String) requestBody.get("reason");
			String status = (String) requestBody.get("status");

			if (rejectReason == null || rejectReason.trim().isEmpty()) {
				response.put("success", false);
				response.put("message", "Reject reason is required.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			AdvancesDetailsMod existingAdvance = existingAdvanceOpt.get();
			existingAdvance.setEntityCreFlg("N");
			existingAdvance.setStatus(status); // e.g., "rejected"
			existingAdvance.setRmodTime(new Date());
			existingAdvance.setRejectreason(rejectReason);
			existingAdvance.setPaymentStatus(null);
			advancesDetailsModRepository.save(existingAdvance);

			String empId = existingAdvance.getEmpId();
			usermaintenance employee = usermaintenanceRepository.findByEmpid(empId);

			if (employee != null && employee.getEmailid() != null) {
				String to = employee.getEmailid();
				String subject = "Advance Request Rejected - ID: " + advanceId;
				String body = "Dear " + employee.getFirstname() + ",\n\n" + "Your advance request with ID: " + advanceId
						+ " has been rejected by your manager.\n" + "Reason: " + rejectReason + "\n\n"
						+ "Please contact your manager for further details.\n\n" + "Regards,\nHRMS Team";

				String fromEmail = "";
				if (employee.getRepoteTo() != null) {
					usermaintenance manager = usermaintenanceRepository.findByEmpid(employee.getRepoteTo());
					if (manager != null && manager.getEmailid() != null) {
						fromEmail = manager.getEmailid();

						// Optional notification to manager's manager
						if (manager.getRepoteTo() != null) {
							usermaintenance managersManager = usermaintenanceRepository
									.findByEmpid(manager.getRepoteTo());
							if (managersManager != null && managersManager.getEmailid() != null) {
								String ccSubject = "Advance Rejection Notification";
								String ccBody = "Dear " + managersManager.getFirstname() + ",\n\n" + "Your reportee "
										+ manager.getFirstname() + " has rejected the advance request from "
										+ employee.getFirstname() + ".\n\n" + "Advance ID: " + advanceId + "\nReason: "
										+ rejectReason + "\n\nRegards,\nHRMS System";

								emailService.sendLeaveEmail(fromEmail, managersManager.getEmailid(), ccSubject, ccBody);
							}
						}
					}
				}

				emailService.sendLeaveEmail(fromEmail, to, subject, body);
			} else {
				System.out.println("Employee email not found for ID: " + empId);
			}

			response.put("success", true);
			response.put("message", "Advance updated and rejection email sent.");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Error occurred while updating advance.");
			response.put("errorDetails", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	private String reject() {
		// TODO Auto-generated method stub
		return null;
	}

	@PutMapping("/expenses/{expenseId}")
	@Transactional
	public ResponseEntity<Map<String, Object>> updateExpense(@PathVariable String expenseId,
			@RequestBody ExpenseDetailsMod updatedExpense) {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<ExpenseDetailsMod> existingExpenseOpt = expenseDetailsRepository.findById(expenseId);
			if (!existingExpenseOpt.isPresent()) {
				response.put("success", false);
				response.put("message", "Expense not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			ExpenseDetailsMod existingExpense = existingExpenseOpt.get();
			updatedExpense.setExpenseId(existingExpense.getExpenseId()); // Ensure ID consistency
			updatedExpense.setRmodtime(new Date());
			expenseDetailsRepository.save(updatedExpense);

			response.put("success", true);
			response.put("message", "Expense updated successfully.");

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Error occurred while updating expense.");
			response.put("errorDetails", e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@DeleteMapping("/expenses/{expenseId}")
	@Transactional
	public ResponseEntity<Map<String, Object>> deleteExpense(@PathVariable String expenseId) {
		Map<String, Object> response = new HashMap<>();
		try {
			if (!expenseDetailsRepository.existsById(expenseId)) {
				response.put("success", false);
				response.put("message", "Expense not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			expenseDetailsRepository.deleteById(expenseId);

			response.put("success", true);
			response.put("message", "Expense deleted successfully.");

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Error occurred while deleting expense.");
			response.put("errorDetails", e.getMessage());

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping(value = "/expenses", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Transactional
	public ResponseEntity<Map<String, Object>> addExpense(@RequestPart("expenseDetails") String expenseDetailsJson, // Receive
																													// as
																													// String
			@RequestPart("receipt") MultipartFile receipt) throws IOException {
		Map<String, Object> response = new HashMap<>();
		String uploadDir = docUploadDir + "/expense_bills";
		try {
			
			Path path = Paths.get(uploadDir);
			System.out.println("TEST_GJ");
			// Deserialize JSON to your ExpenseDetailsMod object
			ObjectMapper objectMapper = new ObjectMapper();
			ExpenseDetailsMod expenseDetails = objectMapper.readValue(expenseDetailsJson, ExpenseDetailsMod.class);

			// Validate receipt file
			if (receipt == null || receipt.isEmpty()) {
				response.put("success", false);
				response.put("message", "No file uploaded.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			// Validate input fields
			if (expenseDetails.getAmount() == null || expenseDetails.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
				response.put("success", false);
				response.put("message", "Amount must be greater than zero.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
			usermaintenance employee = usermaintenanceRepository.findByEmpid(expenseDetails.getEmpId());

			boolean isUpdate = false;

			// 🟨 If expenseId is present, check if it exists
			if (expenseDetails.getExpenseId() != null
					&& expenseDetailsRepository.existsById(expenseDetails.getExpenseId())) {
				isUpdate = true;
				ExpenseDetailsMod existingExpense = expenseDetailsRepository.findById(expenseDetails.getExpenseId())
						.orElseThrow(() -> new RuntimeException("Expense not found"));

				// Update fields
				existingExpense.setDate(expenseDetails.getDate());
				existingExpense.setCategory(expenseDetails.getCategory());
				existingExpense.setAmount(expenseDetails.getAmount());
				existingExpense.setCurrency(expenseDetails.getCurrency());
				existingExpense.setDescription(expenseDetails.getDescription());
				existingExpense.setRmoduserid(expenseDetails.getEmpId());
				existingExpense.setApprover(employee.getRepoteTo());

				usermaintenance nextApprover = usermaintenanceRepository.findByEmpid(employee.getRepoteTo());

				Optional<UserRoleMaintenance> roleOpt = userRoleMaintenanceRepository
						.findByRoleid(nextApprover.getRoleid());
				existingExpense.setStatus("Pending for " + roleOpt.get().getRolename() + " approval");

				// Handle file update only if a new one is uploaded
				if (receipt != null && !receipt.isEmpty()) {
					String filename = expenseDetails.getExpenseId() + "_" + receipt.getOriginalFilename();

					Path filePath = path.resolve(filename);
					String fullFilePath = Paths.get(uploadDir, filename).toString();
					receipt.transferTo(filePath);
					existingExpense.setRecipt(fullFilePath);
				}

				expenseDetailsRepository.save(existingExpense);

				emailService.sendExpenseNotificationEmail(expenseDetails.getEmpId(), "updated",
						existingExpense.getExpenseId());

				response.put("success", true);
				response.put("message", "Expense updated successfully.");
				response.put("expenseId", existingExpense.getExpenseId());
				return ResponseEntity.ok(response);

			} else {

				// Generate expense ID and set other default fields
				String expenseId = generateCustomExpenseId();
				expenseDetails.setExpenseId(expenseId);
				expenseDetails.setRcretime(new Date());
				expenseDetails.setRcreuserid(expenseDetails.getEmpId());
				expenseDetails.setEntitycreflg("N");
				expenseDetails.setDelflg("N");
				expenseDetails.setApprover(employee.getRepoteTo());
				usermaintenance nextApprover = usermaintenanceRepository.findByEmpid(employee.getRepoteTo());
				Optional<UserRoleMaintenance> roleOpt = userRoleMaintenanceRepository
						.findByRoleid(nextApprover.getRoleid());
				expenseDetails.setStatus("Pending for " + roleOpt.get().getRolename() + " approval");
				expenseDetails.setRmoduserid(expenseDetails.getEmpId());

				// Handle file upload

				if (!Files.exists(path)) {
					Files.createDirectories(path);
				}

				String filename = expenseDetails.getExpenseId() + "_" + receipt.getOriginalFilename();
				Path filePath = path.resolve(filename);
				String fullFilePath = Paths.get(uploadDir, filename).toString();
				receipt.transferTo(filePath);

				// Save file path in the database
				expenseDetails.setRecipt(fullFilePath);

				// Log file path for confirmation
				System.out.println("File uploaded and stored at: " + filePath.toString());

				// Save to database
				ExpenseDetailsMod savedExpense = expenseDetailsRepository.save(expenseDetails);

				emailService.sendExpenseNotificationEmail(expenseDetails.getEmpId(), "submitted",
						savedExpense.getExpenseId()); // ✅ Correct

				// Prepare the response
				response.put("success", true);
				response.put("message", "Expense added successfully.");
				response.put("expenseId", savedExpense.getExpenseId());
				// Include file path in response
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Error occurred while adding expense.");
			response.put("errorDetails", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	private static final AtomicInteger sequence = new AtomicInteger(0); // Replace with DB-backed value if needed

	public String generateCustomExpenseId() {
		LocalDate today = LocalDate.now();
		String datePart = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		int seqNumber = sequence.incrementAndGet(); // This resets on restart, use DB for persistent sequence
		String paddedSeq = String.format("%03d", seqNumber); // Pads with zeros (e.g., 001)
		return "EXP" + datePart + "_" + paddedSeq;
	}

	public String generateCustomAdvanceId() {
		LocalDate today = LocalDate.now();
		String datePart = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		int seqNumber = sequence.incrementAndGet(); // This resets on restart, use DB for persistent sequence
		String paddedSeq = String.format("%03d", seqNumber); // Pads with zeros (e.g., 001)
		return "ADV" + datePart + "_" + paddedSeq;
	}

	// edit
	@GetMapping("/expenses/{expenseId}")
	public ResponseEntity<Map<String, Object>> getExpenseById(@PathVariable String expenseId) {
		Map<String, Object> response = new HashMap<>();
		try {
			// Fetch the expense details by ID
			Optional<ExpenseDetailsMod> existingExpenseOpt = expenseDetailsRepository.findById(expenseId);

			if (!existingExpenseOpt.isPresent()) {
				response.put("success", false);
				response.put("message", "Expense not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			// Retrieve the expense object
			ExpenseDetailsMod existingExpense = existingExpenseOpt.get();

			// Prepare the response data
			response.put("success", true);
			response.put("data", existingExpense);

			// Check if a receipt file is associated with the expense
			// String uploadDir = "D:\\Sowmiya"; // Ensure this matches the location where
			// files are stored
//	            String filename =  existingExpense.getRecipt();  // Get the filename based on the expense ID
//	            
//	            Path filePath = Paths.get(filename);
//	            System.out.println("Constructed file path: " + filePath.toString());
//	            if (Files.exists(filePath)) {
//	                // If the file exists, include its URL or path in the response
//	                // If you are serving the file via an HTTP endpoint, modify this line to return a URL
//	                String fileUrl = "/files/" + filename; // Example URL for serving the file via another endpoint
//	                response.put("file", fileUrl);  // Send URL/path to frontend
//	            } else {
//	                response.put("file", "File not found.");
//	            }

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Error occurred while fetching expense.");
			response.put("errorDetails", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@Autowired
	private SalaryTemplateRepository salaryTemplateRepository;

	@Autowired
	private SalaryTemplateComponentRepository salaryTemplateComponentRepository;

	@PostMapping("/salary-template/save")
	public ResponseEntity<?> saveSalaryTemplate(@RequestBody SalaryTemplatePayload payload) {
		try {
			// Print the whole payload object for debugging
			ObjectMapper objectMapper = new ObjectMapper();

			// Convert payload to JSON string and print for debugging
			String payloadJson = objectMapper.writeValueAsString(payload);
			System.out.println("Received Payload JSON: " + payloadJson);
			// Extract and map template details
			SalaryTemplate salaryTemplate = new SalaryTemplate();
			salaryTemplate.setTemplateName(payload.getTemplate().getTemplateName());
			salaryTemplate.setDescription(payload.getTemplate().getDescription());
			salaryTemplate.setAnnualCTC(payload.getTemplate().getAnnualCTC());
			salaryTemplate.setRcreuserid(payload.getTemplate().getRcreuserid());
			salaryTemplate.setDelflg("N");
			salaryTemplate.setRcretime(new Date());
			salaryTemplate.setEntitycreflg("Y");

			// Save salary template
			SalaryTemplate savedTemplate = salaryTemplateRepository.save(salaryTemplate);

			// Process and save each component
			for (SalaryTemplatePayload.Component componentPayload : payload.getComponents()) {
				// Print the details of each component for debugging
				System.out.println("Component: " + componentPayload.toString());

				System.out.println("EARNING >>>>>>   " + componentPayload.isEarning());
				SalaryTemplateComponent component = new SalaryTemplateComponent();
				component.setComponentName(componentPayload.getComponentName());
				component.setCalculationType(componentPayload.getCalculationType());
				component.setValue(componentPayload.getValue());
				component.setMonthlyAmount(componentPayload.getMonthlyAmount());
				component.setAnnualAmount(componentPayload.getAnnualAmount());
				component.setIsEarning(componentPayload.isEarning());
				component.setSalaryTemplate(savedTemplate); // Associate with saved template
				component.setRcreuserid(payload.getTemplate().getRcreuserid());
				component.setDelflg("N");
				component.setRcretime(new Date());
				component.setEntitycreflg("Y");

				// Save the component
				salaryTemplateComponentRepository.save(component);
			}

			return ResponseEntity.ok(savedTemplate);
		} catch (Exception e) {
			// Handle errors gracefully and return a response
			return ResponseEntity.status(500)
					.body("An error occurred while saving the salary template: " + e.getMessage());
		}
	}

	// Get all templates
	@GetMapping("/templates")
	public ResponseEntity<?> getAllTemplates() {
		try {
			List<SalaryTemplate> templates = salaryTemplateRepository.findAll();
			return ResponseEntity.ok(templates);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("An error occurred while fetching the templates: " + e.getMessage());
		}
	}

	// Get template details by ID
	@GetMapping("/templates/{templateId}")
	public ResponseEntity<?> getTemplateById(@PathVariable Long templateId) {
		try {
			SalaryTemplate template = salaryTemplateRepository.findById(templateId)
					.orElseThrow(() -> new ResourceNotFoundException("Template not found with id: " + templateId));
			return ResponseEntity.ok(template);
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(404).body("Template not found: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(500).body("An error occurred while fetching the template: " + e.getMessage());
		}
	}

	@Autowired
	private EmployeeProfileRepository employeeProfileRepository;

//	@GetMapping("/payroll/employee/{empid}")
//	public ResponseEntity<EmployeeProfile> getEmployeeDetails(@PathVariable String empid) {
//		Optional<EmployeeProfile> employee = employeeProfileRepository.findByEmpId(empid);
//
//		if (employee.isPresent()) {
//			EmployeeProfile empProfile = employee.get();
//
//			// Convert Date to LocalDate using a specific timezone
//			if (empProfile.getDateofbirth() != null) {
//				Date dobDate = empProfile.getDateofbirth();
//				LocalDate localDob = dobDate.toInstant().atZone(ZoneId.of("Asia/Kolkata")) // Use the correct timezone
//						.toLocalDate();
//
//				System.out.println("Formatted DOB to send to frontend: " + localDob);
//
//				// Convert LocalDate back to java.util.Date
//				Date convertedDate = Date.from(localDob.atStartOfDay(ZoneId.of("Asia/Kolkata")).toInstant());
//
//				// Set the converted date back to the EmployeeProfile
//				empProfile.setDateofbirth(convertedDate);
//			}
//
//			// Log the JSON response
//			ObjectMapper mapper = new ObjectMapper();
//			try {
//				String jsonResponse = mapper.writeValueAsString(empProfile);
//				System.out.println("JSON Response: " + jsonResponse);
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//
//			return ResponseEntity.ok(empProfile);
//		} else {
//			return ResponseEntity.status(404).body(null); // Return 404 if employee not found
//		}
//	}

	@GetMapping("/payroll/employee/{empid}")
	public ResponseEntity<usermaintenance> getUserDetails(@PathVariable String empid) {

		Optional<usermaintenance> user = usermaintenanceRepository.findByEmpid1(empid);

		if (user.isPresent()) {
			usermaintenance userData = user.get();

			// Log JSON response for debugging
			ObjectMapper mapper = new ObjectMapper();
			try {
				String jsonResponse = mapper.writeValueAsString(userData);
				System.out.println("JSON Response: " + jsonResponse);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			return ResponseEntity.ok(userData);

		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping("/payroll/employees")
	public List<Map<String, String>> getAllEmployeeIds() {
		return usermaintenanceRepository.findByStatusIgnoreCase("Active").stream().map(emp -> {
			Map<String, String> empMap = new HashMap<>();
			empMap.put("employeeId", emp.getEmpid()); // Employee ID
			empMap.put("employeeName", emp.getFirstname()); // Full Name
			return empMap;
		}).collect(Collectors.toList());
	}
	
	
	

	// total expense amount
	@CrossOrigin(origins = "http://localhost:4200") // Allow frontend access
	@RestController
	@RequestMapping("/api/expenses") // Correct request mapping
	public class ExpenseController1 {

		@Autowired
		private ExpenseDetailsRepository expenseDetailsRepository;

		@GetMapping("/stats")
		public ResponseEntity<Map<String, Object>> getExpenseStats() {
			Map<String, Object> stats = new HashMap<>();

			BigDecimal totalExpenses = expenseDetailsRepository.getTotalExpensesAmount();
			stats.put("totalExpenses", totalExpenses);
			// stats.put("totalExpenses", 0); // Dummy data for testing
			return ResponseEntity.ok(stats);

		}

	}

	// edit
	@GetMapping("/advance/{advanceId}")
	public ResponseEntity<Map<String, Object>> getAdvanceById3(@PathVariable String advanceId) {
		System.out.println("advanceId:: " + advanceId);
		Map<String, Object> response = new HashMap<>();
		try {

			// Fetch the advance details by ID
//			Optional<AdvancesDetailsMod> existingAdvanceOpt = Optional.empty();
			Optional<AdvancesDetailsMod> existingAdvanceOpt = advancesDetailsModRepository.findById(advanceId);

			System.out.println("MS :: entre" + existingAdvanceOpt);

			if (!existingAdvanceOpt.isPresent()) {
				response.put("success", false);
				response.put("message", "Advance not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			// Retrieve the advance object
			AdvancesDetailsMod existingAdvance = existingAdvanceOpt.get();

			// Prepare the response data
			response.put("success", true);
			response.put("data", existingAdvance);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Error occurred while fetching advance.");
			response.put("errorDetails", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// total advance amount

	@RequestMapping("/api/expenses") // API path for advances
	public class AdvanceController {

		@Autowired
		private AdvancesDetailsModRepository advanceDetailsModRepository;

		@GetMapping("/advancestats")
		public ResponseEntity<Map<String, Object>> getAdvanceStats() {
			Map<String, Object> stats = new HashMap<>();

			// Fetch total advances
			BigDecimal totalAdvances = advanceDetailsModRepository.getTotalAdvanceAmount();

			stats.put("totalAdvances", totalAdvances);
			return ResponseEntity.ok(stats);
		}
	}

	// pending expenses
	/*
	 * @RequestMapping("/api/expenses") // API path for advances public class
	 * AdvanceController1 {
	 * 
	 * @Autowired private AdvancesDetailsModRepository advanceDetailsModRepository;
	 * 
	 * @GetMapping("/pendingadstats") public ResponseEntity<Map<String, Object>>
	 * getPendingAdvancesStats() { Map<String, Object> stats = new HashMap<>();
	 * 
	 * // Fetch total pending advances BigDecimal pendingAdvances; try {
	 * pendingAdvances = AdvancesDetailsModRepository.getTotalPendingAdvance(); }
	 * catch (Exception e) { // TODO Auto-generated catch block e.printStackTrace();
	 * }
	 * 
	 * // Fix the JSON key name to match frontend stats.put("pendingAdvances",
	 * pendingAdvances);
	 * 
	 * return ResponseEntity.ok(stats); } }
	 */

	@RequestMapping("/api/expenses") // API path for advances
	public class AdvanceController11 {

		@Autowired
		private ExpenseDetailsRepository expenseDetailsRepository;

		@GetMapping("/pendingexstats")
		public ResponseEntity<Map<String, Object>> getPendingExpensesStats() {
			Map<String, Object> stats = new HashMap<>();

			// Fetch total pending advances
			BigDecimal pendingExpenses = expenseDetailsRepository.getTotalPendingExpenses();

			// Fix the JSON key name to match frontend
			stats.put("pendingExpenses", pendingExpenses);

			return ResponseEntity.ok(stats);
		}
	}

	@PostMapping({ "/advances", "/update/advances/{id}" })
	public ResponseEntity<?> submitAdvance(@RequestBody AdvancesDetailsMod advancesDetails,
			@PathVariable(value = "id", required = false) String Oldadvanceid) throws IOException {

		Map<String, Object> response = new HashMap<>();
		try {
			System.out.println("Advance Date: " + advancesDetails.getAdvanceDate());
			System.out.println("Employee ID: " + advancesDetails.getEmpId());
			String empId = advancesDetails.getEmpId();
			usermaintenance existingEmployee = usermaintenanceRepository.findByEmpIdOrUserId(empId)
					.orElseThrow(() -> new RuntimeException("Employee not found"));

			if (advancesDetails.getAdvanceDate() == null) {
				response.put("success", false);
				response.put("message", "Advance Date is required.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			if (advancesDetails.getAmount() == null || advancesDetails.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
				response.put("success", false);
				response.put("message", "Amount must be greater than zero.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			boolean isUpdate = false;
			String action = "add";

			if (Oldadvanceid != null && !Oldadvanceid.isEmpty()) {
				Optional<AdvancesDetailsMod> existingAdvanceOpt = advancesDetailsModRepository.findById(Oldadvanceid);
				if (existingAdvanceOpt.isPresent()) {
					AdvancesDetailsMod existing = existingAdvanceOpt.get();
					existing.setAmount(advancesDetails.getAmount());
					existing.setAdvanceDate(advancesDetails.getAdvanceDate());
					existing.setEmployeeName(existingEmployee.getFirstname());
					existing.setRmodUserId(advancesDetails.getEmpId());
					advancesDetails.setApprover(existingEmployee.getRepoteTo());
					existing.setRmodTime(new Date());
					advancesDetails = existing;
					isUpdate = true;
					action = "update";
				} else {
					response.put("success", false);
					response.put("message", "Advance not found for update.");
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
				}
			} else {
				String advanceId = generateCustomAdvanceId();
				advancesDetails.setEmployeeName(existingEmployee.getFirstname());
				advancesDetails.setAdvanceId(advanceId);
				advancesDetails.setRcreTime(new Date());
				advancesDetails.setRcreUserId(advancesDetails.getEmpId());
				advancesDetails.setRmodUserId(advancesDetails.getEmpId());
				advancesDetails.setRmodTime(new Date());
				advancesDetails.setEntityCreFlg("N");
				advancesDetails.setDelFlg("N");
				advancesDetails.setApprover(existingEmployee.getRepoteTo());
				usermaintenance nextApprover = usermaintenanceRepository.findByEmpid(existingEmployee.getRepoteTo());
				Optional<UserRoleMaintenance> roleOpt = userRoleMaintenanceRepository
						.findByRoleid(nextApprover.getRoleid());
				advancesDetails.setStatus("Pending for " + roleOpt.get().getRolename() + " approval");
			}

			advancesDetailsModRepository.save(advancesDetails);

			emailService.sendAdvanceNotificationEmail(advancesDetails.getEmpId(), advancesDetails.getAdvanceId(),
					action, advancesDetails.getAmount());

			response.put("success", true);
			response.put("message", isUpdate ? "Advance updated successfully." : "Advance added successfully.");
			response.put("advanceId", advancesDetails.getAdvanceId());
			return ResponseEntity.status(HttpStatus.CREATED).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Error occurred while processing advance.");
			response.put("errorDetails", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("/edit/advances/{advanceId}")
	public ResponseEntity<Map<String, Object>> getAdvanceById(@PathVariable String advanceId) {
		Map<String, Object> response = new HashMap<>();
		try {
			// Fetch the advance details by ID
			Optional<AdvancesDetailsMod> existingAdvanceOpt = advancesDetailsModRepository.findById(advanceId);

			if (!existingAdvanceOpt.isPresent()) {
				response.put("success", false);
				response.put("message", "Advance not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			// Retrieve the advance object
			AdvancesDetailsMod existingAdvance = existingAdvanceOpt.get();

			// Prepare the response data
			response.put("success", true);
			response.put("data", existingAdvance);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Error occurred while fetching advance.");
			response.put("errorDetails", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@DeleteMapping("/delete/advances/{advanceId}")
	public ResponseEntity<Map<String, Object>> softDeleteAdvance(@PathVariable String advanceId) {
		Map<String, Object> response = new HashMap<>();
		try {
			Optional<AdvancesDetailsMod> existingAdvanceOpt = advancesDetailsModRepository.findById(advanceId);

			if (!existingAdvanceOpt.isPresent()) {
				response.put("success", false);
				response.put("message", "Advance not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			// Retrieve and update delFlg
			AdvancesDetailsMod existingAdvance = existingAdvanceOpt.get();
			existingAdvance.setDelFlg("Y"); // Mark as deleted

			advancesDetailsModRepository.save(existingAdvance);

			response.put("success", true);
			response.put("message", "Advance deleted (delFlg set to 'Y').");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Error occurred while deleting advance.");
			response.put("errorDetails", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("/advances")
	public ResponseEntity<?> getAllAdvances() {
		try {
			List<AdvancesDetailsMod> advancesList = advancesDetailsModRepository.findAll();

			if (advancesList.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No advance records found.");
			}

			return ResponseEntity.ok(advancesList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while retrieving advances: " + e.getMessage());
		}
	}

	@Autowired
	private ExpenseDetailsRepository expenseDetailsRepository1;

	@GetMapping("/stats")
	public ResponseEntity<Map<String, Object>> getExpenseStats() {
		Map<String, Object> stats = new HashMap<>();

		BigDecimal totalExpenses = expenseDetailsRepository1.getTotalExpensesAmount();
		stats.put("totalExpenses", totalExpenses);
		// stats.put("totalExpenses", 0); // Dummy data for testing
		return ResponseEntity.ok(stats);

	}

	@Autowired
	private EmployeeSalaryTblRepository employeeSalaryTblRepository;

	@Autowired
	private EmployeeSalaryHistoryTblRepository employeeSalaryHistoryTblRepository;

	@PostMapping("/employeesalarydetail")
	@Transactional
	public ResponseEntity<EmployeeSalaryTbl> saveOrUpdate(@RequestBody EmployeeSalaryTbl employeeSalary) {
		// Print request body for debugging purposes
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			// Check if earnings is an instance of List
			if (employeeSalary.getEarnings() != null && isValidJsonArray(employeeSalary.getEarnings())) {
				String earningsJson = objectMapper.writeValueAsString(employeeSalary.getEarnings());
				employeeSalary.setEarnings(earningsJson); // Set the earnings as a JSON string
			}

			// Check if deductions is an instance of List
			if (employeeSalary.getDeductions() != null && isValidJsonArray(employeeSalary.getDeductions())) {
				String deductionsJson = objectMapper.writeValueAsString(employeeSalary.getDeductions());
				employeeSalary.setDeductions(deductionsJson); // Set the deductions as a JSON string
			}

			// Check if the employee salary already exists
			EmployeeSalaryTbl existingSalary = employeeSalaryTblRepository.findByEmpid(employeeSalary.getEmpid());

			if (existingSalary != null) {
				// Move the existing record to the history table
				EmployeeSalaryHistory history = new EmployeeSalaryHistory();
				history.setEmpId(existingSalary.getEmpid());
				history.setFirstName(existingSalary.getFirstname());
				history.setLastName(existingSalary.getLastname());
				history.setDateOfBirth(existingSalary.getDateofbirth());
				history.setDateOfJoin(existingSalary.getDateOfJoin());
				history.setOfficialEmail(existingSalary.getOfficialemail());
				history.setEmailId(existingSalary.getEmailid());
				history.setMobileNumber(existingSalary.getMobilenumber());
				history.setLocationType(existingSalary.getLocationType());
				history.setDepartment(existingSalary.getDepartment());
				history.setAnnualCTC(existingSalary.getAnnualCTC());
				history.setEarnings(existingSalary.getEarnings());
				history.setDeductions(existingSalary.getDeductions());
				history.setBankName(existingSalary.getBankName());
				history.setAccountNumber(existingSalary.getAccountNumber());
				history.setIfscCode(existingSalary.getIfscCode());
				history.setModifiedBy(existingSalary.getModifiedBy());
				history.setModifiedAt(LocalDateTime.now()); // Capture the current timestamp

				// Save the history record
				try {
					employeeSalaryHistoryTblRepository.save(history);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Mark existing salary as archived or updated as per requirement
				// existingSalary.setStatus("ARCHIVED"); // Or any other status you want to use
				// for marking archived records
				employeeSalaryTblRepository.deleteByEmpid(existingSalary.getEmpid().toString()); // Save the updated
																									// existing record
																									// (now archived)
				employeeSalaryTblRepository.flush();
				System.out.println("DELETED SUCCESS FULL");
			}

			// Save or update the EmployeeSalaryTbl
			employeeSalary.setModifiedAt(LocalDateTime.now());
			EmployeeSalaryTbl savedEmployeeSalary = employeeSalaryTblRepository.save(employeeSalary);

			// Return the saved employee salary details
			return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployeeSalary);

		} catch (Exception e) {
			// Log the exception (you can replace with proper logger if needed)
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/employeesalarydetail/{empid}")
	public ResponseEntity<EmployeeSalaryTbl> getEmployeeSalaryByEmpId(@PathVariable String empid) {
		try {
			EmployeeSalaryTbl employeeSalary = employeeSalaryTblRepository.findByEmpid(empid);
			if (employeeSalary != null) {
				return ResponseEntity.ok(employeeSalary);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/employeesalarydetail") // Ensure this annotation exists
	public ResponseEntity<List<EmployeeSalaryTbl>> getAllEmployeeSalaries() {
		List<EmployeeSalaryTbl> salaryDetails = employeeSalaryTblRepository.findAll();
		return ResponseEntity.ok(salaryDetails);
	}

	// Helper method to check if the given String represents a valid JSON array
	private boolean isValidJsonArray(String json) {
		try {
			new ObjectMapper().readTree(json);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Autowired
	private PayrollRepository payrollRepository;

	@Autowired
	private PayrollHistoryRepository payrollHistoryRepository;

	@Autowired
	private PayrollAdjustmentRepository payrollAdjustmentRepository;

	/** ✅ 1️⃣ RUN PAYROLL — Generate payroll for current month */
	@PostMapping("/Payroll")
	public ResponseEntity<Map<String, Object>> saveOrUpdatePayroll() {
		Map<String, Object> response = new HashMap<>();
		List<Payroll> payrollList = new ArrayList<>();

		YearMonth currentMonth = YearMonth.now();
		YearMonth previousMonth = currentMonth.minusMonths(1);
		LocalDate lastDayOfMonth = currentMonth.atEndOfMonth();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			// STEP 0: CRITICAL CHECK - Are all adjustments APPROVED?
			List<PayrollAdjustment> allAdjustments = payrollAdjustmentRepository.findByMonth(currentMonth.toString());

			if (allAdjustments.isEmpty()) {
				response.put("status", "Payroll blocked: Adjustments not generated yet. Please run /generate first.");
				response.put("error",
						"Payroll blocked: Adjustments not generated yet. Please run /generate first. " + currentMonth);
				return ResponseEntity.badRequest().body(response);
			}

			boolean allApproved = allAdjustments.stream().allMatch(adj -> "APPROVED".equals(adj.getApprovalStatus()));

			if (!allApproved) {
				long pendingCount = allAdjustments.stream().filter(adj -> !"APPROVED".equals(adj.getApprovalStatus()))
						.count();

				response.put("status", "Payroll blocked: Manager approval pending");
				response.put("error",
						pendingCount + " employee(s) not approved yet. All must be APPROVED before running payroll.");
				response.put("pending_count", pendingCount);
				return ResponseEntity.status(400).body(response);
			}

			// STEP 1: Move previous month payroll to history
			List<Payroll> previousPayrolls = payrollRepository.findByMonth(previousMonth.toString());
			if (!previousPayrolls.isEmpty()) {
				List<PayrollHistory> historyRecords = previousPayrolls.stream().map(p -> {
					PayrollHistory h = new PayrollHistory();
					BeanUtils.copyProperties(p, h);
					return h;
				}).collect(Collectors.toList());
				payrollHistoryRepository.saveAllAndFlush(historyRecords);
				payrollRepository.deleteByMonth(previousMonth.toString());
			}

			// STEP 2: Generate payroll using APPROVED adjustment values
			LocalDate periodStart = LocalDate.of(previousMonth.getYear(), previousMonth.getMonth(), 27);
			LocalDate periodEnd = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), 26);

			List<WsslCalendarMod> holidays = wsslCalendarModRepository
					.findByEventDateBetween(java.sql.Date.valueOf(periodStart), java.sql.Date.valueOf(periodEnd));
			List<Date> weekOffDates = getWeekOffsInRange(java.sql.Date.valueOf(periodStart),
					java.sql.Date.valueOf(periodEnd));

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Set<String> holidaySet = holidays.stream().map(h -> sdf.format(h.getEventDate()))
					.collect(Collectors.toSet());
			Set<String> weekOffSet = weekOffDates.stream().map(d -> sdf.format(d)).collect(Collectors.toSet());

//	        long effectiveWorkingDays = 0;
//	        LocalDate loopDate = periodStart;
//	        while (!loopDate.isAfter(periodEnd)) {
//	            String dateStr = loopDate.toString();
//	            if (!holidaySet.contains(dateStr) && !weekOffSet.contains(dateStr)) {
//	                effectiveWorkingDays++;
//	            }
//	            loopDate = loopDate.plusDays(1);
//	        }
//	        if (effectiveWorkingDays == 0) effectiveWorkingDays = currentMonth.lengthOfMonth();

			for (PayrollAdjustment adj : allAdjustments) {
				String empId = adj.getEmpId();
				EmployeeSalaryTbl empSalary = employeeSalaryTblRepository.findByEmpid(empId);
				if (empSalary == null)
					continue;

				usermaintenance user = usermaintenanceRepository.findByEmpid(empId);
				if (user == null)
					continue;

				// Use manager-approved values
				long allowanceDays = adj.getAllowanceDays();
				float lopDays = adj.getLopDays();
				double otherDeductions = adj.getOtherDeductions();
				double effectiveWorkingDays = adj.getEffectiveWorkingDays();

				// Parse Earnings & Deductions
				String earningsJson = cleanJson(empSalary.getEarnings());
				String deductionsJson = cleanJson(empSalary.getDeductions());
				JsonNode earningsNode = objectMapper.readTree(earningsJson);
				JsonNode deductionsNode = objectMapper.readTree(deductionsJson);

				double totalEarnings = StreamSupport.stream(earningsNode.spliterator(), false)
						.mapToDouble(e -> e.get("monthlyAmount").asDouble(0.0)).sum();
				double totalDeductions = StreamSupport.stream(deductionsNode.spliterator(), false)
						.mapToDouble(d -> d.get("monthlyAmount").asDouble(0.0)).sum();

				// Identify allowances
				double perDayRate = 0, pgRentAllowance = 0;
				for (JsonNode e : earningsNode) {
					String name = e.get("name").asText();
					double amount = e.get("monthlyAmount").asDouble(0.0);
					if ("Per Day Allowance".equalsIgnoreCase(name)) {
						perDayRate = e.has("amount") && !e.get("amount").isNull() ? e.get("amount").asDouble(0.0)
								: amount;
					} else if ("PG Rent Allowance".equalsIgnoreCase(name)) {
						pgRentAllowance = amount;
					}
				}

				double totalPerDayAllowance = perDayRate * allowanceDays; // Approved days
				double baseSalary = totalEarnings - (totalPerDayAllowance + pgRentAllowance);

				double perDayBaseRate = effectiveWorkingDays > 0 ? baseSalary / effectiveWorkingDays : 0;
				double lopDeduction = perDayBaseRate * lopDays;

				double finalDeductions = totalDeductions + lopDeduction + otherDeductions;
				double Deductions = lopDeduction + otherDeductions;
				double netPay = baseSalary + totalPerDayAllowance + pgRentAllowance - Deductions;

				// Round all values
				baseSalary = round(baseSalary);
				totalPerDayAllowance = round(totalPerDayAllowance);
				pgRentAllowance = round(pgRentAllowance);
				lopDeduction = round(lopDeduction);
				finalDeductions = round(finalDeductions);
				netPay = round(netPay);
				totalEarnings = round(netPay + finalDeductions);
				totalDeductions = round(totalDeductions);

				// Save Payroll
				Payroll payroll = payrollRepository.findByEmpidAndMonth(empId, currentMonth.toString());
				if (payroll == null)
					payroll = new Payroll();

				payroll.setEmpid(empId);
				payroll.setMonth(currentMonth.toString());
				payroll.setPymtDate(lastDayOfMonth);
				payroll.setStatus("PROCESSED");
				payroll.setAmount(baseSalary);
				payroll.setBnfName(user.getFirstname() + " " + (user.getLastname() != null ? user.getLastname() : ""));
				payroll.setBeneAccNo(empSalary.getAccountNumber());
				payroll.setBeneIfsc(empSalary.getIfscCode());
				payroll.setEmailId(empSalary.getEmailid());
				payroll.setMobileNum(empSalary.getMobilenumber());
				payroll.setDebitAccNo("611905056804");
				payroll.setCreditNarr("NA");
				payroll.setDebitNarr("NA");
				payroll.setPymtMode(
						empSalary.getBankName().toLowerCase().contains("icic".toLowerCase()) ? "FT" : "NEFT");
				payroll.setPymtProdTypeCode("PAB_VENDOR");
				payroll.setRemark(currentMonth.getMonth().name() + " " + currentMonth.getYear() + " Salary");
				// Store all calculated values
				payroll.setLopDays(lopDays);
				payroll.setTotalEarnings(totalEarnings);
				payroll.setTotalDeductions(totalDeductions);
				payroll.setLopDeduction(lopDeduction);
				payroll.setDeductions(finalDeductions);
				payroll.setOtherDeductions(otherDeductions); // New field recommended
				payroll.setPerDayRate(perDayRate);
				payroll.setPerDayAllowance(totalPerDayAllowance);
				payroll.setPerDayAllowanceDays(allowanceDays);
				payroll.setPgRentAllowance(pgRentAllowance);
				payroll.setEffectiveWorkingDays(effectiveWorkingDays);
				payroll.setNetPay(netPay);
				payroll.setOtherDeductionsRemarks(adj.getOtherDeductionsRemarks());
				payroll.setCreatedDate(LocalDateTime.now());

				payrollList.add(payroll);
			}

			if (!payrollList.isEmpty()) {
				payrollRepository.saveAllAndFlush(payrollList);
				response.put("status", "Payroll processed successfully for " + currentMonth);
				response.put("processed_count", payrollList.size());
				response.put("message",
						"All " + payrollList.size() + " employees processed using manager-approved values");
			} else {
				response.put("status", "No payroll records processed");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.put("error", "Payroll processing failed: " + e.getMessage());
			return ResponseEntity.status(500).body(response);
		}

		return ResponseEntity.ok(response);
	}

	// Helper method
	private double round(double value) {
		return Math.round(value * 100.0) / 100.0;
	}

	/** ✅ 2️⃣ PAYROLL PREVIEW — Fetch all payroll for current month */
	@GetMapping("/PayrollPreview")
	public ResponseEntity<Map<String, Object>> previewPayroll() {
		Map<String, Object> response = new HashMap<>();
		List<Map<String, Object>> previewList = new ArrayList<>();

		try {
			YearMonth currentMonth = YearMonth.now();
			String monthString = currentMonth.toString();

			List<Payroll> payrollRecords = payrollRepository.findByMonth(monthString);
			if (payrollRecords.isEmpty()) {
				response.put("status", "⚠️ No payroll records found for " + monthString);
				response.put("previewData", Collections.emptyList());
				return ResponseEntity.ok(response);
			}

			for (Payroll p : payrollRecords) {
				Map<String, Object> emp = new HashMap<>();

				// 🧾 Basic Employee & Payroll Info
				emp.put("empId", p.getEmpid());
				emp.put("bnfName", p.getBnfName());
				emp.put("month", p.getMonth());
				emp.put("pymtDate", p.getPymtDate());
				emp.put("status", p.getStatus());
				emp.put("remark", p.getRemark());

				// 🏦 Bank Details
				emp.put("accountNumber", p.getBeneAccNo());
				emp.put("ifsc", p.getBeneIfsc());
				emp.put("email", p.getEmailId());
				emp.put("mobile", p.getMobileNum());

				// 💰 Salary Components
				emp.put("basesalary", p.getAmount());
				emp.put("totalEarnings", p.getTotalEarnings());
				emp.put("totalDeductions", p.getTotalDeductions());
				emp.put("lopDays", p.getLopDays());
				emp.put("lopDeduction", p.getLopDeduction());
				emp.put("totalDeduction", p.getDeductions());
				emp.put("otherDeduction", p.getOtherDeductions());
				emp.put("netPay", p.getNetPay());
				emp.put("deductionRemarks", p.getOtherDeductionsRemarks());

				// 📅 Working Days
				emp.put("effectiveWorkingDays", p.getEffectiveWorkingDays());
				emp.put("perDayAllowanceDays", p.getPerDayAllowanceDays());

				// 💵 Allowances
				emp.put("perDayRate", p.getPerDayRate());
				emp.put("perDayAllowance", p.getPerDayAllowance());
				emp.put("pgAllowance", p.getPgAllowance());
				emp.put("pgRentAllowance", p.getPgRentAllowance());

				// 🕒 Metadata
				emp.put("createdDate", p.getCreatedDate());
				emp.put("creditNarr", p.getCreditNarr());
				emp.put("debitAccNo", p.getDebitAccNo());

				previewList.add(emp);
			}

			response.put("month", monthString);
			response.put("count", previewList.size());
			response.put("previewData", previewList);
			response.put("status", "✅ Payroll Preview Fetched Successfully");

		} catch (Exception e) {
			e.printStackTrace();
			response.put("error", e.getMessage());
		}

		return ResponseEntity.ok(response);
	}

	@PostMapping("/PayrollUpdate")
	public ResponseEntity<Map<String, Object>> updatePayroll(@RequestBody List<Map<String, Object>> payrollData) {
		Map<String, Object> response = new HashMap<>();
		List<Payroll> updatedList = new ArrayList<>();
		YearMonth currentMonth = YearMonth.now();

		try {
			for (Map<String, Object> empData : payrollData) {
				String empId = empData.get("empId").toString();

				// ✅ Safe parsing with defaults
				double netPay = empData.get("netPay") != null ? Double.parseDouble(empData.get("netPay").toString())
						: 0;
				double perDayRate = empData.get("perDayRate") != null
						? Double.parseDouble(empData.get("perDayRate").toString())
						: 0;
				double perDayAllowance = empData.get("perDayAllowance") != null
						? Double.parseDouble(empData.get("perDayAllowance").toString())
						: 0;
				double effectiveWorkingDays = empData.get("effectiveWorkingDays") != null
						? Long.parseLong(empData.get("effectiveWorkingDays").toString())
						: 0;
				float lopDays = empData.get("lopDays") != null ? Float.parseFloat(empData.get("lopDays").toString())
						: 0;
				long allowanceDays = empData.get("allowanceDays") != null
						? Long.parseLong(empData.get("allowanceDays").toString())
						: 0;

				Payroll payroll = payrollRepository.findByEmpidAndMonth(empId, currentMonth.toString());
				if (payroll != null) {
					payroll.setAmount(netPay);
					payroll.setLopDays(lopDays);
					payroll.setEffectiveWorkingDays(effectiveWorkingDays);
					payroll.setPerDayRate(perDayRate);
					payroll.setPerDayAllowance(perDayAllowance);
					payroll.setPerDayAllowanceDays(allowanceDays);
					payroll.setStatus("UPDATED");
					updatedList.add(payroll);
				}
			}

			if (!updatedList.isEmpty()) {
				payrollRepository.saveAllAndFlush(updatedList);
				response.put("status", "✅ Payroll updated successfully");
				response.put("processed_count", updatedList.size());
			} else {
				response.put("status", "⚠️ No payroll records found to update");
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.put("error", e.getMessage());
		}

		return ResponseEntity.ok(response);
	}

	/** ✅ Utility to clean malformed JSON */
	// **Fix JSON Formatting Issues**
	public String cleanJson(String json) {
		if (json == null)
			return "{}";
		json = json.startsWith("\"") && json.endsWith("\"") ? json.substring(1, json.length() - 1) : json;
		return StringEscapeUtils.unescapeJson(json);
	}

	@GetMapping("/exportsalaryupload")
	public ResponseEntity<byte[]> exportPayrollData() throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Payroll Data");

		// Define headers
		List<String> headers = Arrays.asList("PYMT_PROD_TYPE_CODE", "PYMT_MODE", "DEBIT_ACC_NO", "BNF_NAME",
				"BENE_ACC_NO", "BENE_IFSC", "AMOUNT", "DEBIT_NARR", "CREDIT_NARR", "MOBILE_NUM", "EMAIL_ID", "REMARK",
				"PYMT_DATE", "REF_NO", "ADDL_INFO1", "ADDL_INFO2", "ADDL_INFO3", "ADDL_INFO4", "ADDL_INFO5");

		// Create header row
		Row headerRow = sheet.createRow(0);
		CellStyle headerStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		headerStyle.setFont(font);

		for (int i = 0; i < headers.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers.get(i));
			cell.setCellStyle(headerStyle);
		}

		// Fetch payroll data from the database
		List<Payroll> payrollData = payrollRepository.findAll(); // Assuming you have a PayrollRepository

		// Fill data rows
		int rowNum = 1;
		for (Payroll payroll : payrollData) {
			Row row = sheet.createRow(rowNum++);

			row.createCell(0).setCellValue(nullToNA(payroll.getPymtProdTypeCode()));
			row.createCell(1).setCellValue(nullToNA(payroll.getPymtMode()));
			row.createCell(2).setCellValue(nullToNA(payroll.getDebitAccNo()));
			row.createCell(3).setCellValue(nullToNA(payroll.getBnfName()));
			row.createCell(4).setCellValue(nullToNA(payroll.getBeneAccNo()));
			row.createCell(5).setCellValue(nullToNA(payroll.getBeneIfsc()));
			row.createCell(6)
					.setCellValue(payroll.getNetPay() != null ? String.valueOf(Math.round(payroll.getNetPay())) : "NA");
			row.createCell(7).setCellValue(nullToNA(payroll.getDebitNarr()));
			row.createCell(8).setCellValue(nullToNA(payroll.getCreditNarr()));
			row.createCell(9).setCellValue(nullToNA(payroll.getMobileNum()));
			row.createCell(10).setCellValue(nullToNA(payroll.getEmailId()));
			row.createCell(11).setCellValue(nullToNA(payroll.getRemark()));
			row.createCell(12).setCellValue(payroll.getPymtDate() != null ? payroll.getPymtDate().toString() : "NA");
			row.createCell(13).setCellValue(nullToNA(payroll.getRefNo()));
			row.createCell(14).setCellValue(nullToNA(payroll.getAddlInfo1()));
			row.createCell(15).setCellValue(nullToNA(payroll.getAddlInfo2()));
			row.createCell(16).setCellValue(nullToNA(payroll.getAddlInfo3()));
			row.createCell(17).setCellValue(nullToNA(payroll.getAddlInfo4()));
			row.createCell(18).setCellValue(nullToNA(payroll.getAddlInfo5()));
		}

		// Auto-size columns
		for (int i = 0; i < headers.size(); i++) {
			sheet.autoSizeColumn(i);
		}

		// Convert to byte array
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();
		String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM"));
		// Return file as response
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=Bank_Upload_File_" + currentMonth + ".xlsx")
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(outputStream.toByteArray());
	}

	private String nullToNA(String value) {
		return value != null ? value : "NA";
	}

	@Autowired
	private PayslipService payslipService;

	@PostMapping("/payslip/send/all")
	public ResponseEntity<String> sendPayslipsToAll(@RequestBody Map<String, String> request) {
		String month = request.get("month");
		payslipService.sendPayslipToAll(month);
		return ResponseEntity.ok("Payslips sent to all employees successfully");
	}

	@PostMapping("/payslips/send")
	public ResponseEntity<String> sendPayslipToEmployee(@RequestBody Map<String, String> request) {
		String employeeId = request.get("employeeId");
		String month = request.get("month");
		payslipService.sendPayslip(employeeId, month);
		return ResponseEntity.ok("Payslip sent to employee: " + employeeId);
	}

	@GetMapping("/payslip/download/{employeeId}/{month}")
	public ResponseEntity<ByteArrayResource> downloadPayslip(@PathVariable String employeeId,
			@PathVariable String month) {
		try {
			ByteArrayResource payslipFile = payslipService.generatePayslip(employeeId, month);

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=payslip_" + employeeId + "_" + month + ".pdf")
					.contentType(MediaType.APPLICATION_PDF).body(payslipFile); // No need for explicit casting
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Return an error response
		}
	}

	@Autowired
	private LocationAllowanceRepository locationrepository;

	@GetMapping("/locations")
	public List<LocationAllowance> getAllAllowances() {
		return locationrepository.findAll();
	}

	@GetMapping("/allowance/{locationName}")
	public ResponseEntity<Map<String, Double>> getAllowance(@PathVariable String locationName) {
		System.out.println("locationName>>>>>>" + locationName);
		List<LocationAllowance> allowances = locationrepository.findByLocationName(locationName);
		Map<String, Double> response = new HashMap<>();

		for (LocationAllowance allowance : allowances) {
			if (allowance.getType() == AllowanceType.PER_DAY_ALLOWANCE) {
				System.out.println("allowance.getAmount()" + allowance.getAmount());
				response.put("perDayAllowance", allowance.getAmount());
			} else if (allowance.getType() == AllowanceType.PG_RENT) {
				System.out.println("pgRent" + allowance.getAmount());
				response.put("pgRent", allowance.getAmount());
			}
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/getrole")
	public List<UserRoleMaintenance> getRoles() {
		return userRoleMaintenanceRepository.findAll(); // Fetch roles directly from the database
	}

	@GetMapping("/getmanagers")
	public List<usermaintenance> getmanagers() {
		return usermaintenanceRepository.findAll(); // Fetch roles directly from the database
	}

	@PostMapping("/approve")
	public String updateEmployee(@RequestParam String empid, @RequestParam String roleid,
			@RequestParam String managerid) {
		System.out.println("empid>>>>>Onboard" + empid);
		usermaintenance existingEmployee = usermaintenanceRepository.findById(empid)
				.orElseThrow(() -> new RuntimeException("Employee not found"));

		existingEmployee.setRoleid(roleid);
		existingEmployee.setRepoteTo(managerid);
		usermaintenanceRepository.save(existingEmployee);
		return "Employee updated successfully!";
	}

	@GetMapping("/manager-email/{empId}")
	public ResponseEntity<Map<String, String>> getManagerEmail(@PathVariable("empId") String empId) {
		try {
			// Step 1: Find employee in either usermaintenance or traineemaster
			Optional<usermaintenance> empOpt = usermaintenanceRepository.findByEmpIdOrUserId(empId);
			Optional<TraineeMaster> traineeOpt = Optional.empty();

			if (empOpt.isEmpty()) {
				traineeOpt = traineemasterRepository.findByTrngidOrUserId(empId);
			}

			if (empOpt.isEmpty() && traineeOpt.isEmpty()) {
				throw new RuntimeException("Employee not found in both employee and trainee tables");
			}

			String managerId;
			if (empOpt.isPresent()) {
				managerId = empOpt.get().getRepoteTo();
			} else {
				managerId = traineeOpt.get().getRepoteTo();
			}

			if (managerId == null) {
				throw new RuntimeException("Manager not assigned to this employee");
			}

			// Step 2: Find manager in either usermaintenance or traineemaster
			Optional<usermaintenance> managerOpt = usermaintenanceRepository.findByEmpIdOrUserId(managerId);
			Optional<TraineeMaster> managerTraineeOpt = Optional.empty();

			if (managerOpt.isEmpty()) {
				managerTraineeOpt = traineemasterRepository.findByTrngidOrUserId(managerId);
			}

			if (managerOpt.isEmpty() && managerTraineeOpt.isEmpty()) {
				throw new RuntimeException("Manager not found in both tables");
			}

			String managerEmail;
			if (managerOpt.isPresent()) {
				managerEmail = managerOpt.get().getEmailid();
			} else {
				managerEmail = managerTraineeOpt.get().getEmailid();
			}

			// Step 3: Return manager email
			Map<String, String> response = new HashMap<>();
			response.put("email", managerEmail);
			return ResponseEntity.ok(response);

		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", e.getMessage()));
		}
	}

	@GetMapping("/expenses")
	public ResponseEntity<List<Map<String, Object>>> getAllExpenses() {
		try {
			List<ExpenseDetailsMod> expenses = expenseDetailsRepository.findByDelflg("N");
			System.out.println("Fetched:: Expenses: " + expenses);
			expenses.sort(Comparator.comparing(ExpenseDetailsMod::getRcretime).reversed());

			// Add receipt URL and employeeName to each expense
			List<Map<String, Object>> response = expenses.stream().map(expense -> {
				Map<String, Object> expenseData = new HashMap<>();
				expenseData.put("expenseId", expense.getExpenseId());
				expenseData.put("empId", expense.getEmpId());
				expenseData.put("date", expense.getDate());
				expenseData.put("category", expense.getCategory());
				expenseData.put("amount", expense.getAmount());
				expenseData.put("currency", expense.getCurrency());
				expenseData.put("description", expense.getDescription());
				expenseData.put("status", expense.getStatus());
				expenseData.put("receiptUrl", "/HRMS/expenses/receipt/" + expense.getExpenseId());
				expenseData.put("employeeName", expense.getEmployeeName()); // Add employeeName
				return expenseData;
			}).collect(Collectors.toList());
			System.out.println("Response Data: " + response);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@Autowired
	private AdvancesDetailsModRepository advancesDetailsModRepository; // ✅ Inject the repository

	@GetMapping("/advances/{employeeId}")
	public ResponseEntity<?> getAdvancesByEmployee(@PathVariable String employeeId) {
		List<AdvancesDetailsMod> advances = advancesDetailsModRepository.findByEmpIdAndDelFlg(employeeId, "N");
		if (advances.isEmpty()) {
			Map<String, String> response = new HashMap<>();
			response.put("message", "No advance records found for employee ID: " + employeeId);
			return ResponseEntity.ok(response);
		}
		List<AdvancesDetailsMod> updatedAdvances = new ArrayList<>();
		for (AdvancesDetailsMod advance : advances) {
			Optional<usermaintenance> employeeOpt = usermaintenanceRepository.findByEmpIdOrUserId(employeeId);
			if (employeeOpt.isPresent()) {
				advance.setEmployeeName(employeeOpt.get().getFirstname());
			} else {
				advance.setEmployeeName("Employee Not Found");
			}
			updatedAdvances.add(advance);
		}
		return ResponseEntity.ok(updatedAdvances);
	}

//	
//	
//	
	@Autowired
	private ExpenseDetailsService expenseDetailsService;

	@GetMapping("/expenses/total")
	public ResponseEntity<BigDecimal> getTotalExpenses() {
		BigDecimal totalExpenses = expenseDetailsService.getTotalExpenses();
		return ResponseEntity.ok(totalExpenses);
	}

	@GetMapping("/expenses/approved")
	public ResponseEntity<BigDecimal> getTotalApprovedExpenses() {
		BigDecimal totalApprovedExpenses = expenseDetailsService.getTotalApprovedExpenses();
		return ResponseEntity.ok(totalApprovedExpenses);
	}

	@GetMapping("/expenses/pending")
	public ResponseEntity<BigDecimal> getTotalPendingExpenses() {
		try {
			BigDecimal pendingExpenses = expenseDetailsRepository.getTotalPendingExpenses();
			System.out.println("Pending Expenses from DB: " + pendingExpenses); // Add this line
			return ResponseEntity.ok(pendingExpenses);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/advances/open")
	public ResponseEntity<BigDecimal> getTotalOpenAdvances() {
		BigDecimal totalOpenAdvances = expenseDetailsService.getTotalOpenAdvances();
		return ResponseEntity.ok(totalOpenAdvances);
	}

	@GetMapping("/advances/stats")
	public ResponseEntity<Map<String, BigDecimal>> getAdvanceStats() {
		Map<String, BigDecimal> stats = advanceService.getAdvanceStats();
		return ResponseEntity.ok(stats);
	}

	@GetMapping("/approvals/pending-employees")
	public ResponseEntity<List<Map<String, String>>> getPendingEmployees() {
		try {
			List<ExpenseDetailsMod> pendingExpenses = expenseDetailsRepository.findByStatus("Pending");

			// Extract unique employee IDs and names
			Set<Map<String, String>> uniqueEmployeeInfo = pendingExpenses.stream().map(expense -> {
				Map<String, String> employeeInfo = new HashMap<>();
				employeeInfo.put("empId", expense.getEmpId());
				employeeInfo.put("empName", expense.getEmployeeName());
				return employeeInfo;
			}).collect(Collectors.toSet());

			// Convert Set to List for response
			List<Map<String, String>> employeeInfoList = new ArrayList<>(uniqueEmployeeInfo);

			return ResponseEntity.ok(employeeInfoList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/app/approvedadvances/all")
	public ResponseEntity<List<AdvancesDetailsMod>> getAllApprovedAdvances() {
		try {
			List<AdvancesDetailsMod> advances = advancesDetailsModRepository.findByDelFlg("N");
			List<AdvancesDetailsMod> approvedAdvances = advances.stream().filter(a -> a.getPaymentStatus() != null)
					.collect(Collectors.toList());
			return ResponseEntity.ok(approvedAdvances != null ? approvedAdvances : new ArrayList<>());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
		}
	}

	@GetMapping("/app/approvedadvances/{Empid}")
	public ResponseEntity<List<AdvancesDetailsMod>> getApprovedAdvancesByEmpId(@PathVariable String Empid) {
		try {
			// Collect all employee IDs in the reporting hierarchy
			Set<String> allEmpIds = new HashSet<>();
			Deque<String> queue = new ArrayDeque<>();
			queue.add(Empid); // Start with the given employee

			while (!queue.isEmpty()) {
				String currentEmpId = queue.poll();
				List<usermaintenance> reportingEmployees = usermaintenanceRepository.findByRepoteToCustom(currentEmpId);

				for (usermaintenance emp : reportingEmployees) {
					String empId = emp.getEmpid();
					if (allEmpIds.add(empId)) {
						queue.add(empId); // Add to queue for further traversal
					}
				}
			}

			// Include the original employee ID
			allEmpIds.add(Empid);

			if (allEmpIds.isEmpty()) {
				return ResponseEntity.ok(Collections.emptyList());
			}

			// Fetch advances for all employee IDs in the hierarchy
			List<AdvancesDetailsMod> advances = advancesDetailsModRepository
					.findByEmpIdInAndDelFlg(new ArrayList<>(allEmpIds), "N");

			// Filter advances with non-null payment status
			List<AdvancesDetailsMod> approvedAdvances = advances.stream().filter(a -> a.getPaymentStatus() != null)
					.collect(Collectors.toList());

			return ResponseEntity.ok(approvedAdvances != null ? approvedAdvances : new ArrayList<>());

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
		}
	}

	@SuppressWarnings("unused")
	@PutMapping("/update/advance/Paymentstatus/{advanceId}")
	@Transactional
	public ResponseEntity<Map<String, Object>> updateAdvancePaymentStatus(@PathVariable String advanceId,
			@RequestBody Map<String, Object> requestBody) {

		Map<String, Object> response = new HashMap<>();

		try {
			Optional<AdvancesDetailsMod> existingAdvanceOpt = advancesDetailsModRepository.findById(advanceId);

			// Check if the advance exists
			if (!existingAdvanceOpt.isPresent()) {
				response.put("success", false);
				response.put("message", "Advance not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			AdvancesDetailsMod advance = existingAdvanceOpt.get();
			String empId = advance.getEmpId();

			usermaintenance employee = usermaintenanceRepository.findByEmpid(empId);
			if (employee == null || employee.getRepoteTo() == null) {
				response.put("success", false);
				response.put("message", "Employee or reporting manager not found.");
				return ResponseEntity.badRequest().body(response);
			}

			usermaintenance manager = usermaintenanceRepository.findByEmpid(employee.getRepoteTo());
			Integer paymentStatus = null;

			try {
				paymentStatus = Integer.parseInt(requestBody.get("paymentStatus").toString());
				System.out.println("paymentStatus>>>>>>>>>>>_____" + paymentStatus);
				if (paymentStatus == null) {
					response.put("success", false);
					response.put("message", "Missing paymentStatus.");
					return ResponseEntity.badRequest().body(response);
				}

				// Update the payment status
				advance.setPaymentStatus(paymentStatus);

				if (paymentStatus == 0) {
					advance.setStatus("Payment To Be Initiate");
				} else if (paymentStatus == 1) {
					advance.setStatus("Payment Initiated");
				} else if (paymentStatus == 2) {
					advance.setStatus("Payment Done");
				}
				advance.setRmodTime(new Date());
				advancesDetailsModRepository.save(advance);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Send email to employee
			String subjectToEmployee = "Advance Payment Status Updated";
			String bodyToEmployee = "Dear " + employee.getFirstname() + ",\n\n" + "Your advance (ID: " + advanceId
					+ ") has been updated to status: " + advance.getStatus() + " by your manager.\n\n"
					+ "Regards,\nHRMS System";
			emailService.sendLeaveEmail(manager.getEmailid(), employee.getEmailid(), subjectToEmployee, bodyToEmployee);

			// If the manager has a manager, notify them
			if (manager.getRepoteTo() != null) {
				usermaintenance managersManager = usermaintenanceRepository.findByEmpid(manager.getRepoteTo());
				if (managersManager != null && managersManager.getEmailid() != null) {
					String subjectToSuperManager = "Your Reportee Performed an Action";
					String bodyToSuperManager = "Dear " + managersManager.getFirstname() + ",\n\n" + "Your reportee "
							+ manager.getFirstname() + " has updated the payment status of an advance submitted by "
							+ employee.getFirstname() + ".\n\n" + "Advance ID: " + advanceId + "\nUpdated Status: "
							+ paymentStatus + "\n\nRegards,\nHRMS System";
					emailService.sendLeaveEmail(manager.getEmailid(), managersManager.getEmailid(),
							subjectToSuperManager, bodyToSuperManager);
				}
			}

			response.put("success", true);
			response.put("message", "Advance payment status updated and notifications sent.");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "Error updating advance payment status.");
			response.put("errorDetails", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("/data")
	public ResponseEntity<?> getTimesheetData(
	    @RequestParam int year, 
	    @RequestParam int month,
	    @RequestParam(required = false) String repoteTo) {

	    if (year < 2000 || month < 1 || month > 12) {
	        return ResponseEntity.badRequest().body(Map.of("error", "Invalid year or month"));
	    }

	    List<Map<String, Object>> timesheetData = new ArrayList<>();
	    
	    List<usermaintenance> employees = (repoteTo == null || repoteTo.isBlank()) 
	        ? usermaintenanceRepository.findAll()
	        : new ArrayList<>(usermaintenanceRepository.findByRepoteTo(repoteTo));

	    usermaintenanceRepository.findByEmpid1(repoteTo).ifPresent(employees::add);

	    if (employees.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body(Map.of("error", "No employees found" + (repoteTo != null ? " for " + repoteTo : "")));
	    }

	    // Date range: 27th of previous month to 26th of current month
	    Calendar startCal = Calendar.getInstance();
	    startCal.set(year, month - 1, 27);
	    clearTime(startCal);

	    Calendar endCal = (Calendar) startCal.clone();
	    endCal.add(Calendar.MONTH, 1);
	    endCal.set(Calendar.DAY_OF_MONTH, 26);
	    clearTime(endCal);

	    Date startDate = startCal.getTime();
	    Date endDate = endCal.getTime();

	    Calendar today = Calendar.getInstance();
	    clearTime(today);
	    Date attendanceEndDate = endDate.after(today.getTime()) ? today.getTime() : endDate;

	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

	    // Fetch Weekoffs & Holidays
	    Set<String> weekOffSet = calculateWeekOffsForPreviousAndCurrentMonth(year, month).stream()
	        .map(dateFormat::format)
	        .collect(Collectors.toSet());

	    Map<String, WsslCalendarMod> holidayMap = wsslCalendarModRepository
	        .findByEventDateBetween(startDate, attendanceEndDate).stream()
	        .collect(Collectors.toMap(
	            h -> dateFormat.format(h.getEventDate()), 
	            h -> h, 
	            (v1, v2) -> v1
	        ));

	    int sno = 1;

	    for (usermaintenance emp : employees) {
	        Map<String, Object> data = new HashMap<>();
	        data.put("sno", sno++);
	        data.put("employeeId", emp.getEmpid());
	        data.put("members", (emp.getFirstname() + " " + emp.getLastname()).trim());

	        // Fetch attendance
	        Map<String, String> attendanceMap = usermasterattendancemodrepository
	            .findByAttendanceidAndDateRange(emp.getEmpid(), startDate, attendanceEndDate).stream()
	            .collect(Collectors.toMap(
	                ar -> dateFormat.format(removeTime(ar.getAttendancedate())),
	                ar -> ar.getStatus(),
	                (v1, v2) -> v1
	            ));

	        double present = 0, absent = 0, missPunch = 0;
	        int effectiveWorkingDays = 0;
	        int od = 0, compoff = 0, holiday = 0, weekoff = 0;

	        Calendar loopCal = (Calendar) startCal.clone();

	        while (!loopCal.getTime().after(attendanceEndDate)) {
	            String dateStr = dateFormat.format(loopCal.getTime());
	            String status = attendanceMap.get(dateStr);
	            boolean isHoliday = holidayMap.containsKey(dateStr);
	            boolean isWeekOff = weekOffSet.contains(dateStr);

	            // Always count holidays and weekoffs (even if user came)
	            if (isHoliday) holiday++;
	            if (isWeekOff) weekoff++;

	            // Only normal working days count toward effectiveWorkingDays
	            if (!isHoliday && !isWeekOff) {
	                effectiveWorkingDays++;

	                if (status == null || status.isBlank()) {
	                    missPunch++;
	                } else {
	                    String upperStatus = status.trim().toUpperCase();
	                    switch (upperStatus) {
	                        case "PRESENT":
	                        case "EARLY LEAVE":
	                        case "FORGOT":
	                            present += 1.0;
	                            break;
	                        case "HALF DAY":
	                            present += 0.5;
	                            break;
	                        case "ABSENT":
	                            absent += 1.0;
	                            break;
	                        case "OD":
	                            od++;
	                            present += 1.0; // OD counts as present
	                            break;
	                        case "COMP OFF":
	                            compoff++;
	                            present += 1.0; // Comp-Off counts as present
	                            break;
	                        case "MISS PUNCH":
	                            missPunch++;
	                            break;
	                        default:
	                            // Optional: log unknown status
	                            break;
	                    }
	                }
	            } else {
	                // It's Holiday or WeekOff → check if user marked attendance (bonus)
	                if (status != null && !status.isBlank()) {
	                    String upperStatus = status.trim().toUpperCase();
	                    if (upperStatus.contains("PRESENT") || upperStatus.contains("OD") || upperStatus.contains("COMP")) {
	                        present += 1.0; // Optional: give credit for coming on off day
	                    }
	                }
	            }

	            loopCal.add(Calendar.DAY_OF_MONTH, 1);
	        }

	        // Final values
	        data.put("effectiveWorkingDays", effectiveWorkingDays);
	        data.put("present",present);
	        data.put("absent", absent);
	        data.put("missPunch", missPunch);
	        data.put("od", od);
	        data.put("compoff", compoff);
	        data.put("holiday", holiday);
	        data.put("weekoff", weekoff);

	        timesheetData.add(data);
	    }

	    return ResponseEntity.ok(timesheetData);
	}

	// Utility Method: Remove Time
	private Date removeTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		clearTime(cal);
		return cal.getTime();
	}

	private void clearTime(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}

	@GetMapping("/events/{employeeId}")
	public ResponseEntity<?> getAttendanceEvents(@PathVariable String employeeId,
			@RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month) {
		try {
			Calendar todayCal = Calendar.getInstance();

			// 🗓️ Default to current year and current month if not provided
			if (year == null) {
				year = todayCal.get(Calendar.YEAR);
			}
			if (month == null) {
				month = todayCal.get(Calendar.MONTH) + 1; // 1-based month
			}

			// 🔒 Input validation
			if (year < 2000 || month < 1 || month > 12 || employeeId == null || employeeId.isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap("error", "Invalid year, month, or employeeId"));
			}

			// 🔍 Verify employee
			usermaintenance employee = usermaintenanceRepository.findByEmpid(employeeId);
			if (employee == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(Collections.singletonMap("error", "Employee not found"));
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");

			// ✅ From January 1st of the selected year to the end of the current month
			Calendar startCal = Calendar.getInstance();
			startCal.set(year, Calendar.JANUARY, 1);
			Date startDate = startCal.getTime();

			Calendar endCal = Calendar.getInstance();
			endCal.set(Calendar.DAY_OF_MONTH, endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
			Date endDate = endCal.getTime();

			// ⏳ Limit end date to today if in current month
			Calendar today = Calendar.getInstance();
			Date attendanceEndDate = endDate.after(today.getTime()) ? today.getTime() : endDate;

			// 📆 Calculate week-offs using your method
			List<Date> weekOffDays = getWeekOffsInRange(startDate, attendanceEndDate);
			Set<String> weekOffSet = weekOffDays.stream().map(d -> dateFormat.format(d)).collect(Collectors.toSet());

			// 📊 Attendance records from DB
			List<UserMasterAttendanceMod> attendanceRecords = usermasterattendancemodrepository
					.findByAttendanceidAndDateRange(employeeId, startDate, attendanceEndDate);

			Map<String, UserMasterAttendanceMod> attendanceMap = new HashMap<>();
			for (UserMasterAttendanceMod record : attendanceRecords) {
				attendanceMap.put(dateFormat.format(record.getAttendancedate()), record);
			}

			// 🎉 Holidays
			List<WsslCalendarMod> holidays = wsslCalendarModRepository.findByEventDateBetween(startDate, endDate);
			Map<String, WsslCalendarMod> holidayMap = new HashMap<>();
			for (WsslCalendarMod holiday : holidays) {
				holidayMap.put(dateFormat.format(holiday.getEventDate()), holiday);
			}

			// 📅 Build event list
			List<Map<String, Object>> events = new ArrayList<>();
			Calendar loopCal = (Calendar) startCal.clone();

			while (!loopCal.after(endCal)) {
				Date currentDate = loopCal.getTime();
				String dateStr = dateFormat.format(currentDate);
				String dayOfWeek = dayFormat.format(currentDate);

				Map<String, Object> event = new HashMap<>();
				Map<String, String> extendedProps = new HashMap<>();
				extendedProps.put("dayOfWeek", dayOfWeek);

				UserMasterAttendanceMod attendanceRecord = attendanceMap.get(dateStr);
				boolean hasAttendance = attendanceRecord != null && attendanceRecord.getStatus() != null
						&& !attendanceRecord.getStatus().trim().isEmpty();

				// 🟨 Holiday with attendance record
				if (holidayMap.containsKey(dateStr) && hasAttendance) {
					WsslCalendarMod holiday = holidayMap.get(dateStr);
					String status = attendanceRecord.getStatus().trim();
					event.put("title", holiday.getEventName() + " - " + status);
					event.put("backgroundColor", getStatusColor(status));
					extendedProps.put("status", status);
					extendedProps.put("holiday", holiday.getEventName());
					extendedProps.put("type", "Holiday Attendance");

					// 🟦 Week Off with attendance record
				} else if (weekOffSet.contains(dateStr) && hasAttendance) {
					String status = attendanceRecord.getStatus().trim();
					event.put("title", "Week Off - " + status);
					event.put("backgroundColor", getStatusColor(status));
					extendedProps.put("status", status);
					extendedProps.put("type", "Week Off Attendance");

					// 🟨 Regular Holiday
				} else if (holidayMap.containsKey(dateStr)) {
					WsslCalendarMod holiday = holidayMap.get(dateStr);
					event.put("title", holiday.getEventName());
					event.put("backgroundColor", "#ffc107");
					extendedProps.put("status", "Holiday");

					// 🟦 Regular Week Off
				} else if (weekOffSet.contains(dateStr)) {
					event.put("title", "Week Off");
					event.put("backgroundColor", "#e0e0e0");
					extendedProps.put("status", "Week Off");

					// 🟩 Regular Attendance Record
				} else if (hasAttendance) {
					String status = attendanceRecord.getStatus().trim();
					String originalStatus = status; // Keep original status for display

					switch (status.toLowerCase()) {
					case "present":
					case "forgot":
						event.put("backgroundColor", "#28a745"); // green
						break;
					case "early leave":
						event.put("backgroundColor", "#fd7e14"); // orange for early leave
						break;
					case "miss punch":
						event.put("backgroundColor", "#ffff84"); // yellow
						break;
					case "week off":
						event.put("backgroundColor", "#e0e0e0"); // gray
						break;
					case "comp off":
						event.put("backgroundColor", "#17a2b8"); // teal
						break;
					case "public holiday":
						event.put("backgroundColor", "#ffc107"); // amber
						break;
					case "od":
						event.put("backgroundColor", "#6f42c1"); // purple
						break;
					case "absent":
						// For Absent, check if CL is available
						Optional<EmployeeLeaveSummary> summaryOpt = employeeLeaveSummaryRepository
								.findByEmpIdAndYear(employeeId, year);
						if (summaryOpt.isPresent() && summaryOpt.get().getCasualLeaveBalance() > 0) {
							status = "Absent (CL Used)";
							event.put("backgroundColor", "#ffa500"); // orange
						} else {
							status = "Absent (LOP)";
							event.put("backgroundColor", "#ff4c4c"); // red
						}
						break;
					default:
						// For any other status including "absent", check CL balance
						if ("absent".equalsIgnoreCase(status)) {
							Optional<EmployeeLeaveSummary> summaryOpt1 = employeeLeaveSummaryRepository
									.findByEmpIdAndYear(employeeId, year);
							if (summaryOpt1.isPresent() && summaryOpt1.get().getCasualLeaveBalance() > 0) {
								status = "Absent (CL Used)";
								event.put("backgroundColor", "#ffa500"); // orange
							} else {
								status = "Absent (LOP)";
								event.put("backgroundColor", "#ff4c4c"); // red
							}
						} else {
							event.put("backgroundColor", getStatusColor(status));
						}
						break;
					}

					event.put("title", status);
					extendedProps.put("status", status);
					extendedProps.put("originalStatus", originalStatus);

					// 🟠 No record (Miss Punch or Future Date)
				} else {
					if (!currentDate.after(today.getTime())) {
						event.put("title", "Miss Punch");
						event.put("backgroundColor", "#ffff84");
						extendedProps.put("status", "Miss Punch");
					} else {
						event.put("title", "");
						event.put("backgroundColor", "#ffffff");
						extendedProps.put("status", "");
					}
				}

				event.put("date", dateStr);
				event.put("extendedProps", extendedProps);
				events.add(event);
				loopCal.add(Calendar.DAY_OF_MONTH, 1);
			}

			return ResponseEntity.ok(events);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", "Failed to fetch attendance events"));
		}
	}

	// Helper method to get color based on status
	private String getStatusColor(String status) {
		if (status == null)
			return "#ffffff";

		String normalizedStatus = status.toLowerCase();

		switch (normalizedStatus) {
		case "present":
			return "#28a745";
		case "early leave":
			return "#fd7e14"; // orange for early leave
		case "absent":
			return "#ff4c4c";
		case "absent (cl used)":
			return "#ffa500"; // orange for CL used
		case "absent (lop)":
			return "#ff4c4c"; // red for LOP
		case "miss punch":
		case "forgot":
			return "#ffff84";
		case "week off":
			return "#e0e0e0";
		case "comp off":
			return "#17a2b8";
		case "public holiday":
			return "#ffc107";
		case "od":
			return "#6f42c1";
		default:
			return "#ffffff";
		}
	}

	/**
	 * ✅ Calculates Week-Offs (All Sundays + 2nd & 4th Saturdays)
	 */
	/**
	 * ✅ Calculates Week-Offs (All Sundays + 2nd & 4th Saturdays)
	 */
	List<Date> getWeekOffsInRange(Date startDate, Date endDate) {
		List<Date> weekOffDays = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);

		int saturdayCount = 0;
		int currentMonth = cal.get(Calendar.MONTH); // Track month separately

		while (!cal.getTime().after(endDate)) {
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

			// Every Sunday is week off
			if (dayOfWeek == Calendar.SUNDAY) {
				weekOffDays.add(cal.getTime());
			}

			// Count Saturdays — add only 2nd and 4th
			if (dayOfWeek == Calendar.SATURDAY) {
				saturdayCount++;
				if (saturdayCount == 2 || saturdayCount == 4) {
					weekOffDays.add(cal.getTime());
				}
			}

			// Move to next day
			cal.add(Calendar.DAY_OF_MONTH, 1);

			// Reset Saturday count when month changes
			int nextMonth = cal.get(Calendar.MONTH);
			if (nextMonth != currentMonth) {
				saturdayCount = 0;
				currentMonth = nextMonth;
			}
		}

		return weekOffDays;
	}

	private int calculateEffectiveWorkingDays(int year, int month) {
		// Set start date to 27th of given month
		Calendar startCal = Calendar.getInstance();
		startCal.set(year, month - 1, 27);
		Date startDate = startCal.getTime();

		// Set end date to 26th of next month
		Calendar endCal = (Calendar) startCal.clone();
		endCal.add(Calendar.MONTH, 1);
		endCal.set(Calendar.DAY_OF_MONTH, 26);
		Date endDate = endCal.getTime();

		// Calculate total days
		long diffInMillies = endDate.getTime() - startDate.getTime();
		int totalDays = (int) (diffInMillies / (1000 * 60 * 60 * 24)) + 1;

		// Week-offs in that specific date range
		List<Date> weekOffDays = getWeekOffsInRange(startDate, endDate);

		System.out.println("Total Days: " + totalDays);
		System.out.println("Week Offs: " + weekOffDays.size());
		System.out.println("Week Off Dates: " + weekOffDays);

		return totalDays - weekOffDays.size();
	}

	private List<Date> getWeekOffsInRange1(Date startDate, Date endDate) {
		List<Date> weekOffDays = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);

		int saturdayCount = 0;
		List<Integer> saturdayTracker = new ArrayList<>();

		while (!cal.getTime().after(endDate)) {
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

			if (dayOfWeek == Calendar.SUNDAY) {
				weekOffDays.add(cal.getTime());
			} else if (dayOfWeek == Calendar.SATURDAY) {
				saturdayCount++;
				if (saturdayCount == 2 || saturdayCount == 4) {
					weekOffDays.add(cal.getTime());
				}
			}

			cal.add(Calendar.DAY_OF_MONTH, 1);
		}

		return weekOffDays;
	}

	private List<Date> calculateWeekOffsForPreviousAndCurrentMonth(int year, int month) {
		List<Date> weekOffDays = new ArrayList<>();

		// Previous month
		Calendar prevMonth = Calendar.getInstance();
		prevMonth.set(year, month - 1, 1); // Set to current month
		prevMonth.add(Calendar.MONTH, 1); // Move to previous month
		weekOffDays.addAll(getWeekOffsOfMonth(prevMonth.get(Calendar.YEAR), prevMonth.get(Calendar.MONTH) + 1));

		// Current month
		weekOffDays.addAll(getWeekOffsOfMonth(year, month));

		return weekOffDays;
	}

	private List<Date> getWeekOffsOfMonth(int year, int month) {
		List<Date> weekOffs = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1); // Start of month

		int saturdayCount = 0;

		while (cal.get(Calendar.MONTH) == month - 1) {
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

			if (dayOfWeek == Calendar.SUNDAY) {
				weekOffs.add(cal.getTime());
			} else if (dayOfWeek == Calendar.SATURDAY) {
				saturdayCount++;
				if (saturdayCount == 2 || saturdayCount == 4) {
					weekOffs.add(cal.getTime());
				}
			}

			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		System.out.println("Week off+++++++++++++++" + weekOffs);
		return weekOffs;
	}

	@PostMapping("/attendance/update")
	public ResponseEntity<?> updateOrSaveAttendance(@RequestBody UserMasterAttendanceMod attendance) {
		try {
			Optional<UserMasterAttendanceMod> existing = usermasterattendancemodrepository
					.findByAttendanceidAndAttendancedate(attendance.getAttendanceid(), attendance.getAttendancedate());

			Date attendanceDate = attendance.getAttendancedate();

			// Use combineDateTime to create default times as Date
			Date defaultCheckin = combineDateTime(attendanceDate, 9, 0); // 09:00
			Date defaultCheckout = combineDateTime(attendanceDate, 18, 0); // 18:00

			if (existing.isPresent()) {
				// Update logic
				UserMasterAttendanceMod existingAttendance = existing.get();
				existingAttendance.setStatus(attendance.getStatus());
				existingAttendance.setRemarks(attendance.getRemarks());
				attendance.setUserid("2019" + attendance.getAttendanceid());
				existingAttendance.setRmoduserid(attendance.getRmoduserid());
				existingAttendance.setRmodtime(new Date());
				existingAttendance.setCheckintime(
						attendance.getCheckintime() != null ? attendance.getCheckintime() : defaultCheckin);
				existingAttendance.setCheckouttime(
						attendance.getCheckouttime() != null ? attendance.getCheckouttime() : defaultCheckout);

				usermasterattendancemodrepository.save(existingAttendance);
			} else {
				// Save new
				attendance.setRcreuserid(attendance.getRmoduserid());
				attendance.setRcretime(new Date());
				attendance.setStatus(attendance.getStatus());
				attendance.setUserid("2019" + attendance.getAttendanceid());
				attendance.setCheckintime(
						attendance.getCheckintime() != null ? attendance.getCheckintime() : defaultCheckin);
				attendance.setCheckouttime(
						attendance.getCheckouttime() != null ? attendance.getCheckouttime() : defaultCheckout);

				usermasterattendancemodrepository.save(attendance);
			}

			return ResponseEntity.ok(Map.of("message", "Attendance saved or updated successfully"));
		} catch (PropertyValueException e) {
			return ResponseEntity.badRequest().body(Map.of("error", "Missing required field: " + e.getPropertyName()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
		}
	}

	// Utility method to convert time to Date with specified hours and minutes
	private Date combineDateTime(Date date, int hour, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	@GetMapping("/attendance/pie")
	public ResponseEntity<?> getAttendancePie(@RequestParam("empId") String empId) {
		try {
			// Step 1: Get all employees under the given manager (including indirect)
			Set<String> allEmpIds = new HashSet<>();
			Deque<String> queue = new ArrayDeque<>();
			queue.add(empId); // Always include the main empId

			allEmpIds.add(empId); // Add self

			while (!queue.isEmpty()) {
				String current = queue.poll();
				List<usermaintenance> reportees = usermaintenanceRepository.findByRepoteToCustom(current);
				for (usermaintenance emp : reportees) {
					String eid = emp.getEmpid();
					if (allEmpIds.add(eid)) {
						queue.add(eid);
					}
				}
			}

			List<String> empIdList = new ArrayList<>(allEmpIds); // Even if only 1

			// Step 2: Prepare date ranges
			LocalDate today = LocalDate.now();
			LocalDate yesterday = today.minusDays(1);

			Date todayStart = Timestamp.valueOf(today.atStartOfDay());
			Date todayEnd = Timestamp.valueOf(today.plusDays(1).atStartOfDay());

			Date yesterdayStart = Timestamp.valueOf(yesterday.atStartOfDay());
			Date yesterdayEnd = Timestamp.valueOf(yesterday.plusDays(1).atStartOfDay());

			// Step 3: Fetch attendance records
			List<UserMasterAttendanceMod> todayRecords = usermasterattendancemodrepository
					.findAllByAttendancedateAndAttendanceidIn(todayStart, todayEnd, empIdList);

			List<UserMasterAttendanceMod> yesterdayRecords = usermasterattendancemodrepository
					.findAllByAttendancedateAndAttendanceidIn(yesterdayStart, yesterdayEnd, empIdList);

			long total = empIdList.size();

			// Step 4: Build response
			Map<String, Object> result = new HashMap<>();

			Map<String, Object> todayMap = new HashMap<>();
			long todayPresent = todayRecords.stream().filter(r -> r.getCheckintime() != null).count();
			todayMap.put("present", todayPresent);
			todayMap.put("absent", total - todayPresent);
			todayMap.put("day", today.getDayOfWeek().toString());

			Map<String, Object> yesterdayMap = new HashMap<>();
			long yesterdayPresent = yesterdayRecords.stream().filter(r -> r.getCheckintime() != null).count();
			yesterdayMap.put("present", yesterdayPresent);
			yesterdayMap.put("absent", total - yesterdayPresent);
			yesterdayMap.put("day", yesterday.getDayOfWeek().toString());

			result.put("today", todayMap);
			result.put("yesterday", yesterdayMap);

			return ResponseEntity.ok(result);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", "Unable to fetch hierarchical pie data"));
		}
	}

	// ✅ GET all
	@GetMapping("/location-allowances")
	public ResponseEntity<List<LocationAllowance>> getAll() {
		try {
			List<LocationAllowance> allowances = locationrepository.findAll();
			return ResponseEntity.ok(allowances);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// ✅ POST create
	@PostMapping("/location-allowances")
	public ResponseEntity<?> create(@RequestBody LocationAllowance allowance) {
		try {
			if (allowance.getLocationName() == null || allowance.getLocationName().isEmpty()) {
				return ResponseEntity.badRequest().body("Location name is required");
			}
			if (allowance.getAmount() == null || allowance.getAmount() <= 0) {
				return ResponseEntity.badRequest().body("Amount must be greater than zero");
			}
			LocationAllowance saved = locationrepository.save(allowance);
			return ResponseEntity.status(HttpStatus.CREATED).body(saved);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving allowance");
		}
	}

	// ✅ DELETE by ID
	@DeleteMapping("/location-allowances/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		try {
			Optional<LocationAllowance> existing = locationrepository.findById(id);
			if (existing.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Allowance not found with id: " + id);
			}
			locationrepository.deleteById(id);
			return ResponseEntity.ok("Deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting allowance");
		}
	}

	@PutMapping("/location-allowances/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody LocationAllowance allowance) {
		try {
			Optional<LocationAllowance> existingOpt = locationrepository.findById(id);
			if (existingOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Allowance not found with id: " + id);
			}

			LocationAllowance existing = existingOpt.get();
			if (allowance.getLocationName() != null)
				existing.setLocationName(allowance.getLocationName());
			if (allowance.getType() != null)
				existing.setType(allowance.getType());
			if (allowance.getAmount() != null)
				existing.setAmount(allowance.getAmount());

			return ResponseEntity.ok(locationrepository.save(existing));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating allowance");
		}
	}

	@PostMapping("/permissionRequest")
	public ResponseEntity<?> permissionRequest(@RequestBody EmployeePermissionMasterTbl employeePermissionMasterTbl) {
		try {
			System.out.println("Received permission request: " + employeePermissionMasterTbl);

			String empId = employeePermissionMasterTbl.getEmpid();
			System.out.println("EmpId: " + empId);
			LocalDate startLocalDate = employeePermissionMasterTbl.getStartTime().toLocalDateTime().toLocalDate();
			java.sql.Date startDate = java.sql.Date.valueOf(startLocalDate); // Convert LocalDate to java.sql.Date
			System.out.println("StartDate: " + startDate);

			// Check if permission exists for the same start time
			System.out.println("Checking if permission exists for empId: " + empId + ", startTime: "
					+ employeePermissionMasterTbl.getStartTime());
			boolean existingPermission = employeePermissionMasterRepository.countByEmpidAndStartTime(empId,
					employeePermissionMasterTbl.getStartTime()) > 0;
			System.out.println("Existing permission check result: " + existingPermission);

			if (existingPermission) {
				System.out.println("Permission request already exists for the selected time.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\": \"Permission request already exists for the selected time.\"}");
			}

			// Check daily limit
			System.out.println("Checking daily hours for empId: " + empId + ", date: " + startDate);

			Float existingDayHours = employeePermissionMasterRepository.sumHoursByEmpidAndDate(empId, startDate);
			System.out.println("Existing daily hours: " + existingDayHours);

			if (existingDayHours == null)
				existingDayHours = 0f;
			float newHours = employeePermissionMasterTbl.getHours();
			System.out.println("New hours: " + newHours);
			if (existingDayHours + newHours > 2) {
				System.out.println("Exceeded daily permission limit of 2 hours.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\": \"Exceeded daily permission limit of 2 hours.\"}");
			}

			// Check monthly limit
			int year = startLocalDate.getYear();
			int month = startLocalDate.getMonthValue();
			System.out.println("Checking monthly hours for empId: " + empId + ", year: " + year + ", month: " + month);

			Float existingMonthHours = employeePermissionMasterRepository.sumHoursByEmpidAndMonth(empId, year, month);
			System.out.println("Existing monthly hours: " + existingMonthHours);

			if (existingMonthHours == null)
				existingMonthHours = 0f;
			if (existingMonthHours + newHours > 6) {
				System.out.println("Exceeded monthly permission limit of 6 hours.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\": \"Exceeded monthly permission limit of 6 hours.\"}");
			}

			// Save the permission request
			System.out.println("Saving permission request...");
			employeePermissionMasterTbl.setDelflg("N");
			employeePermissionMasterTbl.setEntitycreflg("N");
			employeePermissionMasterTbl.setStatus("Pending");
			EmployeePermissionMasterTbl savedRequest = employeePermissionMasterRepository
					.save(employeePermissionMasterTbl);
			System.out.println("Saved permission request: " + savedRequest);

			// Fetch employee details (from either usermaintenance or TraineeMaster)
			System.out.println("Fetching employee details for empId: " + empId);
			Optional<usermaintenance> empOpt = usermaintenanceRepository.findByEmpIdOrUserId(empId);
			Optional<TraineeMaster> traineeOpt = Optional.empty();
			if (empOpt.isEmpty()) {
				System.out.println("Employee not found in usermaintenance, checking TraineeMaster...");
				traineeOpt = traineemasterRepository.findByTrngidOrUserId(empId);
			}
			if (empOpt.isEmpty() && traineeOpt.isEmpty()) {
				System.out.println("Employee not found in both tables");
				throw new RuntimeException("Employee not found in both employee and trainee tables");
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
				System.out.println("Employee found in usermaintenance: managerId=" + managerId + ", firstName="
						+ employeeFirstName + ", email=" + employeeEmail + ", roleId=" + roleId);
			} else {
				TraineeMaster emp = traineeOpt.get();
				managerId = emp.getRepoteTo();
				employeeFirstName = emp.getFirstname();
				employeeEmail = emp.getEmailid();
				roleId = emp.getRoleid();
				System.out.println("Employee found in TraineeMaster: managerId=" + managerId + ", firstName="
						+ employeeFirstName + ", email=" + employeeEmail + ", roleId=" + roleId);
			}

			if (managerId == null) {
				System.out.println("Manager not assigned to this employee");
				throw new RuntimeException("Manager not assigned to this employee");
			}

			// Fetch manager details (from either usermaintenance or TraineeMaster)
			System.out.println("Fetching manager details for managerId: " + managerId);
			Optional<usermaintenance> managerOpt = usermaintenanceRepository.findByEmpIdOrUserId(managerId);
			Optional<TraineeMaster> managerTraineeOpt = Optional.empty();
			if (managerOpt.isEmpty()) {
				System.out.println("Manager not found in usermaintenance, checking TraineeMaster...");
				managerTraineeOpt = traineemasterRepository.findByTrngidOrUserId(managerId);
			}
			if (managerOpt.isEmpty() && managerTraineeOpt.isEmpty()) {
				System.out.println("Manager not found in both tables");
				throw new RuntimeException("Manager not found in both tables");
			}

			String managerFirstName;
			String managerEmail;
			if (managerOpt.isPresent()) {
				managerFirstName = managerOpt.get().getFirstname();
				managerEmail = managerOpt.get().getEmailid();
				System.out.println(
						"Manager found in usermaintenance: firstName=" + managerFirstName + ", email=" + managerEmail);
			} else {
				managerFirstName = managerTraineeOpt.get().getFirstname();
				managerEmail = managerTraineeOpt.get().getEmailid();
				System.out.println(
						"Manager found in TraineeMaster: firstName=" + managerFirstName + ", email=" + managerEmail);
			}

			// Prepare response
			Map<String, Object> response = new HashMap<>();
			response.put("message", "Permission Request sent successfully");
			response.put("data", savedRequest);

			// Send email if manager email exists
			if (managerEmail != null && !managerEmail.isEmpty()) {
				System.out.println("Fetching role details for roleId: " + roleId);
				UserRoleMaintenance role = userRoleMaintenanceRepository.findByRoleid(roleId)
						.orElseThrow(() -> new RuntimeException("Role not found"));
				System.out.println(
						"Role found: roleName=" + role.getRolename() + ", description=" + role.getDescription());

				String subject = "Permission Request Approval Needed for " + employeeFirstName;
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

				String startTimeFormatted = sdf.format(employeePermissionMasterTbl.getStartTime());
				String endTimeFormatted = sdf.format(employeePermissionMasterTbl.getEndTime());
				System.out.println("Email details: subject=" + subject + ", startTime=" + startTimeFormatted
						+ ", endTime=" + endTimeFormatted + ", hours=" + employeePermissionMasterTbl.getHours()
						+ ", reason=" + employeePermissionMasterTbl.getReason());

				String body = String.format("Dear %s,\n\n"
						+ "Employee %s (%s) has submitted a permission request. Please find the details below:\n\n"
						+ "Start Time: %s\n" + "End Time: %s\n" + "Hours: %s\n" + "Reason: %s\n\n"
						+ "Kindly review the request and take necessary action.\n\n" + "Regards,\n" + "%s,\n"
						+ "%s - %s,\n" + "Whitestone Software Solution Pvt Ltd.\n", managerFirstName, employeeFirstName,
						empId, startTimeFormatted, endTimeFormatted, employeePermissionMasterTbl.getHours(),
						employeePermissionMasterTbl.getReason(), employeeFirstName, role.getRolename(),
						role.getDescription());

				System.out.println("Sending email to: " + managerEmail);
				emailService.sendLeaveEmail(employeeEmail, managerEmail, subject, body);
				System.out.println("Email sent successfully to: " + managerEmail);
				response.put("emailStatus", "Email sent to manager: " + managerEmail);
			} else {
				System.out.println("Manager email not found");
				response.put("emailStatus", "Manager email not found");
			}

			System.out.println("Returning response: " + response);
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			System.out.println("Error processing permission request: " + e.getMessage());

			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\": \"Failed to send permission request\"}");
		}
	}

	@PostMapping("/approvePermissionRequest")
	public ResponseEntity<Map<String, String>> approvePermissionRequest(@RequestBody Map<String, String> requestData) {
		String empid = requestData.get("empid");
		String srlnum = requestData.get("srlnum");
		System.out.println("Received empid: " + empid + ", srlnum: " + srlnum);

		Map<String, String> response = new HashMap<>();
		try {
			EmployeePermissionMasterTbl permissionRequest = employeePermissionMasterRepository
					.findByEmpidAndId(empid, Long.valueOf(srlnum))
					.orElseThrow(() -> new RuntimeException("Permission request not found"));

			permissionRequest.setEntitycreflg("Y");
			permissionRequest.setStatus("Approved");
			employeePermissionMasterRepository.save(permissionRequest);
			System.out.println("Permission request approved: " + permissionRequest);

			Optional<usermaintenance> empOpt = usermaintenanceRepository.findByEmpIdOrUserId(empid);
			Optional<TraineeMaster> traineeOpt = Optional.empty();
			if (empOpt.isEmpty()) {
				traineeOpt = traineemasterRepository.findByTrngidOrUserId(empid);
			}
			if (empOpt.isEmpty() && traineeOpt.isEmpty()) {
				throw new RuntimeException("Employee not found in both employee and trainee tables");
			}

			String employeeEmail = empOpt.isPresent() ? empOpt.get().getEmailid() : traineeOpt.get().getEmailid();
			String employeeFirstName = empOpt.isPresent() ? empOpt.get().getFirstname()
					: traineeOpt.get().getFirstname();
			String managerId = empOpt.isPresent() ? empOpt.get().getRepoteTo() : traineeOpt.get().getRepoteTo();

			Optional<usermaintenance> managerOpt = usermaintenanceRepository.findByEmpIdOrUserId(managerId);
			Optional<TraineeMaster> managerTraineeOpt = Optional.empty();
			if (managerOpt.isEmpty()) {
				managerTraineeOpt = traineemasterRepository.findByTrngidOrUserId(managerId);
			}
			if (managerOpt.isEmpty() && managerTraineeOpt.isEmpty()) {
				throw new RuntimeException("Manager not found in both tables");
			}

			String managerFirstName = managerOpt.isPresent() ? managerOpt.get().getFirstname()
					: managerTraineeOpt.get().getFirstname();
			String managerEmail = managerOpt.isPresent() ? managerOpt.get().getEmailid()
					: managerTraineeOpt.get().getEmailid();

			if (employeeEmail != null && !employeeEmail.isEmpty()) {
				String subject = "Permission Request Approved";
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				String startTimeFormatted = sdf.format(permissionRequest.getStartTime());
				String endTimeFormatted = sdf.format(permissionRequest.getEndTime());
				String body = String.format(
						"Dear %s,\n\nYour permission request from %s to %s for %s hours has been approved.\n\nRegards,\n%s,\nWhitestone Software Solution Pvt Ltd.",
						employeeFirstName, startTimeFormatted, endTimeFormatted, permissionRequest.getHours(),
						managerFirstName);
				emailService.sendLeaveEmail(managerEmail, employeeEmail, subject, body);
				System.out.println("Email sent to: " + employeeEmail);
			}

			response.put("status", "success");
			response.put("message", "Permission request approved successfully.");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			System.out.println("Error approving permission request: " + e.getMessage());
			e.printStackTrace();
			response.put("status", "failure");
			response.put("message", "Failed to approve permission request: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/rejectPermissionRequest")
	public ResponseEntity<Map<String, String>> rejectPermissionRequest(@RequestBody Map<String, String> requestData) {
		String empid = requestData.get("empid");
		String srlnum = requestData.get("srlnum");
		System.out.println("Received empid: " + empid + ", srlnum: " + srlnum);

		Map<String, String> response = new HashMap<>();
		try {
			EmployeePermissionMasterTbl permissionRequest = employeePermissionMasterRepository
					.findByEmpidAndId(empid, Long.valueOf(srlnum))
					.orElseThrow(() -> new RuntimeException("Permission request not found"));

			permissionRequest.setDelflg("Y");
			permissionRequest.setStatus("Rejected");
			employeePermissionMasterRepository.save(permissionRequest);
			System.out.println("Permission request rejected: " + permissionRequest);

			Optional<usermaintenance> empOpt = usermaintenanceRepository.findByEmpIdOrUserId(empid);
			Optional<TraineeMaster> traineeOpt = Optional.empty();
			if (empOpt.isEmpty()) {
				traineeOpt = traineemasterRepository.findByTrngidOrUserId(empid);
			}
			if (empOpt.isEmpty() && traineeOpt.isEmpty()) {
				throw new RuntimeException("Employee not found in both employee and trainee tables");
			}

			String employeeEmail = empOpt.isPresent() ? empOpt.get().getEmailid() : traineeOpt.get().getEmailid();
			String employeeFirstName = empOpt.isPresent() ? empOpt.get().getFirstname()
					: traineeOpt.get().getFirstname();
			String managerId = empOpt.isPresent() ? empOpt.get().getRepoteTo() : traineeOpt.get().getRepoteTo();

			Optional<usermaintenance> managerOpt = usermaintenanceRepository.findByEmpIdOrUserId(managerId);
			Optional<TraineeMaster> managerTraineeOpt = Optional.empty();
			if (managerOpt.isEmpty()) {
				managerTraineeOpt = traineemasterRepository.findByTrngidOrUserId(managerId);
			}
			if (managerOpt.isEmpty() && managerTraineeOpt.isEmpty()) {
				throw new RuntimeException("Manager not found in both tables");
			}

			String managerFirstName = managerOpt.isPresent() ? managerOpt.get().getFirstname()
					: managerTraineeOpt.get().getFirstname();
			String managerEmail = managerOpt.isPresent() ? managerOpt.get().getEmailid()
					: managerTraineeOpt.get().getEmailid();

			if (employeeEmail != null && !employeeEmail.isEmpty()) {
				String subject = "Permission Request Rejected";
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				String startTimeFormatted = sdf.format(permissionRequest.getStartTime());
				String endTimeFormatted = sdf.format(permissionRequest.getEndTime());
				String body = String.format(
						"Dear %s,\n\nYour permission request from %s to %s for %s hours has been rejected. Please contact your manager, %s, for further details.\n\nRegards,\n%s,\nWhitestone Software Solution Pvt Ltd.",
						employeeFirstName, startTimeFormatted, endTimeFormatted, permissionRequest.getHours(),
						managerFirstName, managerFirstName);
				emailService.sendLeaveEmail(managerEmail, employeeEmail, subject, body);
				System.out.println("Email sent to: " + employeeEmail);
			}

			response.put("status", "success");
			response.put("message", "Permission request rejected successfully.");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			System.out.println("Error rejecting permission request: " + e.getMessage());
			e.printStackTrace();
			response.put("status", "failure");
			response.put("message", "Failed to reject permission request: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("/permissionRequests/get/{empId}")
	public ResponseEntity<Map<String, Object>> getPermissionRequestsByEmpId(@PathVariable String empId) {
		try {
			List<String> empIds = new ArrayList<>();
			boolean isHR = false;

			// Determine if the user is HR
			usermaintenance user = usermaintenanceRepository.findByEmpid(empId);
			if (user != null) {
				Optional<UserRoleMaintenance> role = userRoleMaintenanceRepository.findByRoleid(user.getRoleid());
				if (role.isPresent() && "HR".equalsIgnoreCase(role.get().getRolename())) {
					isHR = true;
				}
			}

			if (isHR) {
				empIds.addAll(usermaintenanceRepository.findAll().stream().map(usermaintenance::getEmpid).toList());
				empIds.addAll(traineemasterRepository.findAll().stream().map(TraineeMaster::getTrngid).toList());
			} else {
				empIds = getDirectReports(empId);
			}

			// Fetch Master permission requests
			List<EmployeePermissionMasterTbl> masterPermissionRequests = employeePermissionMasterRepository
					.findByEmpidInAndEntitycreflgIn(empIds, Arrays.asList("N", "Y"));

			// Build empId → name mapping
			Map<String, String> empIdToName = new HashMap<>();
			usermaintenanceRepository.findByEmpidIn(empIds)
					.forEach(u -> empIdToName.put(u.getEmpid(), u.getFirstname()));
			traineemasterRepository.findByTrngidIn(empIds)
					.forEach(t -> empIdToName.put(t.getTrngid(), t.getFirstname()));

			// Convert to response
			List<Map<String, Object>> combinedPermissionRequests = new ArrayList<>();
			masterPermissionRequests.forEach(request -> {
				Map<String, Object> permissionData = new HashMap<>();
				permissionData.put("name", empIdToName.getOrDefault(request.getEmpid(), "N/A"));
				permissionData.put("srlnum", request.getId());
				permissionData.put("empid", request.getEmpid());
				permissionData.put("permissiontype", "Permission");
				permissionData.put("startdate", request.getStartTime());
				permissionData.put("enddate", request.getEndTime());
				permissionData.put("teamemail", request.getTeamemail());
				permissionData.put("permissionreason", request.getReason());
				permissionData.put("status", request.getStatus() != null ? request.getStatus() : "Pending");
				permissionData.put("noofdays", request.getHours());
				permissionData.put("entitycreflg", request.getEntitycreflg() != null ? request.getEntitycreflg() : "N");
				permissionData.put("delflg", request.getDelflg() != null ? request.getDelflg() : "N");
				combinedPermissionRequests.add(permissionData);
			});

			Map<String, Object> response = new HashMap<>();
			response.put("data", combinedPermissionRequests);
			response.put("message",
					combinedPermissionRequests.isEmpty() ? "No data found" : "Data fetched successfully");

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("status", "error");
			errorResponse.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@GetMapping("/employeesdetails")
	public ResponseEntity<List<usermaintenance>> getAllEmployeesDetails() {
		List<usermaintenance> employees = usermaintenanceRepository.findAll();
		return ResponseEntity.ok(employees);
	}

	@GetMapping("/trainees")
	public ResponseEntity<List<TraineeMaster>> getAllTrainees() {
		List<TraineeMaster> trainees = traineemasterRepository.findAll();
		return ResponseEntity.ok(trainees);
	}

	@PutMapping("/employee/{userId}/status")
	public ResponseEntity<Void> updateEmployeeStatus(@PathVariable String userId,
			@RequestBody Map<String, String> statusUpdate) {
		usermaintenance user = usermaintenanceRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("Employee not found"));
		user.setStatus(statusUpdate.get("status"));
		user.setRmodtime(new Date());
		usermaintenanceRepository.save(user);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/trainee/{userId}/status")
	public ResponseEntity<Void> updateTraineeStatus(@PathVariable String userId,
			@RequestBody Map<String, String> statusUpdate) {
		TraineeMaster user = traineemasterRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("Trainee not found"));
		user.setStatus(statusUpdate.get("status"));
		user.setRmodtime(new Date());
		traineemasterRepository.save(user);
		return ResponseEntity.ok().build();
	}

	@Autowired
	private EmployeeProjectHistoryRepository projectHistoryRepository;

	@GetMapping("/projects/{empId}")
	public ResponseEntity<Map<String, Object>> getProjectHistoryByEmpId(@PathVariable String empId) {
		try {
			// Step 1: Only use the given empId (no BFS hierarchy expansion)
			List<String> empIds = List.of(empId);

			// Step 2: Fetch project history for this employee
			List<EmployeeProjectHistory> projectHistory = projectHistoryRepository
					.findByEmpIdInAndEntityCreFlgIn(empIds, List.of("N", "Y"));

			// Step 3: Fetch employee name
			Map<String, String> empIdToName = new HashMap<>();
			usermaintenanceRepository.findByEmpidIn(empIds)
					.forEach(u -> empIdToName.put(u.getEmpid(), u.getFirstname() + " " + u.getLastname()));

			// Step 4: Combine results
			List<Map<String, Object>> combinedProjectHistory = new ArrayList<>();
			for (EmployeeProjectHistory project : projectHistory) {
				Map<String, Object> projectData = new HashMap<>();
				projectData.put("name", empIdToName.getOrDefault(project.getEmpId(), "N/A"));
				projectData.put("id", project.getId());
				projectData.put("empId", project.getEmpId());
				projectData.put("projectName", project.getProjectName());
				projectData.put("projectStartDate", project.getProjectStartDate());
				projectData.put("projectEndDate", project.getProjectEndDate());
				projectData.put("location", project.getLocation());
				projectData.put("clientInfo", project.getClientInfo());
				projectData.put("vendorDetails", project.getVendorDetails());
				projectData.put("techParkName", project.getTechParkName());
				projectData.put("vendorName", project.getVendorName());
				projectData.put("modeOfWork", project.getModeOfWork());
				projectData.put("entityCreFlg", project.getEntityCreFlg());
				projectData.put("delFlg", project.getDelFlg());
				projectData.put("rcreUserId", project.getRcreUserId());
				projectData.put("rcreTime", project.getRcreTime());
				projectData.put("rmodUserId", project.getRmodUserId());
				projectData.put("rmodTime", project.getRmodTime());
				combinedProjectHistory.add(projectData);
			}

			// Step 5: Prepare response
			Map<String, Object> response = new HashMap<>();
			response.put("data", combinedProjectHistory);
			response.put("message", combinedProjectHistory.isEmpty() ? "No data found" : "Data fetched successfully");

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("status", "error");
			errorResponse.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@PostMapping("/projects")
	public ResponseEntity<Map<String, Object>> addProject(@RequestBody EmployeeProjectHistory project) {
		try {
			System.out.println("print");
			// Set default values for new project
			project.setRcreTime(LocalDateTime.now());
			project.setEntityCreFlg("N");
			project.setDelFlg("N");
			projectHistoryRepository.save(project);

			Map<String, Object> response = new HashMap<>();
			response.put("message", "Project added successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("status", "error");
			errorResponse.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@GetMapping("/employees/reporting-to/{managerEmpId}")
	public ResponseEntity<?> getReportingEmployees(@PathVariable String managerEmpId) {
		try {
			// Fetch the user's details to check their role and reportTo
			Optional<usermaintenance> userOptional = usermaintenanceRepository.findByEmpid1(managerEmpId);
			if (!userOptional.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Employee with ID " + managerEmpId + " not found");
			}

			usermaintenance user = userOptional.get();
			// Fetch the user's role details
			Optional<UserRoleMaintenance> role = userRoleMaintenanceRepository.findByRoleid(user.getRoleid());
			if (role != null && "HR".equalsIgnoreCase(role.get().getRolename())) {
				// HR Manager with null reportTo: fetch all employees
				List<usermaintenance> allEmployees = usermaintenanceRepository.findAll();
				return ResponseEntity.ok(allEmployees);
			}

			// Non-HR Manager or HR Manager with non-null reportTo: use hierarchy logic
			Set<String> allEmpIds = new HashSet<>();
			Deque<String> queue = new ArrayDeque<>();
			queue.add(managerEmpId); // Start with manager
			allEmpIds.add(managerEmpId); // Include manager in results

			while (!queue.isEmpty()) {
				String currentEmpId = queue.poll();

				// Get direct reports of current employee
				List<usermaintenance> directReports = usermaintenanceRepository.findByRepoteTo(currentEmpId);

				for (usermaintenance emp : directReports) {
					if (allEmpIds.add(emp.getEmpid())) { // Add if not already added
						queue.add(emp.getEmpid()); // Enqueue for further traversal
					}
				}
			}

			// Fetch user details for all employees in the hierarchy (including manager)
			List<usermaintenance> employees = usermaintenanceRepository.findByEmpidIn(new ArrayList<>(allEmpIds));
			return ResponseEntity.ok(employees);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error fetching reporting employees: " + e.getMessage());
		}
	}

	@PutMapping("/project/{id}")
	public EmployeeProjectHistory updateProject(@PathVariable Long id, @RequestBody EmployeeProjectHistory project) {

		EmployeeProjectHistory existing = projectHistoryRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Project not found"));

		existing.setProjectName(project.getProjectName());
		existing.setProjectStartDate(project.getProjectStartDate());
		existing.setProjectEndDate(project.getProjectEndDate());
		existing.setLocation(project.getLocation());
		existing.setClientInfo(project.getClientInfo());
		existing.setVendorDetails(project.getVendorDetails());
		existing.setTechParkName(project.getTechParkName());
		existing.setVendorName(project.getVendorName());
		existing.setModeOfWork(project.getModeOfWork());

		existing.setRmodUserId(project.getEmpId());
		existing.setRmodTime(LocalDateTime.now());

		return projectHistoryRepository.save(existing);
	}

	@GetMapping("/employeesforEdit/{empid}")
	public ResponseEntity<usermaintenance> getEmployeeById(@PathVariable String empid) {
		usermaintenance user = usermaintenanceRepository.findByEmpid(empid);

		return ResponseEntity.ok(user);
	}

	@PutMapping("/employees/{empid}")
	public ResponseEntity<usermaintenance> updateEmployee(@PathVariable String empid,
			@RequestBody usermaintenance updatedUser) {

		usermaintenance existingUser = usermaintenanceRepository.findByEmpid(empid);
		if (existingUser == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		// update fields
		existingUser.setUsername(updatedUser.getUsername());
		existingUser.setFirstname(updatedUser.getFirstname());
		existingUser.setLastname(updatedUser.getLastname());
		existingUser.setEmailid(updatedUser.getEmailid());
		existingUser.setPhonenumber(updatedUser.getPhonenumber());
		existingUser.setRoleid(updatedUser.getRoleid());
		existingUser.setRepoteTo(updatedUser.getRepoteTo());
		existingUser.setEmpType(updatedUser.getEmpType());
		existingUser.setStatus(updatedUser.getStatus());
		existingUser.setRmodtime(new Date());

		usermaintenance savedUser = usermaintenanceRepository.save(existingUser);

		// ====================== Send Email on Update ======================
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String subjectUser = "Your Profile Has Been Updated";
			String bodyUser = String.format(
					"Dear %s,\n\nYour profile has been updated on %s.\n\n" + "Updated Details:\n"
							+ "Username: %s\nEmail: %s\nPhone: %s\nRole: %s\nStatus: %s\n\n"
							+ "Best Regards,\nWhitestone Software Solutions Pvt Ltd",
					savedUser.getFirstname(), sdf.format(new Date()), savedUser.getUsername(), savedUser.getEmailid(),
					savedUser.getPhonenumber(), savedUser.getRoleid(), savedUser.getStatus());
			emailService.sendLeaveEmail("noreply@whitestonesoftware.in", savedUser.getEmailid(), subjectUser, bodyUser);

			// Email to Manager if repoteTo is set
			if (savedUser.getRepoteTo() != null) {
				Optional<usermaintenance> managerOpt = usermaintenanceRepository.findByEmpid1(savedUser.getRepoteTo());
				if (managerOpt.isPresent()) {
					usermaintenance manager = managerOpt.get();
					String subjectManager = "Employee Profile Updated";
					String bodyManager = String.format(
							"Dear %s,\n\nThe profile of your reporting employee %s (EmpID: %s) has been updated on %s.\n\n"
									+ "Please review and guide if needed.\n\nBest Regards,\nWhitestone Software Solutions Pvt Ltd",
							manager.getFirstname(), savedUser.getFirstname(), savedUser.getEmpid(),
							sdf.format(new Date()));
					emailService.sendLeaveEmail("noreply@whitestonesoftware.in", manager.getEmailid(), subjectManager,
							bodyManager);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(savedUser);
	}

	@PutMapping("/trainees/{trngid}")
	public ResponseEntity<TraineeMaster> updateTrainee(@PathVariable String trngid,
			@RequestBody TraineeMaster updatedTrainee) {

		TraineeMaster existingTrainee = traineemasterRepository.findByTrngidOrUserId(trngid)
				.orElseThrow(() -> new RuntimeException("Trainee not found"));

		// update fields
		existingTrainee.setFirstname(updatedTrainee.getUsername());
		existingTrainee.setEmailid(updatedTrainee.getEmailid());
		existingTrainee.setPhonenumber(updatedTrainee.getPhonenumber());
		existingTrainee.setRoleid(updatedTrainee.getRoleid());
		existingTrainee.setEmpType(updatedTrainee.getEmpType());
		existingTrainee.setStatus(updatedTrainee.getStatus());
		existingTrainee.setRepoteTo(updatedTrainee.getRepoteTo());
		existingTrainee.setRmodtime(new Date());

		TraineeMaster savedTrainee = traineemasterRepository.save(existingTrainee);

		// ====================== Send Email on Update ======================
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			String subjectUser = "Your Trainee Profile Has Been Updated";
			String bodyUser = String.format(
					"Dear %s,\n\nYour trainee profile has been updated on %s.\n\n" + "Updated Details:\n"
							+ "Email: %s\nPhone: %s\nRole: %s\nStatus: %s\n\n"
							+ "Best Regards,\nWhitestone Software Solutions Pvt Ltd",
					savedTrainee.getFirstname(), sdf.format(new Date()), savedTrainee.getEmailid(),
					savedTrainee.getPhonenumber(), savedTrainee.getRoleid(), savedTrainee.getStatus());
			emailService.sendLeaveEmail("noreply@whitestonesoftware.in", savedTrainee.getEmailid(), subjectUser,
					bodyUser);

			// Email to Manager if repoteTo is set
			if (savedTrainee.getRepoteTo() != null) {
				usermaintenance manager = usermaintenanceRepository.findByEmpIdOrUserId(savedTrainee.getRepoteTo())
						.orElseThrow(() -> new RuntimeException("Manager not found"));
				String subjectManager = "Trainee Profile Updated";
				String bodyManager = String.format(
						"Dear %s,\n\nThe profile of your trainee %s (TraineeID: %s) has been updated on %s.\n\n"
								+ "Please review and guide if needed.\n\nBest Regards,\nWhitestone Software Solutions Pvt Ltd",
						manager.getFirstname(), savedTrainee.getFirstname(), savedTrainee.getTrngid(),
						sdf.format(new Date()));
				emailService.sendLeaveEmail("noreply@whitestonesoftware.in", manager.getEmailid(), subjectManager,
						bodyManager);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(savedTrainee);
	}

	@GetMapping("/trainees/{trngid}")
	public ResponseEntity<TraineeMaster> getTraineeById(@PathVariable String trngid) {
		TraineeMaster trainee = traineemasterRepository.findByTrngidOrUserId(trngid)
				.orElseThrow(() -> new RuntimeException("Trainee not found"));
		return ResponseEntity.ok(trainee);
	}

//	@PutMapping("/trainees/{trngid}")
//	public ResponseEntity<TraineeMaster> updateTrainee(@PathVariable String trngid,
//			@RequestBody TraineeMaster updatedTrainee) {
//
//		TraineeMaster existingTrainee = traineemasterRepository.findByTrngidOrUserId(trngid)
//				.orElseThrow(() -> new RuntimeException("Trainee not found"));
//		// update fields
//		// existingTrainee.setPassword(passwordEncoder.encode(updatedTrainee.getPassword()));
//		existingTrainee.setFirstname(updatedTrainee.getUsername());
//		existingTrainee.setEmailid(updatedTrainee.getEmailid());
//		existingTrainee.setPhonenumber(updatedTrainee.getPhonenumber());
//		existingTrainee.setRoleid(updatedTrainee.getRoleid());
//		existingTrainee.setEmpType(updatedTrainee.getEmpType());
//		existingTrainee.setStatus(updatedTrainee.getStatus());
//		existingTrainee.setRmodtime(new Date());
//
//		TraineeMaster savedTrainee = traineemasterRepository.save(existingTrainee);
//		return ResponseEntity.ok(savedTrainee);
//	}

	// ================= Employee Save =================
	@PostMapping("/usermaintenance-save")
	public ResponseEntity<?> saveUser(@RequestBody usermaintenance user) {
		// check duplicate empid
		boolean exists = usermaintenanceRepository.existsByEmpid(user.getEmpid());
		if (exists) {
			return ResponseEntity.badRequest().body("Employee ID already exists!");
		}

		// current time setup
		LocalDateTime now = LocalDateTime.now();
		Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
		String rawPassword = user.getPassword(); // keep original for mail

		user.setStatus("Active");
		user.setPassword(passwordEncoder.encode(rawPassword));
		user.setRcretime(nowDate);
		user.setRmodtime(nowDate);
		user.setRvfytime(nowDate);
		user.setUserid("2019" + user.getEmpid());
		user.setDisablefromdate(nowDate);

		LocalDate disableTo = LocalDate.now().plusYears(2);
		Date disableToDate = Date.from(disableTo.atStartOfDay(ZoneId.systemDefault()).toInstant());
		user.setDisabletodate(disableToDate);
		user.setLastlogin(nowDate);

		// Save user in DB
		usermaintenance savedUser = usermaintenanceRepository.save(user);

		// ====================== Send Email ======================
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

			// ---------- Email to User ----------
			String subjectUser = "Welcome to Whitestone Software Solutions - You Have Been Appointed";
			String bodyUser = String.format("Dear %s,\n\n"
					+ "You have been appointed as an employee at Whitestone Software Solutions Pvt Ltd on %s.\n\n"
					+ "Here are your login details to access the system:\n\n" + "User ID: %s\n" + "Username: %s\n"
					+ "Password: %s\n\n" + "Please log in and change your password upon first login.\n\n"
					+ "Best Regards,\nWhitestone Software Solutions Pvt Ltd", user.getFirstname(), sdf.format(nowDate),
					user.getUserid(), user.getEmpid(), rawPassword);
			emailService.sendLeaveEmail("noreply@whitestonesoftware.in", user.getEmailid(), subjectUser, bodyUser);

			// ---------- Email to Manager ----------
			if (user.getRepoteTo() != null) {
				Optional<usermaintenance> managerOpt = usermaintenanceRepository.findByEmpid1(user.getRepoteTo());
				if (managerOpt.isPresent()) {
					usermaintenance manager = managerOpt.get();
					String managerEmail = manager.getEmailid(); // manager's email
					if (managerEmail != null && !managerEmail.isEmpty()) {
						String subjectManager = "New User Assigned to You";
						String bodyManager = String.format(
								"Dear %s,\n\n"
										+ "A new associate has been assigned to you as their reporting manager.\n\n"
										+ "User Details:\n" + "Name: %s\n" + "Employee ID: %s\n" + "User ID: %s\n\n"
										+ "Please guide and assist them as needed.\n\n"
										+ "Best Regards,\nWhitestone Software Solutions Pvt Ltd",
								manager.getFirstname(), // manager name
								user.getFirstname(), // new user name
								user.getEmpid(), // employee id
								user.getUserid() // user id
						);

						emailService.sendLeaveEmail("noreply@whitestonesoftware.in", managerEmail, subjectManager,
								bodyManager);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok("User created successfully, but email sending failed: " + e.getMessage());
		}

		return ResponseEntity.ok(savedUser);
	}

	// ================= Trainee Save =================
	@PostMapping("/trng-save")
	public ResponseEntity<?> saveTrainee(@RequestBody TraineeMaster user) {
		// check duplicate trngid
		boolean exists = traineemasterRepository.existsByTrngid(user.getTrngid());
		if (exists) {
			return ResponseEntity.badRequest().body("Trainee ID already exists!");
		}

		LocalDateTime now = LocalDateTime.now();
		Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
		String rawPassword = user.getPassword();

		// Set trainee fields
		user.setRepoteTo(user.getRepoteTo());
		user.setStatus("Active");
		user.setPassword(passwordEncoder.encode(rawPassword));
		user.setRcretime(nowDate);
		user.setRmodtime(nowDate);
		user.setRvfytime(nowDate);
		user.setUserid("2019" + user.getTrngid());
		user.setDisablefromdate(nowDate);
		user.setEmpType(user.getEmpType());

		LocalDate disableTo = LocalDate.now().plusYears(2);
		Date disableToDate = Date.from(disableTo.atStartOfDay(ZoneId.systemDefault()).toInstant());
		user.setDisabletodate(disableToDate);
		user.setLastlogin(nowDate);

		// Save trainee in DB
		TraineeMaster savedUser = traineemasterRepository.save(user);

		// ====================== Send Emails ======================
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

			// ---------- Email to Trainee ----------
			String subjectUser = "Welcome to Whitestone Software Solutions - You Have Been Appointed";
			String bodyUser = String.format(
					"Dear %s,\n\nYou have been appointed as a Junior Associate at Whitestone Software Solutions Pvt Ltd on %s.\n\n"
							+ "Here are your login details to access the system:\n\n" + "User ID: %s\n"
							+ "Trainee ID: %s\n" + "Password: %s\n\n"
							+ "Please log in and change your password upon first login.\n\n"
							+ "Best Regards,\nWhitestone Software Solutions Pvt Ltd",
					savedUser.getFirstname(), sdf.format(nowDate), savedUser.getUserid(), savedUser.getTrngid(),
					rawPassword);
			emailService.sendLeaveEmail("noreply@whitestonesoftware.in", savedUser.getEmailid(), subjectUser, bodyUser);

			// ---------- Email to Manager ----------
			if (savedUser.getRepoteTo() != null) {
				usermaintenance manager = usermaintenanceRepository.findByEmpid1(savedUser.getRepoteTo())
						.orElseThrow(() -> new RuntimeException("Manager not found"));
				String subjectManager = "New Junior Associate Assigned to You";
				String bodyManager = String.format(
						"Dear %s,\n\nA new Junior Associate has been assigned to you as their reporting manager.\n\n"
								+ "Trainee Details:\n" + "Name: %s\n" + "Trainee ID: %s\n" + "User ID: %s\n\n"
								+ "Please guide and assist them as needed.\n\n"
								+ "Best Regards,\nWhitestone Software Solutions Pvt Ltd",
						manager.getFirstname(), savedUser.getFirstname(), savedUser.getTrngid(), savedUser.getUserid());
				emailService.sendLeaveEmail("noreply@whitestonesoftware.in", manager.getEmailid(), subjectManager,
						bodyManager);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok("Trainee created successfully, but email sending failed: " + e.getMessage());
		}

		return ResponseEntity.ok(savedUser);
	}

	@GetMapping("/employees/relation/{empId}")
	public ResponseEntity<?> getReportingRelation(@PathVariable String empId) {
		try {
			// 1️⃣ Get the employee
			Optional<usermaintenance> employeeOpt = usermaintenanceRepository.findByEmpid1(empId);
			if (!employeeOpt.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + empId);
			}

			usermaintenance employee = employeeOpt.get();

			// 2️⃣ Get the manager (the one this employee reports to)
			usermaintenance manager = null;
			if (employee.getRepoteTo() != null && !employee.getRepoteTo().isEmpty()) {
				manager = usermaintenanceRepository.findByEmpid(employee.getRepoteTo());
			}

			// 3️⃣ Get all employees who report to this employee (direct reports)
			List<usermaintenance> directReports = usermaintenanceRepository.findByRepoteTo(empId);

			// 4️⃣ Prepare response map
			Map<String, Object> response = new HashMap<>();
			response.put("employee", employee);
			response.put("manager", manager);
			response.put("reportees", directReports);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error fetching reporting relation: " + e.getMessage());
		}
	}

	@Autowired
	private EmployeePhotoRepository employeerepository;

	@PostMapping("/upload")
	public ResponseEntity<?> uploadPhoto(@RequestParam("employeeId") String employeeId,
			@RequestParam("file") MultipartFile file) {
		try {
			// Check if the employee already uploaded a photo
			if (employeerepository.findByEmployeeId(employeeId).isPresent()) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Photo already uploaded"));
			}

			// File size validation (10 MB)
			if (file.getSize() > 10 * 1024 * 1024) {
				return ResponseEntity.badRequest().body(Map.of("message", "File size exceeds 10 MB limit"));
			}

			// Fetch employee first name from usermaintenance table
			Optional<usermaintenance> employeeOpt = usermaintenanceRepository.findByEmpid1(employeeId);
			if (employeeOpt.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Employee not found"));
			}

			String firstName = employeeOpt.get().getFirstname(); // assuming field name is firstname

			// Format date (e.g. 2025-10-23)
			String currentDate = java.time.LocalDate.now().toString();

			// Extract file extension safely
			String originalFileName = file.getOriginalFilename();
			String fileExtension = "";
			if (originalFileName != null && originalFileName.contains(".")) {
				fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
			}

			// Construct file name: EMPID_FIRSTNAME_DATE.jpg
			String fileName = employeeId + "_" + firstName + "_" + currentDate + fileExtension;
			String uploadDir = docUploadDir + "/profile_pic";
			// Prepare file path: <uploadDir>/<fileName>
			Path filePath = Paths.get(uploadDir, fileName);

			// Create directories if not exist
			Files.createDirectories(filePath.getParent());

			// Save file to server
			Files.write(filePath, file.getBytes());

			// Save record in DB
			EmployeePhoto photo = new EmployeePhoto();
			photo.setEmployeeId(employeeId);
			photo.setFileName(fileName);
			photo.setFileUrl(filePath.toString());
			photo.setFileType(file.getContentType());

			employeerepository.save(photo);

			return ResponseEntity.ok(Map.of("message", "Photo uploaded successfully", "fileUrl", photo.getFileUrl()));

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Upload failed"));
		}
	}

	@GetMapping("/photo/{employeeId}")
	public ResponseEntity<byte[]> serveFile(@PathVariable String employeeId) {
		Optional<EmployeePhoto> optionalPhoto = employeerepository.findByEmployeeId(employeeId);

		if (optionalPhoto.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		EmployeePhoto photo = optionalPhoto.get();
		try {
			Path filePath = Paths.get(photo.getFileUrl());
			byte[] fileBytes = Files.readAllBytes(filePath);

			String contentType = Files.probeContentType(filePath); // get MIME type

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName() + "\"")
					.contentType(MediaType.parseMediaType(contentType)).body(fileBytes);

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/list")
	public List<Map<String, Object>> getAllPhotos() {
		List<EmployeePhoto> photos = employeerepository.findAll();
		List<Map<String, Object>> response = new ArrayList<>();

		for (EmployeePhoto p : photos) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", p.getId());
			map.put("employeeId", p.getEmployeeId());
			map.put("fileUrl", p.getFileUrl());
			response.add(map);
		}
		return response;
	}

	@PutMapping("/updateLeaveTaken")
	public ResponseEntity<?> updateLeaveTaken(@RequestBody Map<String, Object> payload) {
		String empId = (String) payload.get("empId");
		Float leaveTaken = ((Number) payload.get("leaveTaken")).floatValue();

		Optional<EmployeeLeaveSummary> summaryOpt = employeeLeaveSummaryRepository.findByEmpIdAndYear(empId,
				LocalDate.now().getYear());
		if (summaryOpt.isPresent()) {
			EmployeeLeaveSummary summary = summaryOpt.get();

			// Update leave taken
			summary.setLeaveTaken(leaveTaken);

			// Update casual leave balance
			float totalCL = 12.0f; // default total casual leave
			float updatedBalance = totalCL - leaveTaken;
			summary.setCasualLeaveBalance(updatedBalance >= 0 ? updatedBalance : 0);

			employeeLeaveSummaryRepository.save(summary);

			Map<String, Object> response = new HashMap<>();
			response.put("message", "Leave updated");
			response.put("leaveTaken", summary.getLeaveTaken());
			response.put("casualLeaveBalance", summary.getCasualLeaveBalance());

			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("message", "Employee not found"));
		}
	}

	@Autowired
	private AttendanceChangeRequestRepository attendanceChangeRequestRepository;

	@GetMapping("/requests/pending/{managerId}")
	public Map<String, Object> getPendingRequests(@PathVariable String managerId) {

		// 1️⃣ Get employee IDs who report to this manager
		List<String> empIds = usermaintenanceRepository.findAll().stream()
				.filter(u -> managerId.equals(u.getRepoteTo())).map(u -> u.getEmpid()).collect(Collectors.toList());

		// 2️⃣ Get trainee IDs who report to this manager
		List<String> traineeIds = traineemasterRepository.findAll().stream()
				.filter(t -> managerId.equals(t.getRepoteTo())).map(t -> t.getTrngid()).collect(Collectors.toList());

		// 3️⃣ Combine employees + trainees + manager himself
		Set<String> allIds = new HashSet<>();
		allIds.addAll(empIds);
		allIds.addAll(traineeIds);
		allIds.add(managerId);

		// 4️⃣ Get all pending requests for these employees/trainees
		List<AttendanceChangeRequest> requests = attendanceChangeRequestRepository
				.findByStatusAndEmployeeIdIn("Pending", new ArrayList<>(allIds));

		// 5️⃣ Build name lookup from both repositories
		Map<String, String> empIdToName = new HashMap<>();

		// From employee table
		usermaintenanceRepository.findByEmpidIn(new ArrayList<>(allIds))
				.forEach(u -> empIdToName.put(u.getEmpid(), u.getUsername()));

		// From trainee table
		traineemasterRepository.findByTrngidIn(new ArrayList<>(allIds))
				.forEach(t -> empIdToName.put(t.getTrngid(), t.getUsername()));

		// 6️⃣ Map requests to structured response
		List<Map<String, Object>> requestList = requests.stream().map(r -> {
			Map<String, Object> map = new HashMap<>();
			map.put("id", r.getId());
			map.put("employeeId", r.getEmployeeId());
			map.put("employeeName", empIdToName.getOrDefault(r.getEmployeeId(), "N/A"));
			map.put("attendanceDate", r.getAttendanceDate() != null ? r.getAttendanceDate() : "");
			map.put("requestedStatus", r.getRequestedStatus());
			map.put("remarks", r.getRemarks());
			map.put("status", r.getStatus());
			map.put("createdBy", r.getCreatedBy());
			map.put("createdAt", r.getCreatedAt());
			map.put("approvedBy", r.getApprovedBy());
			map.put("approvedAt", r.getApprovedAt());

			// ✅ Identify if request belongs to the manager himself
			map.put("isManagerRequest", r.getEmployeeId().equals(managerId));

			return map;
		}).collect(Collectors.toList());

		// 7️⃣ Return structured JSON response
		return Map.of("managerId", managerId, "totalRequests", requestList.size(), "data", requestList);
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping("/requests/approve")
	public ResponseEntity<?> approveRequest(@RequestBody Map<String, Object> payload) {
		try {
			Long requestId = Long.valueOf(payload.get("requestId").toString());
			String action = payload.get("action").toString(); // Approved / Rejected
			String managerId = payload.get("managerId").toString();

			AttendanceChangeRequest request = attendanceChangeRequestRepository.findById(requestId)
					.orElseThrow(() -> new RuntimeException("Request not found"));

			// ✅ Restrict self-approval
			if (request.getEmployeeId().equals(managerId)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body(Map.of("error", "You cannot approve or reject your own request."));
			}

			// ✅ Identify whether Employee or Trainee
			usermaintenance emp = usermaintenanceRepository.findByEmpid(request.getEmployeeId());
			TraineeMaster trainee = null;
			if (emp == null) {
				trainee = traineemasterRepository.findByTrngid(request.getEmployeeId());
			}

			// ✅ Validate manager relationship
			boolean authorized = false;
			if (emp != null && managerId.equals(emp.getRepoteTo())) {
				authorized = true;
			} else if (trainee != null && managerId.equals(trainee.getRepoteTo())) {
				authorized = true;
			}

			if (!authorized) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body(Map.of("error", "You are not authorized to approve this request."));
			}

			LocalDate attendanceDate = request.getAttendanceDate();
			int year = attendanceDate.getYear();
			int month = attendanceDate.getMonthValue();
			String empId = request.getEmployeeId();

			// ✅ Handle Absent Approval (Employee or Trainee)
			if ("Approved".equalsIgnoreCase(action) && "Absent".equalsIgnoreCase(request.getRequestedStatus())) {

				// Only employees have leave summary — skip for trainees
				if (emp != null) {
					EmployeeLeaveSummary leaveSummary = employeeLeaveSummaryRepository.findByEmpIdAndYear(empId, year)
							.orElseGet(() -> {
								EmployeeLeaveSummary summary = new EmployeeLeaveSummary();
								summary.setEmpId(empId);
								summary.setYear(year);
								summary.setCasualLeaveBalance(0f);
								summary.setLop(0f);
								return summary;
							});

					Float availableCL = leaveSummary.getCasualLeaveBalance() != null
							? leaveSummary.getCasualLeaveBalance()
							: 0f;
					Float lopDays = 0f;

					if (availableCL >= 1) {
						leaveSummary.setCasualLeaveBalance(availableCL - 1);
					} else {
						lopDays = 1f;
					}

					switch (month) {
					case 1:
						leaveSummary.setLopJan(addLop(leaveSummary.getLopJan(), lopDays));
						break;
					case 2:
						leaveSummary.setLopFeb(addLop(leaveSummary.getLopFeb(), lopDays));
						break;
					case 3:
						leaveSummary.setLopMar(addLop(leaveSummary.getLopMar(), lopDays));
						break;
					case 4:
						leaveSummary.setLopApr(addLop(leaveSummary.getLopApr(), lopDays));
						break;
					case 5:
						leaveSummary.setLopMay(addLop(leaveSummary.getLopMay(), lopDays));
						break;
					case 6:
						leaveSummary.setLopJun(addLop(leaveSummary.getLopJun(), lopDays));
						break;
					case 7:
						leaveSummary.setLopJul(addLop(leaveSummary.getLopJul(), lopDays));
						break;
					case 8:
						leaveSummary.setLopAug(addLop(leaveSummary.getLopAug(), lopDays));
						break;
					case 9:
						leaveSummary.setLopSep(addLop(leaveSummary.getLopSep(), lopDays));
						break;
					case 10:
						leaveSummary.setLopOct(addLop(leaveSummary.getLopOct(), lopDays));
						break;
					case 11:
						leaveSummary.setLopNov(addLop(leaveSummary.getLopNov(), lopDays));
						break;
					case 12:
						leaveSummary.setLopDec(addLop(leaveSummary.getLopDec(), lopDays));
						break;
					}

					leaveSummary.setLeaveTaken(
							(leaveSummary.getLeaveTaken() == null ? 0 : leaveSummary.getLeaveTaken()) + 1);
					leaveSummary.setUpdatedAt(LocalDateTime.now());
					employeeLeaveSummaryRepository.save(leaveSummary);

					// ✅ Record Leave in Leave Master Table
					EmployeeLeaveMasterTbl leaveRecord = new EmployeeLeaveMasterTbl();
					leaveRecord.setEmpid(empId);
					Timestamp ts = Timestamp.valueOf(attendanceDate.atStartOfDay());
					leaveRecord.setStartdate(ts);
					leaveRecord.setEnddate(ts);
					leaveRecord.setNoofdays(1.0f);
					leaveRecord.setNoofbooked(1.0f);
					leaveRecord.setDelflg("N");
					leaveRecord.setEntitycreflg("N");
					leaveRecord.setStatus("Approved");
					leaveRecord.setLeavetype(lopDays > 0 ? "LOP" : "CL");
					leaveRecord.setLeavereason("Auto Leave applied due to Absent Approval");
					leaveRecord.setTeamemail(emp.getEmailid());
					employeeLeaveMasterRepository.save(leaveRecord);
				}
			}

			// ✅ Update Request Status
			request.setStatus(action);
			request.setApprovedBy(managerId);
			request.setApprovedAt(new Date());
			attendanceChangeRequestRepository.save(request);

			// ✅ Update Attendance Table if Approved
			if ("Approved".equalsIgnoreCase(action)) {
				Date attendanceDate1 = Date.from(attendanceDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

				UserMasterAttendanceMod attendance = usermasterattendancemodrepository
						.findByAttendanceidAndAttendancedate(request.getEmployeeId(), attendanceDate1)
						.orElse(new UserMasterAttendanceMod());

				attendance.setUserid("2019" + request.getEmployeeId());
				attendance.setAttendanceid(request.getEmployeeId());
				attendance.setAttendancedate(attendanceDate1);
				attendance.setStatus(request.getRequestedStatus());
				attendance.setRemarks(request.getRemarks());

				// ✅ Default Check-in / Check-out
				LocalDateTime checkInTime = attendanceDate.atTime(9, 0);
				LocalDateTime checkOutTime = attendanceDate.atTime(18, 0);

				attendance.setCheckintime(Date.from(checkInTime.atZone(ZoneId.systemDefault()).toInstant()));
				attendance.setCheckouttime(Date.from(checkOutTime.atZone(ZoneId.systemDefault()).toInstant()));
				attendance.setTotalhoursworked("9:00");
				attendance.setCheckinstatus("On-Time");
				attendance.setCheckoutstatus("Completed");
				attendance.setRcreuserid(request.getEmployeeId());
				attendance.setRcretime(new Date());
				attendance.setRmoduserid(managerId);
				attendance.setRmodtime(new Date());

				usermasterattendancemodrepository.save(attendance);
			}

			// ✅ Send Email Notification (Employee or Trainee)
			String empName = (emp != null ? emp.getFirstname() : trainee.getFirstname());
			String empEmail = (emp != null ? emp.getEmailid() : trainee.getEmailid());

			String subject = "Your Attendance Request Has Been " + action;
			String body = String.format(
					"Dear %s,\n\nYour attendance change request for %s (%s) has been %s by your manager.\n\nRemarks: %s\n\nRegards,\nWhitestone Software Solutions",
					empName, request.getAttendanceDate(), request.getRequestedStatus(), action, request.getRemarks());

			emailService.sendLeaveEmail("noreply@whitestonesoftware.in", empEmail, subject, body);

			return ResponseEntity.ok(Map.of("message", "Request " + action.toLowerCase() + " successfully."));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Transaction failed and rolled back: " + e.getMessage());
		}
	}

	private Float addLop(Float existing, Float add) {
		return (existing == null ? 0 : existing) + (add == null ? 0 : add);
	}

	@PostMapping("/attendance/requestChange")
	public ResponseEntity<Map<String, Object>> requestAttendanceChange(@RequestBody AttendanceChangeRequest request) {
		Map<String, Object> response = new HashMap<>();

		try {
			System.out.println("DATEEEEEE>>>>>>>>>> " + request.getAttendanceDate());

			// ✅ Step 1: Check if request already exists for same employee/trainee & date
			boolean exists = attendanceChangeRequestRepository
					.existsByEmployeeIdAndAttendanceDate(request.getEmployeeId(), request.getAttendanceDate());

			if (exists) {
				response.put("status", "error");
				response.put("message", "A request for this date already exists and cannot be submitted again.");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
			}

			// ✅ Step 2: Save new request
			request.setStatus("Pending");
			request.setCreatedAt(new Date());
			attendanceChangeRequestRepository.save(request);

			// ✅ Step 3: Identify whether it's an Employee or Trainee
			usermaintenance emp = usermaintenanceRepository.findByEmpid(request.getEmployeeId());
			TraineeMaster trainee = null;
			usermaintenance manager = null;

			if (emp == null) {
				trainee = traineemasterRepository.findByTrngid(request.getEmployeeId());
			}

			// ✅ Step 4: Fetch Manager / Mentor
			if (emp != null && emp.getRepoteTo() != null) {
				manager = usermaintenanceRepository.findByEmpid(emp.getRepoteTo());
			} else if (trainee != null && trainee.getRepoteTo() != null) {
				manager = usermaintenanceRepository.findByEmpid(trainee.getRepoteTo());
			}

			// ✅ Step 5: Send email notification if manager found
			if (manager != null) {
				String subject = "New Attendance Change Request";

				String empName = (emp != null ? emp.getFirstname() : trainee.getFirstname());
				String empEmail = (emp != null ? emp.getEmailid() : trainee.getEmailid());

				String body = String.format(
						"Dear %s,\n\n%s has submitted an attendance change request for %s with requested status: %s.\n\nRemarks: %s\n\nPlease review and approve/reject it.\n\nBest Regards,\nWhitestone Software Solutions",
						manager.getFirstname(), empName, request.getAttendanceDate(), request.getRequestedStatus(),
						request.getRemarks());

				// from, to, subject, body
				emailService.sendLeaveEmail(empEmail, manager.getEmailid(), subject, body);
			}

			response.put("status", "success");
			response.put("message", "Attendance change request submitted successfully.");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "error");
			response.put("message", "An unexpected error occurred while submitting the request: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("/attendance/checkin/eligibility/{employeeId}")
	public ResponseEntity<Map<String, Object>> checkCheckInEligibility(@PathVariable String employeeId) {
		Map<String, Object> response = new HashMap<>();

		try {
			// 🔹 Step 1: Check if employee exists
			usermaintenance employee = usermaintenanceRepository.findByEmpid(employeeId);
			TraineeMaster trainee = null;

			if (employee == null) {
				trainee = traineemasterRepository.findByTrngid(employeeId);
			}

			if (employee == null && trainee == null) {
				response.put("status", "error");
				response.put("eligible", false);
				response.put("message", "Employee or Trainee not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			// 🔹 Step 2: Get yesterday’s date
			LocalDate yesterday = LocalDate.now().minusDays(1);

			// 🔹 Step 3: Week off check (Sunday or 2nd/4th Saturday)
			if (isWeekOff(yesterday)) {
				response.put("status", "success");
				response.put("eligible", true);
				response.put("message", "Yesterday was a week off (Sunday or 2nd/4th Saturday). Check-in allowed.");
				return ResponseEntity.ok(response);
			}

			// 🔹 Step 4: Convert to java.util.Date
			Date startOfDay = Date.from(yesterday.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date endOfDay = Date.from(yesterday.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

			// 🔹 Step 5: Check attendance record
			Optional<UserMasterAttendanceMod> attendanceOpt = usermasterattendancemodrepository
					.findByAttendanceidAndAttendancedate(employeeId, startOfDay);

			if (attendanceOpt.isPresent()) {
				String status = attendanceOpt.get().getStatus();

				if ("Present".equalsIgnoreCase(status)) {
					response.put("status", "success");
					response.put("eligible", true);
					response.put("message", "Eligible for check-in. Yesterday’s attendance is marked as Present.");
				} else if ("Early Leave".equalsIgnoreCase(status)) {
					response.put("status", "warning");
					response.put("eligible", true);
					response.put("message", "Eligible for check-in, but yesterday was marked as Early Leave.");
				} else if ("COMP OFF".equalsIgnoreCase(status)) {
					response.put("status", "warning");
					response.put("eligible", true);
					response.put("message", "Eligible for check-in, but yesterday was marked as COMP OFF.");
				} else if ("OD".equalsIgnoreCase(status)) {
					response.put("status", "warning");
					response.put("eligible", true);
					response.put("message", "Eligible for check-in, but yesterday was marked as OD.");
				} else if ("FORGOT".equalsIgnoreCase(status)) {
					response.put("status", "warning");
					response.put("eligible", true);
					response.put("message", "Eligible for check-in, but yesterday was marked as FORGOT.");
				} else if ("Half-Day".equalsIgnoreCase(status)) {
					response.put("status", "warning");
					response.put("eligible", true);
					response.put("message", "Eligible for check-in, but yesterday was marked as Half-Day.");
				} else if ("Absent".equalsIgnoreCase(status)) {
					response.put("status", "success");
					response.put("eligible", true);
					response.put("message", "Eligible for check-in, but yesterday was marked as Absent.");
				}
				else if ("Week off".equalsIgnoreCase(status)) {
					response.put("status", "success");
					response.put("eligible", true);
					response.put("message", "Eligible for check-in, but yesterday was marked as Week off.");
				}

				else if ("Public holiday".equalsIgnoreCase(status)) {
					response.put("status", "success");
					response.put("eligible", true);
					response.put("message", "Eligible for check-in, but yesterday was marked as Public holiday.");
				}

				else if ("holiday".equalsIgnoreCase(status)) {
					response.put("status", "success");
					response.put("eligible", true);
					response.put("message", "Eligible for check-in, but yesterday was marked as holiday.");
				}
				return ResponseEntity.ok(response);
			}

			// 🔹 Step 6: Check pending attendance change request
			Optional<AttendanceChangeRequest> pendingRequestOpt = attendanceChangeRequestRepository
					.findByEmployeeIdAndAttendanceDateAndStatus(employeeId, yesterday, "Pending");

			if (pendingRequestOpt.isPresent()) {
				response.put("status", "success");
				response.put("eligible", true);
				response.put("message", "Pending attendance change request found. Check-in allowed.");
				return ResponseEntity.ok(response);
			}

			// 🔹 Step 7: Check leave record covering yesterday
			List<EmployeeLeaveMasterTbl> leaves = employeeLeaveMasterRepository
					.findByEmpidAndStartdateBetweenAndStatusInIgnoreCase(employeeId, startOfDay, endOfDay,
							Arrays.asList("approved", "pending"));

			if (!leaves.isEmpty()) {
				EmployeeLeaveMasterTbl leave = leaves.get(0);
				LocalDate startDate = leave.getStartdate().toLocalDateTime().toLocalDate();
				LocalDate endDate = leave.getEnddate().toLocalDateTime().toLocalDate();

				response.put("status", "success");
				response.put("eligible", true);
				response.put("message", String.format(
						"Yesterday (%s) falls within your %s leave period (%s to %s, %s status). Check-in allowed.",
						yesterday, leave.getLeavetype(), startDate, endDate, leave.getStatus()));
				return ResponseEntity.ok(response);
			}

			// 🔹 Step 8: Not eligible (no attendance, leave, or request)
			response.put("status", "error");
			response.put("eligible", false);
			response.put("message",
					"Yesterday's attendance is not marked and no leave/pending request found. Please update the timesheet and try again.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "error");
			response.put("eligible", false);
			response.put("message", "An unexpected error occurred: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	private boolean isWeekOff(LocalDate date) {
		// ✅ Always treat Sunday as week off
		if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
			return true;
		}

		// ✅ For Saturdays: check if it’s the 2nd or 4th one in the month
		if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
			LocalDate firstDay = date.withDayOfMonth(1);
			int saturdayCount = 0;
			for (int i = 0; i < date.getDayOfMonth(); i++) {
				LocalDate d = firstDay.plusDays(i);
				if (d.getDayOfWeek() == DayOfWeek.SATURDAY) {
					saturdayCount++;
				}
			}
			// 2nd or 4th Saturday → week off
			if (saturdayCount == 2 || saturdayCount == 4) {
				return true;
			}
		}

		return false;
	}

	@GetMapping("/download/employees/excel")
	public ResponseEntity<byte[]> downloadEmployeeExcel() {
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Employee Photos");

			// Create header row
			Row headerRow = sheet.createRow(0);
			String[] headers = { "S.No", "Employee ID", "Name", "Photo" };
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
			}

			// Adjust column width
			sheet.setColumnWidth(0, 2000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 7000);
			sheet.setColumnWidth(3, 8000); // enough space for portrait photo

			// ✅ Fetch only active employees
			List<usermaintenance> employees = usermaintenanceRepository.findByStatusIgnoreCase("Active");

			int rowNum = 1;
			CreationHelper helper = workbook.getCreationHelper();
			Drawing<?> drawing = sheet.createDrawingPatriarch();

			for (usermaintenance emp : employees) {
				Row row = sheet.createRow(rowNum);
				row.setHeightInPoints(150); // Taller row for photo

				row.createCell(0).setCellValue(rowNum);
				row.createCell(1).setCellValue(emp.getEmpid());
				row.createCell(2).setCellValue(emp.getFirstname() + " " + emp.getLastname());

				// Fetch employee photo
				Optional<EmployeePhoto> photoOpt = employeerepository.findByEmployeeId(emp.getEmpid());

				if (photoOpt.isPresent()) {
					String filePath = photoOpt.get().getFileUrl();
					File file = new File(filePath);

					if (file.exists()) {
						try (InputStream is = new FileInputStream(file)) {
							byte[] bytes = IOUtils.toByteArray(is);
							int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);

							ClientAnchor anchor = helper.createClientAnchor();
							anchor.setCol1(3);
							anchor.setRow1(rowNum);
							anchor.setCol2(4);
							anchor.setRow2(rowNum + 1);

							// Center image in the cell
							anchor.setDx1(Units.toEMU(20)); // left padding
							anchor.setDy1(Units.toEMU(10)); // top padding

							Picture pict = drawing.createPicture(anchor, pictureIdx);

							// ✅ Resize while maintaining aspect ratio (0.5–0.6 is best for portraits)
							pict.resize(0.55);
						} catch (IOException e) {
							row.createCell(3).setCellValue("Error loading photo");
						}
					} else {
						row.createCell(3).setCellValue("File not found");
					}
				} else {
					row.createCell(3).setCellValue("No Photo");
				}

				rowNum++;
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);

			HttpHeaders headersResponse = new HttpHeaders();
			headersResponse.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Active_Employee_Photos.xlsx");
			headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);

			return new ResponseEntity<>(outputStream.toByteArray(), headersResponse, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(("Failed to generate Excel: " + e.getMessage()).getBytes());
		}
	}

	@Autowired
	private EmployeeFingerprintRepository fingerprintrepository;

	@PostMapping("/save")
	public Map<String, Object> saveFingerprint(@RequestBody Map<String, String> request) {
		String employeeId = request.get("employeeId");
		String fingerData = request.get("fingerData");

		Map<String, Object> response = new HashMap<>();

		try {
			EmployeeFingerprint fingerprint = fingerprintrepository.findByEmployeeId(employeeId)
					.orElse(new EmployeeFingerprint());

			fingerprint.setEmployeeId(employeeId);
			fingerprint.setFingerprintData(fingerData);

			fingerprintrepository.save(fingerprint);

			response.put("status", "success");
			response.put("message", "Fingerprint saved successfully for employee: " + employeeId);
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
		}

		return response;
	}

	@PostMapping("/validatefinger")
	public Map<String, Object> fingerprintLogin(@RequestBody Map<String, String> request) {
		String employeeId = request.get("employeeId");
		String liveFingerData = request.get("fingerData");

		Map<String, Object> response = new HashMap<>();

		try {
			Optional<EmployeeFingerprint> fingerprintOpt = fingerprintrepository.findByEmployeeId(employeeId);
			if (!fingerprintOpt.isPresent()) {
				response.put("status", "error");
				response.put("message", "No fingerprint found for employee " + employeeId);
				return response;
			}

			String storedFingerData = fingerprintOpt.get().getFingerprintData();

			// ✅ In real implementation:
			// Use Mantra SDK API to match fingerprints
			// Example: MatchResult result = MantraSDK.Match(storedFingerData,
			// liveFingerData);

			boolean isMatched = storedFingerData.trim().equals(liveFingerData.trim()); // 🔹 demo comparison

			if (isMatched) {
				response.put("status", "success");
				response.put("message", "Fingerprint matched successfully!");
				response.put("employeeId", employeeId);
				response.put("token", UUID.randomUUID().toString()); // mock token
			} else {
				response.put("status", "error");
				response.put("message", "Fingerprint did not match. Please try again.");
			}
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
		}

		return response;
	}
	
//	 @Autowired
//	    FingerprintService service;
//
//	    @GetMapping("/capture")
//	    public Map<String, Object> capture() {
//	        Map<String, Object> res = new HashMap<>();
//
//	        try {
//	            String template = service.captureFinger();
//	            res.put("status", "success");
//	            res.put("template", template);
//	        } catch (Exception e) {
//	            res.put("status", "error");
//	            res.put("message", e.getMessage());
//	        }
//	        return res;
//	    }

}
