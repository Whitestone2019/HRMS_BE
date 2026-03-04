package com.whitestone.hrms.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.whitestone.entity.EmployeeProfile;
import com.whitestone.entity.EmployeeSalaryTbl;
import com.whitestone.entity.PayrollAdjustment;
import com.whitestone.hrms.repo.EmployeeProfileRepository;
import com.whitestone.hrms.repo.EmployeeSalaryTblRepository;
import com.whitestone.hrms.repo.PayrollAdjustmentRepository;

@Component
public class PayslipGenerator {

    @Autowired
    private EmployeeSalaryTblRepository employeeSalaryTblRepository;

    @Autowired
    private EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    private PayrollAdjustmentRepository payrollAdjustmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Define fonts
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLACK);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.BLACK);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
    private static final Font BODY_FONT = new Font(Font.FontFamily.HELVETICA, 10);
    private static final Font FOOTER_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);

    public byte[] createPayslipPdf(String employeeId, String month) {
        
        // Parse month and year from input (e.g., "February 2026")
        String[] parts = month.split("\\s+");
        if (parts.length != 2) {
            throw new RuntimeException("Invalid month format. Use 'Month YYYY' (e.g., February 2026)");
        }
        
        String monthName = parts[0];
        int year = Integer.parseInt(parts[1]);
        
        Month requestedMonth;
        try {
            requestedMonth = Month.valueOf(monthName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid month: " + monthName);
        }

        // Format month for payroll_adjustments query (YYYY-MM format)
        String monthYearStr = String.format("%04d-%02d", year, requestedMonth.getValue());
        
        System.out.println("========== PAYSLIP GENERATION ==========");
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Requested Month: " + month);
        System.out.println("Formatted Month for DB: " + monthYearStr);

        // STEP 1: Check if employee exists in EmployeeSalaryTbl
        EmployeeSalaryTbl employeeSalary = employeeSalaryTblRepository.findByEmpid(employeeId);
        if (employeeSalary == null) {
            throw new RuntimeException("Employee not found for ID: " + employeeId);
        }
        System.out.println("✓ Employee found in EmployeeSalaryTbl");

        // STEP 2: FIXED - Check if employee was employed DURING the requested month
        // Now checks if employee joined BEFORE the END of the month
        LocalDate doj = employeeSalary.getDateOfJoin();
        LocalDate lastDayOfMonth = LocalDate.of(year, requestedMonth, requestedMonth.length(YearMonth.of(year, requestedMonth).isLeapYear()));
        
        System.out.println("Date of Joining: " + doj);
        System.out.println("Last day of requested month: " + lastDayOfMonth);
        
        if (doj != null && doj.isAfter(lastDayOfMonth)) {
            // Employee joined AFTER the month ended
            throw new RuntimeException("Employee was not employed during " + month + " (Joined on " + doj + ")");
        }
        
        if (doj != null && doj.isAfter(LocalDate.of(year, requestedMonth, 1))) {
            // Employee joined DURING the month - calculate pro-rated salary
            System.out.println("⚠ Employee joined during this month on: " + doj);
            System.out.println("Will generate payslip from join date onwards");
        } else {
            System.out.println("✓ Employee was employed for full month");
        }

        // STEP 3: Check Payroll Adjustment table for the specific month
        PayrollAdjustment payrollAdjustment = null;
        try {
            System.out.println("Querying for: Employee ID = " + employeeId + ", Month = " + monthYearStr);
            payrollAdjustment = payrollAdjustmentRepository.findByEmpIdAndMonth(employeeId, monthYearStr);
            
            if (payrollAdjustment != null) {
                System.out.println("✓ Found payroll adjustment data");
                System.out.println("  - LOP Days: " + payrollAdjustment.getLopDays());
                System.out.println("  - Other Deductions: " + payrollAdjustment.getOtherDeductions());
            } else {
                System.out.println("ℹ No payroll adjustment found for this month");
            }
        } catch (Exception e) {
            System.out.println("⚠ Error checking payroll adjustment: " + e.getMessage());
        }

        // STEP 4: Fetch Employee Profile for PAN, UAN
        Optional<EmployeeProfile> employeeProfileOpt = employeeProfileRepository.findByEmpId(employeeId);
        EmployeeProfile employeeProfile = employeeProfileOpt.orElse(null);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // Header with Logo & Company Details
            addHeader(document);

            // Payslip Title
            Paragraph subtitle = new Paragraph("Payslip for the Month of " + month, SUBTITLE_FONT);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);
            document.add(Chunk.NEWLINE);

            // Employee Details Table
            addEmployeeDetails(document, employeeId, employeeSalary, employeeProfile, 
                             payrollAdjustment, requestedMonth, year, doj);

            // Get earnings and deductions JSON from EmployeeSalaryTbl
            String earningsJson = employeeSalary.getEarnings();
            String deductionsJson = employeeSalary.getDeductions();
            String payrollDeductionsJson = employeeSalary.getPayrollDeductions();

            // Clean and parse JSON
            earningsJson = cleanJsonString(earningsJson);
            deductionsJson = cleanJsonString(deductionsJson);
            payrollDeductionsJson = cleanJsonString(payrollDeductionsJson);

            List<Map<String, Object>> earnings = parseJsonToList(earningsJson);
            List<Map<String, Object>> deductions = parseJsonToList(deductionsJson);
            List<Map<String, Object>> payrollDeductions = parseJsonToList(payrollDeductionsJson);

            // Process Salary and Deductions
            double totalEarnings = 0;
            double totalDeductions = 0;

            PdfPTable salaryDeductionTable = new PdfPTable(2);
            salaryDeductionTable.setWidthPercentage(100);
            salaryDeductionTable.setWidths(new int[] { 2, 2 });

            // Process Earnings - FIXED: Calculate pro-rated earnings if employee joined mid-month
            totalEarnings = processEarnings(salaryDeductionTable, earnings, employeeSalary, doj, requestedMonth, year);

            // Get LOP days and other deductions from Payroll Adjustment if available
            Float lopDays = 0.0f;
            Double otherDeductions = 0.0;
            
            if (payrollAdjustment != null) {
                lopDays = payrollAdjustment.getLopDays();
                otherDeductions = payrollAdjustment.getOtherDeductions();
                System.out.println("✓ Using payroll adjustment data - LOP: " + lopDays + " days");
            }
            
            Double lopDeductionAmount = calculateLopDeductionAmount(lopDays, totalEarnings, year, requestedMonth);

            // Process Deductions
            totalDeductions = processDeductions(salaryDeductionTable, deductions, payrollDeductions, 
                                               lopDeductionAmount, otherDeductions);

            document.add(salaryDeductionTable);
            document.add(Chunk.NEWLINE);

            // Add Summary
            addSummary(document, totalEarnings, totalDeductions);

            // Footer
            Paragraph footer = new Paragraph(
                    "This is a Computer Generated Document, No Signature is Required.",
                    FOOTER_FONT);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            System.out.println("✓ Payslip generated successfully!");
            System.out.println("======================================");
            
            return out.toByteArray();

        } catch (Exception e) {
            System.err.println("✗ Error generating payslip: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error generating payslip PDF: " + e.getMessage());
        }
    }

    /**
     * FIXED: Process earnings with pro-ration if employee joined mid-month
     */
    private double processEarnings(PdfPTable salaryDeductionTable, 
                                   List<Map<String, Object>> earnings,
                                   EmployeeSalaryTbl employeeSalary,
                                   LocalDate doj,
                                   Month requestedMonth, 
                                   int year) {
        PdfPTable salaryTable = new PdfPTable(2);
        salaryTable.setWidthPercentage(100);
        addTableHeader(salaryTable, "Earnings", "Monthly Amount");

        double totalEarnings = 0;
        
        // Calculate pro-ration factor if employee joined mid-month
        double proRationFactor = 1.0;
        LocalDate firstDayOfMonth = LocalDate.of(year, requestedMonth, 1);
        LocalDate lastDayOfMonth = LocalDate.of(year, requestedMonth, requestedMonth.length(YearMonth.of(year, requestedMonth).isLeapYear()));
        
        if (doj != null && doj.isAfter(firstDayOfMonth) && doj.isBefore(lastDayOfMonth)) {
            // Employee joined after month started - calculate days from join date
            int totalDaysInMonth = lastDayOfMonth.getDayOfMonth();
            int daysFromJoin = lastDayOfMonth.getDayOfMonth() - doj.getDayOfMonth() + 1;
            proRationFactor = (double) daysFromJoin / totalDaysInMonth;
            System.out.println("Pro-ration factor: " + proRationFactor + " (" + daysFromJoin + "/" + totalDaysInMonth + " days)");
        }
        
        String[] earningFields = {
            "Monthly Basic Salary",
            "House Rent Allowance",
            "Conveyance Allowance",
            "Special Allowance",
            "Project Allowance",
            "Flat Allowance"
        };

        java.util.Map<String, String> fieldMapping = new java.util.HashMap<>();
        fieldMapping.put("monthly basic salary", "Monthly Basic Salary");
        fieldMapping.put("basic", "Monthly Basic Salary");
        fieldMapping.put("basic salary", "Monthly Basic Salary");
        fieldMapping.put("house rent allowance", "House Rent Allowance");
        fieldMapping.put("hra", "House Rent Allowance");
        fieldMapping.put("house rent", "House Rent Allowance");
        fieldMapping.put("conveyance allowance", "Conveyance Allowance");
        fieldMapping.put("conveyance", "Conveyance Allowance");
        fieldMapping.put("special allowance", "Special Allowance");
        fieldMapping.put("special", "Special Allowance");
        fieldMapping.put("project allowance", "Project Allowance");
        fieldMapping.put("project", "Project Allowance");
        fieldMapping.put("flat allowance", "Flat Allowance");
        fieldMapping.put("flat", "Flat Allowance");

        java.util.Map<String, Double> earningsMap = new java.util.HashMap<>();
        for (Map<String, Object> earning : earnings) {
            String name = (String) earning.get("name");
            Double amount = parseAmount(earning.get("monthlyAmount"));
            if (name != null) {
                earningsMap.put(name.toLowerCase().trim(), amount);
            }
        }

        for (String field : earningFields) {
            Double amount = 0.0;
            
            for (java.util.Map.Entry<String, String> mapping : fieldMapping.entrySet()) {
                if (mapping.getValue().equals(field)) {
                    String searchKey = mapping.getKey();
                    if (earningsMap.containsKey(searchKey)) {
                        amount = earningsMap.get(searchKey);
                        break;
                    }
                }
            }
            
            if (amount == 0.0) {
                amount = earningsMap.getOrDefault(field.toLowerCase(), 0.0);
            }
            
            // Apply pro-ration if needed
            double finalAmount = amount;
            if (proRationFactor < 1.0) {
                finalAmount = amount * proRationFactor;
                System.out.println("Pro-rating " + field + ": " + amount + " × " + proRationFactor + " = " + finalAmount);
            }
            
            if (finalAmount > 0) {
                addTableRow(salaryTable, field, String.format("%.2f", finalAmount));
                totalEarnings += finalAmount;
            }
        }

        PdfPCell salaryCell = new PdfPCell(salaryTable);
        salaryCell.setBorder(Rectangle.BOX);
        salaryDeductionTable.addCell(salaryCell);
        
        return totalEarnings;
    }

    /**
     * Calculate LOP deduction amount
     */
    private Double calculateLopDeductionAmount(Float lopDays, double totalEarnings, int year, Month month) {
        if (lopDays == null || lopDays <= 0) {
            return 0.0;
        }
        
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
        double perDaySalary = totalEarnings / daysInMonth;
        double lopDeductionAmount = perDaySalary * lopDays;
        
        System.out.println("✓ LOP Calculation: " + lopDays + " days × " + 
                         String.format("%.2f", perDaySalary) + " = " + 
                         String.format("%.2f", lopDeductionAmount));
        
        return lopDeductionAmount;
    }

    /**
     * Add header with logo and company info
     */
    private void addHeader(Document document) throws Exception {
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new int[] { 1, 3 });

        try {
            Image logo = Image.getInstance(new ClassPathResource("static/logo.png").getURL());
            logo.scaleToFit(100, 100);
            PdfPCell logoCell = new PdfPCell(logo);
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerTable.addCell(logoCell);
        } catch (IOException e) {
            System.out.println("ℹ Logo not found, continuing without logo");
            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
            emptyCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(emptyCell);
        }

        Paragraph companyInfo = new Paragraph("WHITESTONE SOFTWARE SOLUTIONS PVT LTD", TITLE_FONT);
        companyInfo.setAlignment(Element.ALIGN_LEFT);

        Paragraph address = new Paragraph(
                "3/331/Z3, KAMARASAR NAGAR, NALLAMPALLI, DHARMAPURI, TAMIL NADU – 636807", BODY_FONT);

        PdfPCell companyCell = new PdfPCell();
        companyCell.addElement(companyInfo);
        companyCell.addElement(address);
        companyCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(companyCell);

        document.add(headerTable);
        document.add(Chunk.NEWLINE);
    }

    /**
     * Add employee details section
     */
    private void addEmployeeDetails(Document document, String employeeId, 
                                   EmployeeSalaryTbl employeeSalary,
                                   EmployeeProfile employeeProfile, 
                                   PayrollAdjustment payrollAdjustment,
                                   Month requestedMonth, int year,
                                   LocalDate doj) throws Exception {
        
        PdfPTable detailsTable = new PdfPTable(4);
        detailsTable.setWidthPercentage(100);
        detailsTable.setSpacingBefore(10f);

        // Get LOP days from payroll adjustment if available
        Float lopDays = (payrollAdjustment != null) ? payrollAdjustment.getLopDays() : 0.0f;

        // Format Date of Joining
        String dateOfJoining = "";
        if (doj != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            dateOfJoining = doj.format(formatter);
        }

        // Get PAN and UAN from EmployeeProfile
        String panNumber = employeeProfile != null && employeeProfile.getPannumber() != null ? 
                          employeeProfile.getPannumber() : "";
        String uanNumber = employeeProfile != null && employeeProfile.getUannumber() != null ? 
                          employeeProfile.getUannumber() : "";

        // Use employee name from current record
        String employeeName = employeeSalary.getFirstname() + " " + (employeeSalary.getLastname() != null ? employeeSalary.getLastname() : "");
        
        addDetailsRow(detailsTable, "Employee ID", employeeId, "Employee Name", employeeName);
        
        // Bank details
        String bankName = employeeSalary.getBankName() != null ? employeeSalary.getBankName() : "";
        String accountNumber = employeeSalary.getAccountNumber() != null ? employeeSalary.getAccountNumber() : "";
        
        addDetailsRow(detailsTable, "Bank Name", bankName, "Bank Account No", accountNumber);
        
        // Designation and Location
        String department = employeeSalary.getDepartment() != null ? employeeSalary.getDepartment() : "";
        String locationType = employeeSalary.getLocationType() != null ? employeeSalary.getLocationType() : "";
        
        addDetailsRow(detailsTable, "Designation", department, "Location", locationType);
        addDetailsRow(detailsTable, "Date of Joining", dateOfJoining, "PAN", panNumber);
        addDetailsRow(detailsTable, "PF Number", "", "UAN", uanNumber);
        
        // Calculate days for the requested month
        int[] daysData = calculateDaysForMonth(YearMonth.of(year, requestedMonth));
        addDetailsRow(detailsTable, "Days in Month", String.valueOf(daysData[0]), "Effective Working Days",
                String.valueOf(daysData[1]));
        
        // Display LOP days
        String lopDisplay = String.format("%.1f", lopDays);
        addDetailsRow(detailsTable, "LOP", lopDisplay, "", "");

        PdfPTable outerDetailsTable = new PdfPTable(1);
        outerDetailsTable.setWidthPercentage(100);
        PdfPCell outerDetailsCell = new PdfPCell(detailsTable);
        outerDetailsCell.setBorder(Rectangle.BOX);
        outerDetailsCell.setPadding(5);
        outerDetailsTable.addCell(outerDetailsCell);

        document.add(outerDetailsTable);
        document.add(Chunk.NEWLINE);
    }

    /**
     * Process deductions and add to table
     */
    private double processDeductions(PdfPTable salaryDeductionTable,
                                     List<Map<String, Object>> deductions,
                                     List<Map<String, Object>> payrollDeductions,
                                     Double lopDeductionAmount,
                                     Double otherDeductionsFromPA) {
        
        PdfPTable deductionTable = new PdfPTable(2);
        deductionTable.setWidthPercentage(100);
        addTableHeader(deductionTable, "Deductions", "Monthly Amount");

        double totalDeductions = 0;

        java.util.Map<String, Double> deductionsMap = new java.util.HashMap<>();
        
        // Add from regular deductions
        for (Map<String, Object> deduction : deductions) {
            String name = (String) deduction.get("name");
            Double amount = parseAmount(deduction.get("monthlyAmount"));
            if (name != null) {
                deductionsMap.put(name.toLowerCase().trim(), amount);
            }
        }

        // Add from payroll_deductions
        for (Map<String, Object> deduction : payrollDeductions) {
            String name = (String) deduction.get("name");
            Double amount = parseAmount(deduction.get("monthlyAmount"));
            if (name != null) {
                deductionsMap.put(name.toLowerCase().trim(), amount);
            }
        }

        java.util.Map<String, String> deductionFieldMapping = new java.util.HashMap<>();
        deductionFieldMapping.put("pf", "PF");
        deductionFieldMapping.put("provident fund", "PF");
        deductionFieldMapping.put("prof tax", "Prof Tax");
        deductionFieldMapping.put("professional tax", "Prof Tax");
        deductionFieldMapping.put("pt", "Prof Tax");
        deductionFieldMapping.put("mis", "Miscellaneous deduction");
        deductionFieldMapping.put("misc", "Miscellaneous deduction");
        deductionFieldMapping.put("lop deduction", "Lop Deduction");
        deductionFieldMapping.put("lop", "Lop Deduction");
        deductionFieldMapping.put("loss of pay", "Lop Deduction");
        deductionFieldMapping.put("advance", "Advance");
        deductionFieldMapping.put("tds", "TDS Deduction");

        String[] requiredDeductions = {
            "PF", "Prof Tax", "Miscellaneous deduction", 
            "Lop Deduction", "Advance", "TDS Deduction"
        };

        java.util.Map<String, Double> finalDeductionMap = new java.util.HashMap<>();

        for (String fieldName : requiredDeductions) {
            Double amount = 0.0;
            
            for (java.util.Map.Entry<String, String> mapping : deductionFieldMapping.entrySet()) {
                if (mapping.getValue().equals(fieldName)) {
                    String searchKey = mapping.getKey();
                    if (deductionsMap.containsKey(searchKey)) {
                        amount = deductionsMap.get(searchKey);
                        break;
                    }
                }
            }
            
            if (amount == 0.0) {
                amount = deductionsMap.getOrDefault(fieldName.toLowerCase(), 0.0);
            }
            
            finalDeductionMap.put(fieldName, amount);
        }

        // Use LOP Deduction if available
        if (lopDeductionAmount > 0) {
            finalDeductionMap.put("Lop Deduction", lopDeductionAmount);
        }

        // Add other deductions from payroll adjustment
        if (otherDeductionsFromPA != null && otherDeductionsFromPA > 0) {
            Double currentMisc = finalDeductionMap.getOrDefault("Miscellaneous deduction", 0.0);
            finalDeductionMap.put("Miscellaneous deduction", currentMisc + otherDeductionsFromPA);
        }

        // Add rows to table
        for (String fieldName : requiredDeductions) {
            Double amount = finalDeductionMap.getOrDefault(fieldName, 0.0);
            if (amount > 0) {
                addTableRow(deductionTable, fieldName, String.format("%.2f", amount));
                totalDeductions += amount;
            }
        }

        PdfPCell deductionCell = new PdfPCell(deductionTable);
        deductionCell.setBorder(Rectangle.BOX);
        salaryDeductionTable.addCell(deductionCell);
        
        return totalDeductions;
    }

    /**
     * Add summary section
     */
    private void addSummary(Document document, double totalEarnings, double totalDeductions) 
            throws Exception {
        
        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(100);
        summaryTable.setWidths(new float[] { 3, 3 });

        PdfPCell grossEarningsCell = new PdfPCell(
                new Phrase("Gross Earnings: " + String.format("%.2f", totalEarnings), BODY_FONT));
        grossEarningsCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        grossEarningsCell.setBorder(Rectangle.BOX);
        grossEarningsCell.setPadding(5);

        PdfPCell totalDeductionsCell = new PdfPCell(
                new Phrase("Total Deductions: " + String.format("%.2f", totalDeductions), BODY_FONT));
        totalDeductionsCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalDeductionsCell.setBorder(Rectangle.BOX);
        totalDeductionsCell.setPadding(5);

        summaryTable.addCell(grossEarningsCell);
        summaryTable.addCell(totalDeductionsCell);

        double netPay = totalEarnings - totalDeductions;
        String netPayInWords = convertNumberToWords((int) Math.abs(Math.round(netPay))) + " Only";

        PdfPCell netPayLabelCell = new PdfPCell(new Phrase("Net Pay", BODY_FONT));
        netPayLabelCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        netPayLabelCell.setBorder(Rectangle.NO_BORDER);
        netPayLabelCell.setPadding(5);

        PdfPCell netPayAmountCell = new PdfPCell(new Phrase(String.format("%.2f", netPay), BODY_FONT));
        netPayAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        netPayAmountCell.setBorder(Rectangle.NO_BORDER);
        netPayAmountCell.setPadding(5);

        summaryTable.addCell(netPayLabelCell);
        summaryTable.addCell(netPayAmountCell);

        PdfPCell netPayWordsCell = new PdfPCell(
                new Phrase("Net Pay for the Month: " + netPayInWords, BODY_FONT));
        netPayWordsCell.setColspan(2);
        netPayWordsCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        netPayWordsCell.setBorder(Rectangle.TOP);
        netPayWordsCell.setPadding(5);
        summaryTable.addCell(netPayWordsCell);

        PdfPTable outerSummaryTable = new PdfPTable(1);
        outerSummaryTable.setWidthPercentage(100);
        PdfPCell outerSummaryCell = new PdfPCell(summaryTable);
        outerSummaryCell.setBorder(Rectangle.BOX);
        outerSummaryCell.setPadding(5);
        outerSummaryTable.addCell(outerSummaryCell);

        document.add(outerSummaryTable);
        document.add(Chunk.NEWLINE);
    }

    /**
     * Clean JSON string
     */
    private String cleanJsonString(String json) {
        if (json == null) return "[]";
        if (json.startsWith("\"") && json.endsWith("\"")) {
            json = json.substring(1, json.length() - 1);
            json = json.replace("\\\"", "\"");
        }
        return json;
    }

    /**
     * Parse JSON to List
     */
    private List<Map<String, Object>> parseJsonToList(String json) {
        if (json == null || json.isEmpty() || json.equals("[]")) {
            return new java.util.ArrayList<>();
        }

        try {
            return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            try {
                Map<String, Object> singleObj = objectMapper.readValue(json, 
                    new TypeReference<Map<String, Object>>() {});
                List<Map<String, Object>> list = new java.util.ArrayList<>();
                list.add(singleObj);
                return list;
            } catch (Exception ex) {
                System.out.println("DEBUG - Failed to parse JSON: " + ex.getMessage());
                return new java.util.ArrayList<>();
            }
        }
    }

    /**
     * Calculate days for a specific month
     */
    private int[] calculateDaysForMonth(YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        int totalDays = 0;
        int workingDays = 0;
        int saturdayCount = 0;

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            totalDays++;

            DayOfWeek day = date.getDayOfWeek();

            if (day == DayOfWeek.SUNDAY) {
                continue;
            }

            if (day == DayOfWeek.SATURDAY) {
                saturdayCount++;
                if (saturdayCount == 2 || saturdayCount == 4) {
                    continue;
                }
            }

            workingDays++;
        }

        return new int[] { totalDays, workingDays };
    }

    private Double parseAmount(Object amountObj) {
        if (amountObj == null) return 0.0;
        if (amountObj instanceof Integer) return ((Integer) amountObj).doubleValue();
        if (amountObj instanceof Double) return (Double) amountObj;
        if (amountObj instanceof Float) return ((Float) amountObj).doubleValue();
        if (amountObj instanceof Long) return ((Long) amountObj).doubleValue();
        if (amountObj instanceof String) {
            try {
                return Double.parseDouble((String) amountObj);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    private String convertNumberToWords(int number) {
        if (number == 0) return "Zero";
        
        String[] units = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine" };
        String[] teens = { "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
                "Eighteen", "Nineteen" };
        String[] tens = { "", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety" };

        if (number < 10) return units[number];
        if (number < 20) return teens[number - 10];
        if (number < 100) return tens[number / 10] + (number % 10 != 0 ? " " + units[number % 10] : "");
        if (number < 1000) return units[number / 100] + " Hundred" + (number % 100 != 0 ? " " + convertNumberToWords(number % 100) : "");
        if (number < 100000) return convertNumberToWords(number / 1000) + " Thousand" + (number % 1000 != 0 ? " " + convertNumberToWords(number % 1000) : "");
        if (number < 10000000) return convertNumberToWords(number / 100000) + " Lakh" + (number % 100000 != 0 ? " " + convertNumberToWords(number % 100000) : "");
        return convertNumberToWords(number / 10000000) + " Crore" + (number % 10000000 != 0 ? " " + convertNumberToWords(number % 10000000) : "");
    }

    private void addDetailsRow(PdfPTable table, String label1, String value1, String label2, String value2) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label1, BODY_FONT));
        cell1.setBorder(Rectangle.NO_BORDER);
        PdfPCell cell2 = new PdfPCell(new Phrase(value1 != null ? value1 : "", BODY_FONT));
        cell2.setBorder(Rectangle.NO_BORDER);
        PdfPCell cell3 = new PdfPCell(new Phrase(label2, BODY_FONT));
        cell3.setBorder(Rectangle.NO_BORDER);
        PdfPCell cell4 = new PdfPCell(new Phrase(value2 != null ? value2 : "", BODY_FONT));
        cell4.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
    }

    private void addTableHeader(PdfPTable table, String header1, String header2) {
        PdfPCell headerCell1 = new PdfPCell(new Phrase(header1, HEADER_FONT));
        headerCell1.setBorder(Rectangle.BOX);
        headerCell1.setPadding(5);
        headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell headerCell2 = new PdfPCell(new Phrase(header2, HEADER_FONT));
        headerCell2.setBorder(Rectangle.BOX);
        headerCell2.setPadding(5);
        headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(headerCell1);
        table.addCell(headerCell2);
    }

    private void addTableRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, BODY_FONT));
        labelCell.setBorder(Rectangle.NO_BORDER);
        labelCell.setPadding(5);
        labelCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, BODY_FONT));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setPadding(5);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}