package com.whitestone.hrms.repo;

import com.whitestone.entity.ExitFormMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExitFormMasterRepository extends JpaRepository<ExitFormMaster, String> {

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

    // Fetch forms pending HR review (Manager approved, HR not yet done)
    @Query("SELECT e FROM ExitFormMaster e WHERE e.managerAction = 'APPROVE' AND e.hrAction IS NULL AND e.delFlag = 'N'")
    List<ExitFormMaster> findPendingHrReview();

    // Fetch forms where HR review already completed
    @Query("SELECT e FROM ExitFormMaster e WHERE e.hrAction IS NOT NULL AND e.delFlag = 'N'")
    List<ExitFormMaster> findHrReviewedForms();
}
