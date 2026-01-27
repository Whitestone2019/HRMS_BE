package com.whitestone.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "employee_leave_summary", uniqueConstraints = @UniqueConstraint(columnNames = {"emp_id", "year"}))
public class EmployeeLeaveSummary {

	  @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leave_summary_seq")
	    @SequenceGenerator(name = "leave_summary_seq", sequenceName = "EMPLOYEE_LEAVE_SUMMARY_SEQ", allocationSize = 1)
	    private Long id;

    @Column(name = "emp_id", nullable = false)
    private String empId;

    @Column(nullable = false)
    private int year;

    // CHANGED: Added columnDefinition to ensure DECIMAL(5,1) in database
    @Column(name = "casual_leave_balance", nullable = false, columnDefinition = "DECIMAL(5,1) DEFAULT 18.0")
    private Float casualLeaveBalance = 18.0f;  // Default 18 casual leaves

    // CHANGED: Added columnDefinition
    @Column(name = "leave_taken", nullable = false, columnDefinition = "DECIMAL(5,1) DEFAULT 0.0")
    private Float leaveTaken = 0.0f;

    // CHANGED: Added columnDefinition
    @Column(nullable = false, columnDefinition = "DECIMAL(5,1) DEFAULT 0.0")
    private Float lop = 0.0f; // Total Loss of Pay (LOP)

    // CHANGED: Added columnDefinition to all LOP fields
    @Column(name = "lop_jan", nullable = false, columnDefinition = "DECIMAL(5,1) DEFAULT 0.0")
    private Float lopJan = 0.0f;

    @Column(name = "lop_feb", nullable = false, columnDefinition = "DECIMAL(5,1) DEFAULT 0.0")
    private Float lopFeb = 0.0f;

    @Column(name = "lop_mar", nullable = false, columnDefinition = "DECIMAL(5,1) DEFAULT 0.0")
    private Float lopMar = 0.0f;

    @Column(name = "lop_apr", nullable = false, columnDefinition = "DECIMAL(5,1) DEFAULT 0.0")
    private Float lopApr = 0.0f;

    @Column(name = "lop_may", nullable = false, columnDefinition = "DECIMAL(5,1) DEFAULT 0.0")
    private Float lopMay = 0.0f;

    @Column(name = "lop_jun", nullable = false, columnDefinition = "DECIMAL(5,1) DEFAULT 0.0")
    private Float lopJun = 0.0f;

    @Column(name = "lop_jul", nullable = false, columnDefinition = "DECIMAL(5,1) DEFAULT 0.0")
    private Float lopJul = 0.0f;

    @Column(name = "lop_aug", nullable = false, columnDefinition = "DECIMAL(5,1) DEFAULT 0.0")
    private Float lopAug = 0.0f;

    @Column(name = "lop_sep", nullable = false, columnDefinition = "DECIMAL(5,1) DEFAULT 0.0")
    private Float lopSep = 0.0f;

    @Column(name = "lop_oct", nullable = false, columnDefinition = "DECIMAL(5,1) DEFAULT 0.0")
    private Float lopOct = 0.0f;

    @Column(name = "lop_nov", nullable = false, columnDefinition = "DECIMAL(5,1) DEFAULT 0.0")
    private Float lopNov = 0.0f;

    @Column(name = "lop_dec", nullable = false, columnDefinition = "DECIMAL(5,1) DEFAULT 0.0")
    private Float lopDec = 0.0f;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Helper method to format float to 1 decimal place
    private Float formatToOneDecimal(Float value) {
        if (value == null) return 0.0f;
        return Math.round(value * 10) / 10.0f;
    }

    // Override setters to ensure 1 decimal precision
    public void setCasualLeaveBalance(Float casualLeaveBalance) {
        this.casualLeaveBalance = formatToOneDecimal(casualLeaveBalance);
    }

    public void setLeaveTaken(Float leaveTaken) {
        this.leaveTaken = formatToOneDecimal(leaveTaken);
    }

    public void setLop(Float lop) {
        this.lop = formatToOneDecimal(lop);
    }

    public void setLopJan(Float lopJan) {
        this.lopJan = formatToOneDecimal(lopJan);
    }

    public void setLopFeb(Float lopFeb) {
        this.lopFeb = formatToOneDecimal(lopFeb);
    }

    public void setLopMar(Float lopMar) {
        this.lopMar = formatToOneDecimal(lopMar);
    }

    public void setLopApr(Float lopApr) {
        this.lopApr = formatToOneDecimal(lopApr);
    }

    public void setLopMay(Float lopMay) {
        this.lopMay = formatToOneDecimal(lopMay);
    }

    public void setLopJun(Float lopJun) {
        this.lopJun = formatToOneDecimal(lopJun);
    }

    public void setLopJul(Float lopJul) {
        this.lopJul = formatToOneDecimal(lopJul);
    }

    public void setLopAug(Float lopAug) {
        this.lopAug = formatToOneDecimal(lopAug);
    }

    public void setLopSep(Float lopSep) {
        this.lopSep = formatToOneDecimal(lopSep);
    }

    public void setLopOct(Float lopOct) {
        this.lopOct = formatToOneDecimal(lopOct);
    }

    public void setLopNov(Float lopNov) {
        this.lopNov = formatToOneDecimal(lopNov);
    }

    public void setLopDec(Float lopDec) {
        this.lopDec = formatToOneDecimal(lopDec);
    }

    // Getters remain the same
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

    public Float getLeaveTaken() {
        return leaveTaken;
    }

    public Float getLop() {
        return lop;
    }

    public Float getLopJan() {
        return lopJan;
    }

    public Float getLopFeb() {
        return lopFeb;
    }

    public Float getLopMar() {
        return lopMar;
    }

    public Float getLopApr() {
        return lopApr;
    }

    public Float getLopMay() {
        return lopMay;
    }

    public Float getLopJun() {
        return lopJun;
    }

    public Float getLopJul() {
        return lopJul;
    }

    public Float getLopAug() {
        return lopAug;
    }

    public Float getLopSep() {
        return lopSep;
    }

    public Float getLopOct() {
        return lopOct;
    }

    public Float getLopNov() {
        return lopNov;
    }

    public Float getLopDec() {
        return lopDec;
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