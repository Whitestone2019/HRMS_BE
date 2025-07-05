package com.whitestone.hrms.repo;
import com.whitestone.entity.EmployeeEducationDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeEducationDetailsRepository extends JpaRepository<EmployeeEducationDetails, String> {
	//You can define custom query methods here if needed.
}