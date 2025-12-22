package com.whitestone.hrms.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.whitestone.entity.PayrollAdjustment;

import java.util.List;

public interface PayrollAdjustmentRepository extends JpaRepository<PayrollAdjustment, Long> {

    PayrollAdjustment findByEmpIdAndMonth(String empId, String month);

    List<PayrollAdjustment> findByManagerIdAndMonth(String managerId, String month);

	List<PayrollAdjustment> findByMonth(String month);
	
	long countByManagerIdAndMonthAndApprovalStatus(String managerId, String month, String approvalStatus);
}
