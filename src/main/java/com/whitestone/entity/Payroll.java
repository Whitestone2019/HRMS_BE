package com.whitestone.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "payroll")
public class Payroll {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emp_id")
    private String empid;

    @Column(name = "month")
    private String month;

    @Column(name = "status")
    private String status; // Pending, Processed, Failed, etc.

    @Column(name = "pymt_prod_type_code")
    private String pymtProdTypeCode;

    @Column(name = "pymt_mode")
    private String pymtMode;

    @Column(name = "debit_acc_no")
    private String debitAccNo;

    @Column(name = "bnf_name")
    private String bnfName;

    @Column(name = "bene_acc_no")
    private String beneAccNo;

    @Column(name = "bene_ifsc")
    private String beneIfsc;

    @Column(name = "amount")
    private Double amount; 

    @Column(name = "debit_narr")
    private String debitNarr;

    @Column(name = "credit_narr")
    private String creditNarr;

    @Column(name = "mobile_num")
    private String mobileNum;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "remark")
    private String remark;
    
    @Column(name = "other_deductions_remarks", length = 500)
    private String otherDeductionsRemarks = "";

    @Column(name = "pymt_date")
    private LocalDate pymtDate;

    @Column(name = "ref_no")
    private String refNo;

    @Column(name = "addl_info1")
    private String addlInfo1;

    @Column(name = "addl_info2")
    private String addlInfo2;

    @Column(name = "addl_info3")
    private String addlInfo3;

    @Column(name = "addl_info4")
    private String addlInfo4;

    @Column(name = "addl_info5")
    private String addlInfo5;

    @Column(name = "per_day_rate")
    private Double perDayRate;

    @Column(name = "per_day_allowance")
    private Double perDayAllowance;

    @Column(name = "per_day_allowance_days")
    private Long perDayAllowanceDays;

    @Column(name = "lop_days")
    private Float lopDays;

    @Column(name = "lop_deduction")
    private Double lopDeduction; // newly added

    @Column(name = "deductions")
    private Double deductions; // total combined deductions
    
    @Column(name = "other_Deductions")
    private Double OtherDeductions; // total combined deductions

    @Column(name = "pg_allowance")
    private Double pgAllowance; // newly added

    @Column(name = "pg_rent_allowance")
    private Double pgRentAllowance; // newly added

    @Column(name = "total_earnings")
    private Double totalEarnings; // newly added

    @Column(name = "total_deductions")
    private Double totalDeductions; // newly added

    @Column(name = "net_pay")
    private Double netPay; // final net pay

    @Column(name = "effective_working_days")
    private Double effectiveWorkingDays;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmpid() {
		return empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPymtProdTypeCode() {
		return pymtProdTypeCode;
	}

	public void setPymtProdTypeCode(String pymtProdTypeCode) {
		this.pymtProdTypeCode = pymtProdTypeCode;
	}

	public String getPymtMode() {
		return pymtMode;
	}

	public void setPymtMode(String pymtMode) {
		this.pymtMode = pymtMode;
	}

	public String getDebitAccNo() {
		return debitAccNo;
	}

	public void setDebitAccNo(String debitAccNo) {
		this.debitAccNo = debitAccNo;
	}

	public String getBnfName() {
		return bnfName;
	}

	public void setBnfName(String bnfName) {
		this.bnfName = bnfName;
	}

	public String getBeneAccNo() {
		return beneAccNo;
	}

	public void setBeneAccNo(String beneAccNo) {
		this.beneAccNo = beneAccNo;
	}

	public String getBeneIfsc() {
		return beneIfsc;
	}

	public void setBeneIfsc(String beneIfsc) {
		this.beneIfsc = beneIfsc;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDebitNarr() {
		return debitNarr;
	}

	public void setDebitNarr(String debitNarr) {
		this.debitNarr = debitNarr;
	}

	public String getCreditNarr() {
		return creditNarr;
	}

	public void setCreditNarr(String creditNarr) {
		this.creditNarr = creditNarr;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOtherDeductionsRemarks() {
		return otherDeductionsRemarks;
	}

	public void setOtherDeductionsRemarks(String otherDeductionsRemarks) {
		this.otherDeductionsRemarks = otherDeductionsRemarks;
	}

	public LocalDate getPymtDate() {
		return pymtDate;
	}

	public void setPymtDate(LocalDate pymtDate) {
		this.pymtDate = pymtDate;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getAddlInfo1() {
		return addlInfo1;
	}

	public void setAddlInfo1(String addlInfo1) {
		this.addlInfo1 = addlInfo1;
	}

	public String getAddlInfo2() {
		return addlInfo2;
	}

	public void setAddlInfo2(String addlInfo2) {
		this.addlInfo2 = addlInfo2;
	}

	public String getAddlInfo3() {
		return addlInfo3;
	}

	public void setAddlInfo3(String addlInfo3) {
		this.addlInfo3 = addlInfo3;
	}

	public String getAddlInfo4() {
		return addlInfo4;
	}

	public void setAddlInfo4(String addlInfo4) {
		this.addlInfo4 = addlInfo4;
	}

	public String getAddlInfo5() {
		return addlInfo5;
	}

	public void setAddlInfo5(String addlInfo5) {
		this.addlInfo5 = addlInfo5;
	}

	public Double getPerDayRate() {
		return perDayRate;
	}

	public void setPerDayRate(Double perDayRate) {
		this.perDayRate = perDayRate;
	}

	public Double getPerDayAllowance() {
		return perDayAllowance;
	}

	public void setPerDayAllowance(Double perDayAllowance) {
		this.perDayAllowance = perDayAllowance;
	}

	public Long getPerDayAllowanceDays() {
		return perDayAllowanceDays;
	}

	public void setPerDayAllowanceDays(Long perDayAllowanceDays) {
		this.perDayAllowanceDays = perDayAllowanceDays;
	}

	public Float getLopDays() {
		return lopDays;
	}

	public void setLopDays(Float lopDays) {
		this.lopDays = lopDays;
	}

	public Double getLopDeduction() {
		return lopDeduction;
	}

	public void setLopDeduction(Double lopDeduction) {
		this.lopDeduction = lopDeduction;
	}

	public Double getDeductions() {
		return deductions;
	}

	public void setDeductions(Double deductions) {
		this.deductions = deductions;
	}

	public Double getOtherDeductions() {
		return OtherDeductions;
	}

	public void setOtherDeductions(Double otherDeductions) {
		OtherDeductions = otherDeductions;
	}

	public Double getPgAllowance() {
		return pgAllowance;
	}

	public void setPgAllowance(Double pgAllowance) {
		this.pgAllowance = pgAllowance;
	}

	public Double getPgRentAllowance() {
		return pgRentAllowance;
	}

	public void setPgRentAllowance(Double pgRentAllowance) {
		this.pgRentAllowance = pgRentAllowance;
	}

	public Double getTotalEarnings() {
		return totalEarnings;
	}

	public void setTotalEarnings(Double totalEarnings) {
		this.totalEarnings = totalEarnings;
	}

	public Double getTotalDeductions() {
		return totalDeductions;
	}

	public void setTotalDeductions(Double totalDeductions) {
		this.totalDeductions = totalDeductions;
	}

	public Double getNetPay() {
		return netPay;
	}

	public void setNetPay(Double netPay) {
		this.netPay = netPay;
	}

	public Double getEffectiveWorkingDays() {
		return effectiveWorkingDays;
	}

	public void setEffectiveWorkingDays(Double effectiveWorkingDays) {
		this.effectiveWorkingDays = effectiveWorkingDays;
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
