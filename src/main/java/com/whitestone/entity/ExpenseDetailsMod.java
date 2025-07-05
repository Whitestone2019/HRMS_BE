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

	@Override
	public String toString() {
		return "ExpenseDetailsMod [expenseId=" + expenseId + ", empId=" + empId + ", employeeName=" + employeeName
				+ ", date=" + date + ", category=" + category + ", amount=" + amount + ", currency=" + currency
				+ ", description=" + description + ", recipt=" + recipt + ", status=" + status + ", approver="
				+ approver + ", entitycreflg=" + entitycreflg + ", delflg=" + delflg + ", rcreuserid=" + rcreuserid
				+ ", rcretime=" + rcretime + ", rmoduserid=" + rmoduserid + ", rmodtime=" + rmodtime + ", rvfyuserid="
				+ rvfyuserid + ", rvfytime=" + rvfytime + ", rejectreason=" + rejectreason + ", paymentStatus="
				+ paymentStatus + "]";
	}

	public ExpenseDetailsMod(String expenseId, String empId, String employeeName, Date date, String category,
			BigDecimal amount, String currency, String description, String recipt, String status, String approver,
			String entitycreflg, String delflg, String rcreuserid, Date rcretime, String rmoduserid, Date rmodtime,
			String rvfyuserid, Date rvfytime, String rejectreason, Integer paymentStatus) {
		super();
		this.expenseId = expenseId;
		this.empId = empId;
		this.employeeName = employeeName;
		this.date = date;
		this.category = category;
		this.amount = amount;
		this.currency = currency;
		this.description = description;
		this.recipt = recipt;
		this.status = status;
		this.approver = approver;
		this.entitycreflg = entitycreflg;
		this.delflg = delflg;
		this.rcreuserid = rcreuserid;
		this.rcretime = rcretime;
		this.rmoduserid = rmoduserid;
		this.rmodtime = rmodtime;
		this.rvfyuserid = rvfyuserid;
		this.rvfytime = rvfytime;
		this.rejectreason = rejectreason;
		this.paymentStatus = paymentStatus;
	}

	public ExpenseDetailsMod() {
		super();
		// TODO Auto-generated constructor stub
	}

}