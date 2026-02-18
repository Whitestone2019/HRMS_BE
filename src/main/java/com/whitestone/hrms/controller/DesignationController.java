package com.whitestone.hrms.controller;

import com.whitestone.entity.UserRoleMaintenance;
import com.whitestone.hrms.repo.UserRoleMaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/designations")
public class DesignationController {
    
    // ADD THIS METHOD - Handle OPTIONS for this specific endpoint
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        System.out.println("Handling OPTIONS request for /api/designations");
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .header("Access-Control-Max-Age", "3600")
                .build();
    }

    @Autowired
    private UserRoleMaintenanceRepository designationRepository;

    // GET: Get all designations with delflg = "0" OR delflg = "Y"
    @GetMapping
    public ResponseEntity<?> getAllDesignations() {
        try {
            System.out.println("=== GET ALL DESIGNATIONS ===");
            
            // Fetch records where delflg is "0" OR "Y"
            List<UserRoleMaintenance> designationsWith0 = designationRepository.findByDelflg("0");
            List<UserRoleMaintenance> designationsWithY = designationRepository.findByDelflg("N");
            
            // Combine both lists
            List<UserRoleMaintenance> allDesignations = designationsWith0;
            allDesignations.addAll(designationsWithY);
            
            System.out.println("Found " + allDesignations.size() + " designations (0:" + 
                              designationsWith0.size() + ", N:" + designationsWithY.size() + ")");
            
            // Convert to frontend format
            List<Map<String, Object>> responseList = allDesignations.stream()
                .map(designation -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("designationid", designation.getRoleid());
                    map.put("designationname", designation.getRolename());
                    map.put("roleId", designation.getRoleid());
                    map.put("roleName", designation.getRolename());
                    map.put("description", designation.getDescription());
                    map.put("department", "");
                    map.put("status", designation.getStatus());
                    map.put("salaryrange", "15000 - 25000");
                    map.put("rcreuserid", designation.getRcreuserid());
                    map.put("delflg", designation.getDelflg()); // Add this
                    return map;
                })
                .collect(Collectors.toList());
                
            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            System.err.println("ERROR in getAllDesignations: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }

    // POST: Create new designation
    @PostMapping
    public ResponseEntity<?> addDesignation(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("=== CREATE DESIGNATION REQUEST ===");
            System.out.println("Request data: " + request);
            
            // Get fields (accept any naming convention)
            String roleId = null;
            String roleName = null;
            String description = "";
            String userId = "SYSTEM";
            
            // Try all possible field names
            if (request.containsKey("roleId")) roleId = request.get("roleId").toString();
            else if (request.containsKey("roleid")) roleId = request.get("roleid").toString();
            else if (request.containsKey("designationid")) roleId = request.get("designationid").toString();
            
            if (request.containsKey("roleName")) roleName = request.get("roleName").toString();
            else if (request.containsKey("rolename")) roleName = request.get("rolename").toString();
            else if (request.containsKey("designationname")) roleName = request.get("designationname").toString();
            
            if (request.containsKey("description")) description = request.get("description").toString();
            if (request.containsKey("rcreuserid")) userId = request.get("rcreuserid").toString();
            
            System.out.println("Parsed: roleId=" + roleId + ", roleName=" + roleName);
            
            // Validate
            if (roleId == null || roleId.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Role ID is required"));
            }
            
            if (roleName == null || roleName.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Role Name is required"));
            }
            
            // Check duplicate
            if (designationRepository.existsByRoleid(roleId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Role ID already exists"));
            }
            
            // Create and save
            UserRoleMaintenance designation = new UserRoleMaintenance();
            designation.setRoleid(roleId);
            designation.setRolename(roleName);
            designation.setDescription(description);
            designation.setPermissions("READ,WRITE");
            designation.setStatus("ACTIVE");
            designation.setEntitycreflg("Y");
            designation.setDelflg("0"); // Set to "0" for active
            designation.setRcreuserid(userId);
            designation.setRmoduserid(userId);
            designation.setRvfyuserid("VERIFY_USER");
            
            Date now = new Date();
            designation.setRcretime(now);
            designation.setRmodtime(now);
            designation.setRvfytime(now);
            
            UserRoleMaintenance saved = designationRepository.save(designation);
            System.out.println("Saved designation: " + saved.getRoleid());
            
            // Return success
            Map<String, Object> response = new HashMap<>();
            response.put("designationid", saved.getRoleid());
            response.put("designationname", saved.getRolename());
            response.put("roleId", saved.getRoleid());
            response.put("roleName", saved.getRolename());
            response.put("description", saved.getDescription());
            response.put("department", "");
            response.put("status", saved.getStatus());
            response.put("salaryrange", "15000 - 25000");
            response.put("rcreuserid", saved.getRcreuserid());
            response.put("delflg", saved.getDelflg());
            response.put("success", true);
            response.put("message", "Designation created successfully");
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            System.err.println("ERROR in addDesignation: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create designation: " + e.getMessage()));
        }
    }

    // PUT: Update designation
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDesignation(@PathVariable String id, 
                                               @RequestBody Map<String, Object> request) {
        try {
            System.out.println("=== UPDATE DESIGNATION ===");
            System.out.println("ID: " + id + ", Data: " + request);
            
            Optional<UserRoleMaintenance> existingOpt = designationRepository.findByRoleid(id);
            if (!existingOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Designation not found"));
            }
            
            UserRoleMaintenance designation = existingOpt.get();
            
            // Update fields
            if (request.containsKey("roleName")) {
                designation.setRolename(request.get("roleName").toString());
            } else if (request.containsKey("rolename")) {
                designation.setRolename(request.get("rolename").toString());
            } else if (request.containsKey("designationname")) {
                designation.setRolename(request.get("designationname").toString());
            }
            
            if (request.containsKey("description")) {
                designation.setDescription(request.get("description").toString());
            }
            
            designation.setRmodtime(new Date());
            if (request.containsKey("rcreuserid")) {
                designation.setRmoduserid(request.get("rcreuserid").toString());
            }
            
            UserRoleMaintenance updated = designationRepository.save(designation);
            
            Map<String, Object> response = new HashMap<>();
            response.put("designationid", updated.getRoleid());
            response.put("designationname", updated.getRolename());
            response.put("roleId", updated.getRoleid());
            response.put("roleName", updated.getRolename());
            response.put("description", updated.getDescription());
            response.put("department", "");
            response.put("status", updated.getStatus());
            response.put("salaryrange", "15000 - 25000");
            response.put("rcreuserid", updated.getRcreuserid());
            response.put("delflg", updated.getDelflg());
            response.put("success", true);
            response.put("message", "Designation updated successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("ERROR in updateDesignation: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update designation: " + e.getMessage()));
        }
    }

    // DELETE: Delete designation
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDesignation(@PathVariable String id) {
        try {
            System.out.println("=== DELETE DESIGNATION ===");
            System.out.println("ID: " + id);
            
            Optional<UserRoleMaintenance> existingOpt = designationRepository.findByRoleid(id);
            if (!existingOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Designation not found"));
            }
            
            UserRoleMaintenance designation = existingOpt.get();
            designation.setDelflg("1"); // Set to "1" for deleted (soft delete)
            designation.setRmodtime(new Date());
            designation.setRmoduserid("SYSTEM");
            
            designationRepository.save(designation);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Designation deleted successfully",
                "designationid", id
            ));
            
        } catch (Exception e) {
            System.err.println("ERROR in deleteDesignation: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete designation: " + e.getMessage()));
        }
    }

    // GET: Get single designation
    @GetMapping("/{id}")
    public ResponseEntity<?> getDesignationById(@PathVariable String id) {
        try {
            System.out.println("=== GET DESIGNATION BY ID ===");
            System.out.println("ID: " + id);
            
            Optional<UserRoleMaintenance> designationOpt = designationRepository.findByRoleid(id);
            if (!designationOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Designation not found"));
            }
            
            UserRoleMaintenance designation = designationOpt.get();
            
            Map<String, Object> response = new HashMap<>();
            response.put("designationid", designation.getRoleid());
            response.put("designationname", designation.getRolename());
            response.put("roleId", designation.getRoleid());
            response.put("roleName", designation.getRolename());
            response.put("description", designation.getDescription());
            response.put("department", "");
            response.put("status", designation.getStatus());
            response.put("salaryrange", "15000 - 25000");
            response.put("rcreuserid", designation.getRcreuserid());
            response.put("delflg", designation.getDelflg());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("ERROR in getDesignationById: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to get designation: " + e.getMessage()));
        }
    }
}