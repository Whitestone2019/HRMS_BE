package com.whitestone.hrms.repo;
import com.whitestone.entity.EmployeeProfile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, String> {
	//You can define custom query methods here if needed.
	
	@Query("SELECT e FROM EmployeeProfile e WHERE e.empid = :empid")
	Optional<EmployeeProfile> findByEmpId(@Param("empid") String empid);

}