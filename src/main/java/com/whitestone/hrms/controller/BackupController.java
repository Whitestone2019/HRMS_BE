/*
 * // File: src/main/java/com/whitestone/hrms/controller/BackupController.java
 * package com.whitestone.hrms.controller;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.jdbc.core.JdbcTemplate; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import javax.annotation.PostConstruct; import java.time.LocalDateTime; import
 * java.time.format.DateTimeFormatter; import java.util.ArrayList; import
 * java.util.HashSet; import java.util.List; import java.util.Map; import
 * java.util.Set; import java.util.UUID;
 * 
 * @RestController public class BackupController {
 * 
 * @Autowired private JdbcTemplate jdbcTemplate;
 * 
 *//**
	 * THIS RUNS AUTOMATICALLY WHEN APPLICATION STARTS No API endpoints needed - it
	 * just runs on startup
	 */
/*
 * @PostConstruct public void autoBackupAndRecreateTable() { System.out.println(
 * "╔══════════════════════════════════════════════════════════╗"); System.out.
 * println("║           AUTO BACKUP & RESTORE PROCESS STARTING         ║");
 * System.out.println(
 * "╚══════════════════════════════════════════════════════════╝");
 * 
 * String tableName = "employee_leave_summary";
 * 
 * try { // STEP 1: Get table structure BEFORE checking existence
 * List<Map<String, Object>> tableColumns = getTableStructure(tableName);
 * 
 * if (tableColumns.isEmpty()) { System.out.println("Table '" + tableName +
 * "' doesn't exist or has no columns. Creating fresh table with ENTITY structure."
 * ); createFreshTableWithEntityStructure(tableName);
 * System.out.println("✅ Fresh table created successfully with ENTITY fields!");
 * return; }
 * 
 * // STEP 2: Create backup table with timestamp String backupTableName =
 * createOracleBackupTable(tableName);
 * 
 * // STEP 3: Copy ALL data from original to backup int rowsBackedUp =
 * copyDataToOracleBackup(tableName, backupTableName);
 * 
 * // STEP 4: DROP original table dropOracleTable(tableName);
 * 
 * // STEP 5: Create FRESH new table with EXACT same structure boolean
 * tableCreated = createFreshOracleTableWithSameStructure(tableName,
 * tableColumns);
 * 
 * if (!tableCreated) {
 * System.out.println("❌ Failed to create fresh table. Restoring from backup..."
 * ); restoreTableFromBackup(backupTableName, tableName); return; }
 * 
 * // STEP 6: RESTORE data from backup table to fresh table int rowsRestored =
 * restoreDataFromBackup(backupTableName, tableName, tableColumns);
 * 
 * // STEP 7: Print summary printSummary(tableName, backupTableName,
 * rowsBackedUp, rowsRestored, tableColumns.size());
 * 
 * } catch (Exception e) {
 * System.err.println("❌ AUTO BACKUP & RESTORE FAILED!");
 * System.err.println("Error: " + e.getMessage()); e.printStackTrace(); } }
 * 
 *//**
	 * Get table structure/columns from Oracle
	 */
/*
 * private List<Map<String, Object>> getTableStructure(String tableName) {
 * List<Map<String, Object>> columns = new ArrayList<>();
 * 
 * try { // Oracle system view to get column information String sql = "SELECT "
 * + "    column_name, " + "    data_type, " + "    data_length, " +
 * "    data_precision, " + "    data_scale, " + "    nullable, " +
 * "    data_default " + "FROM all_tab_columns " +
 * "WHERE table_name = UPPER(?) " + "ORDER BY column_id";
 * 
 * columns = jdbcTemplate.queryForList(sql, tableName.toUpperCase());
 * 
 * if (!columns.isEmpty()) { System.out.println("📋 Found table structure with "
 * + columns.size() + " columns:"); for (Map<String, Object> column : columns) {
 * System.out.println("   - " + column.get("COLUMN_NAME") + " : " +
 * column.get("DATA_TYPE")); } }
 * 
 * } catch (Exception e) {
 * System.out.println("ℹ️ Could not get table structure (table may not exist): "
 * + e.getMessage()); }
 * 
 * return columns; }
 * 
 *//**
	 * Create backup table with timestamp (Oracle)
	 */
/*
 * private String createOracleBackupTable(String originalTableName) { //
 * Generate UNIQUE backup table name with multiple fallbacks String
 * backupTableName = generateUniqueBackupName(originalTableName);
 * 
 * System.out.println("📦 Step 1: Creating backup table: " + backupTableName);
 * 
 * // Drop backup table if it already exists (to avoid conflicts)
 * safeDropTable(backupTableName);
 * 
 * // Oracle syntax - CREATE TABLE AS SELECT (CTAS) - creates table with SAME
 * STRUCTURE AND DATA try { String createSQL = "CREATE TABLE " + backupTableName
 * + " AS SELECT * FROM " + originalTableName.toUpperCase();
 * 
 * jdbcTemplate.execute(createSQL);
 * System.out.println("   ✅ Backup table created with all data");
 * 
 * } catch (Exception e) { System.err.println("   ❌ Failed to create backup: " +
 * e.getMessage()); // Try alternative method backupTableName =
 * createBackupAlternative(originalTableName); }
 * 
 * return backupTableName; }
 * 
 *//**
	 * Generate unique backup table name with fallbacks
	 */
/*
 * private String generateUniqueBackupName(String originalTableName) { try { //
 * Method 1: Try with timestamp String timestamp =
 * LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
 * String name = originalTableName.toUpperCase() + "_BAK_" + timestamp;
 * 
 * // Oracle limit: 30 characters if (name.length() <= 30) { return name; } }
 * catch (Exception e) { System.out.println("⚠️  Timestamp generation failed: "
 * + e.getMessage()); }
 * 
 * // Method 2: Use UUID if timestamp fails String uuid =
 * UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
 * String name = originalTableName.toUpperCase() + "_BAK_" + uuid;
 * 
 * // Ensure <= 30 characters return name.length() > 30 ? name.substring(0, 30)
 * : name; }
 * 
 *//**
	 * Alternative backup method
	 */
/*
 * private String createBackupAlternative(String originalTableName) { String
 * backupTableName = originalTableName.toUpperCase() + "_BAK_" +
 * System.currentTimeMillis() % 1000000;
 * 
 * if (backupTableName.length() > 30) { backupTableName =
 * backupTableName.substring(0, 30); }
 * 
 * safeDropTable(backupTableName);
 * 
 * // Create empty table with structure only String createSQL = "CREATE TABLE "
 * + backupTableName + " AS SELECT * FROM " + originalTableName.toUpperCase() +
 * " WHERE 1=0"; jdbcTemplate.execute(createSQL);
 * 
 * // Copy data String copySQL = "INSERT INTO " + backupTableName +
 * " SELECT * FROM " + originalTableName.toUpperCase(); int rows =
 * jdbcTemplate.update(copySQL); jdbcTemplate.execute("COMMIT");
 * 
 * System.out.println("   ✅ Alternative backup created with " + rows + " rows");
 * return backupTableName; }
 * 
 *//**
	 * Safely drop table - FIXED: Use String concatenation instead of parameter
	 */
/*
 * private void safeDropTable(String tableName) { try { // Use dynamic SQL with
 * exception handling - FIXED: Concatenate table name String dropSQL = "BEGIN "
 * + "   EXECUTE IMMEDIATE 'DROP TABLE ' || '" + tableName.toUpperCase() +
 * "' || ' CASCADE CONSTRAINTS'; " + "EXCEPTION " + "   WHEN OTHERS THEN " +
 * "      IF SQLCODE != -942 THEN " + // -942 = table does not exist
 * "         RAISE; " + "      END IF; " + "END;";
 * 
 * jdbcTemplate.execute(dropSQL); } catch (Exception e) {
 * System.out.println("Note: Could not drop " + tableName + " - " +
 * e.getMessage()); } }
 * 
 *//**
	 * Copy ALL data from original table to backup table (Oracle)
	 */
/*
 * private int copyDataToOracleBackup(String originalTableName, String
 * backupTableName) {
 * System.out.println("📊 Step 2: Getting row count from backup...");
 * 
 * // Since we used CREATE TABLE AS SELECT, data is already copied // Just get
 * the count to show in summary try { String countSQL = "SELECT COUNT(*) FROM "
 * + backupTableName; int rowsCopied = jdbcTemplate.queryForObject(countSQL,
 * Integer.class);
 * 
 * System.out.println("   ✅ Backup contains " + rowsCopied + " rows"); return
 * rowsCopied; } catch (Exception e) {
 * System.err.println("   ❌ Could not count rows in backup: " + e.getMessage());
 * return 0; } }
 * 
 *//**
	 * DROP the original table completely (Oracle)
	 */
/*
 * private void dropOracleTable(String tableName) {
 * System.out.println("🗑️  Step 3: Dropping original table '" + tableName +
 * "'...");
 * 
 * // Use safe drop method safeDropTable(tableName);
 * System.out.println("   ✅ Original table dropped (if existed)"); }
 * 
 *//**
	 * Create FRESH new table with EXACT same structure as original
	 */
/*
 * private boolean createFreshOracleTableWithSameStructure(String tableName,
 * List<Map<String, Object>> columns) {
 * System.out.println("🆕 Step 4: Creating fresh table with same structure...");
 * 
 * if (columns.isEmpty()) {
 * System.out.println("❌ No columns found! Creating ENTITY structure.");
 * createFreshTableWithEntityStructure(tableName); return true; }
 * 
 * try { // First, get column names to check if EMP_ID and YEAR exist
 * Set<String> columnNames = new HashSet<>(); for (Map<String, Object> column :
 * columns) { columnNames.add(((String)
 * column.get("COLUMN_NAME")).toUpperCase()); }
 * 
 * // Build CREATE TABLE SQL with all original columns StringBuilder createSQL =
 * new StringBuilder();
 * createSQL.append("CREATE TABLE ").append(tableName.toUpperCase()).
 * append(" (\n");
 * 
 * List<String> columnDefinitions = new ArrayList<>();
 * 
 * // Add all columns with their original definitions for (Map<String, Object>
 * column : columns) { String columnName = ((String)
 * column.get("COLUMN_NAME")).toUpperCase(); String dataType = ((String)
 * column.get("DATA_TYPE")).toUpperCase(); String nullable =
 * "Y".equals(column.get("NULLABLE")) ? "NULL" : "NOT NULL"; Object dataDefault
 * = column.get("DATA_DEFAULT");
 * 
 * // Build column definition StringBuilder columnDef = new StringBuilder();
 * columnDef.append("    ").append(columnName).append(" ").append(dataType);
 * 
 * // Add length/precision for certain data types if (dataType.contains("CHAR")
 * || dataType.equals("VARCHAR2") || dataType.equals("NVARCHAR2")) { Object
 * dataLength = column.get("DATA_LENGTH"); if (dataLength != null) {
 * columnDef.append("(").append(dataLength).append(")"); } } else if
 * (dataType.equals("NUMBER")) { Object dataPrecision =
 * column.get("DATA_PRECISION"); Object dataScale = column.get("DATA_SCALE"); if
 * (dataPrecision != null) { columnDef.append("(").append(dataPrecision); if
 * (dataScale != null && Integer.parseInt(dataScale.toString()) != 0) {
 * columnDef.append(",").append(dataScale); } columnDef.append(")"); } }
 * 
 * // Add DEFAULT value if exists if (dataDefault != null) { String defaultValue
 * = dataDefault.toString().trim(); if (!defaultValue.isEmpty() &&
 * !defaultValue.equalsIgnoreCase("null")) { defaultValue =
 * cleanDefaultValue(defaultValue);
 * columnDef.append(" DEFAULT ").append(defaultValue); } }
 * 
 * // Add NULL/NOT NULL constraint columnDef.append(" ").append(nullable);
 * 
 * columnDefinitions.add(columnDef.toString()); }
 * 
 * // Add all column definitions createSQL.append(String.join(",\n",
 * columnDefinitions));
 * 
 * // Add unique constraint ONLY if both EMP_ID and YEAR columns exist if
 * (columnNames.contains("EMP_ID") && columnNames.contains("YEAR")) {
 * createSQL.append(",\n    CONSTRAINT UNQ_EMP_YEAR UNIQUE (EMP_ID, YEAR)"); }
 * 
 * createSQL.append("\n)");
 * 
 * System.out.println("Creating table with " + columns.size() + " columns...");
 * 
 * // Execute the create table jdbcTemplate.execute(createSQL.toString());
 * System.out.println("   ✅ Fresh table created with " + columns.size() +
 * " columns");
 * 
 * // Create indexes if columns exist try { if (columnNames.contains("EMP_ID"))
 * { jdbcTemplate.execute( "CREATE INDEX IDX_" + tableName.toUpperCase() +
 * "_EMPID ON " + tableName.toUpperCase() + " (EMP_ID)" ); }
 * 
 * if (columnNames.contains("YEAR")) { jdbcTemplate.execute( "CREATE INDEX IDX_"
 * + tableName.toUpperCase() + "_YEAR ON " + tableName.toUpperCase() + " (YEAR)"
 * ); } System.out.println("   ✅ Indexes created for existing columns"); } catch
 * (Exception e) { System.out.println("   ℹ️ Could not create indexes: " +
 * e.getMessage()); }
 * 
 * return true;
 * 
 * } catch (Exception e) { System.err.println("❌ Failed to create table: " +
 * e.getMessage()); System.err.println("SQL Error: " + e.getMessage());
 * 
 * // Fallback to entity structure
 * System.out.println("🔄 Falling back to entity structure...");
 * createFreshTableWithEntityStructure(tableName); return true; } }
 * 
 *//**
	 * NEW METHOD: Restore data from backup table to fresh table - IMPROVED VERSION
	 */
/*
 * private int restoreDataFromBackup(String backupTableName, String
 * freshTableName, List<Map<String, Object>> originalColumns) {
 * System.out.println("🔄 Step 5: Restoring data from backup to fresh table..."
 * );
 * 
 * if (originalColumns == null || originalColumns.isEmpty()) {
 * System.out.println("   ⚠️  No columns found, cannot restore data"); return 0;
 * }
 * 
 * try { // Get columns from backup table (actual backup) String
 * backupColumnsSQL = "SELECT column_name FROM all_tab_columns " +
 * "WHERE table_name = UPPER(?) ORDER BY column_id"; List<String> backupColumns
 * = jdbcTemplate.queryForList(backupColumnsSQL, String.class, backupTableName);
 * 
 * // Get columns from fresh table (newly created) String freshColumnsSQL =
 * "SELECT column_name FROM all_tab_columns " +
 * "WHERE table_name = UPPER(?) ORDER BY column_id"; List<String> freshColumns =
 * jdbcTemplate.queryForList(freshColumnsSQL, String.class, freshTableName);
 * 
 * System.out.println("   📋 Backup columns: " + backupColumns.size());
 * System.out.println("   📋 Fresh columns: " + freshColumns.size());
 * 
 * // Find common columns between backup and fresh table List<String>
 * commonColumns = new ArrayList<>(); for (String freshCol : freshColumns) { if
 * (backupColumns.contains(freshCol)) { commonColumns.add(freshCol); } }
 * 
 * System.out.println("   📋 Common columns found: " + commonColumns.size());
 * 
 * if (commonColumns.isEmpty()) {
 * System.out.println("   ❌ No common columns found! Cannot restore data.");
 * return 0; }
 * 
 * // Build column list for INSERT statement String columnList =
 * String.join(", ", commonColumns);
 * 
 * // Build INSERT statement to copy data from backup to fresh table String
 * insertSQL = "INSERT INTO " + freshTableName.toUpperCase() + " (" + columnList
 * + ") " + "SELECT " + columnList + " FROM " + backupTableName;
 * 
 * System.out.println("   📝 Executing data restore with columns: " +
 * columnList);
 * 
 * // Execute the insert int rowsRestored = jdbcTemplate.update(insertSQL);
 * 
 * // Commit the transaction jdbcTemplate.execute("COMMIT");
 * 
 * System.out.println("   ✅ Successfully restored " + rowsRestored + " rows");
 * 
 * // Verify the restore verifyDataRestore(backupTableName, freshTableName,
 * rowsRestored);
 * 
 * return rowsRestored;
 * 
 * } catch (Exception e) { System.err.println("   ❌ Failed to restore data: " +
 * e.getMessage()); e.printStackTrace();
 * 
 * // Try simple restore as last resort try {
 * System.out.println("   🔄 Trying simple restore (SELECT *)..."); String
 * simpleInsertSQL = "INSERT INTO " + freshTableName.toUpperCase() +
 * " SELECT * FROM " + backupTableName; int rows =
 * jdbcTemplate.update(simpleInsertSQL); jdbcTemplate.execute("COMMIT");
 * System.out.println("   ✅ Simple restore completed: " + rows + " rows");
 * return rows; } catch (Exception ex) {
 * System.err.println("   ❌ Simple restore also failed: " + ex.getMessage());
 * return 0; } } }
 * 
 *//**
	 * Verify data restoration
	 */
/*
 * private void verifyDataRestore(String backupTableName, String freshTableName,
 * int expectedRows) { try { // Count rows in fresh table String freshCountSQL =
 * "SELECT COUNT(*) FROM " + freshTableName.toUpperCase(); int freshRows =
 * jdbcTemplate.queryForObject(freshCountSQL, Integer.class);
 * 
 * // Count rows in backup table String backupCountSQL = "SELECT COUNT(*) FROM "
 * + backupTableName; int backupRows =
 * jdbcTemplate.queryForObject(backupCountSQL, Integer.class);
 * 
 * System.out.println("   🔍 Verification:");
 * System.out.println("     - Backup rows: " + backupRows);
 * System.out.println("     - Fresh rows: " + freshRows);
 * System.out.println("     - Expected: " + expectedRows);
 * 
 * if (freshRows == backupRows && freshRows == expectedRows) {
 * System.out.println("     ✅ Data restoration verified successfully!"); } else
 * { System.out.println("     ⚠️  Data mismatch detected!");
 * System.out.println("     ℹ️  Some data may not have been restored properly");
 * }
 * 
 * } catch (Exception e) { System.err.println("   ❌ Verification failed: " +
 * e.getMessage()); } }
 * 
 *//**
	 * Fallback method to restore entire table from backup
	 */
/*
 * private void restoreTableFromBackup(String backupTableName, String tableName)
 * { System.out.println("🔄 EMERGENCY: Restoring entire table from backup...");
 * 
 * try { // First, drop the fresh table if it exists safeDropTable(tableName);
 * 
 * // Recreate table from backup using CTAS String restoreSQL = "CREATE TABLE "
 * + tableName.toUpperCase() + " AS SELECT * FROM " + backupTableName;
 * 
 * jdbcTemplate.execute(restoreSQL);
 * 
 * // Get row count String countSQL = "SELECT COUNT(*) FROM " +
 * tableName.toUpperCase(); int rows = jdbcTemplate.queryForObject(countSQL,
 * Integer.class);
 * 
 * System.out.println("   ✅ Emergency restore completed! Table restored with " +
 * rows + " rows"); System.out.
 * println("   ⚠️  Note: Table structure may not match entity structure");
 * 
 * } catch (Exception e) { System.err.println("   ❌ Emergency restore failed: "
 * + e.getMessage()); } }
 * 
 *//**
	 * Clean default values from Oracle
	 */
/*
 * private String cleanDefaultValue(String defaultValue) { defaultValue =
 * defaultValue.replace("\"", "").replace("'", "").trim();
 * 
 * if (defaultValue.endsWith(";")) { defaultValue = defaultValue.substring(0,
 * defaultValue.length() - 1); }
 * 
 * // Handle Oracle functions if (defaultValue.equalsIgnoreCase("SYSDATE")) {
 * return "SYSDATE"; } else if (defaultValue.equalsIgnoreCase("SYSTIMESTAMP")) {
 * return "SYSTIMESTAMP"; } else if
 * (defaultValue.equalsIgnoreCase("CURRENT_TIMESTAMP")) { return
 * "CURRENT_TIMESTAMP"; }
 * 
 * return defaultValue; }
 * 
 *//**
	 * Create FRESH table with YOUR ENTITY STRUCTURE
	 */
/*
 * private void createFreshTableWithEntityStructure(String tableName) {
 * System.out.println("🆕 Creating fresh table with ENTITY structure...");
 * 
 * // Drop table if exists safeDropTable(tableName);
 * 
 * // CREATE TABLE SQL based on YOUR ENTITY CLASS String createSQL =
 * "CREATE TABLE " + tableName.toUpperCase() + " (" +
 * "    ID NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY," +
 * "    EMP_ID VARCHAR2(255) NOT NULL," + // From @Column(name = "emp_id")
 * "    YEAR NUMBER(10) NOT NULL," + // From int year
 * "    CASUAL_LEAVE_BALANCE NUMBER(5,1) DEFAULT 18.0 NOT NULL," + //
 * DECIMAL(5,1) DEFAULT 18.0 "    LEAVE_TAKEN NUMBER(5,1) DEFAULT 0.0 NOT NULL,"
 * + // DECIMAL(5,1) DEFAULT 0.0 "    LOP NUMBER(5,1) DEFAULT 0.0 NOT NULL," +
 * // DECIMAL(5,1) DEFAULT 0.0 "    LOP_JAN NUMBER(5,1) DEFAULT 0.0 NOT NULL," +
 * "    LOP_FEB NUMBER(5,1) DEFAULT 0.0 NOT NULL," +
 * "    LOP_MAR NUMBER(5,1) DEFAULT 0.0 NOT NULL," +
 * "    LOP_APR NUMBER(5,1) DEFAULT 0.0 NOT NULL," +
 * "    LOP_MAY NUMBER(5,1) DEFAULT 0.0 NOT NULL," +
 * "    LOP_JUN NUMBER(5,1) DEFAULT 0.0 NOT NULL," +
 * "    LOP_JUL NUMBER(5,1) DEFAULT 0.0 NOT NULL," +
 * "    LOP_AUG NUMBER(5,1) DEFAULT 0.0 NOT NULL," +
 * "    LOP_SEP NUMBER(5,1) DEFAULT 0.0 NOT NULL," +
 * "    LOP_OCT NUMBER(5,1) DEFAULT 0.0 NOT NULL," +
 * "    LOP_NOV NUMBER(5,1) DEFAULT 0.0 NOT NULL," +
 * "    LOP_DEC NUMBER(5,1) DEFAULT 0.0 NOT NULL," +
 * "    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP," + // LocalDateTime
 * "    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP," + // LocalDateTime
 * "    CONSTRAINT UNQ_EMP_YEAR UNIQUE (EMP_ID, YEAR)" + //
 * From @UniqueConstraint ")";
 * 
 * try { jdbcTemplate.execute(createSQL);
 * System.out.println("   ✅ Fresh table created with 19 ENTITY columns");
 * 
 * // Create indexes for entity createEntityIndexes(tableName);
 * 
 * } catch (Exception e) {
 * System.err.println("❌ Failed to create entity table: " + e.getMessage());
 * throw e; } }
 * 
 *//**
	 * Create indexes for entity structure
	 */
/*
 * private void createEntityIndexes(String tableName) { try { // Create index on
 * emp_id jdbcTemplate.execute( "CREATE INDEX IDX_" + tableName.toUpperCase() +
 * "_EMPID ON " + tableName.toUpperCase() + " (EMP_ID)" );
 * 
 * // Create index on year jdbcTemplate.execute( "CREATE INDEX IDX_" +
 * tableName.toUpperCase() + "_YEAR ON " + tableName.toUpperCase() + " (YEAR)"
 * );
 * 
 * System.out.println("   ✅ Entity indexes created (EMP_ID, YEAR)"); } catch
 * (Exception e) { System.out.println("   ℹ️ Could not create entity indexes: "
 * + e.getMessage()); } }
 * 
 *//**
	 * Print final summary
	 */
/*
 * private void printSummary(String originalTableName, String backupTableName,
 * int rowsBackedUp, int rowsRestored, int columnCount) {
 * System.out.println("\n" + "=".repeat(70));
 * System.out.println("✅ AUTO BACKUP & RESTORE COMPLETED SUCCESSFULLY!");
 * System.out.println("=".repeat(70)); System.out.println("📋 SUMMARY:");
 * System.out.println("   Fresh Table: " + originalTableName.toUpperCase() +
 * " (with restored data)"); System.out.println("   Columns: " + columnCount +
 * " columns"); System.out.println("   Backup Table: " + backupTableName +
 * " (preserved for future reference)");
 * System.out.println("   Rows Backed Up: " + rowsBackedUp);
 * System.out.println("   Rows Restored: " + rowsRestored);
 * 
 * if (rowsBackedUp > 0 && rowsRestored > 0) { if (rowsBackedUp == rowsRestored)
 * { System.out.println("   ✅ All data successfully restored!"); } else {
 * System.out.println("   ⚠️  Data mismatch: " + (rowsBackedUp - rowsRestored) +
 * " rows not restored"); } }
 * 
 * System.out.println("   Process Time: " + LocalDateTime.now());
 * System.out.println("=".repeat(70));
 * 
 * // Final verification try { int finalRowCount = jdbcTemplate.queryForObject(
 * "SELECT COUNT(*) FROM " + originalTableName.toUpperCase(), Integer.class );
 * System.out.println("   🔍 FINAL VERIFICATION: " +
 * originalTableName.toUpperCase() + " contains " + finalRowCount + " rows");
 * 
 * if (finalRowCount > 0) {
 * System.out.println("   ✅ DATA IS PRESENT IN THE TABLE!"); } else {
 * System.out.println("   ⚠️  TABLE IS EMPTY - Check backup table: " +
 * backupTableName); } } catch (Exception e) {
 * System.err.println("   ❌ Could not verify final table: " + e.getMessage()); }
 * 
 * System.out.println("=".repeat(70));
 * 
 * System.out.println("\n📌 VERIFICATION COMMANDS:");
 * System.out.println("   -- Check backup data (preserved):");
 * System.out.println("   SELECT COUNT(*) AS BACKUP_ROWS FROM " +
 * backupTableName + ";"); System.out.println("   SELECT * FROM " +
 * backupTableName + " WHERE ROWNUM <= 3;");
 * System.out.println("\n   -- Check restored data in fresh table:");
 * System.out.println("   SELECT COUNT(*) AS CURRENT_ROWS FROM " +
 * originalTableName.toUpperCase() + ";");
 * System.out.println("   SELECT * FROM " + originalTableName.toUpperCase() +
 * " WHERE ROWNUM <= 3;"); System.out.println("   DESC " +
 * originalTableName.toUpperCase() + ";");
 * System.out.println("\n📌 IMPORTANT:");
 * System.out.println("   • Backup table '" + backupTableName +
 * "' is preserved");
 * System.out.println("   • You can manually drop it later if not needed");
 * System.out.println("   • Data integrity verified: " + rowsRestored + "/" +
 * rowsBackedUp + " rows restored"); System.out.println("=".repeat(70)); }
 * 
 *//**
	 * MANUAL BACKUP TRIGGER - API Endpoint
	 */
/*
 * @org.springframework.web.bind.annotation.PostMapping("/api/backup/trigger")
 * public String triggerManualBackup() { try {
 * System.out.println("=== MANUAL BACKUP & RESTORE REQUESTED ===");
 * autoBackupAndRecreateTable(); return
 * "Manual backup & restore completed successfully!"; } catch (Exception e) {
 * return "Manual backup & restore failed: " + e.getMessage(); } }
 * 
 *//**
	 * CHECK TABLE STATUS - API Endpoint
	 */
/*
 * @org.springframework.web.bind.annotation.GetMapping("/api/backup/status")
 * public String checkTableStatus() { String tableName =
 * "employee_leave_summary";
 * 
 * try { boolean exists = tableExists(tableName); int rowCount = 0; int
 * columnCount = 0;
 * 
 * if (exists) { try { rowCount = jdbcTemplate.queryForObject(
 * "SELECT COUNT(*) FROM " + tableName.toUpperCase(), Integer.class ); } catch
 * (Exception e) { rowCount = -1; }
 * 
 * try { columnCount = jdbcTemplate.queryForList(
 * "SELECT column_name FROM all_tab_columns WHERE table_name = ?", String.class,
 * tableName.toUpperCase() ).size(); } catch (Exception e) { columnCount = -1; }
 * }
 * 
 * StringBuilder backupInfo = new StringBuilder(); try { String backupSQL =
 * "SELECT table_name, created FROM all_tables " + "WHERE table_name LIKE '" +
 * tableName.toUpperCase() + "_BAK%' " + "OR table_name LIKE '" +
 * tableName.toUpperCase() + "_BACKUP%' " + "ORDER BY created DESC";
 * 
 * List<Map<String, Object>> backups = jdbcTemplate.queryForList(backupSQL); if
 * (!backups.isEmpty()) {
 * backupInfo.append("\nBackup Tables Found: ").append(backups.size());
 * 
 * for (int i = 0; i < Math.min(backups.size(), 3); i++) { String backupTable =
 * (String) backups.get(i).get("TABLE_NAME"); Object created =
 * backups.get(i).get("CREATED");
 * 
 * try { int backupRows = jdbcTemplate.queryForObject( "SELECT COUNT(*) FROM " +
 * backupTable, Integer.class ); backupInfo.append("\n  ").append(i +
 * 1).append(". ").append(backupTable)
 * .append(" (").append(backupRows).append(" rows, created: ").append(created).
 * append(")"); } catch (Exception e) { backupInfo.append("\n  ").append(i +
 * 1).append(". ").append(backupTable)
 * .append(" (created: ").append(created).append(")"); } }
 * 
 * if (backups.size() > 3) {
 * backupInfo.append("\n  ... and ").append(backups.size() -
 * 3).append(" more backups"); } } else {
 * backupInfo.append("\nNo backup tables found."); } } catch (Exception e) {
 * backupInfo.append("\nError checking backups: ").append(e.getMessage()); }
 * 
 * return String.format( "Table Status Report:\n" + "====================\n" +
 * "Table Name: %s\n" + "Exists: %s\n" + "Row Count: %s\n" +
 * "Column Count: %s\n" + "%s\n" +
 * "\nNote: Auto-backup runs on application startup and preserves backup tables."
 * , tableName.toUpperCase(), exists ? "YES" : "NO", rowCount >= 0 ? rowCount :
 * "Error counting", columnCount >= 0 ? columnCount : "Error counting",
 * backupInfo.toString() );
 * 
 * } catch (Exception e) { return "Error checking table status: " +
 * e.getMessage(); } }
 * 
 *//**
	 * Helper method to check if table exists
	 *//*
		 * private boolean tableExists(String tableName) { try { String sql =
		 * "SELECT COUNT(*) FROM user_tables WHERE table_name = ?"; int count =
		 * jdbcTemplate.queryForObject(sql, Integer.class, tableName.toUpperCase());
		 * return count > 0; } catch (Exception e) { return false; } } }
		 */