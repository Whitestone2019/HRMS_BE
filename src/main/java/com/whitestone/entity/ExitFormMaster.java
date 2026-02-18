package com.whitestone.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "EXIT_FORM_MASTER", schema = "HRMSUSER")
public class ExitFormMaster {

    @Id
    private String id;   

    // EXIT FORM DETAILS
    private String employeeId;
    private String employeeName;
    private LocalDate noticeStartDate;
    private String reason;
    private String comments;
    private String status;  
    private int noticePeriod;
    private LocalDate noticeEndDate;
    private String attachment;
    
 // Withdraw details
    private String withdrawPurpose;
    private LocalDate withdrawDate;
    private String withdrawBy;

    // MANAGER REVIEW DETAILS
    private String performance;
    private String managerNoticeperiod;
    private String projectDependency;
    private String knowledgeTransfer;
    private String managerRemarks;
    private String managerAction;
    private String managerName;
    private String purposeOfChange;

    // HR CHECKLIST DETAILS
    private Boolean hrNoticePeriod;
    private Boolean hrLeaveBalances;
    private Boolean hrPolicyCompliance;
    private Boolean hrExitEligibility;
    @Column(name = "HR_NAME")
    private String hrName;
    
    private String hrNoticePeriodComments;
    private String hrLeaveBalancesComments;
    private String hrPolicyComplianceComments;
    private String hrExitEligibilityComments;
    @Column(name = "HR_GENERAL_COMMENTS", length = 1000)
    private String hrGeneralComments;
    
    private String hrAction;
    private String hrReviewDate;
    
    //Asset Clearance
    private String assetClearance;
    
    @Column(name = "ASSET_SUBMITTED_BY")
    private String assetSubmittedBy;
    
    
    @Column(name = "PAYROLL_SUBMITTED_BY")
    private String payrollSubmittedBy;
    
    //Hr-offboarding
    private String hrOffboardingChecks;
    
    //Payroll checks
    private String payrollChecks; 
    
    //Final Hr remarks
    private String finalHrRemarks;
    private String finalHrApprovedBy;
    private String finalHrApprovedOn;
    @Column(name = "FINAL_CHECKLIST_DATA", length = 4000)  // ← Increased size, NO @Lob
    private String finalChecklistData; // ← CORRECT: lowercase 'f'
    

    // Audit Fields
    private String createdOn;
    private String createdBy;
    private String updatedOn;
    private String updatedBy;
    private String delFlag = "N";
    
    
 // Add these new fields for submission dates
    @Column(name = "USER_SUBMITTED_ON")
    private LocalDateTime userSubmittedOn;
    
    @Column(name = "MANAGER_SUBMITTED_ON")
    private LocalDateTime managerSubmittedOn;
    
    @Column(name = "HR_ROUND1_SUBMITTED_ON")
    private LocalDateTime hrRound1SubmittedOn;
    
    @Column(name = "ASSET_SUBMITTED_ON")
    private LocalDateTime assetSubmittedOn;
    
    @Column(name = "HR_ROUND2_SUBMITTED_ON")
    private LocalDateTime hrRound2SubmittedOn;
    
    @Column(name = "PAYROLL_SUBMITTED_ON")
    private LocalDateTime payrollSubmittedOn;
    
    @Column(name = "FINAL_HR_SUBMITTED_ON")
    private LocalDateTime finalHrSubmittedOn;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public LocalDate getNoticeStartDate() {
		return noticeStartDate;
	}

	public void setNoticeStartDate(LocalDate noticeStartDate) {
		this.noticeStartDate = noticeStartDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getNoticePeriod() {
		return noticePeriod;
	}

	public void setNoticePeriod(int noticePeriod) {
		this.noticePeriod = noticePeriod;
	}

	public LocalDate getNoticeEndDate() {
		return noticeEndDate;
	}

	public void setNoticeEndDate(LocalDate noticeEndDate) {
		this.noticeEndDate = noticeEndDate;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getWithdrawPurpose() {
		return withdrawPurpose;
	}

	public void setWithdrawPurpose(String withdrawPurpose) {
		this.withdrawPurpose = withdrawPurpose;
	}

	public LocalDate getWithdrawDate() {
		return withdrawDate;
	}

	public void setWithdrawDate(LocalDate withdrawDate) {
		this.withdrawDate = withdrawDate;
	}

	public String getWithdrawBy() {
		return withdrawBy;
	}

	public void setWithdrawBy(String withdrawBy) {
		this.withdrawBy = withdrawBy;
	}

	public String getPerformance() {
		return performance;
	}

	public void setPerformance(String performance) {
		this.performance = performance;
	}

	public String getManagerNoticeperiod() {
		return managerNoticeperiod;
	}

	public void setManagerNoticeperiod(String managerNoticeperiod) {
		this.managerNoticeperiod = managerNoticeperiod;
	}

	public String getProjectDependency() {
		return projectDependency;
	}

	public void setProjectDependency(String projectDependency) {
		this.projectDependency = projectDependency;
	}

	public String getKnowledgeTransfer() {
		return knowledgeTransfer;
	}

	public void setKnowledgeTransfer(String knowledgeTransfer) {
		this.knowledgeTransfer = knowledgeTransfer;
	}

	public String getManagerRemarks() {
		return managerRemarks;
	}

	public void setManagerRemarks(String managerRemarks) {
		this.managerRemarks = managerRemarks;
	}

	public String getManagerAction() {
		return managerAction;
	}

	public void setManagerAction(String managerAction) {
		this.managerAction = managerAction;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getPurposeOfChange() {
		return purposeOfChange;
	}

	public void setPurposeOfChange(String purposeOfChange) {
		this.purposeOfChange = purposeOfChange;
	}

	public Boolean getHrNoticePeriod() {
		return hrNoticePeriod;
	}

	public void setHrNoticePeriod(Boolean hrNoticePeriod) {
		this.hrNoticePeriod = hrNoticePeriod;
	}

	public Boolean getHrLeaveBalances() {
		return hrLeaveBalances;
	}

	public void setHrLeaveBalances(Boolean hrLeaveBalances) {
		this.hrLeaveBalances = hrLeaveBalances;
	}

	public Boolean getHrPolicyCompliance() {
		return hrPolicyCompliance;
	}

	public void setHrPolicyCompliance(Boolean hrPolicyCompliance) {
		this.hrPolicyCompliance = hrPolicyCompliance;
	}

	public Boolean getHrExitEligibility() {
		return hrExitEligibility;
	}

	public void setHrExitEligibility(Boolean hrExitEligibility) {
		this.hrExitEligibility = hrExitEligibility;
	}

	public String getHrName() {
		return hrName;
	}

	public void setHrName(String hrName) {
		this.hrName = hrName;
	}

	public String getHrNoticePeriodComments() {
		return hrNoticePeriodComments;
	}

	public void setHrNoticePeriodComments(String hrNoticePeriodComments) {
		this.hrNoticePeriodComments = hrNoticePeriodComments;
	}

	public String getHrLeaveBalancesComments() {
		return hrLeaveBalancesComments;
	}

	public void setHrLeaveBalancesComments(String hrLeaveBalancesComments) {
		this.hrLeaveBalancesComments = hrLeaveBalancesComments;
	}

	public String getHrPolicyComplianceComments() {
		return hrPolicyComplianceComments;
	}

	public void setHrPolicyComplianceComments(String hrPolicyComplianceComments) {
		this.hrPolicyComplianceComments = hrPolicyComplianceComments;
	}

	public String getHrExitEligibilityComments() {
		return hrExitEligibilityComments;
	}

	public void setHrExitEligibilityComments(String hrExitEligibilityComments) {
		this.hrExitEligibilityComments = hrExitEligibilityComments;
	}

	public String getHrGeneralComments() {
		return hrGeneralComments;
	}

	public void setHrGeneralComments(String hrGeneralComments) {
		this.hrGeneralComments = hrGeneralComments;
	}

	public String getHrAction() {
		return hrAction;
	}

	public void setHrAction(String hrAction) {
		this.hrAction = hrAction;
	}

	public String getHrReviewDate() {
		return hrReviewDate;
	}

	public void setHrReviewDate(String hrReviewDate) {
		this.hrReviewDate = hrReviewDate;
	}

	public String getAssetClearance() {
		return assetClearance;
	}

	public void setAssetClearance(String assetClearance) {
		this.assetClearance = assetClearance;
	}

	public String getAssetSubmittedBy() {
		return assetSubmittedBy;
	}

	public void setAssetSubmittedBy(String assetSubmittedBy) {
		this.assetSubmittedBy = assetSubmittedBy;
	}

	public String getPayrollSubmittedBy() {
		return payrollSubmittedBy;
	}

	public void setPayrollSubmittedBy(String payrollSubmittedBy) {
		this.payrollSubmittedBy = payrollSubmittedBy;
	}

	public String getHrOffboardingChecks() {
		return hrOffboardingChecks;
	}

	public void setHrOffboardingChecks(String hrOffboardingChecks) {
		this.hrOffboardingChecks = hrOffboardingChecks;
	}

	public String getPayrollChecks() {
		return payrollChecks;
	}

	public void setPayrollChecks(String payrollChecks) {
		this.payrollChecks = payrollChecks;
	}

	public String getFinalHrRemarks() {
		return finalHrRemarks;
	}

	public void setFinalHrRemarks(String finalHrRemarks) {
		this.finalHrRemarks = finalHrRemarks;
	}

	public String getFinalHrApprovedBy() {
		return finalHrApprovedBy;
	}

	public void setFinalHrApprovedBy(String finalHrApprovedBy) {
		this.finalHrApprovedBy = finalHrApprovedBy;
	}

	public String getFinalHrApprovedOn() {
		return finalHrApprovedOn;
	}

	public void setFinalHrApprovedOn(String finalHrApprovedOn) {
		this.finalHrApprovedOn = finalHrApprovedOn;
	}

	public String getFinalChecklistData() {
		return finalChecklistData;
	}

	public void setFinalChecklistData(String finalChecklistData) {
		this.finalChecklistData = finalChecklistData;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public LocalDateTime getUserSubmittedOn() {
		return userSubmittedOn;
	}

	public void setUserSubmittedOn(LocalDateTime userSubmittedOn) {
		this.userSubmittedOn = userSubmittedOn;
	}

	public LocalDateTime getManagerSubmittedOn() {
		return managerSubmittedOn;
	}

	public void setManagerSubmittedOn(LocalDateTime managerSubmittedOn) {
		this.managerSubmittedOn = managerSubmittedOn;
	}

	public LocalDateTime getHrRound1SubmittedOn() {
		return hrRound1SubmittedOn;
	}

	public void setHrRound1SubmittedOn(LocalDateTime hrRound1SubmittedOn) {
		this.hrRound1SubmittedOn = hrRound1SubmittedOn;
	}

	public LocalDateTime getAssetSubmittedOn() {
		return assetSubmittedOn;
	}

	public void setAssetSubmittedOn(LocalDateTime assetSubmittedOn) {
		this.assetSubmittedOn = assetSubmittedOn;
	}

	public LocalDateTime getHrRound2SubmittedOn() {
		return hrRound2SubmittedOn;
	}

	public void setHrRound2SubmittedOn(LocalDateTime hrRound2SubmittedOn) {
		this.hrRound2SubmittedOn = hrRound2SubmittedOn;
	}

	public LocalDateTime getPayrollSubmittedOn() {
		return payrollSubmittedOn;
	}

	public void setPayrollSubmittedOn(LocalDateTime payrollSubmittedOn) {
		this.payrollSubmittedOn = payrollSubmittedOn;
	}

	public LocalDateTime getFinalHrSubmittedOn() {
		return finalHrSubmittedOn;
	}

	public void setFinalHrSubmittedOn(LocalDateTime finalHrSubmittedOn) {
		this.finalHrSubmittedOn = finalHrSubmittedOn;
	}

	
	
}