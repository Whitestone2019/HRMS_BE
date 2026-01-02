package com.whitestone.hrms.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.EmployeeProfessionalDetailsMod;

@Repository
public interface EmployeeProfessionalDetailsModRepository extends JpaRepository<EmployeeProfessionalDetailsMod, String> {
    
    // SIMPLEST POSSIBLE QUERY - Test if basic query works
    @Query(value = "SELECT USER_ID FROM EMPLOYEE_PROFESSIONAL_DETAILS_MOD_TBL WHERE ROWNUM < 5", 
           nativeQuery = true)
    List<String> testQuery();
    
    // CURRENT WORKING QUERY - keep this as backup
//    @Query(value = "SELECT * FROM EMPLOYEE_PROFESSIONAL_DETAILS_MOD_TBL WHERE USER_ID = ?1", 
//           nativeQuery = true)
//    List<EmployeeProfessionalDetailsMod> findByUserid(String userId);
    
    // SOLUTION 1: Try this simple version with DEL_FLG
    @Query(value = "SELECT * FROM EMPLOYEE_PROFESSIONAL_DETAILS_MOD_TBL WHERE USER_ID = ?1 AND DEL_FLG = 'N'", 
           nativeQuery = true)
    List<EmployeeProfessionalDetailsMod> findByUserid(String userId);
    

}