package com.whitestone.hrms.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

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
import com.whitestone.entity.EmployeeLeaveSummary;
import com.whitestone.entity.EmployeeSalaryTbl;
import com.whitestone.hrms.repo.EmployeeLeaveSummaryRepository;
import com.whitestone.hrms.repo.EmployeeSalaryTblRepository;

@Component
public class PayslipGenerator {

    @Autowired
    private EmployeeSalaryTblRepository employeeSalaryTblRepository;

    @Autowired
    private EmployeeLeaveSummaryRepository employeeLeaveSummaryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Define common fonts for consistency
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLUE);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
    private static final Font BODY_FONT = new Font(Font.FontFamily.HELVETICA, 10);
    private static final Font FOOTER_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);

    public byte[] createPayslipPdf(String employeeId, String month) {
        EmployeeSalaryTbl employeeSalary = employeeSalaryTblRepository.findByEmpid(employeeId);

        if (employeeSalary == null) {
            throw new RuntimeException("Employee not found for ID: " + employeeId);
        }

        // Parse the month string to Month enum (e.g., "January" -> Month.JANUARY)
        Month requestedMonth;
        String monthName = month.split("\\s+")[0]; // Extract the first word (e.g., "March" from "March 2025")
        try {
            requestedMonth = Month.valueOf(monthName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid month: " + month, e);
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // Header with Logo & Company Details
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
                throw new RuntimeException("Logo image not found", e);
            }

            // Company Info
            Paragraph companyInfo = new Paragraph("WHITESTONE SOFTWARE SOLUTIONS PVT LTD", TITLE_FONT);
            companyInfo.setAlignment(Element.ALIGN_LEFT);

            Paragraph address = new Paragraph(
                    "3/331/Z3, KAMARASAR NAGAR, NALLAMPALLI, \r\n" + " DHARMAPURI, TAMIL NADU – 636807", BODY_FONT);

            PdfPCell companyCell = new PdfPCell();
            companyCell.addElement(companyInfo);
            companyCell.addElement(address);
            companyCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(companyCell);

            document.add(headerTable);
            document.add(Chunk.NEWLINE);

            // Payslip Title
            Paragraph subtitle = new Paragraph("Payslip for the Month of " + month + " - 2025", SUBTITLE_FONT);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            document.add(subtitle);
            document.add(Chunk.NEWLINE);

            // Employee Details Table
            PdfPTable detailsTable = new PdfPTable(4);
            detailsTable.setWidthPercentage(100);
            detailsTable.setSpacingBefore(10f);

            // Retrieve LOP days
            EmployeeLeaveSummary leaveSummary = employeeLeaveSummaryRepository.findByEmpId(employeeId);
            Float lopDays = 0.0f;
            if (leaveSummary != null) {
                switch (requestedMonth) {
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

            addDetailsRow(detailsTable, "Employee ID", employeeSalary.getEmpid(), "Employee Name",
                    employeeSalary.getFirstname());
            addDetailsRow(detailsTable, "Bank Name", employeeSalary.getBankName(), "Bank Account No",
                    employeeSalary.getAccountNumber());
            addDetailsRow(detailsTable, "Designation", employeeSalary.getDepartment(), "Location",
                    employeeSalary.getLocationType());
            addDetailsRow(detailsTable, "Date of Joining", "", "PAN", "");
            addDetailsRow(detailsTable, "PF Number", "", "UAN", "");
            int[] daysData = calculateDays(); // Get Days in Month & Effective Working Days
            addDetailsRow(detailsTable, "Days in Month", String.valueOf(daysData[0]), "Effective Working Days",
                    String.valueOf(daysData[1]));
            addDetailsRow(detailsTable, "LOP", String.valueOf(lopDays), "", "");

            PdfPTable outerDetailsTable = new PdfPTable(1);
            outerDetailsTable.setWidthPercentage(100);
            PdfPCell outerDetailsCell = new PdfPCell(detailsTable);
            outerDetailsCell.setBorder(Rectangle.BOX);
            outerDetailsCell.setPadding(5);
            outerDetailsTable.addCell(outerDetailsCell);

            document.add(outerDetailsTable);
            document.add(Chunk.NEWLINE);

            // Parse Earnings and Deductions
            String earningsJson = employeeSalary.getEarnings();
            String deductionsJson = employeeSalary.getDeductions();

            if (earningsJson.startsWith("\"") && earningsJson.endsWith("\"")) {
                earningsJson = earningsJson.substring(1, earningsJson.length() - 1);
                earningsJson = earningsJson.replace("\\\"", "\"");
            }

            if (deductionsJson.startsWith("\"") && deductionsJson.endsWith("\"")) {
                deductionsJson = deductionsJson.substring(1, deductionsJson.length() - 1);
                deductionsJson = deductionsJson.replace("\\\"", "\"");
            }

            List<Map<String, Object>> earnings = objectMapper.readValue(earningsJson,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
            List<Map<String, Object>> deductions = objectMapper.readValue(deductionsJson,
                    new TypeReference<List<Map<String, Object>>>() {
                    });

            double totalEarnings = 0;
            double totalDeductions = 0;

            // Salary & Deductions Table
            PdfPTable salaryDeductionTable = new PdfPTable(2);
            salaryDeductionTable.setWidthPercentage(100);
            salaryDeductionTable.setWidths(new int[] { 2, 2 });

            PdfPTable salaryTable = new PdfPTable(2);
            salaryTable.setWidthPercentage(100);
            addTableHeader(salaryTable, "Earnings", "Monthly Amount");

            for (Map<String, Object> earning : earnings) {
                String name = (String) earning.get("name");
                Double monthlyAmount = parseAmount(earning.get("monthlyAmount"));
                addTableRow(salaryTable, name, String.format("%.2f", monthlyAmount));
                totalEarnings += monthlyAmount;
            }

            PdfPCell salaryCell = new PdfPCell(salaryTable);
            salaryCell.setBorder(Rectangle.BOX);
            salaryDeductionTable.addCell(salaryCell);

            PdfPTable deductionTable = new PdfPTable(2);
            deductionTable.setWidthPercentage(100);
            addTableHeader(deductionTable, "Deductions", "Monthly Amount");

            for (Map<String, Object> deduction : deductions) {
                String name = (String) deduction.get("name");
                Double monthlyAmount = parseAmount(deduction.get("monthlyAmount"));

                // If the deduction is Provident Fund (PF) at 24%, only show 12%
                if ("Provident Fund".equalsIgnoreCase(name) || "PF".equalsIgnoreCase(name)) {
                    monthlyAmount = monthlyAmount / 2; // Show only 12% instead of 24%
                }

                addTableRow(deductionTable, name, String.format("%.2f", monthlyAmount));
                totalDeductions += monthlyAmount;
            }

            // Calculate LOP Deduction
            double perDaySalary = totalEarnings / LocalDate.of(2025, requestedMonth, 1).lengthOfMonth();
            double lopDeduction = perDaySalary * lopDays;
            if (lopDeduction > 0) {
                addTableRow(deductionTable, "Loss of Pay", String.format("%.2f", lopDeduction));
                totalDeductions += lopDeduction;
            }

            PdfPCell deductionCell = new PdfPCell(deductionTable);
            deductionCell.setBorder(Rectangle.BOX);
            salaryDeductionTable.addCell(deductionCell);

            document.add(salaryDeductionTable);
            document.add(Chunk.NEWLINE);

            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(100);
            summaryTable.setWidths(new float[] { 3, 3 });

            // Gross Earnings & Total Deductions in Single Row
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

            // Net Pay Calculation
            double netPay = totalEarnings - totalDeductions;
            String netPayInWords = convertNumberToWords((int) netPay) + " Only";

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

            PdfPCell netPayWordsCell = new PdfPCell(new Phrase("Net Pay for the Month: " + netPayInWords, BODY_FONT));
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

            // Footer
            Paragraph footer = new Paragraph(
                    "This is a Computer Generated Document, No Signature is Required. This document contains confidential information.",
                    FOOTER_FONT);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating payslip PDF", e);
        }
    }

    // Helper methods (parseAmount, convertNumberToWords, addDetailsRow, addTableHeader, addTableRow, addTableRowWithBorder, calculateDays)
    // ... (Keep all existing helper methods as they are, as shown in the original code)

    private Double parseAmount(Object amountObj) {
        if (amountObj instanceof Integer) {
            return ((Integer) amountObj).doubleValue();
        } else if (amountObj instanceof Double) {
            return (Double) amountObj;
        }
        return 0.0;
    }

    private String convertNumberToWords(int number) {
        String[] units = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine" };
        String[] teens = { "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
                "Eighteen", "Nineteen" };
        String[] tens = { "", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety" };

        if (number == 0)
            return "Zero";
        if (number < 10)
            return units[number];
        if (number < 20)
            return teens[number - 10];
        if (number < 100)
            return tens[number / 10] + " " + units[number % 10];
        if (number < 1000)
            return units[number / 100] + " Hundred " + convertNumberToWords(number % 100);
        if (number < 100000)
            return convertNumberToWords(number / 1000) + " Thousand " + convertNumberToWords(number % 1000);
        if (number < 10000000)
            return convertNumberToWords(number / 100000) + " Lakh " + convertNumberToWords(number % 100000);
        return convertNumberToWords(number / 10000000) + " Crore " + convertNumberToWords(number % 10000000);
    }

    private void addDetailsRow(PdfPTable table, String label1, String value1, String label2, String value2) {
        PdfPCell cell1 = new PdfPCell(new Phrase(label1, BODY_FONT));
        cell1.setBorder(Rectangle.NO_BORDER);
        PdfPCell cell2 = new PdfPCell(new Phrase(value1, BODY_FONT));
        cell2.setBorder(Rectangle.NO_BORDER);
        PdfPCell cell3 = new PdfPCell(new Phrase(label2, BODY_FONT));
        cell3.setBorder(Rectangle.NO_BORDER);
        PdfPCell cell4 = new PdfPCell(new Phrase(value2, BODY_FONT));
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

    private void addTableRowWithBorder(PdfPTable table, String col1, String col2, String col3, String col4) {
        PdfPCell cell1 = new PdfPCell(new Phrase(col1, BODY_FONT));
        PdfPCell cell2 = new PdfPCell(new Phrase(col2, BODY_FONT));
        PdfPCell cell3 = new PdfPCell(new Phrase(col3, BODY_FONT));
        PdfPCell cell4 = new PdfPCell(new Phrase(col4, BODY_FONT));

        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3.setBorder(Rectangle.NO_BORDER);
        cell4.setBorder(Rectangle.NO_BORDER);

        cell1.setPadding(5);
        cell2.setPadding(5);
        cell3.setPadding(5);
        cell4.setPadding(5);

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);
        table.addCell(cell4);
    }

    public static int[] calculateDays() {
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        LocalDate startDate = today.minusMonths(1).withDayOfMonth(27);
        LocalDate endDate = today.withDayOfMonth(26);

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
}