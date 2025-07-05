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

    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "EMP_LEAVE_SEQ")

    @SequenceGenerator(name = "EMP_LEAVE_SEQ", sequenceName = "EMP_LEAVE_SEQ", allocationSize = 1)

    @Column(name = "SRL_NUM", nullable = false)

    private Long srlnum;
 
    @Column(name = "EMP_ID", nullable = false, length = 20)

    private String empid;
 
    @Column(name = "LEAVE_TYPE", nullable = false, length = 80)

    private String leaveType;
 
    @Column(name = "START_DATE", nullable = false)

    @JsonFormat(pattern = "yyyy-MM-dd")

    private java.sql.Timestamp startDate;
 
    @Column(name = "END_DATE", nullable = false)

    @JsonFormat(pattern = "yyyy-MM-dd")

    private java.sql.Timestamp endDate;
 
    @Column(name = "TEAM_EMAIL", nullable = false, length = 50)

    private String teamEmail;
 
    @Column(name = "LEAVE_REASON", nullable = false, length = 255)

    private String leaveReason;
 
    @Column(name = "STATUS", length = 20)

    private String status;
 
    @Column(name = "NO_OF_DAYS")

    private Long noofdays;
 
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

    private Long noofbooked;

 
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

		return leaveType;

	}
 
	public void setLeavetype(String leaveType) {

		this.leaveType = leaveType;

	}
 
	public java.sql.Timestamp getStartdate() {

		return startDate;

	}
 
	public void setStartdate(java.sql.Timestamp startDate) {

		this.startDate = startDate;

	}
 
	public java.sql.Timestamp getEnddate() {

		return endDate;

	}
 
	public void setEnddate(java.sql.Timestamp endDate) {

		this.endDate = endDate;

	}
 
	public String getTeamemail() {

		return teamEmail;

	}
 
	public void setTeamemail(String teamEmail) {

		this.teamEmail = teamEmail;

	}
 
	public String getLeavereason() {

		return leaveReason;

	}
 
	public void setLeavereason(String leaveReason) {

		this.leaveReason = leaveReason;

	}
 
	public String getStatus() {

		return status;

	}
 
	public void setStatus(String status) {

		this.status = status;

	}
 
	public Long getnoofdays() {

		return noofdays;

	}
 
	public void setnoofdays(Long noofdays) {

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

	public void setnoofbooked(Long noofbooked) {

		this.noofbooked = noofbooked;

	}

	public Long getnoofbooked() {

		return noofbooked;

	}

 
	@Override

	public String toString() {

		return "EmployeeLeaveMasterTbl [srlnum=" + srlnum + ", empid=" + empid + ", leaveType=" + leaveType

				+ ", startDate=" + startDate + ", endDate=" + endDate + ", teamEmail=" + teamEmail + ", leaveReason="

				+ leaveReason + ", status=" + status + ", noofdays=" + noofdays + ", entitycreflg=" + entitycreflg

				+ ", delflg=" + delflg + ", rcreuserid=" + rcreuserid + ", rcretime=" + rcretime + ", rmoduserid="

				+ rmoduserid + ", rmodtime=" + rmodtime + ", rvfyuserid=" + rvfyuserid + ", rvfytime=" + rvfytime

				+ ", noofbooked=" + noofbooked +",getSrlnum()=" + getSrlnum() + ", getEmpid()=" + getEmpid() + ", getLeavetype()=" + getLeavetype()

				+ ", getStartdate()=" + getStartdate() + ", getEnddate()=" + getEnddate() + ", getTeamemail()="

				+ getTeamemail() + ", getLeavereason()=" + getLeavereason() + ", getStatus()=" + getStatus()

				+ ", getnoofdays()=" + getnoofdays() + ", getEntitycreflg()=" + getEntitycreflg() + ", getDelflg()="

				+ getDelflg() + ", getRcreuserid()=" + getRcreuserid() + ", getRcretime()=" + getRcretime()

				+ ", getRmoduserid()=" + getRmoduserid() + ", getRmodtime()=" + getRmodtime() + ", getRvfyuserid()="

				+ getRvfyuserid() + ", getRvfytime()=" + getRvfytime() + ", getClass()=" + getClass() + ", hashCode()="

				+ hashCode() + ", toString()=" + super.toString() + "]";

	}
 
	public EmployeeLeaveMasterTbl(Long srlnum, String empid, String leaveType, Timestamp startDate, Timestamp endDate,

			String teamEmail, String leaveReason, String status, Long noofdays, String entitycreflg, String delflg,

			String rcreuserid, Timestamp rcretime, String rmoduserid, Timestamp rmodtime, String rvfyuserid,

			Timestamp rvfytime,Long noofbooked) {

		super();

		this.srlnum = srlnum;

		this.empid = empid;

		this.leaveType = leaveType;

		this.startDate = startDate;

		this.endDate = endDate;

		this.teamEmail = teamEmail;

		this.leaveReason = leaveReason;

		this.status = status;

		this.noofdays = noofdays;

		this.entitycreflg = entitycreflg;

		this.delflg = delflg;

		this.rcreuserid = rcreuserid;

		this.rcretime = rcretime;

		this.rmoduserid = rmoduserid;

		this.rmodtime = rmodtime;

		this.rvfyuserid = rvfyuserid;

		this.rvfytime = rvfytime;

		this.noofbooked = noofbooked;

	}
 
	public EmployeeLeaveMasterTbl() {

		super();

		// TODO Auto-generated constructor stub

	}
 
	public boolean isPresent() {

		// TODO Auto-generated method stub

		return false;

	}
 
	public EmployeeLeaveMasterTbl get() {

		// TODO Auto-generated method stub

		return null;

	}    

}
 