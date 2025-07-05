package com.whitestone.hrms.repo;
import org.springframework.data.jpa.repository.JpaRepository;

import com.whitestone.entity.EmployeeSalaryTbl;

public interface EmployeeSalaryTblRepository extends JpaRepository<EmployeeSalaryTbl, String> {
	
	EmployeeSalaryTbl findByEmpid(String empid);
    // You can define custom queries here if needed, but JpaRepository provides basic CRUD functionality

	void deleteByEmpid(String empid); // If you don't need the return value

	
}
