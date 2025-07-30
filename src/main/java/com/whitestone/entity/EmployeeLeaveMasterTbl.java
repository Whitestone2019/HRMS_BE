package com.whitestone.entity;

import java.sql.Timestamp;

import java.util.Date;

import javax.persistence.Column;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;

import javax.persistence.GenerationType;

import javax.persistence.Id;

import javax.persistence.SequenceGenerator;

import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import oracle.sql.NUMBER;

@Entity

@Table(name = "EMPLOYEE_LEAVE_MASTER_TBL", schema = "HRMSUSER")

@JsonIgnoreProperties(ignoreUnknown = true)

public class EmployeeLeaveMasterTbl {

	@Id

	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMP_LEAVE_SEQ")

	@SequenceGenerator(name = "EMP_LEAVE_SEQ", sequenceName = "EMP_LEAVE_SEQ", allocationSize = 1)

	@Column(name = "SRL_NUM", nullable = false)

	private Long srlnum;

	@Column(name = "EMP_ID", nullable = false, length = 20)

	private String empid;

	@Column(name = "LEAVE_TYPE", nullable = false, length = 80)

	private String leavetype;

	@Column(name = "START_DATE", nullable = false)

	@JsonFormat(pattern = "yyyy-MM-dd")

	private java.sql.Timestamp startdate;

	@Column(name = "END_DATE", nullable = false)

	@JsonFormat(pattern = "yyyy-MM-dd")

	private java.sql.Timestamp enddate;

	@Column(name = "TEAM_EMAIL", nullable = false, length = 50)

	private String teamemail;

	@Column(name = "LEAVE_REASON", nullable = false, length = 255)

	private String leavereason;

	@Column(name = "STATUS", length = 20)

	private String status;

	@Column(name = "NO_OF_DAYS")

	private Float noofdays;

	@Column(name = "ENTITY_CRE_FLG", length = 1)

	private String entitycreflg;

	@Column(name = "DEL_FLG", length = 1)

	private String delflg;

	@Column(name = "RCRE_USER_ID", length = 20)

	private String rcreuserid;

	@Column(name = "RCRE_TIME")

	@JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")

	private java.sql.Timestamp rcretime;

	@Column(name = "RMOD_USER_ID", length = 20)

	private String rmoduserid;

	@Column(name = "RMOD_TIME")

	@JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")

	private java.sql.Timestamp rmodtime;

	@Column(name = "RVFY_USER_ID", length = 15)

	private String rvfyuserid;

	@Column(name = "RVFY_TIME")

	@JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")

	private java.sql.Timestamp rvfytime;

	@Column(name = "NO_OF_BOOKED_LEAVES", length = 5)

	private Float noofbooked;

	public Long getSrlnum() {
		return srlnum;
	}

	public void setSrlnum(Long srlnum) {
		this.srlnum = srlnum;
	}

	public String getEmpid() {
		return empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	public String getLeavetype() {
		return leavetype;
	}

	public void setLeavetype(String leavetype) {
		this.leavetype = leavetype;
	}

	public java.sql.Timestamp getStartdate() {
		return startdate;
	}

	public void setStartdate(java.sql.Timestamp startdate) {
		this.startdate = startdate;
	}

	public java.sql.Timestamp getEnddate() {
		return enddate;
	}

	public void setEnddate(java.sql.Timestamp enddate) {
		this.enddate = enddate;
	}

	public String getTeamemail() {
		return teamemail;
	}

	public void setTeamemail(String teamemail) {
		this.teamemail = teamemail;
	}

	public String getLeavereason() {
		return leavereason;
	}

	public void setLeavereason(String leavereason) {
		this.leavereason = leavereason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Float getNoofdays() {
		return noofdays;
	}

	public void setNoofdays(Float noofdays) {
		this.noofdays = noofdays;
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

	public java.sql.Timestamp getRcretime() {
		return rcretime;
	}

	public void setRcretime(java.sql.Timestamp rcretime) {
		this.rcretime = rcretime;
	}

	public String getRmoduserid() {
		return rmoduserid;
	}

	public void setRmoduserid(String rmoduserid) {
		this.rmoduserid = rmoduserid;
	}

	public java.sql.Timestamp getRmodtime() {
		return rmodtime;
	}

	public void setRmodtime(java.sql.Timestamp rmodtime) {
		this.rmodtime = rmodtime;
	}

	public String getRvfyuserid() {
		return rvfyuserid;
	}

	public void setRvfyuserid(String rvfyuserid) {
		this.rvfyuserid = rvfyuserid;
	}

	public java.sql.Timestamp getRvfytime() {
		return rvfytime;
	}

	public void setRvfytime(java.sql.Timestamp rvfytime) {
		this.rvfytime = rvfytime;
	}

	public Float getNoofbooked() {
		return noofbooked;
	}

	public void setNoofbooked(Float noofbooked) {
		this.noofbooked = noofbooked;
	}

}
