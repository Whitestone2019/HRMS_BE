package com.whitestone.hrms.repo;
import com.whitestone.entity.EmployeeProfileMod;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeProfileModRepository extends JpaRepository<EmployeeProfileMod, Long> {
	Optional<EmployeeProfileMod> findByEmpid(String empid);
	//You can define custom query methods here if needed.
	
	  // Custom query to fetch rows where DEL_FLG is not null
	// @Query("SELECT e FROM EmployeeProfileMod e WHERE e.delflg IS NOT NULL")
	@Query("SELECT e FROM EmployeeProfileMod e WHERE e.delflg = 'N'")
	    List<EmployeeProfileMod> findAllWhereDelFlgIsNotNull();
}