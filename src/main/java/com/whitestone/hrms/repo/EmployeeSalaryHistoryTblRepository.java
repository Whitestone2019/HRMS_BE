package com.whitestone.hrms.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.EmployeeSalaryHistory;

@Repository
public interface EmployeeSalaryHistoryTblRepository extends JpaRepository<EmployeeSalaryHistory, Long> {
    
    // Corrected method with proper parameter mapping
    @Query("SELECT e FROM EmployeeSalaryHistory e WHERE e.empId = :empId AND e.modifiedAt <= :monthEnd ORDER BY e.modifiedAt DESC")
    List<EmployeeSalaryHistory> findLatestBeforeDate(
        @Param("empId") String empId, 
        @Param("monthEnd") LocalDateTime monthEnd, 
        Pageable pageable
    );
    
    // Alternative: If you don't need Pageable, use this simpler version
    List<EmployeeSalaryHistory> findByEmpIdOrderByModifiedAtDesc(String empId);

	List<EmployeeSalaryHistory> findByEmpId(String employeeId);
    
    // Find by employee ID and year/month (if you have these fields)
    // List<EmployeeSalaryHistory> findByEmpIdAndYearAndMonth(String empId, int year, int month);
}