package com.whitestone.hrms.repo;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.whitestone.entity.EmployeeEducationDetailsMod;

@Repository
public interface EmployeeEducationDetailsModRepository extends JpaRepository<EmployeeEducationDetailsMod, String> {
    
    // ACTIVE RECORDS - USE Long parameter (not String)

    @Query(value = "SELECT * FROM EMPLOYEE_EDUCATION_DETAILS_MOD WHERE USER_ID = ?1 AND DEL_FLG = 'N'", 
           nativeQuery = true)
    List<EmployeeEducationDetailsMod> findByUserid(Long userId);
    

    // ALL RECORDS (including deleted) for processing
 
    @Query(value = "SELECT * FROM EMPLOYEE_EDUCATION_DETAILS_MOD WHERE USER_ID = ?1", 
           nativeQuery = true)
    List<EmployeeEducationDetailsMod> findAllByUserid(Long userId);
    

    // EXISTING METHODS - Keep as is
   
    @Query(value = "SELECT COALESCE(MAX(e.SRL_NUM), 0) FROM EMPLOYEE_EDUCATION_DETAILS_MOD e WHERE e.USER_ID = ?1", 
           nativeQuery = true)
    Long findMaxSerialNumber(Long userId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM EmployeeEducationDetailsMod e WHERE e.userid = :userid")
    void deleteAllByUserid(@Param("userid") Long userid);
    
    // Keep for backward compatibility (deprecated)
    void deleteByUserid(Long userid);
}