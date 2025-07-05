package com.whitestone.payslip;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocaleDateExample {
    public static void main(String[] args) {
        // Get current date
        LocalDate currentDate = LocalDate.now();
        System.out.println("Formatted Date: " + currentDate);
        
        // Define a custom date format (e.g., yyyy-MM-dd)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd ");
        
        // Format the current date
        String formattedDate = currentDate.format(formatter);
        
        // Output the formatted date
        System.out.println("Formatted Date: " + formattedDate);
    }
}
