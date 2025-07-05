package com.whitestone.hrms.repo;
import com.whitestone.entity.EmployeeAddress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeAddressRepository extends JpaRepository<EmployeeAddress, String> {
	//You can define custom query methods here if needed.
}