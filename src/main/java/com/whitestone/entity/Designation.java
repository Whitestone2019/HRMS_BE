package com.whitestone.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "designation")
public class Designation {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DEPARTMENT")
    private String department;


    @Column(name = "SALARY_MIN")
    private Double salaryMin;

    @Column(name = "SALARY_MAX")
    private Double salaryMax;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ENTITY_CRE_FLG", length = 1)
    private String entityCreFlg;

    @Column(name = "DEL_FLG", length = 1)
    private String delFlg;

    @Column(name = "RCRE_USER_ID", nullable = false, length = 15)
    private String rCreUserId;

    @Column(name = "RCRE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rCreTime;

    @Column(name = "RMOD_USER_ID", length = 15)
    private String rModUserId;

    @Column(name = "RMOD_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rModTime;

    @Column(name = "RVFY_USER_ID", length = 15)
    private String rVfyUserId;

    @Column(name = "RVFY_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rVfyTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Double getSalaryMin() {
		return salaryMin;
	}

	public void setSalaryMin(Double salaryMin) {
		this.salaryMin = salaryMin;
	}

	public Double getSalaryMax() {
		return salaryMax;
	}

	public void setSalaryMax(Double salaryMax) {
		this.salaryMax = salaryMax;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getrCreUserId() {
		return rCreUserId;
	}

	public void setrCreUserId(String rCreUserId) {
		this.rCreUserId = rCreUserId;
	}

	public Date getrCreTime() {
		return rCreTime;
	}

	public void setrCreTime(Date rCreTime) {
		this.rCreTime = rCreTime;
	}

	public String getrModUserId() {
		return rModUserId;
	}

	public void setrModUserId(String rModUserId) {
		this.rModUserId = rModUserId;
	}

	public Date getrModTime() {
		return rModTime;
	}

	public void setrModTime(Date rModTime) {
		this.rModTime = rModTime;
	}

	public String getrVfyUserId() {
		return rVfyUserId;
	}

	public void setrVfyUserId(String rVfyUserId) {
		this.rVfyUserId = rVfyUserId;
	}

	public Date getrVfyTime() {
		return rVfyTime;
	}

	public void setrVfyTime(Date rVfyTime) {
		this.rVfyTime = rVfyTime;
	}

	@Override
	public String toString() {
		return "Designation [id=" + id + ", title=" + title + ", department=" + department + ", salaryMin=" + salaryMin
				+ ", salaryMax=" + salaryMax + ", status=" + status + ", entityCreFlg=" + entityCreFlg + ", delFlg="
				+ delFlg + ", rCreUserId=" + rCreUserId + ", rCreTime=" + rCreTime + ", rModUserId=" + rModUserId
				+ ", rModTime=" + rModTime + ", rVfyUserId=" + rVfyUserId + ", rVfyTime=" + rVfyTime + ", getId()="
				+ getId() + ", getTitle()=" + getTitle() + ", getDepartment()=" + getDepartment() + ", getSalaryMin()="
				+ getSalaryMin() + ", getSalaryMax()=" + getSalaryMax() + ", getStatus()=" + getStatus()
				+ ", getEntityCreFlg()=" + getEntityCreFlg() + ", getDelFlg()=" + getDelFlg() + ", getrCreUserId()="
				+ getrCreUserId() + ", getrCreTime()=" + getrCreTime() + ", getrModUserId()=" + getrModUserId()
				+ ", getrModTime()=" + getrModTime() + ", getrVfyUserId()=" + getrVfyUserId() + ", getrVfyTime()="
				+ getrVfyTime() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

	public Designation(Long id, String title, String department, Double salaryMin, Double salaryMax, String status,
			String entityCreFlg, String delFlg, String rCreUserId, Date rCreTime, String rModUserId, Date rModTime,
			String rVfyUserId, Date rVfyTime) {
		super();
		this.id = id;
		this.title = title;
		this.department = department;
		this.salaryMin = salaryMin;
		this.salaryMax = salaryMax;
		this.status = status;
		this.entityCreFlg = entityCreFlg;
		this.delFlg = delFlg;
		this.rCreUserId = rCreUserId;
		this.rCreTime = rCreTime;
		this.rModUserId = rModUserId;
		this.rModTime = rModTime;
		this.rVfyUserId = rVfyUserId;
		this.rVfyTime = rVfyTime;
	}

	public Designation() {
		super();
		// TODO Auto-generated constructor stub
	}

	

}