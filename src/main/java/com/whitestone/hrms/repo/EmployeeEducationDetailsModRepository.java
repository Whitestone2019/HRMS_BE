package com.whitestone.hrms.repo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.EmployeeEducationDetailsMod;

@Repository
public interface EmployeeEducationDetailsModRepository extends JpaRepository<EmployeeEducationDetailsMod, String> {
	
	void deleteByUserid(Long userid);
	//You can define custom query methods here if needed.
		@Query(value = "SELECT COALESCE(MAX(e.SRL_NUM), 0) FROM HRMSUSER.EMPLOYEE_EDUCATION_DETAILS_MOD_TBL e WHERE e.user_id = ?1", nativeQuery = true)
		Long findMaxSerialNumber(Long userId);


		List<EmployeeEducationDetailsMod> findByuserid(String empid);


		List<EmployeeEducationDetailsMod> findByUserid(Long userid);
}