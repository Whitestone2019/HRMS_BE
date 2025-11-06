package com.whitestone.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "ADVANCE_DETAILS_MOD_TBL", schema = "HRMSUSER")
public class AdvancesDetailsMod {
    @Id
    @Column(name = "ADVANCE_ID")
    private String advanceId;

    @Column(name = "EMP_ID",  length = 15)
    private String empId;

    @Column(name = "EMPLOYEE_NAME",  length = 100)
    private String employeeName;

    @Column(name = "AMOUNT", precision = 15, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "APPROVED_AMOUNT", precision = 15, scale = 2)
    private BigDecimal approvedAmount;

    @Column(name = "ADVANCE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date advanceDate;

    @Column(name = "PAID_THROUGH", length = 100)
    private String paidThrough;

    @Column(name = "APPLY_TO_TRIP", length = 100)
    private String applyToTrip;

    @Column(name = "STATUS", length = 100)
    private String status;
    
    @Column(name = "APPROVER", length = 15)
	private String approver;
    
    @Column(name = "payment_status")
    private Integer paymentStatus;
    
    @Column(name = "REJECT_REASON", length = 200)
    private String rejectreason;

    @Column(name = "ENTITY_CRE_FLG", length = 1)
    private String entityCreFlg;

    @Column(name = "DEL_FLG", length = 1)
    private String delFlg;

    @Column(name = "RCRE_USER_ID",  length = 15)
    private String rcreUserId;

    @Column(name = "RCRE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rcreTime;

    @Column(name = "RMOD_USER_ID",  length = 15)
    private String rmodUserId;

    @Column(name = "RMOD_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rmodTime;

	public String getAdvanceId() {
		return advanceId;
	}

	public void setAdvanceId(String advanceId) {
		this.advanceId = advanceId;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getApprovedAmount() {
		return approvedAmount;
	}

	public void setApprovedAmount(BigDecimal approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	public Date getAdvanceDate() {
		return advanceDate;
	}

	public void setAdvanceDate(Date advanceDate) {
		this.advanceDate = advanceDate;
	}

	public String getPaidThrough() {
		return paidThrough;
	}

	public void setPaidThrough(String paidThrough) {
		this.paidThrough = paidThrough;
	}

	public String getApplyToTrip() {
		return applyToTrip;
	}

	public void setApplyToTrip(String applyToTrip) {
		this.applyToTrip = applyToTrip;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getRejectreason() {
		return rejectreason;
	}

	public void setRejectreason(String rejectreason) {
		this.rejectreason = rejectreason;
	}

	public String getEntityCreFlg() {
		return entityCreFlg;
	}

	public void setEntityCreFlg(String entityCreFlg) {
		this.entityCreFlg = entityCreFlg;
	}

	public String getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}

	public String getRcreUserId() {
		return rcreUserId;
	}

	public void setRcreUserId(String rcreUserId) {
		this.rcreUserId = rcreUserId;
	}

	public Date getRcreTime() {
		return rcreTime;
	}

	public void setRcreTime(Date rcreTime) {
		this.rcreTime = rcreTime;
	}

	public String getRmodUserId() {
		return rmodUserId;
	}

	public void setRmodUserId(String rmodUserId) {
		this.rmodUserId = rmodUserId;
	}

	public Date getRmodTime() {
		return rmodTime;
	}

	public void setRmodTime(Date rmodTime) {
		this.rmodTime = rmodTime;
	}

	
	
	
}
