package com.whitestone.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "EMPLOYEE_SALARY_HISTORY")

public class EmployeeSalaryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HISTORY_ID")
    private Long historyId;

    @Column(name = "EMP_ID")
    private String empId;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATE_OF_JOIN")
    private LocalDate dateOfJoin;

    @Column(name = "OFFICIAL_EMAIL")
    private String officialEmail;

    @Column(name = "EMAIL_ID")
    private String emailId;

    @Column(name = "MOBILE_NUMBER")
    private String mobileNumber;

    @Column(name = "LOCATION_TYPE")
    private String locationType;

    @Column(name = "DEPARTMENT")
    private String department;

    @Column(name = "ANNUAL_CTC")
    private Double annualCTC;

    @Lob
    @Column(name = "EARNINGS")
    private String earnings;  

    @Lob
    @Column(name = "DEDUCTIONS")
    private String deductions; 

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "IFSC_CODE")
    private String ifscCode;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Column(name = "MODIFIED_AT", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime modifiedAt;

	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public LocalDate getDateOfJoin() {
		return dateOfJoin;
	}

	public void setDateOfJoin(LocalDate dateOfJoin) {
		this.dateOfJoin = dateOfJoin;
	}

	public String getOfficialEmail() {
		return officialEmail;
	}

	public void setOfficialEmail(String officialEmail) {
		this.officialEmail = officialEmail;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Double getAnnualCTC() {
		return annualCTC;
	}

	public void setAnnualCTC(Double annualCTC) {
		this.annualCTC = annualCTC;
	}

	public String getEarnings() {
		return earnings;
	}

	public void setEarnings(String earnings) {
		this.earnings = earnings;
	}

	public String getDeductions() {
		return deductions;
	}

	public void setDeductions(String deductions) {
		this.deductions = deductions;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	@Override
	public String toString() {
		return "EmployeeSalaryHistory [historyId=" + historyId + ", empId=" + empId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", dateOfBirth=" + dateOfBirth + ", dateOfJoin=" + dateOfJoin
				+ ", officialEmail=" + officialEmail + ", emailId=" + emailId + ", mobileNumber=" + mobileNumber
				+ ", locationType=" + locationType + ", department=" + department + ", annualCTC=" + annualCTC
				+ ", earnings=" + earnings + ", deductions=" + deductions + ", bankName=" + bankName
				+ ", accountNumber=" + accountNumber + ", ifscCode=" + ifscCode + ", modifiedBy=" + modifiedBy
				+ ", modifiedAt=" + modifiedAt + ", getHistoryId()=" + getHistoryId() + ", getEmpId()=" + getEmpId()
				+ ", getFirstName()=" + getFirstName() + ", getLastName()=" + getLastName() + ", getDateOfBirth()="
				+ getDateOfBirth() + ", getDateOfJoin()=" + getDateOfJoin() + ", getOfficialEmail()="
				+ getOfficialEmail() + ", getEmailId()=" + getEmailId() + ", getMobileNumber()=" + getMobileNumber()
				+ ", getLocationType()=" + getLocationType() + ", getDepartment()=" + getDepartment()
				+ ", getAnnualCTC()=" + getAnnualCTC() + ", getEarnings()=" + getEarnings() + ", getDeductions()="
				+ getDeductions() + ", getBankName()=" + getBankName() + ", getAccountNumber()=" + getAccountNumber()
				+ ", getIfscCode()=" + getIfscCode() + ", getModifiedBy()=" + getModifiedBy() + ", getModifiedAt()="
				+ getModifiedAt() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

	public EmployeeSalaryHistory(Long historyId, String empId, String firstName, String lastName, LocalDate dateOfBirth,
			LocalDate dateOfJoin, String officialEmail, String emailId, String mobileNumber, String locationType,
			String department, Double annualCTC, String earnings, String deductions, String bankName,
			String accountNumber, String ifscCode, String modifiedBy, LocalDateTime modifiedAt) {
		super();
		this.historyId = historyId;
		this.empId = empId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.dateOfJoin = dateOfJoin;
		this.officialEmail = officialEmail;
		this.emailId = emailId;
		this.mobileNumber = mobileNumber;
		this.locationType = locationType;
		this.department = department;
		this.annualCTC = annualCTC;
		this.earnings = earnings;
		this.deductions = deductions;
		this.bankName = bankName;
		this.accountNumber = accountNumber;
		this.ifscCode = ifscCode;
		this.modifiedBy = modifiedBy;
		this.modifiedAt = modifiedAt;
	}

	public EmployeeSalaryHistory() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
