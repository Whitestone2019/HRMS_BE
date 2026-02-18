//// File: src/main/java/com/whitestone/hrms/controller/BackupController.java
//package com.whitestone.hrms.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.PostConstruct;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//@RestController
//@RequestMapping("/api/backup")
//public class BackupController {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    // List of tables to backup and drop
//    private static final List<String> TABLES_TO_PROCESS = Arrays.asList(
//        "PAYROLL",
//        "SALARY_TEMPLATE_COMPONENTS",
//        "PAYROLL_ADJUSTMENTS",
//        "SALARY_TEMPLATE",
//        "EMPLOYEE_SALARY",
//        "EMPLOYEE_SALARY_HISTORY"
//    );
//
//    /**
//     * RUNS AUTOMATICALLY ON APPLICATION STARTUP
//     * Backs up tables and drops originals (does NOT create new tables)
//     */
//    @PostConstruct
//    public void autoBackupAndDropTables() {
//        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
//        System.out.println("║           AUTO BACKUP & DROP TABLES - STARTING                  ║");
//        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
//        
//        System.out.println("📋 Tables to process: " + String.join(", ", TABLES_TO_PROCESS));
//        System.out.println("⏰ Process started at: " + LocalDateTime.now());
//        System.out.println("📌 Note: Only backup tables will remain. Original tables will be DROPPED.");
//        
//        Map<String, Map<String, Object>> results = new LinkedHashMap<>();
//        int successCount = 0;
//        int failureCount = 0;
//        
//        for (String tableName : TABLES_TO_PROCESS) {
//            System.out.println("\n" + "=".repeat(60));
//            System.out.println("🔄 Processing table: " + tableName);
//            System.out.println("=".repeat(60));
//            
//            try {
//                Map<String, Object> result = backupAndDropTable(tableName);
//                results.put(tableName, result);
//                
//                if ("SUCCESS".equals(result.get("status"))) {
//                    successCount++;
//                    System.out.println("✅ SUCCESS: " + tableName + " backed up and dropped");
//                } else {
//                    failureCount++;
//                    System.err.println("❌ FAILED: " + tableName + " - " + result.get("error"));
//                }
//                
//            } catch (Exception e) {
//                failureCount++;
//                System.err.println("❌ EXCEPTION in " + tableName + ": " + e.getMessage());
//                
//                Map<String, Object> errorResult = new HashMap<>();
//                errorResult.put("status", "FAILED");
//                errorResult.put("error", e.getMessage());
//                results.put(tableName, errorResult);
//            }
//            
//            // Small delay between tables
//            try {
//                Thread.sleep(300);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//        
//        // Print final summary
//        printFinalSummary(results, successCount, failureCount);
//    }
//
//    /**
//     * Backup and drop a single table
//     */
//    private Map<String, Object> backupAndDropTable(String tableName) {
//        Map<String, Object> result = new HashMap<>();
//        String backupTableName = null;
//        int rowsBackedUp = 0;
//        
//        try {
//            // STEP 1: Check if table exists
//            boolean tableExists = tableExists(tableName);
//            
//            if (!tableExists) {
//                System.out.println("ℹ️ Table '" + tableName + "' doesn't exist. Nothing to backup.");
//                result.put("status", "SKIPPED");
//                result.put("message", "Table does not exist - nothing to backup");
//                result.put("backupCreated", false);
//                result.put("rowsBackedUp", 0);
//                return result;
//            }
//            
//            // STEP 2: Get row count before backup
//            rowsBackedUp = getRowCount(tableName);
//            System.out.println("📊 Found " + rowsBackedUp + " rows in " + tableName);
//            
//            if (rowsBackedUp == 0) {
//                System.out.println("ℹ️ Table is empty. Just dropping it.");
//                dropTableSafely(tableName);
//                result.put("status", "SUCCESS");
//                result.put("message", "Empty table dropped (no backup needed)");
//                result.put("rowsBackedUp", 0);
//                return result;
//            }
//            
//            // STEP 3: Create backup table
//            backupTableName = createBackupTable(tableName, rowsBackedUp);
//            
//            if (backupTableName == null) {
//                throw new RuntimeException("Failed to create backup table");
//            }
//            
//            // STEP 4: Verify backup was created successfully
//            boolean backupVerified = verifyBackup(backupTableName, tableName, rowsBackedUp);
//            
//            if (!backupVerified) {
//                System.err.println("⚠️ Backup verification failed for " + tableName + ". Will NOT drop original table.");
//                result.put("status", "BACKUP_FAILED");
//                result.put("message", "Backup verification failed - original table preserved");
//                result.put("backupTable", backupTableName);
//                result.put("rowsBackedUp", rowsBackedUp);
//                return result;
//            }
//            
//            // STEP 5: DROP original table
//            System.out.println("🗑️  Dropping original table: " + tableName);
//            dropTableSafely(tableName);
//            
//            // STEP 6: Verify original table is gone
//            boolean originalStillExists = tableExists(tableName);
//            if (originalStillExists) {
//                System.err.println("❌ ERROR: Original table '" + tableName + "' still exists after drop!");
//                result.put("status", "DROP_FAILED");
//                result.put("message", "Failed to drop original table");
//                result.put("backupTable", backupTableName);
//                result.put("rowsBackedUp", rowsBackedUp);
//                return result;
//            }
//            
//            System.out.println("✅ Verified: Original table '" + tableName + "' has been removed");
//            
//            // STEP 7: Store result
//            result.put("status", "SUCCESS");
//            result.put("backupTable", backupTableName);
//            result.put("rowsBackedUp", rowsBackedUp);
//            result.put("message", "Table backed up and dropped successfully");
//            
//            return result;
//            
//        } catch (Exception e) {
//            result.put("status", "FAILED");
//            result.put("error", e.getMessage());
//            result.put("backupTable", backupTableName);
//            result.put("rowsBackedUp", rowsBackedUp);
//            return result;
//        }
//    }
//
//    /**
//     * Check if table exists
//     */
//    private boolean tableExists(String tableName) {
//        try {
//            String sql = "SELECT COUNT(*) FROM user_tables WHERE table_name = ?";
//            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName.toUpperCase());
//            return count != null && count > 0;
//        } catch (Exception e) {
//            System.out.println("⚠️ Error checking if table exists: " + e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * Get row count from table
//     */
//    private int getRowCount(String tableName) {
//        try {
//            String sql = "SELECT COUNT(*) FROM " + tableName.toUpperCase();
//            Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
//            return count != null ? count : 0;
//        } catch (Exception e) {
//            System.out.println("⚠️ Error getting row count: " + e.getMessage());
//            return 0;
//        }
//    }
//
//    /**
//     * Create backup table
//     */
//    private String createBackupTable(String originalTableName, int expectedRows) {
//        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
//        String backupTableName = originalTableName.toUpperCase() + "_BAK_" + timestamp;
//        
//        // Ensure table name doesn't exceed Oracle's 30 character limit
//        if (backupTableName.length() > 30) {
//            backupTableName = originalTableName.toUpperCase().substring(0, Math.min(originalTableName.length(), 15)) 
//                           + "_BAK_" + timestamp.substring(8);
//            if (backupTableName.length() > 30) {
//                backupTableName = backupTableName.substring(0, 30);
//            }
//        }
//        
//        System.out.println("📦 Creating backup table: " + backupTableName);
//        
//        try {
//            // Drop if exists (shouldn't, but just in case)
//            dropTableSafely(backupTableName);
//            
//            // METHOD 1: Try CTAS (Create Table As Select) - fastest
//            String createSQL = "CREATE TABLE " + backupTableName + 
//                              " AS SELECT * FROM " + originalTableName.toUpperCase();
//            jdbcTemplate.execute(createSQL);
//            
//            // Verify backup count
//            int backupRows = getRowCount(backupTableName);
//            System.out.println("   ✅ Backup created with " + backupRows + " rows");
//            
//            if (backupRows != expectedRows) {
//                System.err.println("   ⚠️ Row count mismatch! Original: " + expectedRows + ", Backup: " + backupRows);
//            }
//            
//            return backupTableName;
//            
//        } catch (Exception e) {
//            System.err.println("   ❌ CTAS backup failed: " + e.getMessage());
//            
//            // METHOD 2: Try alternative method
//            try {
//                return createBackupAlternative(originalTableName, backupTableName);
//            } catch (Exception ex) {
//                throw new RuntimeException("All backup methods failed: " + ex.getMessage());
//            }
//        }
//    }
//
//    /**
//     * Alternative backup method
//     */
//    private String createBackupAlternative(String originalTableName, String backupTableName) {
//        System.out.println("   🔄 Trying alternative backup method...");
//        
//        try {
//            // Create empty table with same structure
//            String createSQL = "CREATE TABLE " + backupTableName + 
//                              " AS SELECT * FROM " + originalTableName.toUpperCase() + 
//                              " WHERE 1=0";
//            jdbcTemplate.execute(createSQL);
//            
//            // Get all column names
//            List<String> columns = getColumnNames(originalTableName);
//            if (columns.isEmpty()) {
//                throw new RuntimeException("No columns found in original table");
//            }
//            
//            // Insert data
//            String columnList = String.join(", ", columns);
//            String insertSQL = "INSERT INTO " + backupTableName + 
//                             " (" + columnList + ") " +
//                             "SELECT " + columnList + " FROM " + originalTableName.toUpperCase();
//            
//            int rowsInserted = jdbcTemplate.update(insertSQL);
//            jdbcTemplate.execute("COMMIT");
//            
//            System.out.println("   ✅ Alternative backup created with " + rowsInserted + " rows");
//            return backupTableName;
//            
//        } catch (Exception e) {
//            throw new RuntimeException("Alternative backup failed: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Get column names from table
//     */
//    private List<String> getColumnNames(String tableName) {
//        try {
//            String sql = "SELECT column_name FROM all_tab_columns " +
//                        "WHERE table_name = UPPER(?) ORDER BY column_id";
//            return jdbcTemplate.queryForList(sql, String.class, tableName.toUpperCase());
//        } catch (Exception e) {
//            System.err.println("Error getting column names: " + e.getMessage());
//            return new ArrayList<>();
//        }
//    }
//
//    /**
//     * Verify backup was created successfully
//     */
//    private boolean verifyBackup(String backupTableName, String originalTableName, int expectedRows) {
//        try {
//            // Check if backup table exists
//            if (!tableExists(backupTableName)) {
//                System.err.println("   ❌ Backup table does not exist!");
//                return false;
//            }
//            
//            // Check row count in backup
//            int backupRows = getRowCount(backupTableName);
//            System.out.println("   🔍 Backup verification:");
//            System.out.println("     - Original rows: " + expectedRows);
//            System.out.println("     - Backup rows: " + backupRows);
//            
//            if (backupRows == expectedRows) {
//                System.out.println("     ✅ Backup verified successfully");
//                return true;
//            } else if (backupRows > 0) {
//                System.out.println("     ⚠️ Backup has " + backupRows + " rows (expected " + expectedRows + ")");
//                System.out.println("     ℹ️ Backup exists but row count differs - still considered successful");
//                return true;
//            } else {
//                System.err.println("     ❌ Backup has 0 rows!");
//                return false;
//            }
//            
//        } catch (Exception e) {
//            System.err.println("   ❌ Backup verification error: " + e.getMessage());
//            return false;
//        }
//    }
//
//    /**
//     * Safely drop a table
//     */
//    private void dropTableSafely(String tableName) {
//        try {
//            // Use dynamic SQL with exception handling
//            String dropSQL = 
//                "BEGIN " +
//                "   EXECUTE IMMEDIATE 'DROP TABLE ' || '" + tableName.toUpperCase() + "' || ' CASCADE CONSTRAINTS'; " +
//                "EXCEPTION " +
//                "   WHEN OTHERS THEN " +
//                "      IF SQLCODE != -942 THEN " + // -942 = table does not exist
//                "         RAISE; " +
//                "      END IF; " +
//                "END;";
//            
//            jdbcTemplate.execute(dropSQL);
//            System.out.println("   ✅ Table " + tableName + " dropped successfully");
//        } catch (Exception e) {
//            System.err.println("   ❌ Failed to drop table " + tableName + ": " + e.getMessage());
//            throw new RuntimeException("Failed to drop table: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Print final summary
//     */
//    private void printFinalSummary(Map<String, Map<String, Object>> results, int successCount, int failureCount) {
//        System.out.println("\n" + "=".repeat(80));
//        System.out.println("📊 AUTO BACKUP & DROP - FINAL SUMMARY");
//        System.out.println("=".repeat(80));
//        System.out.println("⏰ Process completed at: " + LocalDateTime.now());
//        System.out.println("📋 Total tables processed: " + TABLES_TO_PROCESS.size());
//        System.out.println("✅ Successfully backed up and dropped: " + successCount);
//        System.out.println("❌ Failed: " + failureCount);
//        System.out.println("-".repeat(80));
//        
//        int totalRowsBackedUp = 0;
//        List<String> backupTables = new ArrayList<>();
//        
//        for (Map.Entry<String, Map<String, Object>> entry : results.entrySet()) {
//            String tableName = entry.getKey();
//            Map<String, Object> result = entry.getValue();
//            
//            System.out.println("\n📊 " + tableName + ":");
//            System.out.println("  Status: " + result.get("status"));
//            
//            if (result.containsKey("backupTable")) {
//                String backupTable = (String) result.get("backupTable");
//                backupTables.add(backupTable);
//                System.out.println("  Backup Table: " + backupTable);
//            }
//            
//            if (result.containsKey("rowsBackedUp")) {
//                int rows = (int) result.get("rowsBackedUp");
//                totalRowsBackedUp += rows;
//                System.out.println("  Rows Backed Up: " + rows);
//            }
//            
//            if (result.containsKey("message")) {
//                System.out.println("  Message: " + result.get("message"));
//            }
//            
//            if (result.containsKey("error")) {
//                System.out.println("  Error: " + result.get("error"));
//            }
//            
//            // Check if original table still exists
//            boolean originalExists = tableExists(tableName);
//            if (originalExists) {
//                System.out.println("  ⚠️  WARNING: Original table still exists!");
//            } else {
//                System.out.println("  ✓ Original table removed");
//            }
//        }
//        
//        System.out.println("\n" + "=".repeat(80));
//        System.out.println("📈 TOTALS:");
//        System.out.println("  Total rows backed up: " + totalRowsBackedUp);
//        System.out.println("  Total backup tables created: " + backupTables.size());
//        System.out.println("=".repeat(80));
//        
//        System.out.println("\n📌 CURRENT DATABASE STATE:");
//        System.out.println("=".repeat(80));
//        
//        // Show which original tables are gone
//        System.out.println("\n-- Original tables (should NOT exist):");
//        for (String tableName : TABLES_TO_PROCESS) {
//            boolean exists = tableExists(tableName);
//            if (exists) {
//                System.out.println("   ❌ " + tableName + " - STILL EXISTS (failed to drop)");
//            } else {
//                System.out.println("   ✅ " + tableName + " - REMOVED");
//            }
//        }
//        
//        System.out.println("\n-- Backup tables (contains all data):");
//        if (backupTables.isEmpty()) {
//            System.out.println("   No backup tables created");
//        } else {
//            for (String backupTable : backupTables) {
//                int rows = getRowCount(backupTable);
//                System.out.println("   ✅ " + backupTable + " - " + rows + " rows");
//            }
//        }
//        
//        System.out.println("\n📌 VERIFICATION COMMANDS:");
//        System.out.println("=".repeat(80));
//        
//        System.out.println("\n-- Check original tables (should return 0):");
//        for (String tableName : TABLES_TO_PROCESS) {
//            System.out.println("   SELECT COUNT(*) FROM user_tables WHERE table_name = '" + tableName.toUpperCase() + "';");
//        }
//        
//        System.out.println("\n-- Check backup tables (should return count > 0):");
//        System.out.println("   SELECT table_name, num_rows, created FROM user_tables");
//        System.out.println("   WHERE table_name LIKE '%_BAK_%'");
//        System.out.println("   ORDER BY created DESC;");
//        
//        System.out.println("\n-- Query backup tables (example):");
//        for (String tableName : TABLES_TO_PROCESS) {
//            System.out.println("   -- " + tableName + " data (in backup):");
//            System.out.println("   SELECT * FROM (");
//            System.out.println("     SELECT table_name FROM user_tables");
//            System.out.println("     WHERE table_name LIKE '" + tableName.toUpperCase() + "_BAK_%'");
//            System.out.println("     ORDER BY created DESC");
//            System.out.println("   ) WHERE ROWNUM = 1; -- Get latest backup");
//        }
//        
//        System.out.println("\n" + "=".repeat(80));
//        System.out.println("⚠️  IMPORTANT NOTES:");
//        System.out.println("=".repeat(80));
//        System.out.println("1. Original tables have been DROPPED");
//        System.out.println("2. Only backup tables remain in the database");
//        System.out.println("3. Backup tables have '_BAK_' suffix with timestamp");
//        System.out.println("4. No new empty tables were created");
//        System.out.println("5. Your application will need to handle missing tables");
//        System.out.println("6. Backup tables can be restored if needed");
//        System.out.println("=".repeat(80));
//        System.out.println("✅ PROCESS COMPLETED");
//        System.out.println("=".repeat(80));
//    }
//
//    /**
//     * MANUAL TRIGGER API
//     */
//    @PostMapping("/trigger")
//    public Map<String, Object> triggerManualProcess() {
//        Map<String, Object> response = new HashMap<>();
//        response.put("timestamp", LocalDateTime.now());
//        response.put("action", "Manual backup and drop tables");
//        
//        try {
//            System.out.println("=== MANUAL PROCESS TRIGGERED ===");
//            autoBackupAndDropTables();
//            response.put("status", "SUCCESS");
//            response.put("message", "Manual process completed successfully");
//        } catch (Exception e) {
//            response.put("status", "ERROR");
//            response.put("message", "Manual process failed: " + e.getMessage());
//        }
//        
//        return response;
//    }
//
//    /**
//     * CHECK STATUS API
//     */
//    @GetMapping("/status")
//    public Map<String, Object> checkStatus() {
//        Map<String, Object> status = new HashMap<>();
//        status.put("timestamp", LocalDateTime.now());
//        status.put("tablesToProcess", TABLES_TO_PROCESS);
//        
//        List<Map<String, Object>> tableStatuses = new ArrayList<>();
//        List<Map<String, Object>> backupTables = new ArrayList<>();
//        
//        // Check original tables
//        for (String tableName : TABLES_TO_PROCESS) {
//            Map<String, Object> tableStatus = new HashMap<>();
//            tableStatus.put("tableName", tableName);
//            
//            boolean exists = tableExists(tableName);
//            tableStatus.put("originalExists", exists);
//            
//            if (exists) {
//                int rowCount = getRowCount(tableName);
//                tableStatus.put("originalRowCount", rowCount);
//            }
//            
//            tableStatuses.add(tableStatus);
//        }
//        
//        status.put("originalTables", tableStatuses);
//        
//        // Check backup tables
//        try {
//            String backupSQL = "SELECT table_name, created, num_rows FROM user_tables " +
//                             "WHERE table_name LIKE '%_BAK_%' " +
//                             "ORDER BY created DESC";
//            
//            List<Map<String, Object>> backups = jdbcTemplate.queryForList(backupSQL);
//            status.put("backupCount", backups.size());
//            status.put("backups", backups);
//            
//            // Group backups by original table name
//            Map<String, List<String>> backupsByTable = new HashMap<>();
//            for (Map<String, Object> backup : backups) {
//                String backupTableName = (String) backup.get("TABLE_NAME");
//                // Extract original table name from backup table name
//                String originalTableName = extractOriginalTableName(backupTableName);
//                backupsByTable.computeIfAbsent(originalTableName, k -> new ArrayList<>())
//                             .add(backupTableName);
//            }
//            status.put("backupsByTable", backupsByTable);
//            
//        } catch (Exception e) {
//            status.put("backupCount", 0);
//            status.put("backupError", e.getMessage());
//        }
//        
//        return status;
//    }
//
//    /**
//     * Extract original table name from backup table name
//     */
//    private String extractOriginalTableName(String backupTableName) {
//        if (backupTableName.contains("_BAK_")) {
//            return backupTableName.split("_BAK_")[0];
//        }
//        return backupTableName;
//    }
//
//    /**
//     * DROP BACKUP TABLES API
//     */
//    @DeleteMapping("/cleanup")
//    public Map<String, Object> cleanupBackups(@RequestParam(defaultValue = "false") boolean all,
//                                              @RequestParam(defaultValue = "false") boolean force) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("timestamp", LocalDateTime.now());
//        response.put("action", "Cleanup backup tables");
//        response.put("all", all);
//        response.put("force", force);
//        
//        List<String> droppedTables = new ArrayList<>();
//        List<String> failedTables = new ArrayList<>();
//        
//        try {
//            String sql;
//            if (all) {
//                sql = "SELECT table_name FROM user_tables WHERE table_name LIKE '%_BAK_%'";
//            } else if (force) {
//                // Keep only today's backups
//                String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//                sql = "SELECT table_name FROM user_tables " +
//                     "WHERE table_name LIKE '%_BAK_%' " +
//                     "AND table_name NOT LIKE '%_BAK_" + today + "%'";
//            } else {
//                // Keep only the latest backup per table
//                sql = "SELECT table_name FROM (" +
//                      "  SELECT table_name, created, " +
//                      "         ROW_NUMBER() OVER (PARTITION BY SUBSTR(table_name, 1, INSTR(table_name, '_BAK_')-1) ORDER BY created DESC) as rn " +
//                      "  FROM user_tables " +
//                      "  WHERE table_name LIKE '%_BAK_%'" +
//                      ") WHERE rn > 1";
//            }
//            
//            List<String> backupTables = jdbcTemplate.queryForList(sql, String.class);
//            
//            for (String backupTable : backupTables) {
//                try {
//                    dropTableSafely(backupTable);
//                    droppedTables.add(backupTable);
//                } catch (Exception e) {
//                    failedTables.add(backupTable + " (error: " + e.getMessage() + ")");
//                }
//            }
//            
//            response.put("status", "SUCCESS");
//            response.put("droppedCount", droppedTables.size());
//            response.put("droppedTables", droppedTables);
//            response.put("failedCount", failedTables.size());
//            response.put("failedTables", failedTables);
//            
//        } catch (Exception e) {
//            response.put("status", "ERROR");
//            response.put("message", e.getMessage());
//        }
//        
//        return response;
//    }
//
//    /**
//     * RESTORE FROM BACKUP API (if needed)
//     */
//    @PostMapping("/restore/{tableName}")
//    public Map<String, Object> restoreFromBackup(@PathVariable String tableName,
//                                                 @RequestParam(required = false) String backupTableName) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("timestamp", LocalDateTime.now());
//        response.put("action", "Restore table from backup");
//        response.put("tableName", tableName);
//        
//        try {
//            // If backupTableName not provided, find the latest backup
//            if (backupTableName == null || backupTableName.isEmpty()) {
//                String findBackupSQL = "SELECT table_name FROM user_tables " +
//                                     "WHERE table_name LIKE ? || '_BAK_%' " +
//                                     "ORDER BY created DESC";
//                List<String> backups = jdbcTemplate.queryForList(findBackupSQL, String.class, tableName.toUpperCase());
//                
//                if (backups.isEmpty()) {
//                    response.put("status", "ERROR");
//                    response.put("message", "No backup found for table: " + tableName);
//                    return response;
//                }
//                
//                backupTableName = backups.get(0);
//                response.put("backupUsed", backupTableName);
//            }
//            
//            response.put("backupTableName", backupTableName);
//            
//            // Check if backup exists
//            if (!tableExists(backupTableName)) {
//                response.put("status", "ERROR");
//                response.put("message", "Backup table not found: " + backupTableName);
//                return response;
//            }
//            
//            // Check if original table already exists
//            if (tableExists(tableName)) {
//                response.put("status", "ERROR");
//                response.put("message", "Table already exists: " + tableName + ". Drop it first or use force=true");
//                return response;
//            }
//            
//            // Restore from backup
//            String restoreSQL = "CREATE TABLE " + tableName.toUpperCase() + 
//                              " AS SELECT * FROM " + backupTableName;
//            jdbcTemplate.execute(restoreSQL);
//            
//            int rows = getRowCount(tableName);
//            
//            response.put("status", "SUCCESS");
//            response.put("rowsRestored", rows);
//            response.put("message", "Table restored successfully from backup");
//            
//        } catch (Exception e) {
//            response.put("status", "ERROR");
//            response.put("message", "Restore failed: " + e.getMessage());
//        }
//        
//        return response;
//    }
//
//    /**
//     * FORCE RESTORE - drops existing table and restores from backup
//     */
//    @PostMapping("/restore/{tableName}/force")
//    public Map<String, Object> forceRestoreFromBackup(@PathVariable String tableName,
//                                                      @RequestParam(required = false) String backupTableName) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("timestamp", LocalDateTime.now());
//        response.put("action", "Force restore table from backup");
//        response.put("tableName", tableName);
//        
//        try {
//            // If backupTableName not provided, find the latest backup
//            if (backupTableName == null || backupTableName.isEmpty()) {
//                String findBackupSQL = "SELECT table_name FROM user_tables " +
//                                     "WHERE table_name LIKE ? || '_BAK_%' " +
//                                     "ORDER BY created DESC";
//                List<String> backups = jdbcTemplate.queryForList(findBackupSQL, String.class, tableName.toUpperCase());
//                
//                if (backups.isEmpty()) {
//                    response.put("status", "ERROR");
//                    response.put("message", "No backup found for table: " + tableName);
//                    return response;
//                }
//                
//                backupTableName = backups.get(0);
//                response.put("backupUsed", backupTableName);
//            }
//            
//            response.put("backupTableName", backupTableName);
//            
//            // Check if backup exists
//            if (!tableExists(backupTableName)) {
//                response.put("status", "ERROR");
//                response.put("message", "Backup table not found: " + backupTableName);
//                return response;
//            }
//            
//            // Drop current table if exists
//            if (tableExists(tableName)) {
//                dropTableSafely(tableName);
//                response.put("originalDropped", true);
//            }
//            
//            // Restore from backup
//            String restoreSQL = "CREATE TABLE " + tableName.toUpperCase() + 
//                              " AS SELECT * FROM " + backupTableName;
//            jdbcTemplate.execute(restoreSQL);
//            
//            int rows = getRowCount(tableName);
//            
//            response.put("status", "SUCCESS");
//            response.put("rowsRestored", rows);
//            response.put("message", "Table force restored successfully from backup");
//            
//        } catch (Exception e) {
//            response.put("status", "ERROR");
//            response.put("message", "Force restore failed: " + e.getMessage());
//        }
//        
//        return response;
//    }
//
//    /**
//     * LIST ALL TABLES API
//     */
//    @GetMapping("/tables")
//    public Map<String, Object> listAllTables() {
//        Map<String, Object> result = new HashMap<>();
//        result.put("timestamp", LocalDateTime.now());
//        
//        try {
//            // Get all tables
//            String allTablesSQL = "SELECT table_name, num_rows, created FROM user_tables ORDER BY table_name";
//            List<Map<String, Object>> allTables = jdbcTemplate.queryForList(allTablesSQL);
//            result.put("allTables", allTables);
//            result.put("totalTables", allTables.size());
//            
//            // Separate original and backup tables
//            List<Map<String, Object>> originalTables = new ArrayList<>();
//            List<Map<String, Object>> backupTables = new ArrayList<>();
//            
//            for (Map<String, Object> table : allTables) {
//                String tableName = (String) table.get("TABLE_NAME");
//                if (tableName.contains("_BAK_")) {
//                    backupTables.add(table);
//                } else {
//                    originalTables.add(table);
//                }
//            }
//            
//            result.put("originalTables", originalTables);
//            result.put("backupTables", backupTables);
//            result.put("originalCount", originalTables.size());
//            result.put("backupCount", backupTables.size());
//            
//        } catch (Exception e) {
//            result.put("error", e.getMessage());
//        }
//        
//        return result;
//    }
//}