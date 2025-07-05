package com.whitestone.entity;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "employee_leave_summary", uniqueConstraints = @UniqueConstraint(columnNames = {"emp_id", "year"}))
public class EmployeeLeaveSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emp_id", nullable = false)
    private String empId;

    @Column(nullable = false)
    private int year;

    @Column(name = "casual_leave_balance", nullable = false)
    private Long casualLeaveBalance = 12L;  // Default 12 casual leaves

    @Column(name = "leave_taken", nullable = false)
    private Long leaveTaken = 0L; // No leave taken initially

    @Column(nullable = false)
    private Long lop = 0L; // Total Loss of Pay (LOP) initially 0

    // ✅ New columns for monthly LOP tracking
    @Column(name = "lop_jan", nullable = false)
    private Long lopJan = 0L;

    @Column(name = "lop_feb", nullable = false)
    private Long lopFeb = 0L;

    @Column(name = "lop_mar", nullable = false)
    private Long lopMar = 0L;

    @Column(name = "lop_apr", nullable = false)
    private Long lopApr = 0L;

    @Column(name = "lop_may", nullable = false)
    private Long lopMay = 0L;

    @Column(name = "lop_jun", nullable = false)
    private Long lopJun = 0L;

    @Column(name = "lop_jul", nullable = false)
    private Long lopJul = 0L;

    @Column(name = "lop_aug", nullable = false)
    private Long lopAug = 0L;

    @Column(name = "lop_sep", nullable = false)
    private Long lopSep = 0L;

    @Column(name = "lop_oct", nullable = false)
    private Long lopOct = 0L;

    @Column(name = "lop_nov", nullable = false)
    private Long lopNov = 0L;

    @Column(name = "lop_dec", nullable = false)
    private Long lopDec = 0L;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Long getCasualLeaveBalance() {
		return casualLeaveBalance;
	}

	public void setCasualLeaveBalance(Long casualLeaveBalance) {
		this.casualLeaveBalance = casualLeaveBalance;
	}

	public Long getLeaveTaken() {
		return leaveTaken;
	}

	public void setLeaveTaken(Long leaveTaken) {
		this.leaveTaken = leaveTaken;
	}

	public Long getLop() {
		return lop;
	}

	public void setLop(Long lop) {
		this.lop = lop;
	}

	public Long getLopJan() {
		return lopJan;
	}

	public void setLopJan(Long lopJan) {
		this.lopJan = lopJan;
	}

	public Long getLopFeb() {
		return lopFeb;
	}

	public void setLopFeb(Long lopFeb) {
		this.lopFeb = lopFeb;
	}

	public Long getLopMar() {
		return lopMar;
	}

	public void setLopMar(Long lopMar) {
		this.lopMar = lopMar;
	}

	public Long getLopApr() {
		return lopApr;
	}

	public void setLopApr(Long lopApr) {
		this.lopApr = lopApr;
	}

	public Long getLopMay() {
		return lopMay;
	}

	public void setLopMay(Long lopMay) {
		this.lopMay = lopMay;
	}

	public Long getLopJun() {
		return lopJun;
	}

	public void setLopJun(Long lopJun) {
		this.lopJun = lopJun;
	}

	public Long getLopJul() {
		return lopJul;
	}

	public void setLopJul(Long lopJul) {
		this.lopJul = lopJul;
	}

	public Long getLopAug() {
		return lopAug;
	}

	public void setLopAug(Long lopAug) {
		this.lopAug = lopAug;
	}

	public Long getLopSep() {
		return lopSep;
	}

	public void setLopSep(Long lopSep) {
		this.lopSep = lopSep;
	}

	public Long getLopOct() {
		return lopOct;
	}

	public void setLopOct(Long lopOct) {
		this.lopOct = lopOct;
	}

	public Long getLopNov() {
		return lopNov;
	}

	public void setLopNov(Long lopNov) {
		this.lopNov = lopNov;
	}

	public Long getLopDec() {
		return lopDec;
	}

	public void setLopDec(Long lopDec) {
		this.lopDec = lopDec;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "EmployeeLeaveSummary [id=" + id + ", empId=" + empId + ", year=" + year + ", casualLeaveBalance="
				+ casualLeaveBalance + ", leaveTaken=" + leaveTaken + ", lop=" + lop + ", lopJan=" + lopJan
				+ ", lopFeb=" + lopFeb + ", lopMar=" + lopMar + ", lopApr=" + lopApr + ", lopMay=" + lopMay
				+ ", lopJun=" + lopJun + ", lopJul=" + lopJul + ", lopAug=" + lopAug + ", lopSep=" + lopSep
				+ ", lopOct=" + lopOct + ", lopNov=" + lopNov + ", lopDec=" + lopDec + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + ", getId()=" + getId() + ", getEmpId()=" + getEmpId() + ", getYear()="
				+ getYear() + ", getCasualLeaveBalance()=" + getCasualLeaveBalance() + ", getLeaveTaken()="
				+ getLeaveTaken() + ", getLop()=" + getLop() + ", getLopJan()=" + getLopJan() + ", getLopFeb()="
				+ getLopFeb() + ", getLopMar()=" + getLopMar() + ", getLopApr()=" + getLopApr() + ", getLopMay()="
				+ getLopMay() + ", getLopJun()=" + getLopJun() + ", getLopJul()=" + getLopJul() + ", getLopAug()="
				+ getLopAug() + ", getLopSep()=" + getLopSep() + ", getLopOct()=" + getLopOct() + ", getLopNov()="
				+ getLopNov() + ", getLopDec()=" + getLopDec() + ", getCreatedAt()=" + getCreatedAt()
				+ ", getUpdatedAt()=" + getUpdatedAt() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	public EmployeeLeaveSummary() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmployeeLeaveSummary(Long id, String empId, int year, Long casualLeaveBalance, Long leaveTaken, Long lop,
			Long lopJan, Long lopFeb, Long lopMar, Long lopApr, Long lopMay, Long lopJun, Long lopJul, Long lopAug,
			Long lopSep, Long lopOct, Long lopNov, Long lopDec, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.empId = empId;
		this.year = year;
		this.casualLeaveBalance = casualLeaveBalance;
		this.leaveTaken = leaveTaken;
		this.lop = lop;
		this.lopJan = lopJan;
		this.lopFeb = lopFeb;
		this.lopMar = lopMar;
		this.lopApr = lopApr;
		this.lopMay = lopMay;
		this.lopJun = lopJun;
		this.lopJul = lopJul;
		this.lopAug = lopAug;
		this.lopSep = lopSep;
		this.lopOct = lopOct;
		this.lopNov = lopNov;
		this.lopDec = lopDec;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
    
    
}
