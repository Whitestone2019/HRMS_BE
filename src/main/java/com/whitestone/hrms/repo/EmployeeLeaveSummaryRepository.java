package com.whitestone.hrms.repo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.whitestone.entity.EmployeeLeaveSummary;

public interface EmployeeLeaveSummaryRepository extends JpaRepository<EmployeeLeaveSummary, Long> {
    
    // Find leave summary for an employee for a specific year
    Optional<EmployeeLeaveSummary> findByEmpIdAndYear(String empId, int year);
    
    EmployeeLeaveSummary findByEmpId(String empId); 
    
    EmployeeLeaveSummary findByEmpId_AndYear(String empId, int year);
}
