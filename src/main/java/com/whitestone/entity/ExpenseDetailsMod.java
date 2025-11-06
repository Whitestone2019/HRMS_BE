package com.whitestone.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "EXPENSE_DETAILS_MOD_TBL", schema = "HRMSUSER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpenseDetailsMod {
	@Id
	@Column(name = "EXPENSE_ID", length = 36) // Updated length for UUID
	private String expenseId;

	@Column(name = "EMP_ID", length = 15)
	private String empId;

	@Column(name = "EMPLOYEE_NAME", length = 100)
	private String employeeName;

	@Column(name = "EXPENSE_DATE")
	@JsonFormat(pattern = "yyyy-MM-dd") // Corrected format
	private Date date;

	@Column(name = "CATEGORY", length = 50)
	private String category;

	@Column(name = "AMOUNT", precision = 15, scale = 2)
	private BigDecimal amount;
	
	 @Column(name = "APPROVED_AMOUNT", precision = 15, scale = 2)
	    private BigDecimal approvedAmount;

	@Column(name = "CURRENCY", length = 10)
	private String currency;

	@Column(name = "DESCRIPTION", length = 500)
	private String description;

	@Column(name = "RECEIPT", length = 500)
	private String recipt;

	@Column(name = "STATUS", length = 100)
	private String status;

	@Column(name = "APPROVER", length = 15)
	private String approver;

	@Column(name = "ENTITY_CRE_FLG", length = 1)
	private String entitycreflg;

	@Column(name = "DEL_FLG", length = 1)
	private String delflg;

	@Column(name = "RCRE_USER_ID", nullable = false, length = 15)
	private String rcreuserid;

	@Column(name = "RCRE_TIME")
	@JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
	private Date rcretime;

	@Column(name = "RMOD_USER_ID", length = 15)
	private String rmoduserid;

	@Column(name = "RMOD_TIME")
	@JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
	private Date rmodtime;

	@Column(name = "RVFY_USER_ID", length = 15)
	private String rvfyuserid;

	@Column(name = "RVFY_TIME")
	@JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
	private Date rvfytime;

	@Column(name = "REJECT_REASON", length = 200)
	private String rejectreason;

	@Column(name = "payment_status")
	private Integer paymentStatus;

	public String getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(String expenseId) {
		this.expenseId = expenseId;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRecipt() {
		return recipt;
	}

	public void setRecipt(String recipt) {
		this.recipt = recipt;
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

	public String getEntitycreflg() {
		return entitycreflg;
	}

	public void setEntitycreflg(String entitycreflg) {
		this.entitycreflg = entitycreflg;
	}

	public String getDelflg() {
		return delflg;
	}

	public void setDelflg(String delflg) {
		this.delflg = delflg;
	}

	public String getRcreuserid() {
		return rcreuserid;
	}

	public void setRcreuserid(String rcreuserid) {
		this.rcreuserid = rcreuserid;
	}

	public Date getRcretime() {
		return rcretime;
	}

	public void setRcretime(Date rcretime) {
		this.rcretime = rcretime;
	}

	public String getRmoduserid() {
		return rmoduserid;
	}

	public void setRmoduserid(String rmoduserid) {
		this.rmoduserid = rmoduserid;
	}

	public Date getRmodtime() {
		return rmodtime;
	}

	public void setRmodtime(Date rmodtime) {
		this.rmodtime = rmodtime;
	}

	public String getRvfyuserid() {
		return rvfyuserid;
	}

	public void setRvfyuserid(String rvfyuserid) {
		this.rvfyuserid = rvfyuserid;
	}

	public Date getRvfytime() {
		return rvfytime;
	}

	public void setRvfytime(Date rvfytime) {
		this.rvfytime = rvfytime;
	}

	public String getRejectreason() {
		return rejectreason;
	}

	public void setRejectreason(String rejectreason) {
		this.rejectreason = rejectreason;
	}

	public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	

}