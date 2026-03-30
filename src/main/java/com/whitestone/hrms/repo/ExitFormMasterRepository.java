package com.whitestone.hrms.repo;

import com.whitestone.entity.ExitFormMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ExitFormMasterRepository extends JpaRepository<ExitFormMaster, String> {

    // ==================== BASIC QUERIES ====================
    
    // Fetch all non-deleted records
    List<ExitFormMaster> findByDelFlag(String delFlag);

    // Fetch by employeeId + non-deleted
    List<ExitFormMaster> findByDelFlagAndEmployeeId(String delFlag, String employeeId);

    // Fetch all active (delFlag = 'N')
    @Query("SELECT e FROM ExitFormMaster e WHERE e.delFlag = 'N'")
    List<ExitFormMaster> findAllActive();

    // Fetch single active exit form by ID
    @Query("SELECT e FROM ExitFormMaster e WHERE e.id = :id AND e.delFlag = 'N'")
    ExitFormMaster findActiveById(@Param("id") String id);

    // Fetch by employeeId with delFlag check
    List<ExitFormMaster> findByEmployeeIdAndDelFlag(String employeeId, String delFlag);

    // Fetch multiple employeeIds + delFlag
    @Query("SELECT e FROM ExitFormMaster e WHERE e.employeeId IN :employeeIds AND e.delFlag = :delFlag")
    List<ExitFormMaster> findByEmployeeIdInAndDelFlag(
            @Param("employeeIds") List<String> employeeIds,
            @Param("delFlag") String delFlag);

    // Fetch multiple employeeIds (no delFlag check)
    List<ExitFormMaster> findByEmployeeIdIn(List<String> employeeIds);

    // Check if active form exists for an employee
    @Query("SELECT COUNT(e) > 0 FROM ExitFormMaster e WHERE e.employeeId = :employeeId AND e.delFlag = 'N'")
    boolean existsByEmployeeId(@Param("employeeId") String employeeId);

    // ==================== HR REVIEW QUERIES ====================
    
    // Fetch forms pending HR review
    @Query("SELECT e FROM ExitFormMaster e WHERE e.managerAction = 'APPROVE' AND e.hrAction IS NULL AND e.delFlag = 'N'")
    List<ExitFormMaster> findPendingHrReview();

    // Fetch forms where HR review already completed
    @Query("SELECT e FROM ExitFormMaster e WHERE e.hrAction IS NOT NULL AND e.delFlag = 'N'")
    List<ExitFormMaster> findHrReviewedForms();

    // ==================== SIMPLIFIED NATIVE QUERY FOR WITHDRAW ====================
    
    /**
     * SIMPLIFIED native query - uses SYSDATE for dates
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE HRMSUSER.EXIT_FORM_MASTER SET " +
           "WITHDRAW_PURPOSE = :withdrawPurpose, " +
           "WITHDRAW_DATE = SYSDATE, " +
           "WITHDRAW_BY = :withdrawBy, " +
           "DEL_FLAG = 'Y', " +
           "UPDATED_ON = TO_CHAR(SYSDATE, 'YYYY-MM-DD'), " +
           "UPDATED_BY = :withdrawBy " +
           "WHERE ID = :formId", nativeQuery = true)
    int withdrawExitForm(
        @Param("formId") String formId,
        @Param("withdrawPurpose") String withdrawPurpose,
        @Param("withdrawBy") String withdrawBy
    );
    
    /**
     * For verification - directly check what's in database
     */
    @Query(value = "SELECT WITHDRAW_PURPOSE FROM HRMSUSER.EXIT_FORM_MASTER WHERE ID = :formId", nativeQuery = true)
    String getWithdrawPurposeDirect(@Param("formId") String formId);
    
    // ==================== ADDITIONAL HELPER QUERIES ====================
    
    /**
     * Find all withdrawn forms (delFlag = 'Y')
     */
    @Query("SELECT e FROM ExitFormMaster e WHERE e.delFlag = 'Y'")
    List<ExitFormMaster> findAllWithdrawn();
    
    /**
     * Find withdrawn forms by employee
     */
    @Query("SELECT e FROM ExitFormMaster e WHERE e.employeeId = :employeeId AND e.delFlag = 'Y'")
    List<ExitFormMaster> findWithdrawnByEmployee(@Param("employeeId") String employeeId);
    
    /**
     * Count active forms by employee
     */
    @Query("SELECT COUNT(e) FROM ExitFormMaster e WHERE e.employeeId = :employeeId AND e.delFlag = 'N'")
    long countActiveByEmployee(@Param("employeeId") String employeeId);
    
    /**
     * Find forms by status
     */
    List<ExitFormMaster> findByStatusAndDelFlag(String status, String delFlag);
    
    /**
     * Find forms by manager action
     */
    List<ExitFormMaster> findByManagerActionAndDelFlag(String managerAction, String delFlag);
    
    /**
     * Find forms by HR action
     */
    List<ExitFormMaster> findByHrActionAndDelFlag(String hrAction, String delFlag);
}