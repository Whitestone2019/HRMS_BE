package com.whitestone.hrms.repo;
import com.whitestone.entity.EmployeeProfessionalDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeProfessionalDetailsRepository extends JpaRepository<EmployeeProfessionalDetails, String> {
	//You can define custom query methods here if needed.
}