package com.whitestone.entity;
 
import java.time.LocalDate;
import java.time.LocalDateTime;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
 
import com.fasterxml.jackson.annotation.JsonFormat;
 
@Entity
@Table(name = "EMPLOYEE_SALARY")
public class EmployeeSalaryTbl {
 
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_salary_seq")
    @SequenceGenerator(name = "emp_salary_seq", sequenceName = "emp_salary_seq", allocationSize = 1)
    private Long id;
   
    @Column(name = "EMP_ID")
    private String empid;
 
    @Column(name = "FIRST_NAME")
    private String firstname;
 
    @Column(name = "LAST_NAME")
    private String lastname;
 
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateofbirth;
 
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATE_OF_JOIN")
    private LocalDate dateOfJoin;
 
    @Column(name = "OFFICIAL_EMAIL")
    private String officialemail;
 
    @Column(name = "EMAIL_ID")
    private String emailid;
 
    @Column(name = "MOBILE_NUMBER")
    private String phonenumber;
 
    @Column(name = "LOCATION_TYPE")
    private String locationType;
 
    @Column(name = "DEPARTMENT")
    private String department;
 
    @Column(name = "ANNUAL_CTC")
    private Double annualCTC;
 
    @Lob
    @Column(name = "EARNINGS")
    private String earnings;  // Store as JSON string
 
    @Lob
    @Column(name = "DEDUCTIONS")
    private String deductions; // Store as JSON string
   
    @Lob
    @Column(name = "payroll_deductions")
    private String payrollDeductions;
 
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
 
    public String getFirstname() {
        return firstname;
    }
 
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
 
    public String getLastname() {
        return lastname;
    }
 
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
 
    public LocalDate getDateofbirth() {
        return dateofbirth;
    }
 
    public void setDateofbirth(LocalDate dateofbirth) {
        this.dateofbirth = dateofbirth;
    }
 
    public LocalDate getDateOfJoin() {
        return dateOfJoin;
    }
 
    public void setDateOfJoin(LocalDate dateOfJoin) {
        this.dateOfJoin = dateOfJoin;
    }
 
    public String getOfficialemail() {
        return officialemail;
    }
 
    public void setOfficialemail(String officialemail) {
        this.officialemail = officialemail;
    }
 
    public String getEmailid() {
        return emailid;
    }
 
    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }
 
    public String getPhonenumber() {
        return phonenumber;
    }
 
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
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
 
    public String getPayrollDeductions() {
        return payrollDeductions;
    }
 
    public void setPayrollDeductions(String payrollDeductions) {
        this.payrollDeductions = payrollDeductions;
    }
 
   
   
   
   
}
 
 
 