package com.whitestone.hrms.repo;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.whitestone.entity.EmployeePermissionMasterTbl;

public interface EmployeePermissionMasterRepository extends JpaRepository<EmployeePermissionMasterTbl, Long> {

    @Query("SELECT COUNT(e) FROM EmployeePermissionMasterTbl e WHERE e.empid = :empId AND e.startTime = :startTime")
    long countByEmpidAndStartTime(@Param("empId") String empId, @Param("startTime") java.sql.Timestamp startTime);

    @Query("SELECT SUM(e.hours) FROM EmployeePermissionMasterTbl e WHERE e.empid = :empId AND TRUNC(e.startTime) = :startDate AND e.status <> 'Rejected'")
    Float sumHoursByEmpidAndDate(@Param("empId") String empId, @Param("startDate") Date startDate);

    @Query("SELECT SUM(e.hours) FROM EmployeePermissionMasterTbl e WHERE e.empid = :empId AND EXTRACT(YEAR FROM e.startTime) = :year AND EXTRACT(MONTH FROM e.startTime) = :month AND e.status <> 'Rejected'")
    Float sumHoursByEmpidAndMonth(@Param("empId") String empId, @Param("year") int year, @Param("month") int month);

    Optional<EmployeePermissionMasterTbl> findByEmpidAndId(String empid, Long id); // Add this method
    
    
    List<EmployeePermissionMasterTbl> findByEmpidInAndEntitycreflgIn(List<String> empIds, List<String> entitycreflgs);

    long countByEmpidInAndStatusIn(List<String> empIds, List<String> statuses);
    

}