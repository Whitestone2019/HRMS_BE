package com.whitestone.hrms.repo;
 
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.EmployeeLeaveModTbl;
 
@Repository

public interface EmployeeLeaveModTblRepository extends JpaRepository<EmployeeLeaveModTbl, Long> {
 
    @Query("SELECT SUM(e.noofdays) FROM EmployeeLeaveModTbl e WHERE e.leaveType = :leaveType AND e.empid = :empid AND e.status = 'Approved'")

    Long countByLeaveTypeAndEmpid(@Param("leaveType") String leaveType, @Param("empid") String empid);
    
    List<EmployeeLeaveModTbl> findByEmpidIn(List<String> empids); 
    
    @Query("SELECT COUNT(e) FROM EmployeeLeaveModTbl e WHERE e.empid = :empId AND YEAR(e.startDate) = :year AND MONTH(e.startDate) < :currentMonth")
    long countLeaveTakenBefore(@Param("empId") String empId, @Param("year") int year, @Param("currentMonth") int currentMonth);
    
}

 