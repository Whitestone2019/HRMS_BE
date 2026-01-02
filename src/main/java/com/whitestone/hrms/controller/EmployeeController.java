package com.whitestone.hrms.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whitestone.entity.*;
import com.whitestone.hrms.repo.*;
import com.whitestone.hrms.service.ErrorMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/onboard")
public class EmployeeController {

    @Autowired
    private EmployeeProfileModRepository employeeProfileModRepository;

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
    public ResponseEntity<String> addOrUpdateEmployeeWithDocuments(
            @RequestParam("data") String dataJson,
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

            // DEBUG: Print received data
            System.out.println("=== DEBUG: Received Employee Data ===");
            System.out.println("Employee ID: " + empId);
            System.out.println("Education data received: " + requestData.get("education"));
            System.out.println("Professional data received: " + requestData.get("professional"));
            System.out.println("Skills data received: " + requestData.get("skillSet"));

            boolean isUpdate = employeeProfileModRepository.findByEmpid(empId).isPresent();
            EmployeeProfileMod employee;

            if (isUpdate) {
                employee = employeeProfileModRepository.findByEmpid(empId)
                        .orElseThrow(() -> new RuntimeException("Employee not found"));
            } else {
                employee = new EmployeeProfileMod();
                employee.setEmpid(empId);
            }

            mapper.readerForUpdating(employee).readValue(dataJson);

            Date now = new Date();
            if (!isUpdate) {
                employee.setRcretime(now);
                employee.setDelflg("N");
                employee.setEntitycreflg("N");
            }
            employee.setRmodtime(now);

            employee.setEmployeename(
                    (employee.getFirstname() != null ? employee.getFirstname().trim() : "") +
                    (employee.getLastname() != null ? " " + employee.getLastname().trim() : "")
            );

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

            employeeProfileModRepository.save(employee);

            empDirectory = Paths.get(docUploadDir + "/emp_doc/" + empId);
            Files.createDirectories(empDirectory);

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

            // Process data with SRL_NUM and USER_ID validation
            processAddress(requestData.get("address"), employee);
            processEducationDetails(requestData.get("education"), employee);
            processProfessionalDetails(requestData.get("professional"), employee);
            processSkills(requestData.get("skillSet"), employee);

            String message = isUpdate ? "Employee updated successfully!" : "Employee added successfully!";
            return ResponseEntity.ok("{\"message\": \"" + message + "\"}");

        } catch (Exception e) {
            e.printStackTrace();
            savedFiles.forEach(path -> {
                try { if (Files.exists(path)) Files.delete(path); } catch (Exception ignored) {}
            });
            String errorMsg = errorMessageService.getErrorMessage("ADD_EMP_ERROR", "en");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"" + errorMsg + " - " + e.getMessage() + "\"}");
        }
    }


    // FIXED: EDUCATION DETAILS PROCESSING

    private void processEducationDetails(Object obj, EmployeeProfileMod emp) {
        if (!(obj instanceof List)) {
            System.out.println("WARNING: Education data is not a list, type: " + (obj != null ? obj.getClass() : "null"));
            return;
        }
        
        List<?> requestList = (List<?>) obj;
        System.out.println("DEBUG: Processing " + requestList.size() + " education records");
        
        // Get all education records for this user
        List<EmployeeEducationDetailsMod> existingList = 
            employeeEducationDetailsModRepository.findByUserid(emp.getUserid());
        
        // Create maps to track existing records
        Map<Long, EmployeeEducationDetailsMod> existingById = new HashMap<>();
        
        for (EmployeeEducationDetailsMod edu : existingList) {
            existingById.put(edu.getSrlnum(), edu);
        }
        
        // Track which serial numbers are still being used
        Set<Long> usedSerialNumbers = new HashSet<>();
        
        // Process each education record from request
        for (int i = 0; i < requestList.size(); i++) {
            Object item = requestList.get(i);
            if (item instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) item;
                
                // DEBUG: Print raw data
                System.out.println("=== DEBUG Education Record " + (i+1) + " ===");
                System.out.println("Raw map keys: " + map.keySet());
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    System.out.println("  " + entry.getKey() + " = '" + entry.getValue() + "'");
                }
                
                if (isEmptyEducation(map)) {
                    System.out.println("DEBUG: Empty education record, skipping");
                    continue;
                }

                Long requestedSerial = getLongValue(map.get("srlnum"));
                EmployeeEducationDetailsMod edu = null;
                
                // Check if we're updating an existing record
                if (requestedSerial != null && existingById.containsKey(requestedSerial)) {
                    edu = existingById.get(requestedSerial);
                    System.out.println("DEBUG: Updating existing education record with SRL_NUM: " + requestedSerial);
                } else {
                    // Create new record
                    edu = new EmployeeEducationDetailsMod();
                    Long newSerial = getNextAvailableSerialNumber(employeeEducationDetailsModRepository.findAll());
                    edu.setSrlnum(newSerial);
                    edu.setUserid(emp.getUserid());
                    edu.setDelflg("N");
                    edu.setRcreuserid(emp.getUserid().toString());
                    edu.setRcretime(new Date());
                    System.out.println("DEBUG: Creating new education record with SRL_NUM: " + newSerial);
                }
                

                // FIX: PROPERLY SET ALL EDUCATION FIELDS

                edu.setInstitution(getStringValue(map.get("institution")));
                edu.setQualification(getStringValue(map.get("qualification")));
                edu.setRegnum(getStringValue(map.get("regnum")));
                edu.setFieldofstudy(getStringValue(map.get("fieldofstudy")));
                edu.setPercentage(getStringValue(map.get("percentage")));
                edu.setDuration(getStringValue(map.get("duration")));
                edu.setYearofgraduation(getStringValue(map.get("yearofgraduation")));
                edu.setAdditionalnotes(getStringValue(map.get("additionalnotes")));
                
                // Set audit fields
                edu.setRmoduserid(emp.getUserid().toString());
                edu.setRmodtime(new Date());
                edu.setDelflg("N");
                
                // DEBUG: Print what's being saved
                System.out.println("DEBUG: Saving education record:");
                System.out.println("  Institution: " + edu.getInstitution());
                System.out.println("  Qualification: " + edu.getQualification());
                System.out.println("  Regnum: " + edu.getRegnum());
                System.out.println("  Fieldofstudy: " + edu.getFieldofstudy());
                System.out.println("  Percentage: " + edu.getPercentage());
                System.out.println("  Duration: " + edu.getDuration());
                System.out.println("  Yearofgraduation: " + edu.getYearofgraduation());
                System.out.println("  Additionalnotes: " + edu.getAdditionalnotes());
                System.out.println("  User ID: " + edu.getUserid());
                System.out.println("  SRL_NUM: " + edu.getSrlnum());
                
                // Save the record
                EmployeeEducationDetailsMod saved = employeeEducationDetailsModRepository.save(edu);
                usedSerialNumbers.add(saved.getSrlnum());
                System.out.println("DEBUG: Education record saved successfully with ID: " + saved.getSrlnum());
            }
        }
        
        // Soft delete records that are in DB but not in the current request
        for (EmployeeEducationDetailsMod existing : existingList) {
            if (!usedSerialNumbers.contains(existing.getSrlnum())) {
                System.out.println("DEBUG: Soft deleting education record with SRL_NUM: " + existing.getSrlnum());
                existing.setDelflg("Y");
                existing.setRmoduserid(emp.getUserid().toString());
                existing.setRmodtime(new Date());
                employeeEducationDetailsModRepository.save(existing);
            }
        }
        
        System.out.println("DEBUG: Education processing completed");
    }


    // FIXED: PROFESSIONAL DETAILS PROCESSING

    private void processProfessionalDetails(Object obj, EmployeeProfileMod emp) {
        if (!(obj instanceof List)) {
            System.out.println("WARNING: Professional data is not a list");
            return;
        }
        
        List<?> requestList = (List<?>) obj;
        System.out.println("DEBUG: Processing " + requestList.size() + " professional records");
        
        // Get all professional records for this user
        List<EmployeeProfessionalDetailsMod> existingList = 
            employeeProfessionalDetailsModRepository.findByUserid(emp.getUserid().toString());
        
        Map<Long, EmployeeProfessionalDetailsMod> existingById = new HashMap<>();
        for (EmployeeProfessionalDetailsMod prof : existingList) {
            existingById.put(prof.getSrlnum(), prof);
        }
        
        Set<Long> usedSerialNumbers = new HashSet<>();
        
        // Process each professional record from request
        for (int i = 0; i < requestList.size(); i++) {
            Object item = requestList.get(i);
            if (item instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) item;
                
                if (isEmptyProfessional(map)) {
                    System.out.println("DEBUG: Empty professional record, skipping");
                    continue;
                }

                Long requestedSerial = getLongValue(map.get("srlnum"));
                EmployeeProfessionalDetailsMod prof = null;
                
                // Check if we're updating an existing record
                if (requestedSerial != null && existingById.containsKey(requestedSerial)) {
                    prof = existingById.get(requestedSerial);
                } else {
                    // Create new record
                    prof = new EmployeeProfessionalDetailsMod();
                    prof.setSrlnum(getNextAvailableSerialNumber(employeeProfessionalDetailsModRepository.findAll()));
                    prof.setUserid(emp.getUserid().toString());
                    prof.setDelflg("N");
                    prof.setRcreuserid(emp.getUserid().toString());
                    prof.setRcretime(new Date());
                    prof.setOfferletter("SUBMITTED");
                }
                

                // FIX: PROPERLY SET ALL PROFESSIONAL FIELDS

                prof.setOrganisation(getStringValue(map.get("organisation")));
                prof.setLocation(getStringValue(map.get("location")));
                prof.setOrgempid(getStringValue(map.get("orgempid")));
                prof.setOrgdept(getStringValue(map.get("orgdept")));
                prof.setOrgrole(getStringValue(map.get("orgrole")));
                prof.setJoiningdate(toDate(map.get("joiningdate")));
                prof.setRelievingdate(toDate(map.get("relievingdate")));
                prof.setCtc(getStringValue(map.get("ctc")));
                prof.setAdditionalinformation(getStringValue(map.get("additionalinformation")));
                
                // Set audit fields
                prof.setRmoduserid(emp.getUserid().toString());
                prof.setRmodtime(new Date());
                prof.setDelflg("N");
                
                // Save the record
                EmployeeProfessionalDetailsMod saved = employeeProfessionalDetailsModRepository.save(prof);
                usedSerialNumbers.add(saved.getSrlnum());
                System.out.println("DEBUG: Professional record saved successfully");
            }
        }
        
        // Soft delete records that are in DB but not in the current request
        for (EmployeeProfessionalDetailsMod existing : existingList) {
            if (!usedSerialNumbers.contains(existing.getSrlnum())) {
                existing.setDelflg("Y");
                existing.setRmoduserid(emp.getUserid().toString());
                existing.setRmodtime(new Date());
                employeeProfessionalDetailsModRepository.save(existing);
            }
        }
        
        System.out.println("DEBUG: Professional processing completed");
    }


    // FIXED: SKILLS PROCESSING

    private void processSkills(Object obj, EmployeeProfileMod emp) {
        if (!(obj instanceof List)) {
            System.out.println("WARNING: Skills data is not a list");
            return;
        }
        
        List<?> requestList = (List<?>) obj;
        System.out.println("DEBUG: Processing " + requestList.size() + " skill records");
        
        // Get all skill records for this user
        List<EmployeeSkillMod> existingList = 
            employeeSkillModRepository.findByUserid(emp.getUserid());
        
        Map<Long, EmployeeSkillMod> existingById = new HashMap<>();
        for (EmployeeSkillMod skill : existingList) {
            existingById.put(skill.getSrlnum(), skill);
        }
        
        Set<Long> usedSerialNumbers = new HashSet<>();
        
        // Process each skill record from request
        for (int i = 0; i < requestList.size(); i++) {
            Object item = requestList.get(i);
            if (item instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) item;
                
                if (isEmptySkill(map)) {
                    System.out.println("DEBUG: Empty skill record, skipping");
                    continue;
                }

                Long requestedSerial = getLongValue(map.get("srlnum"));
                EmployeeSkillMod skill = null;
                
                // Check if we're updating an existing record
                if (requestedSerial != null && existingById.containsKey(requestedSerial)) {
                    skill = existingById.get(requestedSerial);
                } else {
                    // Create new record
                    skill = new EmployeeSkillMod();
                    skill.setSrlnum(getNextAvailableSerialNumber(employeeSkillModRepository.findAll()));
                    skill.setUserid(emp.getUserid());
                    skill.setDelflg("N");
                    skill.setRcreuserid(emp.getUserid().toString());
                    skill.setRcretime(new Date());
                }
                

                // FIX: PROPERLY SET ALL SKILL FIELDS

                skill.setSkill(getStringValue(map.get("skill")));
                skill.setProficiencylevel(getStringValue(map.get("proficiencylevel")));
                skill.setCertification(getStringValue(map.get("certification")));
                skill.setLastupdated(getStringValue(map.get("lastupdated")));
                
                // Handle years of experience
                Object yearsExpObj = map.get("yearsExperience");
                if (yearsExpObj != null) {
                    skill.setYearsofexp(getStringValue(yearsExpObj));
                }
                if (skill.getYearsofexp() == null || skill.getYearsofexp().isEmpty()) {
                    skill.setYearsofexp("N/A");
                }
                
                // Set audit fields
                skill.setRmoduserid(emp.getUserid().toString());
                skill.setRmodtime(new Date());
                skill.setDelflg("N");
                
                // Save the record
                EmployeeSkillMod saved = employeeSkillModRepository.save(skill);
                usedSerialNumbers.add(saved.getSrlnum());
                System.out.println("DEBUG: Skill record saved successfully");
            }
        }
        
        // Soft delete records that are in DB but not in the current request
        for (EmployeeSkillMod existing : existingList) {
            if (!usedSerialNumbers.contains(existing.getSrlnum())) {
                existing.setDelflg("Y");
                existing.setRmoduserid(emp.getUserid().toString());
                existing.setRmodtime(new Date());
                employeeSkillModRepository.save(existing);
            }
        }
        
        System.out.println("DEBUG: Skills processing completed");
    }

    // HELPER METHODS
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

    // FIXED: Better string value handling
    private String getStringValue(Object obj) {
        if (obj == null) {
            return null;
        }
        String value = obj.toString().trim();
        return value.isEmpty() ? null : value;
    }

    // FIXED: Better long value handling
    private Long getLongValue(Object obj) {
        if (obj == null) return null;
        try {
            if (obj instanceof Number) {
                return ((Number) obj).longValue();
            }
            String str = obj.toString().trim();
            if (str.isEmpty()) return null;
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String saveFile(MultipartFile file, String empId, String type, Path dir, List<Path> track) throws IOException {
        if (file == null || file.isEmpty()) return null;
        String orig = file.getOriginalFilename() != null ? file.getOriginalFilename() : "document";
        orig = new File(orig).getName();
        String ext = "";
        int i = orig.lastIndexOf('.');
        if (i > 0) ext = orig.substring(i);
        Path path = dir.resolve(empId + "_" + type + ext);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        track.add(path);
        return path.toString();
    }

    private void processAddress(Object obj, EmployeeProfileMod emp) {
        if (obj instanceof Map && !((Map<?, ?>) obj).isEmpty()) {
            EmployeeAddressMod addr = new ObjectMapper().convertValue(obj, EmployeeAddressMod.class);
            addr.setUserid(emp.getUserid());
            addr.setDelflg("N");
            addr.setRcreuserid(emp.getUserid().toString());
            addr.setRcretime(emp.getRcretime() != null ? emp.getRcretime() : new Date());
            addr.setRmoduserid(emp.getUserid().toString());
            addr.setRmodtime(new Date());
            
            employeeAddressModRepository.save(addr);
        }
    }

    // Generic method to get next available serial number
    private Long getNextAvailableSerialNumber(List<?> allRecords) {
        Set<Long> usedSerials = new HashSet<>();
        
        for (Object record : allRecords) {
            if (record instanceof EmployeeEducationDetailsMod) {
                usedSerials.add(((EmployeeEducationDetailsMod) record).getSrlnum());
            } else if (record instanceof EmployeeProfessionalDetailsMod) {
                usedSerials.add(((EmployeeProfessionalDetailsMod) record).getSrlnum());
            } else if (record instanceof EmployeeSkillMod) {
                usedSerials.add(((EmployeeSkillMod) record).getSrlnum());
            }
        }
        
        Long nextSerial = 1L;
        while (usedSerials.contains(nextSerial)) {
            nextSerial++;
        }
        return nextSerial;
    }

    private boolean isEmptyEducation(Map<String, Object> map) {
        boolean empty = isBlank(map.get("institution")) && isBlank(map.get("qualification"));
        System.out.println("DEBUG: isEmptyEducation = " + empty);
        return empty;
    }

    private boolean isEmptyProfessional(Map<String, Object> map) {
        return isBlank(map.get("organisation")) && isBlank(map.get("orgrole"));
    }

    private boolean isEmptySkill(Map<String, Object> map) {
        return isBlank(map.get("skill"));
    }

    private boolean isBlank(Object obj) {
        return obj == null || obj.toString().trim().isEmpty();
    }


    // DEBUG ENDPOINT

    @PostMapping("/debug-education")
    public ResponseEntity<Map<String, Object>> debugEducationData(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();
        
        System.out.println("=== DEBUG ENDPOINT: Raw Request Data ===");
        System.out.println("Full request keys: " + requestData.keySet());
        
        if (requestData.containsKey("education")) {
            Object education = requestData.get("education");
            System.out.println("Education data type: " + (education != null ? education.getClass().getName() : "null"));
            
            if (education instanceof List) {
                List<?> eduList = (List<?>) education;
                System.out.println("Number of education records: " + eduList.size());
                
                for (int i = 0; i < eduList.size(); i++) {
                    Object record = eduList.get(i);
                    System.out.println("\n--- Record " + i + " ---");
                    System.out.println("Record type: " + (record != null ? record.getClass().getName() : "null"));
                    
                    if (record instanceof Map) {
                        Map<String, Object> map = (Map<String, Object>) record;
                        System.out.println("Record keys: " + map.keySet());
                        
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            System.out.println("  " + entry.getKey() + " = '" + entry.getValue() + 
                                             "' (type: " + (entry.getValue() != null ? 
                                             entry.getValue().getClass().getSimpleName() : "null") + ")");
                        }
                    }
                }
            } else {
                System.out.println("Education is not a list!");
            }
        } else {
            System.out.println("No education key found in request!");
        }
        
        response.put("status", "debug_data_received");
        response.put("message", "Check server console for details");
        return ResponseEntity.ok(response);
    }
    
    
}