package com.whitestone.hrms.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.PropertyValueException;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitestone.entity.AdvancesDetailsMod;
import com.whitestone.entity.CompanyLocation;
import com.whitestone.entity.Department;
import com.whitestone.entity.Designation;
import com.whitestone.entity.EmployeeAddressMod;
import com.whitestone.entity.EmployeeEducationDetailsMod;
import com.whitestone.entity.EmployeeLeaveMasterTbl;
import com.whitestone.entity.EmployeeLeaveModTbl;
import com.whitestone.entity.EmployeeLeaveSummary;
import com.whitestone.entity.EmployeeProfessionalDetailsMod;
import com.whitestone.entity.EmployeeProfile;
import com.whitestone.entity.EmployeeProfileMod;
import com.whitestone.entity.EmployeeSalaryHistory;
import com.whitestone.entity.EmployeeSalaryTbl;
import com.whitestone.entity.EmployeeSkillMod;
import com.whitestone.entity.ExpenseDetailsMod;
import com.whitestone.entity.LocationAllowance;
import com.whitestone.entity.LocationAllowance.AllowanceType;
import com.whitestone.entity.Organization;
import com.whitestone.entity.PTSlab;
import com.whitestone.entity.Payroll;
import com.whitestone.entity.PayrollHistory;
import com.whitestone.entity.SalaryComponent;
import com.whitestone.entity.SalaryTemplate;
import com.whitestone.entity.SalaryTemplateComponent;
import com.whitestone.entity.StatutorySettings;
import com.whitestone.entity.TravelEntityMod;
import com.whitestone.entity.UserMasterAttendanceMod;
import com.whitestone.entity.UserRoleMaintenance;
import com.whitestone.entity.WsslCalendarMod;
import com.whitestone.entity.usermaintenance;
import com.whitestone.entity.usermaintenancemod;
import com.whitestone.hrms.repo.AdvancesDetailsModRepository;
import com.whitestone.hrms.repo.CompanyLocationRepository;
import com.whitestone.hrms.repo.DepartmentRepository;
import com.whitestone.hrms.repo.DesignationRepository;
import com.whitestone.hrms.repo.EmployeeAddressModRepository;
import com.whitestone.hrms.repo.EmployeeEducationDetailsModRepository;
import com.whitestone.hrms.repo.EmployeeLeaveMasterTblRepository;
import com.whitestone.hrms.repo.EmployeeLeaveModTblRepository;
import com.whitestone.hrms.repo.EmployeeLeaveSummaryRepository;
import com.whitestone.hrms.repo.EmployeeProfessionalDetailsModRepository;
import com.whitestone.hrms.repo.EmployeeProfileModRepository;
import com.whitestone.hrms.repo.EmployeeProfileRepository;
import com.whitestone.hrms.repo.EmployeeSalaryHistoryTblRepository;
import com.whitestone.hrms.repo.EmployeeSalaryTblRepository;
import com.whitestone.hrms.repo.EmployeeSkillModRepository;
import com.whitestone.hrms.repo.ExpenseDetailsModRepository;
import com.whitestone.hrms.repo.ExpenseDetailsRepository;
import com.whitestone.hrms.repo.LocationAllowanceRepository;
import com.whitestone.hrms.repo.OrganizationRepository;
import com.whitestone.hrms.repo.PTSlabRepository;
import com.whitestone.hrms.repo.PayrollHistoryRepository;
import com.whitestone.hrms.repo.PayrollRepository;
import com.whitestone.hrms.repo.SalaryComponentRepository;
import com.whitestone.hrms.repo.SalaryTemplateComponentRepository;
import com.whitestone.hrms.repo.SalaryTemplatePayload;
import com.whitestone.hrms.repo.SalaryTemplateRepository;
import com.whitestone.hrms.repo.StatutorySettingsRepository;
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
@Controller

@RestController
@Service

public class AppController {

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	private ErrorMessageService errorMessageService;

	@Autowired
	private usermaintenanceRepository usermaintenanceRepository;

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

	// User login endpoint
	@RequestMapping(value = { "/login" }, method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> login(@RequestBody usermaintenance employeeDetails) {
		System.out.println("TEST");
		System.out.println("sedfg" + employeeDetails);
		try {
			String employeeid = employeeDetails.getUsername();
			String rawPassword = employeeDetails.getPassword();

			// Find the employee by ID
			Optional<usermaintenance> employeeOpt = usermaintenanceRepository.findByEmpIdOrUserId(employeeid);
			Optional<usermaintenancemod> employeeOpt1 = Optional.empty();
			System.out.println("dfghjk" + employeeOpt);
			if (!employeeOpt.isPresent()) {

				employeeOpt1 = userMaintenanceRepository.findByEmpId(employeeid);
				System.out.println("dfghjkq1" + employeeOpt1);
			}

			// Check if the employee exists and the password matches
			if ((employeeOpt.isPresent() && passwordEncoder.matches(rawPassword, employeeOpt.get().getPassword()))
					|| (employeeOpt1.isPresent()
							&& passwordEncoder.matches(rawPassword, employeeOpt1.get().getPassword()))) {
				// Extract employee details
				Map<String, Object> response = new HashMap<>();
				if (employeeOpt.isPresent()) {
					usermaintenance employee = employeeOpt.get();
					String role = userRoleMaintenanceRepository.findByRoleid(employee.getRoleid())
							.map(UserRoleMaintenance::getRolename).orElse("Unknown Role");

					String employeeId = employee.getEmpid();
					String employeeName = employee.getFirstname();
					String employeeEmail = employee.getEmailid();
					String token = generateToken(employeeId, role);
					String successMessage = errorMessageService.getErrorMessage("VALID_USR_CREDENTIALS", "en");
					response.put("message", successMessage);
					response.put("role", role);
					response.put("employeeId", employeeId);
					response.put("username", employeeName);
					response.put("email", employeeEmail);
					response.put("token", token); // Add token to the response

					// return ResponseEntity.ok(response);
				}
				if (employeeOpt1.isPresent()) {
					usermaintenancemod employee = employeeOpt1.get();
					String role = employee.getRoleid();
					String employeeId = employee.getemp_id();
					String employeeName = employee.getFirstname();
					String employeeEmail = employee.getEmailid();

					// Generate JWT Token
					String token = generateToken(employeeId, role);
					// Fetch success message using ErrorMessageService
					String successMessage = errorMessageService.getErrorMessage("VALID_USR_CREDENTIALS", "en");

					// Create response body using HashMap (Java 8 compatible)

					response.put("message", successMessage);
					response.put("role", role);
					response.put("employeeId", employeeId);
					response.put("username", employeeName);
					response.put("email", employeeEmail);
					response.put("token", token);
				}
				return ResponseEntity.ok(response);
			} else {
				// Fetch the error message for invalid credentials
				String errorMessage = errorMessageService.getErrorMessage("INVALID_USR_CREDENTIALS", "en");

				// Create response body using HashMap (Java 8 compatible)
				Map<String, Object> errorResponse = new HashMap<>();
				errorResponse.put("error", errorMessage);

				return ResponseEntity.badRequest().body(errorResponse);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Handle any internal errors
			String errorMessage = errorMessageService.getErrorMessage("INTERNAL_SERVER_ERROR", "en");

			// Create response body using HashMap (Java 8 compatible)
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
		return handleAttendanceRecord(attendancePayload.getAttendanceid(), true, attendancePayload);
	}

	@PostMapping("/checkOut")
	public ResponseEntity<?> checkOut(@RequestBody UserMasterAttendanceMod attendancePayload) {
		return handleAttendanceRecord(attendancePayload.getAttendanceid(), false, attendancePayload);
	}

	private ResponseEntity<?> handleAttendanceRecord(String employeeId, boolean isCheckIn,
			UserMasterAttendanceMod attendancePayload) {
		try {
			System.out.println("attendancePayload: " + attendancePayload.toString());

			// Fetch employee details
			Optional<usermaintenance> employeeOpt = usermaintenanceRepository.findByEmpIdOrUserId(employeeId);
			if (!employeeOpt.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Employee not found\"}");
			}

			usermaintenance employee = employeeOpt.get();

			if (isCheckIn) {
				// Check-in logic
				Optional<UserMasterAttendanceMod> existingAttendanceToday = usermasterattendancemodrepository
						.findByAttendanceidAndAttendancedate(employeeId, java.sql.Date.valueOf(LocalDate.now()));
				System.out.println("ATT_TDY" + existingAttendanceToday.toString());
				if (existingAttendanceToday.isPresent()) {
					System.out.println("sdfghjkl");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("{\"error\": \"Already checked in for today\"}");
				}

				// Create new attendance record
				UserMasterAttendanceMod attendance = new UserMasterAttendanceMod();
				attendance.setUserid(employee.getUserid().toString());
				attendance.setAttendanceid(attendancePayload.getAttendanceid());
				attendance.setStatus("Present");
				attendance.setCheckinstatus(attendancePayload.getStatus());
				attendance.setCheckinlocation(attendancePayload.getCheckinlocation());
				attendance.setRcreuserid(attendancePayload.getAttendanceid());
				attendance.setAttendancedate(new Date());
				attendance.setCheckintime(new Date());
				attendance.setRcretime(new Date());
				attendance.setRcreuserid(employeeId);
				attendance.setRmodtime(new Date());
				attendance.setRmoduserid(employeeId);

				// Save the record
				usermasterattendancemodrepository.save(attendance);

				Map<String, Object> response = new HashMap<>();
				response.put("message", "Check-in success");
				return ResponseEntity.ok(response);
			} else {
				// Checkout logic
				Optional<UserMasterAttendanceMod> latestRecordOpt = usermasterattendancemodrepository
						.findLatestRecordBySrlNo(employeeId);

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
				long durationMinutes = durationMillis / 60000; // total duration in minutes
				long durationHours = durationMinutes / 60; // total hours
				long remainingMinutes = durationMinutes % 60;
				String totalWorkedTime = String.format("%02d:%02d", durationHours, remainingMinutes);
				attendance.setTotalhoursworked(totalWorkedTime);
				if (durationHours >= 8) {

					attendance.setStatus("Present");
					long overtimeMinutes = durationMinutes - (8 * 60); // Overtime in minutes
					long overtimeHours = overtimeMinutes / 60;
					long overtimeRemainingMinutes = overtimeMinutes % 60;
				} else if (durationHours >= 4) {
					LocalDate date = LocalDate.now(); // Current date
					DayOfWeek dayOfWeek = date.getDayOfWeek();

					if (dayOfWeek == DayOfWeek.SATURDAY) {
						attendance.setStatus("Present");
					} else {
						attendance.setStatus("Half-Day");
					}

				} else {
					attendance.setStatus("Absent");
				}

				// Set the check-out time and status
				attendance.setCheckouttime(checkoutTime);
				attendance.setCheckoutstatus(attendancePayload.getStatus());
				attendance.setRcretime(checkoutTime);
				attendance.setRcreuserid(employeeId);
				attendance.setRmodtime(checkoutTime);
				attendance.setRmoduserid(employeeId);

				System.out.println("Total Worked Time: " + totalWorkedTime);
				System.out.println("Overtime Hours: "
						+ (durationHours >= 9 ? String.format("%02d:%02d", durationHours - 9, remainingMinutes)
								: "None"));

				// Save the updated attendance record to the database
				usermasterattendancemodrepository.save(attendance);

				Map<String, Object> response = new HashMap<>();
				response.put("message", "Check-out success");
				return ResponseEntity.ok(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\": \"Failed to process attendance\"}");
		}
	}

	private String calculateStatus(long durationHours) {
		DayOfWeek day = LocalDate.now().getDayOfWeek();

		if (durationHours >= 8) {
			return "Present";
		} else if (durationHours >= 4) {
			return day == DayOfWeek.SATURDAY ? "Present" : "Half-Day";
		} else {
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
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\": \"Employee ID, old password, and new password are required\"}");
			}

			Optional<usermaintenance> userOpt = usermaintenanceRepository.findByEmpIdOrUserId(employeeId);
			if (!userOpt.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"Employee not found\"}");
			}

			usermaintenance user = userOpt.get();

			// Validate old password
			if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Old password is incorrect\"}");
			}

			// Update to new password
			user.setPassword(passwordEncoder.encode(newPassword));
			usermaintenanceRepository.save(user);

			return ResponseEntity.ok("{\"message\": \"Password reset successfully\"}");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("{\"error\": \"Failed to reset password\"}");
		}
	}

	@GetMapping("/attendance/status/{employeeId}")
	public ResponseEntity<?> getCheckInStatus(@PathVariable String employeeId) {
		try {
			// Fetch the most recent attendance record for the employee based on SRL_NUM
			Optional<UserMasterAttendanceMod> latestRecord = usermasterattendancemodrepository
					.findLatestRecordBySrlNo(employeeId);

			Map<String, Object> response = new HashMap<>();

			if (latestRecord.isPresent()) {
				UserMasterAttendanceMod attendance = latestRecord.get();

				// Check if the employee is checked in but not checked out
				if (attendance.getCheckinstatus() != null && attendance.getCheckoutstatus() == null) {
					response.put("isCheckedIn", true);
					// response.put("checkInTime", attendance.getCheckintime());
					response.put("checkInTime", attendance.getCheckintime().toInstant().toString()); // Converts to ISO
																										// 8601 format
					System.out.println("INTIME>>>>" + attendance.getCheckintime());
					response.put("checkInLocation", attendance.getCheckinlocation());
					response.put("attendanceDate", attendance.getAttendancedate());
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
	@RequestMapping(value = { "/onboard" }, method = RequestMethod.POST)
	@ResponseBody
	public class EmployeeController {

		@Autowired
		private EmployeeProfileModRepository employeeprofilemodrepository;

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

		@PostMapping
		@Transactional
		public ResponseEntity<String> addEmployee1(@RequestBody Map<String, Object> requestData) {
			try {
				// Extract employee data from request
				EmployeeProfileMod employee = new ObjectMapper().convertValue(requestData, EmployeeProfileMod.class);

				// Check if employee ID already exists in the profile table
				Optional<EmployeeProfileMod> existingEmployee = employeeprofilemodrepository
						.findByEmpid(employee.getEmpid());
				if (existingEmployee.isPresent()) {
					String errorMessage = "Error: Employee ID " + employee.getEmpid() + " already exists.";
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"" + errorMessage + "\"}");
				}

				// Save employee profile
				employee.setRcretime(new Date());
				employee.setRmodtime(new Date());
				employee.setDelflg("N");
				employee.setEntitycreflg("N");
				employeeprofilemodrepository.save(employee);

				// Parse and save Address, Education, Professional Details, and Skills
				saveAddress(requestData.get("address"), employee);
				saveEducationDetails(requestData.get("education"), employee);
				saveProfessionalDetails(requestData.get("professional"), employee);
				saveSkills(requestData.get("skillSet"), employee);

				String successMessage = errorMessageService.getErrorMessage("ADD_EMP_SUCCESS", "en");
				return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"" + successMessage + "\"}");
			} catch (Exception e) {
				e.printStackTrace();
				String errorMessage = errorMessageService.getErrorMessage("ADD_EMP_ERROR", "en");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("{\"error\": \"" + errorMessage + " - " + e.getMessage() + "\"}");
			}
		}

		private void saveAddress(Object addressObj, EmployeeProfileMod employee) {
			if (addressObj instanceof Map) {
				EmployeeAddressMod address = new ObjectMapper().convertValue(addressObj, EmployeeAddressMod.class);
				address.setUserid(employee.getUserid());
				address.setDelflg("N");
				address.setRcreuserid(employee.getUserid().toString());
				address.setRcretime(employee.getRcretime());
				address.setRmoduserid(employee.getRmoduserid());
				address.setRmodtime(employee.getRmodtime());
				employeeAddressModRepository.save(address);
			}
		}

		private void saveEducationDetails(Object educationDetailsObj, EmployeeProfileMod employee) {
			if (educationDetailsObj instanceof List) {
				List<?> educationDetails = (List<?>) educationDetailsObj;
				for (Object eduDetailObj : educationDetails) {
					if (eduDetailObj instanceof Map) {
						EmployeeEducationDetailsMod edu = new ObjectMapper().convertValue(eduDetailObj,
								EmployeeEducationDetailsMod.class);
						Long maxSrlNo = employeeEducationDetailsModRepository.findMaxSerialNumber(employee.getUserid());
						edu.setSrlnum((maxSrlNo == null) ? 1L : maxSrlNo + 1);
						edu.setUserid(employee.getUserid());
						edu.setDelflg("N");
						edu.setRcreuserid(employee.getUserid().toString());
						edu.setRcretime(employee.getRcretime());
						edu.setRmoduserid(employee.getRmoduserid());
						edu.setRmodtime(employee.getRmodtime());
						employeeEducationDetailsModRepository.save(edu);
					}
				}
			}
		}

		private void saveProfessionalDetails(Object professionalDetailsObj, EmployeeProfileMod employee) {
			if (professionalDetailsObj instanceof List) {
				List<?> professionalDetails = (List<?>) professionalDetailsObj;
				for (Object profDetailObj : professionalDetails) {
					if (profDetailObj instanceof Map) {
						EmployeeProfessionalDetailsMod prof = new ObjectMapper().convertValue(profDetailObj,
								EmployeeProfessionalDetailsMod.class);
						Long maxSrlNo = employeeProfessionalDetailsModRepository
								.findProfMaxSerialNumber(employee.getUserid());
						prof.setSrlnum((maxSrlNo == null) ? 1L : maxSrlNo + 1);
						prof.setUserid(employee.getUserid().toString());
						prof.setDelflg("N");
						prof.setRcreuserid(employee.getUserid().toString());
						prof.setRcretime(employee.getRcretime());
						prof.setRmoduserid(employee.getRmoduserid());
						prof.setRmodtime(employee.getRmodtime());
						prof.setOfferletter("SUBMITTED");
						employeeProfessionalDetailsModRepository.save(prof);
					}
				}
			}
		}

		private void saveSkills(Object skillsObj, EmployeeProfileMod employee) {
			if (skillsObj instanceof List) {
				List<?> skills = (List<?>) skillsObj;
				for (Object skillObj : skills) {
					if (skillObj instanceof Map) {
						EmployeeSkillMod skillMod = new ObjectMapper().convertValue(skillObj, EmployeeSkillMod.class);
						Long maxSrlNo = employeeSkillModRepository.findSkillMaxSerialNumber(employee.getUserid());
						skillMod.setSrlnum((maxSrlNo == null) ? 1L : maxSrlNo + 1);
						skillMod.setUserid(employee.getUserid());
						skillMod.setDelflg("N");
						skillMod.setRcreuserid(employee.getUserid().toString());
						skillMod.setRcretime(employee.getRcretime());
						skillMod.setRmoduserid(employee.getRmoduserid());
						skillMod.setRmodtime(employee.getRmodtime());
						skillMod.setYearsofexp("3");
						skillMod.setLastupdated("11.12.2022");
						employeeSkillModRepository.save(skillMod);
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

	@GetMapping(value = { "/employees/{empid}" })
	public ResponseEntity<?> fetchEmployeeById(@PathVariable String empid) {
		System.out.println("fetchEmployeeById : " + empid);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, String> response = new HashMap<>();
		try {
			// Fetch the employee profile using the provided empid
			Optional<EmployeeProfileMod> employeeProfileModOptional = employeeProfilemodRepository.findByEmpid(empid);

			if (!employeeProfileModOptional.isPresent()) {
				// If employee not found, return 404 with error message
				response.put("message", "FETCH_EMP_NOTFOUND");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			// Extract the employee profile details
			EmployeeProfileMod employeeProfile = employeeProfileModOptional.get();

			// Validate the userId from the EmployeeProfileMod entity
			Long userid = employeeProfile.getUserid();
			if (userid == null) {
				response.put("message", "User ID is null for employee with empid: " + empid);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			// Fetch employee address
			Optional<EmployeeAddressMod> employeeAddressModOptional = employeeAddressModRepository.findByUserid(userid);

			// Fetch education details
			List<EmployeeEducationDetailsMod> educationDetails = employeeEducationDetailsModRepository
					.findByUserid(userid);

			// Fetch professional details
			List<EmployeeProfessionalDetailsMod> professionalDetails = employeeProfessionalDetailsModRepository
					.findByUserid(String.valueOf(userid));

			// Fetch skill details
			List<EmployeeSkillMod> skillDetails = employeeSkillModRepository.findByUserid(userid);

			// Prepare the response map
			Map<String, Object> response1 = new HashMap<>();
			response.put("empid", employeeProfile.getEmpid());
			response.put("firstName", employeeProfile.getFirstname());
			response.put("lastName", employeeProfile.getLastname());
			response.put("emailid", employeeProfile.getEmailid());
			response.put("mobileNumber", employeeProfile.getMobilenumber());
			response.put("bloodgroup", employeeProfile.getBloodgroup());
			response.put("parentmobnum", employeeProfile.getParentmobnum());
			response.put("parentname", employeeProfile.getParentname());
			response.put("pannumber", employeeProfile.getPannumber());
			response.put("uannumber", employeeProfile.getUannumber());
			response.put("aadhaarnumber", employeeProfile.getAadhaarnumber());
			response.put("officialemail", employeeProfile.getOfficialemail());
			String Dateofbirth = dateFormat.format(employeeProfile.getDateofbirth());
			response.put("dateofbirth", Dateofbirth);

			// Add address details if available
			employeeAddressModOptional.ifPresent(address -> {
				response.put("presentaddressline1", address.getPresentaddressline1());
				response.put("presentaddressline2", address.getPresentaddressline2());
				response.put("presentcity", address.getPresentcity());
				response.put("presentstate", address.getPresentstate());
				response.put("presentpostalcode", address.getPresentpostalcode());
				response.put("presentcountry", address.getPresentcountry());
				response.put("permanentaddressline1", address.getPermanentaddressline1());
				response.put("permanentaddressline2", address.getPermanentaddressline2());
				response.put("permanentcity", address.getPermanentcity());
				response.put("permanentstate", address.getPermanentstate());
				response.put("permanentpostalcode", address.getPermanentpostalcode());
				response.put("permanentcountry", address.getPermanentcountry());
			});

			// Add education details if available
			if (!educationDetails.isEmpty()) {
				List<Map<String, Object>> educationList = new ArrayList<>();
				for (EmployeeEducationDetailsMod edu : educationDetails) {
					Map<String, Object> eduMap = new HashMap<>();
					eduMap.put("qualification", edu.getQualification());
					eduMap.put("institution", edu.getInstitution());
					eduMap.put("regnum", edu.getRegnum());
					eduMap.put("yearofgraduation", edu.getYearofgraduation());
					eduMap.put("percentage", edu.getPercentage());
					eduMap.put("duration", edu.getDuration());
					eduMap.put("fieldofstudy", edu.getFieldofstudy());
					eduMap.put("additionalnotes", edu.getAdditionalnotes());
					educationList.add(eduMap);
				}
				response1.put("education", educationList);
			}

			// Add professional details if available
			if (!professionalDetails.isEmpty()) {
				List<Map<String, Object>> professionalDetailsList = new ArrayList<>();
				for (EmployeeProfessionalDetailsMod professionalDetails1 : professionalDetails) {
					Map<String, Object> professionalMap = new HashMap<>();
					professionalMap.put("organisation", professionalDetails1.getOrganisation());
					professionalMap.put("location", professionalDetails1.getLocation());
					professionalMap.put("orgempid", professionalDetails1.getOrgempid());
					professionalMap.put("orgdept", professionalDetails1.getOrgdept());
					professionalMap.put("orgrole", professionalDetails1.getOrgrole());
					System.out.println("bRIT" + professionalDetails1.getJoiningdate());
					String joindate = dateFormat.format(professionalDetails1.getJoiningdate());
					System.out.println("vasanth" + joindate);
					professionalMap.put("joiningdate", joindate);
					String Relievingdate = dateFormat.format(professionalDetails1.getRelievingdate());
					professionalMap.put("relievingdate", Relievingdate);
					professionalMap.put("ctc", professionalDetails1.getCtc());
					professionalMap.put("additionalinformation", professionalDetails1.getAdditionalinformation());
					professionalDetailsList.add(professionalMap);
				}

				response1.put("professional", professionalDetailsList);
			}

			// Add skill details if available
			if (!skillDetails.isEmpty()) {
				List<Map<String, Object>> skillList = new ArrayList<>();
				for (EmployeeSkillMod skill : skillDetails) {
					Map<String, Object> skillMap = new HashMap<>();
					skillMap.put("skill", skill.getSkill());
					skillMap.put("proficiencylevel", skill.getProficiencylevel());
					skillMap.put("certification", skill.getCertification());
					skillMap.put("yearsExperience", skill.getYearsofexp());

					skillMap.put("lastupdated", skill.getLastupdated());
					skillList.add(skillMap);
				}
				response1.put("skillSet", skillList);
			}

			// Return the employee details as a successful response
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			// Log the exception
			System.out.println("fetchEmployeeById : ERR - " + e.getMessage());
			e.printStackTrace();

			// Prepare error message and return as response
			response.put("message", "ERR_NO_RECORD_FOUND");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
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

		for (usermaintenance emp : reportingEmployees) {
			empIds.add(emp.getEmpid());
		}

		return empIds;
	}

	// leave
	@GetMapping("/leaveRequests/get/{empId}")
	public ResponseEntity<Map<String, Object>> getLeaveRequestsByEmpId(@PathVariable String empId) {
		try {
			// Step 1: Find all employees who report to this empId
			List<String> empIds = getDirectReports(empId);

			// Print the final list of employee IDs
			System.out.println("Fetching leave records for Employee IDs: " + empIds);

			// Fetch records from both Master and Mod tables where entitycreflg = 'N' or 'Y'
			List<EmployeeLeaveMasterTbl> masterLeaveRequests = employeeLeaveMasterRepository
					.findByEmpidInAndEntitycreflgIn(empIds, Arrays.asList("N", "Y"));
			List<EmployeeLeaveModTbl> modLeaveRequests = employeeLeaveModRepository.findByEmpidIn(empIds);

			// Combine results into a single list of maps
			List<Map<String, Object>> combinedLeaveRequests = new ArrayList<>();

			// Convert Master records to map format
			masterLeaveRequests.forEach(request -> {
				Map<String, Object> leaveData = new HashMap<>();
				leaveData.put("srlnum", request.getSrlnum());
				leaveData.put("empid", request.getEmpid());
				leaveData.put("leavetype", request.getLeavetype());
				leaveData.put("startdate", request.getStartdate());
				leaveData.put("enddate", request.getEnddate());
				leaveData.put("teamemail", request.getTeamemail());
				leaveData.put("leavereason", request.getLeavereason());
				leaveData.put("status", request.getStatus());
				leaveData.put("noofdays", request.getNoofdays());
				leaveData.put("entitycreflg", request.getEntitycreflg());
				leaveData.put("delflg", request.getDelflg());
				leaveData.put("rcreuserid", request.getRcreuserid());
				leaveData.put("rcretime", request.getRcretime());
				leaveData.put("rmoduserid", request.getRmoduserid());
				leaveData.put("rmodtime", request.getRmodtime());
				leaveData.put("rvfyuserid", request.getRvfyuserid());
				leaveData.put("rvfytime", request.getRvfytime());
				leaveData.put("noofbooked", request.getNoofbooked());
				combinedLeaveRequests.add(leaveData);
			});

			// Convert Mod records to map format
			modLeaveRequests.forEach(request -> {
				Map<String, Object> leaveData = new HashMap<>();
				leaveData.put("srlnum", request.getSrlnum());
				leaveData.put("empid", request.getEmpid());
				leaveData.put("leavetype", request.getLeavetype());
				leaveData.put("startdate", request.getStartdate());
				leaveData.put("enddate", request.getEnddate());
				leaveData.put("teamemail", request.getTeamEmail());
				leaveData.put("leavereason", request.getLeavereason());
				leaveData.put("status", request.getStatus());
				leaveData.put("noofdays", request.getNoofdays());
				leaveData.put("entitycreflg", request.getEntitycreflg());
				leaveData.put("delflg", request.getDelflg());
				leaveData.put("rcreuserid", request.getRcreuserid());
				leaveData.put("rcretime", request.getRcretime());
				leaveData.put("rmoduserid", request.getRmoduserid());
				leaveData.put("rmodtime", request.getRmodtime());
				leaveData.put("rvfyuserid", request.getRvfyuserid());
				leaveData.put("rvfytime", request.getRvfytime());
				leaveData.put("noofbooked", request.getNoofbooked());
				combinedLeaveRequests.add(leaveData);
			});

			// Prepare the response
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
			@RequestParam(name = "leavereason", required = false) String leavereason) {
		try {
			System.out.println("Approved" + leavereason);
			// Retrieve entity by empid and leaveType
			EmployeeLeaveMasterTbl entity = employeeLeaveMasterRepository.findByEmpidAndLeavereason(empid, leavereason);
			// updateConsolidatedLeave(employeeLeaveMasterTbl);
			if (entity == null) {
				Map<String, String> errorResponse = new HashMap<>();
				errorResponse.put("status", "failure");
				errorResponse.put("message", "Employee not found");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
			}
			updateConsolidatedLeave(entity);

			// Update the flag and status
			entity.setEntitycreflg("Y");
			entity.setStatus("Approved");
			employeeLeaveMasterRepository.save(entity);

			// Copy entity to the mod table
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

			// Delete record from the master table
			employeeLeaveMasterRepository.delete(entity);

			// --- Trigger email to employee notifying approval ---
			try {
				// Retrieve the employee details
				usermaintenance existingEmployee = usermaintenanceRepository.findByEmpIdOrUserId(empid)
						.orElseThrow(() -> new RuntimeException("Employee not found"));
				String employeeEmail = existingEmployee.getEmailid();

				// Retrieve manager details using the employee's manager id (assumed to be
				// stored in repoteTo)
				String managerId = existingEmployee.getRepoteTo();
				usermaintenance manager = usermaintenanceRepository.findByEmpIdOrUserId(managerId)
						.orElseThrow(() -> new RuntimeException("Manager not found"));

				if (employeeEmail != null && !employeeEmail.isEmpty()) {
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
					String subject = "Leave Request Approved";
					String body = String.format(
							"Dear %s,\n\nYour leave request for %s from %s to %s has been approved by your manager, %s.\n\nRegards,\n"
									+ manager.getFirstname() + ",\nWhitestone Software Solution Pvt Ltd",
							existingEmployee.getFirstname(), entity.getLeavetype(), sdf.format(entity.getStartdate()),
							sdf.format(entity.getEnddate()), manager.getFirstname());
					// Replace "noreply@company.com" with your sender email as needed.
					emailService.sendLeaveEmail(manager.getEmailid(), employeeEmail, subject, body);
				}
			} catch (Exception e) {
				// Log the error but continue with the response
				e.printStackTrace();
			}

			// Prepare and return success response
			Map<String, Object> successResponse = new HashMap<>();
			successResponse.put("status", "success");
			successResponse.put("message",
					"Entity flag updated, record copied to mod table, and deleted from master table");
			successResponse.put("empid", empid);
			return ResponseEntity.ok(successResponse);

		} catch (Exception e) {
			// Handle and return error response
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("status", "error");
			errorResponse.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
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
	public ResponseEntity<Map<String, Long>> getLeaveCounts(@RequestParam("empId") String empId) {
		// Fetching counts for each leave type
		Long sickLeaveCount = leaveRepository.countByLeavetypeAndEmpid("medical leave", empId);
		Long casualLeaveCount = leaveRepository.countByLeavetypeAndEmpid("casual leave", empId);
		Long earnedLeaveCount = leaveRepository.countByLeavetypeAndEmpid("earned leave", empId);
		Long leaveWithoutPayCount = leaveRepository.countByLeavetypeAndEmpid("leavewithoutpay", empId);
		Long sabbaticalLeaveCount = leaveRepository.countByLeavetypeAndEmpid("sabbatical leave", empId);

		// Preparing response as a Map
		Map<String, Long> leaveCounts = new HashMap<>();
		leaveCounts.put("medicalleave", sickLeaveCount);
		leaveCounts.put("casualleave", casualLeaveCount);
		leaveCounts.put("earnedleave", earnedLeaveCount);
		leaveCounts.put("leavewithoutpay", leaveWithoutPayCount);
		leaveCounts.put("sabbaticalleave", sabbaticalLeaveCount);

		return ResponseEntity.ok(leaveCounts);
	}

	// Controller
	@PostMapping("/leaveRequest")
	public ResponseEntity<?> leaveRequest(@RequestBody EmployeeLeaveMasterTbl employeeLeaveMasterTbl) {

		try {
			// Convert Timestamp to LocalDate (removes time)
			LocalDate onlyDate = employeeLeaveMasterTbl.getStartdate().toLocalDateTime().toLocalDate();

			// Convert LocalDate back to Timestamp (time set to 00:00:00)
			Timestamp dateOnlyTimestamp = Timestamp.valueOf(onlyDate.atStartOfDay());

			boolean existingLeave = employeeLeaveMasterRepository
					.countByEmpidAndStartDate(employeeLeaveMasterTbl.getEmpid(), dateOnlyTimestamp) > 0;

			System.out.println("existingLeave>>>>>>" + existingLeave + ">>>" + employeeLeaveMasterTbl.getEmpid()
					+ ">>>>" + employeeLeaveMasterTbl.getStartdate());

			if (existingLeave) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("{\"error\": \"Leave request already exists for the selected date.\"}");
			}
			// Save the leave request
			employeeLeaveMasterTbl.setNoofbooked(1.0f);
			employeeLeaveMasterTbl.setDelflg("N");
			employeeLeaveMasterTbl.setEntitycreflg("N");
			employeeLeaveMasterTbl.setStatus("Pending");
			EmployeeLeaveMasterTbl savedRequest = employeeLeaveMasterRepository.save(employeeLeaveMasterTbl);
			System.out.println("employeeLeaveMasterTbl>>>>>>>>>>>>>>" + employeeLeaveMasterTbl.getEmpid());
			// Prepare response
			Map<String, Object> response = new HashMap<>();
			response.put("message", "Leave Request sent successfully");
			response.put("data", savedRequest);
			// updateConsolidatedLeave(employeeLeaveMasterTbl);
			// Retrieve manager's email
			try {
				usermaintenance existingEmployee = usermaintenanceRepository
						.findByEmpIdOrUserId(employeeLeaveMasterTbl.getEmpid())
						.orElseThrow(() -> new RuntimeException("Employee not found"));
				String managerId = existingEmployee.getRepoteTo();
				System.out.println("managerId" + managerId);
				if (managerId == null) {
					throw new RuntimeException("Manager not assigned to this employee");
				}

				usermaintenance manager = usermaintenanceRepository.findByEmpIdOrUserId(managerId)
						.orElseThrow(() -> new RuntimeException("Manager not found"));

				String managerEmail = manager.getEmailid();
				if (managerEmail != null && !managerEmail.isEmpty()) {
					System.out.println("managerEmail" + managerEmail);
					// Send email notification to manager
					UserRoleMaintenance role = userRoleMaintenanceRepository.findByRoleid(existingEmployee.getRoleid())
							.orElseThrow(() -> new RuntimeException("role not found"));
					String subject = "Leave Request Approval Needed for " + existingEmployee.getFirstname();
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

					// Format the start and end dates
					String startDateFormatted = sdf.format(employeeLeaveMasterTbl.getStartdate());
					String endDateFormatted = sdf.format(employeeLeaveMasterTbl.getEnddate());
					String body = String.format("Dear %s,\n\n"
							+ "Employee %s (%s) has submitted a leave request. Please find the details below:\n\n"
							+ "Leave Type: %s\n" + "From Date: %s\n" + "To Date: %s\n" + "No. of Days: %s\n"
							+ "Reason: %s\n\n" + "Kindly review the request and take necessary action.\n\n"
							+ "Regards,\n" + "%s,\n" + "%s - %s,\n" + "Whitestone Software Solution Pvt Ltd.\n",

							manager.getFirstname(), existingEmployee.getFirstname(), existingEmployee.getEmpid(),
							employeeLeaveMasterTbl.getLeavetype(), startDateFormatted, endDateFormatted,
							employeeLeaveMasterTbl.getNoofdays(), employeeLeaveMasterTbl.getLeavereason(),
							existingEmployee.getFirstname(), role.getRolename(), role.getDescription());

					emailService.sendLeaveEmail(existingEmployee.getEmailid(), managerEmail, subject, body);
					response.put("emailStatus", "Email sent to manager: " + managerEmail);
				} else {
					response.put("emailStatus", "Manager email not found");
				}

			} catch (Exception e) {
				e.printStackTrace();
				response.put("emailError", "Failed to send email: " + e.getMessage());
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
			String uploadDir = "D:\\Sowmiya";
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

		try {
			// Fetch the existing expense record by ID
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

			String subjectToEmployee = "Your Expense Request Has Been Approved";
			String bodyToEmployee = "Dear " + employee.getFirstname() + ",\n\n" + "Your expense request (ID: "
					+ expenseId + ") has been approved by the admin.\n\n" + "Regards,\nHRMS System";

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
		String Reason = (String) requestBody.get("reason");
		try {
			// Fetch the existing advance record by ID
			Optional<AdvancesDetailsMod> existingAdvanceOpt = advancesDetailsModRepository.findById(advanceId);
			if (!existingAdvanceOpt.isPresent()) {
				response.put("success", false);
				response.put("message", "Advance not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			// Update the relevant fields
			AdvancesDetailsMod existingAdvance = existingAdvanceOpt.get();
			existingAdvance.setEntityCreFlg("Y");
			existingAdvance.setPaymentStatus(0);
			existingAdvance.setRejectreason(Reason);
			existingAdvance.setRmodTime(new Date());
			String approver = existingAdvance.getApprover();

			usermaintenance currentApprover = usermaintenanceRepository.findByEmpid(approver);

			if (!currentApprover.equals("10029")) {

				if (currentApprover != null && currentApprover.getRepoteTo() != null) {
					usermaintenance nextApprover = usermaintenanceRepository.findByEmpid(currentApprover.getRepoteTo());

					if (nextApprover != null && nextApprover.getRoleid() != null) {
						Optional<UserRoleMaintenance> roleOpt = userRoleMaintenanceRepository
								.findByRoleid(nextApprover.getRoleid());

						if (roleOpt.isPresent()) {
							existingAdvance.setApprover(nextApprover.getEmpid());
							existingAdvance.setStatus("Pending for " + roleOpt.get().getRolename() + " approval");
						} else {
							// Role not found
							existingAdvance.setStatus("Payment to be Initiate");
							existingAdvance.setApprover("10029");
						}
					} else {
						// nextApprover is null
						existingAdvance.setStatus("Payment to be Initiate");
						existingAdvance.setApprover("10029");
					}
				} else {
					existingAdvance.setStatus("Payment to be Initiate");
					existingAdvance.setApprover("10029");
				}
			} else {
				existingAdvance.setStatus("Approved");
				existingAdvance.setApprover("10029");
			}

			// Save the updated advance
			advancesDetailsModRepository.save(existingAdvance);

			// Get employee info related to the advance
			String empId = existingAdvance.getEmpId();
			usermaintenance employee = usermaintenanceRepository.findByEmpid(empId);
			if (employee == null) {
				response.put("success", false);
				response.put("message", "Employee not found.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}

			// Email notification to employee
			String subjectToEmployee = "Your Advance Request Has Been Approved";
			String bodyToEmployee = "Dear " + employee.getFirstname() + ",\n\n" + "Your advance request (ID: "
					+ advanceId + ") has been approved by the admin.\n\n" + "Regards,\nHRMS System";

			usermaintenance manager = usermaintenanceRepository.findByEmpid(employee.getRepoteTo());

			if (employee.getEmailid() != null) {
				emailService.sendLeaveEmail(manager.getEmailid(), employee.getEmailid(), subjectToEmployee,
						bodyToEmployee);
			} else {
				System.out.println("No email found for employee: " + employee.getEmpid());
			}

			// Success response
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
		try {
			String uploadDir = "C:\\WSSL08\\dummy_file_save";
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

	@GetMapping("/payroll/employee/{empid}")
	public ResponseEntity<EmployeeProfile> getEmployeeDetails(@PathVariable String empid) {
		Optional<EmployeeProfile> employee = employeeProfileRepository.findByEmpId(empid);

		if (employee.isPresent()) {
			EmployeeProfile empProfile = employee.get();

			// Convert Date to LocalDate using a specific timezone
			if (empProfile.getDateofbirth() != null) {
				Date dobDate = empProfile.getDateofbirth();
				LocalDate localDob = dobDate.toInstant().atZone(ZoneId.of("Asia/Kolkata")) // Use the correct timezone
						.toLocalDate();

				System.out.println("Formatted DOB to send to frontend: " + localDob);

				// Convert LocalDate back to java.util.Date
				Date convertedDate = Date.from(localDob.atStartOfDay(ZoneId.of("Asia/Kolkata")).toInstant());

				// Set the converted date back to the EmployeeProfile
				empProfile.setDateofbirth(convertedDate);
			}

			// Log the JSON response
			ObjectMapper mapper = new ObjectMapper();
			try {
				String jsonResponse = mapper.writeValueAsString(empProfile);
				System.out.println("JSON Response: " + jsonResponse);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			return ResponseEntity.ok(empProfile);
		} else {
			return ResponseEntity.status(404).body(null); // Return 404 if employee not found
		}
	}

	@GetMapping("/payroll/employees")
	public List<Map<String, String>> getAllEmployeeIds() {
		return usermaintenanceRepository.findAll().stream().map(emp -> {
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

	@PostMapping("/Payroll")
	public ResponseEntity<Map<String, Object>> savePayroll() {
		YearMonth currentMonth = YearMonth.now();
		YearMonth previousMonth = currentMonth.minusMonths(1);
		LocalDate lastDayOfMonth = currentMonth.atEndOfMonth();

		List<String> employeeIds = usermaintenanceRepository.findAll().stream().map(usermaintenance::getEmpid)
				.collect(Collectors.toList());

		ObjectMapper objectMapper = new ObjectMapper();
		List<Payroll> payrollList = new ArrayList<>();
		Map<String, Object> response = new HashMap<>();

		// Step 1: Move Previous Month's Payroll to PayrollHistory
		List<Payroll> previousPayrolls = payrollRepository.findByMonth(previousMonth.toString());
		if (!previousPayrolls.isEmpty()) {
			List<PayrollHistory> historyRecords = previousPayrolls.stream().map(p -> {
				PayrollHistory history = new PayrollHistory();
				history.setEmpid(p.getEmpid());
				history.setAmount(p.getAmount());
				history.setBeneAccNo(p.getBeneAccNo());
				history.setPymtDate(p.getPymtDate());
				history.setBeneIfsc(p.getBeneIfsc());
				history.setBnfName(p.getBnfName());
				history.setCreditNarr(p.getCreditNarr());
				history.setDebitAccNo(p.getDebitAccNo());
				history.setDebitNarr(p.getDebitNarr());
				history.setEmailId(p.getEmailId());
				history.setMobileNum(p.getMobileNum());
				history.setMonth(p.getMonth());
				history.setPymtMode(p.getPymtMode());
				history.setPymtProdTypeCode(p.getPymtProdTypeCode());
				history.setRefNo(p.getRefNo());
				history.setRemark(p.getRemark());
				history.setStatus(p.getStatus());
				return history;
			}).collect(Collectors.toList());

			payrollHistoryRepository.saveAllAndFlush(historyRecords);
			payrollRepository.deleteByMonth(previousMonth.toString());
		}

		// Step 2: Process Payroll for Current Month
		for (String empId : employeeIds) {
			try {
				System.out.println("CHECK:::" + empId + " MONTH::::" + currentMonth.toString());
				boolean payrollExists = payrollRepository.existsByEmpidAndMonth(empId, currentMonth.toString());
				if (payrollExists) {
					response.put(empId, "Payroll already processed for " + currentMonth);
					continue;
				}

				EmployeeSalaryTbl employeeSalary = employeeSalaryTblRepository.findByEmpid(empId);
				if (employeeSalary == null) {
					response.put(empId, "No salary details found.");
					continue;
				}

				// LOP Fetch Logic
				EmployeeLeaveSummary leaveSummary = employeeLeaveSummaryRepository.findByEmpId(empId);
				Float lopDays = 0.0f;
				if (leaveSummary != null) {
					Month monthEnum = currentMonth.getMonth();
					switch (monthEnum) {
					case JANUARY:
						lopDays = leaveSummary.getLopJan();
						break;
					case FEBRUARY:
						lopDays = leaveSummary.getLopFeb();
						break;
					case MARCH:
						lopDays = leaveSummary.getLopMar();
						break;
					case APRIL:
						lopDays = leaveSummary.getLopApr();
						break;
					case MAY:
						lopDays = leaveSummary.getLopMay();
						break;
					case JUNE:
						lopDays = leaveSummary.getLopJun();
						break;
					case JULY:
						lopDays = leaveSummary.getLopJul();
						break;
					case AUGUST:
						lopDays = leaveSummary.getLopAug();
						break;
					case SEPTEMBER:
						lopDays = leaveSummary.getLopSep();
						break;
					case OCTOBER:
						lopDays = leaveSummary.getLopOct();
						break;
					case NOVEMBER:
						lopDays = leaveSummary.getLopNov();
						break;
					case DECEMBER:
						lopDays = leaveSummary.getLopDec();
						break;
					}
				}

				// Fix JSON formatting and parse
				String earningsJson = cleanJson(employeeSalary.getEarnings());
				String deductionsJson = cleanJson(employeeSalary.getDeductions());

				JsonNode earningsNode = objectMapper.readTree(earningsJson);
				JsonNode deductionsNode = objectMapper.readTree(deductionsJson);

				double totalEarnings = earningsNode.iterator().hasNext()
						? StreamSupport.stream(earningsNode.spliterator(), false)
								.mapToDouble(e -> e.get("monthlyAmount").asDouble(0.0)).sum()
						: 0.0;

				double totalDeductions = deductionsNode.iterator().hasNext()
						? StreamSupport.stream(deductionsNode.spliterator(), false)
								.mapToDouble(d -> d.get("monthlyAmount").asDouble(0.0)).sum()
						: 0.0;

				// LOP deduction
				double perDaySalary = totalEarnings / currentMonth.lengthOfMonth();
				double lopDeduction = perDaySalary * lopDays;
				System.out.println("Perdaysal::>>>  " + perDaySalary + "  totalEarnings::>>" + totalEarnings
						+ "currentMonth.lengthOfMonth() ::>>" + currentMonth.lengthOfMonth() + "  lopDays::>>>"
						+ lopDays);
				// Final payable amount
				double finalSalary = totalEarnings - totalDeductions - lopDeduction;

				// Create Payroll record
				Payroll payroll = new Payroll();
				payroll.setEmpid(empId);
				payroll.setAmount(finalSalary);
				payroll.setBeneAccNo(employeeSalary.getAccountNumber());
				payroll.setPymtDate(lastDayOfMonth);
				payroll.setBeneIfsc(employeeSalary.getIfscCode());
				payroll.setBnfName(employeeSalary.getFirstname());
				payroll.setCreditNarr("NA");
				payroll.setDebitAccNo("611905056804");
				payroll.setDebitNarr("NA");
				payroll.setEmailId(employeeSalary.getEmailid());
				payroll.setMobileNum(employeeSalary.getMobilenumber());
				payroll.setMonth(currentMonth.toString());
				payroll.setPymtMode(employeeSalary.getBankName().contains("ICIC") ? "FT" : "NEFT");
				payroll.setPymtProdTypeCode("PAB_VENDOR");
				payroll.setRefNo("");
				payroll.setRemark(currentMonth.getMonth().name() + " " + currentMonth.getYear() + " Salary");
				payroll.setStatus("PENDING");

				payrollList.add(payroll);
			} catch (Exception e) {
				response.put(empId, "Error processing payroll: " + e.getMessage());
				e.printStackTrace();
			}
		}

		// Step 3: Save New Payroll Records
		if (!payrollList.isEmpty()) {
			payrollRepository.saveAllAndFlush(payrollList);
			response.put("status", "Payroll saved successfully!");
			response.put("processed_count", payrollList.size());
			System.out.println(" payrollList.size()>>>" + payrollList.size());
		} else {
			response.put("status", "No new payroll records were created.");
		}

		return ResponseEntity.ok(response);
	}

	// **Fix JSON Formatting Issues**
	private String cleanJson(String json) {
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
					.setCellValue(payroll.getAmount() != null ? String.valueOf(Math.round(payroll.getAmount())) : "NA");
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
			usermaintenance existingEmployee = usermaintenanceRepository.findByEmpIdOrUserId(empId)
					.orElseThrow(() -> new RuntimeException("Employee not found"));

			String managerId = existingEmployee.getRepoteTo();
			System.out.println("Manager ID: " + managerId);

			if (managerId == null) {
				throw new RuntimeException("Manager not assigned to this employee");
			}

			usermaintenance manager = usermaintenanceRepository.findByEmpIdOrUserId(managerId)
					.orElseThrow(() -> new RuntimeException("Manager not found"));

			Map<String, String> response = new HashMap<>();
			response.put("email", manager.getEmailid()); // Wrap response in JSON

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

	@PostMapping("/usermaintenance-save")
	public ResponseEntity<usermaintenance> saveUser(@RequestBody usermaintenance user) {
		LocalDateTime now = LocalDateTime.now();
		Date nowDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
		String rawPassword = user.getPassword();

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
		usermaintenance savedUser = usermaintenanceRepository.save(user);
		return ResponseEntity.ok(savedUser);
	}
	
	@GetMapping("/data")
	public ResponseEntity<?> getTimesheetData(@RequestParam int year, @RequestParam int month, @RequestParam String repoteTo) {
	    if (year < 2000 || month < 1 || month > 12 || repoteTo == null || repoteTo.isEmpty()) {
	        Map<String, String> error = new HashMap<>();
	        error.put("error", "Invalid year, month, or repoteTo");
	        return ResponseEntity.badRequest().body(error);
	    }

	    List<Map<String, Object>> timesheetData = new ArrayList<>();
	    List<usermaintenance> employees = usermaintenanceRepository.findByRepoteTo(repoteTo);
	    if (employees.isEmpty()) {
	        Map<String, String> error = new HashMap<>();
	        error.put("error", "No employees found reporting to " + repoteTo);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	    }

	    int sno = 1;
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(year, month - 1, 27); // Start on 27th
	    Date startDate = calendar.getTime();
	    calendar.add(Calendar.MONTH, 1); // Next month
	    calendar.set(Calendar.DAY_OF_MONTH, 26); // End on 26th
	    Date endDate = calendar.getTime();

	    int effectiveWorkingDays = calculateEffectiveWorkingDays(year, month);

	    for (usermaintenance employee : employees) {
	        Map<String, Object> data = new HashMap<>();
	        data.put("sno", sno++);
	        data.put("employeeId", employee.getEmpid());
	        data.put("members", employee.getFirstname() + " " + employee.getLastname());
	        data.put("effectiveWorkingDays", effectiveWorkingDays);

	        // Get all attendance records for employee in date range
	        List<UserMasterAttendanceMod> attendanceRecords =
	                usermasterattendancemodrepository.findByAttendanceidAndDateRange(employee.getEmpid(), startDate, endDate);

	        // Map attendance by date for fast lookup
	        Map<LocalDate, String> attendanceMap = new HashMap<>();
	        for (UserMasterAttendanceMod record : attendanceRecords) {
	            attendanceMap.put(record.getAttendancedate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
	                              record.getStatus());
	        }

	        int present = 0, absent = 0, missPunch = 0;

	        // Loop through each date in range
	        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
	            if (attendanceMap.containsKey(date)) {
	                String status = attendanceMap.get(date);
	                if ("Present".equalsIgnoreCase(status)) {
	                    present++;
	                } else if ("MissPunch".equalsIgnoreCase(status)) {
	                    missPunch++;
	                } else {
	                    absent++;
	                }
	            } else {
	                // No record — treat as absent
	                absent++;
	            }
	        }

	        // Handle prorated LOP for July 27–31
	        if (month == 7) {
	            EmployeeLeaveSummary leaveSummary = employeeLeaveSummaryRepository.findByEmpId_AndYear(employee.getEmpid(), year);
	            if (leaveSummary != null) {
	                int lopDays = (int) Math.round(leaveSummary.getLopJul().intValue() * (5.0 / 31.0));
	                absent += lopDays;
	            }
	        }

	        data.put("present", present);
	        data.put("absent", absent);
	        data.put("missPunch", missPunch);
	        timesheetData.add(data);
	    }

	    return ResponseEntity.ok(timesheetData);
	}



	@GetMapping("/events/{employeeId}")
	public ResponseEntity<?> getAttendanceEvents(@PathVariable String employeeId,
	                                             @RequestParam(required = false) Integer year,
	                                             @RequestParam(required = false) Integer month) {
	    // Use previous month if year/month not provided
	    Calendar today = Calendar.getInstance();
	    if (year == null || month == null) {
	        today.add(Calendar.MONTH, -1);
	        year = today.get(Calendar.YEAR);
	        month = today.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-based
	    }

	    // Validate inputs
	    if (year < 2000 || month < 1 || month > 12 || employeeId == null || employeeId.isEmpty()) {
	        Map<String, String> error = new HashMap<>();
	        error.put("error", "Invalid year, month, or employeeId");
	        return ResponseEntity.badRequest().body(error);
	    }

	    usermaintenance employee = usermaintenanceRepository.findByEmpid(employeeId);
	    if (employee == null) {
	        Map<String, String> error = new HashMap<>();
	        error.put("error", "Employee not found");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	    }

	    // 27th of previous month
	    Calendar startCal = Calendar.getInstance();
	    startCal.set(year, month - 1, 27);
	    Date startDate = startCal.getTime();

	    // 26th of current month
	    Calendar endCal = (Calendar) startCal.clone();
	    endCal.add(Calendar.MONTH, 1);
	    endCal.set(Calendar.DAY_OF_MONTH, 26);
	    Date endDate = endCal.getTime();

	    // Week off calculation
	    List<Date> weekOffDays = calculateWeekOffsForPreviousAndCurrentMonth(year, month);
	    System.out.println("weekOffDays::::"+weekOffDays);
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");

	    // Map for fast lookup
	    Set<String> weekOffSet = weekOffDays.stream()
	            .map(dateFormat::format)
	            .collect(Collectors.toSet());

	    // Fetch existing attendance
	    List<UserMasterAttendanceMod> attendanceRecords =
	            usermasterattendancemodrepository.findByAttendanceidAndDateRange(employeeId, startDate, endDate);

	    // Map attendance by date string
	    Map<String, UserMasterAttendanceMod> attendanceMap = new HashMap<>();
	    for (UserMasterAttendanceMod record : attendanceRecords) {
	        attendanceMap.put(dateFormat.format(record.getAttendancedate()), record);
	    }

	    List<Map<String, Object>> events = new ArrayList<>();

	    // Loop through each date between 27th - 26th
	    Calendar loopCal = (Calendar) startCal.clone();
	    while (!loopCal.after(endCal)) {
	        Date currentDate = loopCal.getTime();
	        String dateStr = dateFormat.format(currentDate);
	        String dayOfWeek = dayFormat.format(currentDate);

	        Map<String, Object> event = new HashMap<>();
	        Map<String, String> extendedProps = new HashMap<>();
	        extendedProps.put("dayOfWeek", dayOfWeek);

	        if (weekOffSet.contains(dateStr)) {
	            event.put("title", "Week Off");
	            event.put("backgroundColor", "#ffc107");
	            extendedProps.put("status", "Week Off");
	        } else if (attendanceMap.containsKey(dateStr)) {
	            UserMasterAttendanceMod record = attendanceMap.get(dateStr);
	            String status = record.getStatus();
	            status = (status == null || status.trim().isEmpty()) ? "Absent" : status;
	            event.put("title", status);
	            extendedProps.put("status", status);
	            event.put("backgroundColor",
	                    status.equalsIgnoreCase("Present") ? "#28a745" :
	                    status.equalsIgnoreCase("Absent") ? "#dc3545" :
	                    status.equalsIgnoreCase("Miss Punch") ? "#00FFFF" : "#ffc107"
	            );
	        } else {
	            // No record and not week off = Absent
	            event.put("title", "Miss Punch");
	            extendedProps.put("status", "Miss Punch");
	            event.put("backgroundColor", "#00FFFF");
	        }

	        event.put("date", dateStr);
	        event.put("extendedProps", extendedProps);
	        events.add(event);
	        loopCal.add(Calendar.DAY_OF_MONTH, 1);
	    }

	    return ResponseEntity.ok(events);
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
	private List<Date> getWeekOffsInRange(Date startDate, Date endDate) {
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
        prevMonth.set(year, month - 1, 1);  // Set to current month
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
System.out.println("Week off+++++++++++++++"+ weekOffs);
        return weekOffs;
    }

    @PostMapping("/attendance/update")
    public ResponseEntity<?> updateOrSaveAttendance(@RequestBody UserMasterAttendanceMod attendance) {
        try {
            Optional<UserMasterAttendanceMod> existing = usermasterattendancemodrepository
                .findByAttendanceidAndAttendancedate(attendance.getAttendanceid(), attendance.getAttendancedate());

            Date attendanceDate = attendance.getAttendancedate();
            
            // Use combineDateTime to create default times as Date
            Date defaultCheckin = combineDateTime(attendanceDate, 9, 0);  // 09:00
            Date defaultCheckout = combineDateTime(attendanceDate, 18, 0); // 18:00

            if (existing.isPresent()) {
                // Update logic
                UserMasterAttendanceMod existingAttendance = existing.get();
                existingAttendance.setStatus(attendance.getStatus());
                existingAttendance.setRemarks(attendance.getRemarks());
                attendance.setUserid("2019"+attendance.getAttendanceid());
                existingAttendance.setRmoduserid(attendance.getRmoduserid());
                existingAttendance.setRmodtime(new Date());
                existingAttendance.setCheckintime(attendance.getCheckintime() != null ? attendance.getCheckintime() : defaultCheckin);
                existingAttendance.setCheckouttime(attendance.getCheckouttime() != null ? attendance.getCheckouttime() : defaultCheckout);

                usermasterattendancemodrepository.save(existingAttendance);
            } else {
                // Save new
            	attendance.setRcreuserid(attendance.getRmoduserid());
                attendance.setRcretime(new Date());
                attendance.setStatus(attendance.getStatus());
                attendance.setUserid("2019"+attendance.getAttendanceid());
                attendance.setCheckintime(attendance.getCheckintime() != null ? attendance.getCheckintime() : defaultCheckin);
                attendance.setCheckouttime(attendance.getCheckouttime() != null ? attendance.getCheckouttime() : defaultCheckout);

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


    
}
