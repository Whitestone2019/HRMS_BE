package com.whitestone.hrms.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.EmployeeSalaryHistory;

@Repository
public interface EmployeeSalaryHistoryTblRepository extends JpaRepository<EmployeeSalaryHistory, Long> {
    // You can add custom query methods here if needed, for example:
    // List<EmployeeSalaryHistoryTbl> findByEmpId(String empId);
}
