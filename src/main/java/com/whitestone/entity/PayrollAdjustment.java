package com.whitestone.entity;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "payroll_adjustments")
public class PayrollAdjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emp_id")
    private String empId;
    
    @Column(name = "employee_name")
    private String employeeName; 

    @Column(name = "month")
    private String month; // Format: yyyy-MM

    @Column(name = "allowance_days")
    private long allowanceDays;

    @Column(name = "lop_days")
    private float lopDays;

    @Column(name = "effective_working_days")
    private double effectiveWorkingDays;
    
    @Column(name = "other_deductions")
    private double otherDeductions;
    
    @Column(name = "other_deductions_remarks", length = 500)
    private String otherDeductionsRemarks = "";

    @Column(name = "manager_id")
    private String managerId;

    @Column(name = "approval_status")
    private String approvalStatus; // PENDING, APPROVED, REJECTED

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public long getAllowanceDays() {
		return allowanceDays;
	}

	public void setAllowanceDays(long allowanceDays) {
		this.allowanceDays = allowanceDays;
	}

	public float getLopDays() {
		return lopDays;
	}

	public void setLopDays(float lopDays) {
		this.lopDays = lopDays;
	}

	public double getEffectiveWorkingDays() {
		return effectiveWorkingDays;
	}

	public void setEffectiveWorkingDays(double effectiveWorkingDays) {
		this.effectiveWorkingDays = effectiveWorkingDays;
	}

	public double getOtherDeductions() {
		return otherDeductions;
	}

	public void setOtherDeductions(double otherDeductions) {
		this.otherDeductions = otherDeductions;
	}

	public String getOtherDeductionsRemarks() {
		return otherDeductionsRemarks;
	}

	public void setOtherDeductionsRemarks(String otherDeductionsRemarks) {
		this.otherDeductionsRemarks = otherDeductionsRemarks;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	

	
}