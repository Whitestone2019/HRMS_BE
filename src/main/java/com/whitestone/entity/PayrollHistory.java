package com.whitestone.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payroll_history")
public class PayrollHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "Emp_id")
    private String empid;

    @Column(name = "month")
    private String month;

    @Column(name = "status")
    private String status;

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

	@Override
	public String toString() {
		return "PayrollHistory [id=" + id + ", empid=" + empid + ", month=" + month + ", status=" + status
				+ ", pymtProdTypeCode=" + pymtProdTypeCode + ", pymtMode=" + pymtMode + ", debitAccNo=" + debitAccNo
				+ ", bnfName=" + bnfName + ", beneAccNo=" + beneAccNo + ", beneIfsc=" + beneIfsc + ", amount=" + amount
				+ ", debitNarr=" + debitNarr + ", creditNarr=" + creditNarr + ", mobileNum=" + mobileNum + ", emailId="
				+ emailId + ", remark=" + remark + ", pymtDate=" + pymtDate + ", refNo=" + refNo + ", addlInfo1="
				+ addlInfo1 + ", addlInfo2=" + addlInfo2 + ", addlInfo3=" + addlInfo3 + ", addlInfo4=" + addlInfo4
				+ ", addlInfo5=" + addlInfo5 + ", getId()=" + getId() + ", getEmpid()=" + getEmpid() + ", getMonth()="
				+ getMonth() + ", getStatus()=" + getStatus() + ", getPymtProdTypeCode()=" + getPymtProdTypeCode()
				+ ", getPymtMode()=" + getPymtMode() + ", getDebitAccNo()=" + getDebitAccNo() + ", getBnfName()="
				+ getBnfName() + ", getBeneAccNo()=" + getBeneAccNo() + ", getBeneIfsc()=" + getBeneIfsc()
				+ ", getAmount()=" + getAmount() + ", getDebitNarr()=" + getDebitNarr() + ", getCreditNarr()="
				+ getCreditNarr() + ", getMobileNum()=" + getMobileNum() + ", getEmailId()=" + getEmailId()
				+ ", getRemark()=" + getRemark() + ", getPymtDate()=" + getPymtDate() + ", getRefNo()=" + getRefNo()
				+ ", getAddlInfo1()=" + getAddlInfo1() + ", getAddlInfo2()=" + getAddlInfo2() + ", getAddlInfo3()="
				+ getAddlInfo3() + ", getAddlInfo4()=" + getAddlInfo4() + ", getAddlInfo5()=" + getAddlInfo5()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	public PayrollHistory(Long id, String empid, String month, String status, String pymtProdTypeCode, String pymtMode,
			String debitAccNo, String bnfName, String beneAccNo, String beneIfsc, Double amount, String debitNarr,
			String creditNarr, String mobileNum, String emailId, String remark, LocalDate pymtDate, String refNo,
			String addlInfo1, String addlInfo2, String addlInfo3, String addlInfo4, String addlInfo5) {
		super();
		this.id = id;
		this.empid = empid;
		this.month = month;
		this.status = status;
		this.pymtProdTypeCode = pymtProdTypeCode;
		this.pymtMode = pymtMode;
		this.debitAccNo = debitAccNo;
		this.bnfName = bnfName;
		this.beneAccNo = beneAccNo;
		this.beneIfsc = beneIfsc;
		this.amount = amount;
		this.debitNarr = debitNarr;
		this.creditNarr = creditNarr;
		this.mobileNum = mobileNum;
		this.emailId = emailId;
		this.remark = remark;
		this.pymtDate = pymtDate;
		this.refNo = refNo;
		this.addlInfo1 = addlInfo1;
		this.addlInfo2 = addlInfo2;
		this.addlInfo3 = addlInfo3;
		this.addlInfo4 = addlInfo4;
		this.addlInfo5 = addlInfo5;
	}

	public PayrollHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	
    
}
