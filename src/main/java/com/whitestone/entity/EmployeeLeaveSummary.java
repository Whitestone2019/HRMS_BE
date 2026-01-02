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
    private Float casualLeaveBalance = 18.0f;  // Default 12 casual leaves

    @Column(name = "leave_taken", nullable = false)
    private Float leaveTaken = 0.0f;

    @Column(nullable = false)
    private Float lop = 0.0f; // Total Loss of Pay (LOP)

    @Column(name = "lop_jan", nullable = false)
    private Float lopJan = 0.0f;

    @Column(name = "lop_feb", nullable = false)
    private Float lopFeb = 0.0f;

    @Column(name = "lop_mar", nullable = false)
    private Float lopMar = 0.0f;

    @Column(name = "lop_apr", nullable = false)
    private Float lopApr = 0.0f;

    @Column(name = "lop_may", nullable = false)
    private Float lopMay = 0.0f;

    @Column(name = "lop_jun", nullable = false)
    private Float lopJun = 0.0f;

    @Column(name = "lop_jul", nullable = false)
    private Float lopJul = 0.0f;

    @Column(name = "lop_aug", nullable = false)
    private Float lopAug = 0.0f;

    @Column(name = "lop_sep", nullable = false)
    private Float lopSep = 0.0f;

    @Column(name = "lop_oct", nullable = false)
    private Float lopOct = 0.0f;

    @Column(name = "lop_nov", nullable = false)
    private Float lopNov = 0.0f;

    @Column(name = "lop_dec", nullable = false)
    private Float lopDec = 0.0f;

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

	public Float getCasualLeaveBalance() {
		return casualLeaveBalance;
	}

	public void setCasualLeaveBalance(Float casualLeaveBalance) {
		this.casualLeaveBalance = casualLeaveBalance;
	}

	public Float getLeaveTaken() {
		return leaveTaken;
	}

	public void setLeaveTaken(Float leaveTaken) {
		this.leaveTaken = leaveTaken;
	}

	public Float getLop() {
		return lop;
	}

	public void setLop(Float lop) {
		this.lop = lop;
	}

	public Float getLopJan() {
		return lopJan;
	}

	public void setLopJan(Float lopJan) {
		this.lopJan = lopJan;
	}

	public Float getLopFeb() {
		return lopFeb;
	}

	public void setLopFeb(Float lopFeb) {
		this.lopFeb = lopFeb;
	}

	public Float getLopMar() {
		return lopMar;
	}

	public void setLopMar(Float lopMar) {
		this.lopMar = lopMar;
	}

	public Float getLopApr() {
		return lopApr;
	}

	public void setLopApr(Float lopApr) {
		this.lopApr = lopApr;
	}

	public Float getLopMay() {
		return lopMay;
	}

	public void setLopMay(Float lopMay) {
		this.lopMay = lopMay;
	}

	public Float getLopJun() {
		return lopJun;
	}

	public void setLopJun(Float lopJun) {
		this.lopJun = lopJun;
	}

	public Float getLopJul() {
		return lopJul;
	}

	public void setLopJul(Float lopJul) {
		this.lopJul = lopJul;
	}

	public Float getLopAug() {
		return lopAug;
	}

	public void setLopAug(Float lopAug) {
		this.lopAug = lopAug;
	}

	public Float getLopSep() {
		return lopSep;
	}

	public void setLopSep(Float lopSep) {
		this.lopSep = lopSep;
	}

	public Float getLopOct() {
		return lopOct;
	}

	public void setLopOct(Float lopOct) {
		this.lopOct = lopOct;
	}

	public Float getLopNov() {
		return lopNov;
	}

	public void setLopNov(Float lopNov) {
		this.lopNov = lopNov;
	}

	public Float getLopDec() {
		return lopDec;
	}

	public void setLopDec(Float lopDec) {
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

}
