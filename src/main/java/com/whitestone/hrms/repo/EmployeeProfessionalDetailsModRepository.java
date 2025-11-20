package com.whitestone.hrms.repo;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.EmployeeProfessionalDetailsMod;

@Repository
public interface EmployeeProfessionalDetailsModRepository extends JpaRepository<EmployeeProfessionalDetailsMod, String> {
	   @Query(value = "SELECT COALESCE(MAX(TO_NUMBER(e.SRL_NUM)), 0) " +
               "FROM HRMSUSER.EMPLOYEE_PROFESSIONAL_DETAILS_MOD_TBL e " +
               "WHERE e.USER_ID = TO_CHAR(?1)", nativeQuery = true)
Long findProfMaxSerialNumber(Long userId);

// Method for fetching a single record by userId
Optional<EmployeeProfessionalDetailsMod> findByUserid(Long userId);

// Method for fetching a list of records by userId
List<EmployeeProfessionalDetailsMod> findAllByUserid(Long userId);

List<EmployeeProfessionalDetailsMod> findByUserid(String valueOf);

void deleteByUserid(String userid);
}