package com.whitestone.hrms.repo;

import com.whitestone.entity.HrLeaveApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HrLeaveApprovalRepository extends JpaRepository<HrLeaveApproval, Long> {
    
    // Find all non-deleted records
    List<HrLeaveApproval> findByDelFlag(String delFlag);
    
    // Find by employee ID (non-deleted)
    List<HrLeaveApproval> findByEmployeeIdAndDelFlag(String employeeId, String delFlag);
    
    // Find by status and non-deleted
    List<HrLeaveApproval> findByStatusAndDelFlag(String status, String delFlag);
    
    // Find by employee ID and status
    List<HrLeaveApproval> findByEmployeeIdAndStatusAndDelFlag(String employeeId, String status, String delFlag);
    
    // Find pending approvals for specific employee
    List<HrLeaveApproval> findByEmployeeIdAndStatus(String employeeId, String status);
}