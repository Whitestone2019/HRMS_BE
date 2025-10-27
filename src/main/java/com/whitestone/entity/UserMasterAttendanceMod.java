package com.whitestone.entity;
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
@Entity
@Table(name = "USER_MASTER_ATTENDANCE_MOD_TBL", schema = "HRMSUSER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMasterAttendanceMod {

	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "USR_ATTEN_SEQ")
	    @SequenceGenerator(name = "USR_ATTEN_SEQ", sequenceName = "USR_ATTEN_SEQ", allocationSize = 1)
	    @Column(name = "SRL_NUM")
	    private Long srlnum;
	   
	    @Column(name = "USER_ID", nullable = false, length = 20)
	    private String userid;
	
	    @Column(name = "ATTENDANCE_ID", nullable = false, length = 20)
	    private String attendanceid;
	
	    @Column(name = "ATTENDANCE_DATE")
	    @JsonFormat(pattern = "yyyy-MM-dd")
	    private Date attendancedate;
	
	    @Column(name = "STATUS", length = 20)
	    private String status;
	
	    @Column(name = "CHECK_IN_TIME")
	    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
	    private Date checkintime;
	
	    @Column(name = "CHECK_OUT_TIME")
	    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
	    private Date checkouttime;
	
	    @Column(name = "TOTAL_HOURS_WORKED")
	    private String totalhoursworked;
	
	    @Column(name = "CHECK_IN_STATUS", length = 20)
	    private String checkinstatus;
	
	    @Column(name = "CHECK_OUT_STATUS", length = 20)
	    private String checkoutstatus;
	
	    @Column(name = "REMARKS", length = 50)
	    private String remarks;
	
	    @Column(name = "OVER_TIME_HOURS")
	    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
	    private Date overtimehours;
	
	    @Column(name = "SHIFT", length = 50)
	    private String shift;
	
	    @Column(name = "HOLIDAY_STATUS", length = 50)
	    private String holidaystatus;
	
	    @Column(name = "CHECK_IN_LOCATION", length = 50)
	    private String checkinlocation;
	
	    @Column(name = "CHECK_OUT_LOCATION", length = 50)
	    private String checkoutlocation;
	
	    @Column(name = "RCRE_USER_ID", length = 20)
	    private String rcreuserid;
	
	    @Column(name = "RCRE_TIME")
	    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
	    private Date rcretime;
	
	    @Column(name = "RMOD_USER_ID", length = 20)
	    private String rmoduserid;
	
	    @Column(name = "RMOD_TIME")
	    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
	    private Date rmodtime;
	
	    @Column(name = "RVFY_USER_ID", length = 15)
	    private String rvfyuserid;
	
	    @Column(name = "RVFY_TIME")
	    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
	    private Date rvfytime;

	public Long getSrlnum() {
		return srlnum;
	}

	public void setSrlnum(Long srlnum) {
		this.srlnum = srlnum;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAttendanceid() {
		return attendanceid;
	}

	public void setAttendanceid(String attendanceid) {
		this.attendanceid = attendanceid;
	}

	public Date getAttendancedate() {
		return attendancedate;
	}

	public void setAttendancedate(Date attendancedate) {
		this.attendancedate = attendancedate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCheckintime() {
		return checkintime;
	}

	public void setCheckintime(Date checkintime) {
		this.checkintime = checkintime;
	}

	public Date getCheckouttime() {
		return checkouttime;
	}

	public void setCheckouttime(Date checkouttime) {
		this.checkouttime = checkouttime;
	}

	public String getTotalhoursworked() {
		return totalhoursworked;
	}

	public void setTotalhoursworked(String totalhoursworked) {
		this.totalhoursworked = totalhoursworked;
	}

	public String getCheckinstatus() {
		return checkinstatus;
	}

	public void setCheckinstatus(String checkinstatus) {
		this.checkinstatus = checkinstatus;
	}

	public String getCheckoutstatus() {
		return checkoutstatus;
	}

	public void setCheckoutstatus(String checkoutstatus) {
		this.checkoutstatus = checkoutstatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getOvertimehours() {
		return overtimehours;
	}

	public void setOvertimehours(Date overtimehours) {
		this.overtimehours = overtimehours;
	}

	public String getShift() {
		return shift;
	}

	public void setShift(String shift) {
		this.shift = shift;
	}

	public String getHolidaystatus() {
		return holidaystatus;
	}

	public void setHolidaystatus(String holidaystatus) {
		this.holidaystatus = holidaystatus;
	}

	public String getCheckinlocation() {
		return checkinlocation;
	}

	public void setCheckinlocation(String checkinlocation) {
		this.checkinlocation = checkinlocation;
	}

	public String getCheckoutlocation() {
		return checkoutlocation;
	}

	public void setCheckoutlocation(String checkoutlocation) {
		this.checkoutlocation = checkoutlocation;
	}

	public String getRcreuserid() {
		return rcreuserid;
	}

	public void setRcreuserid(String rcreuserid) {
		this.rcreuserid = rcreuserid;
	}

	public Date getRcretime() {
		return rcretime;
	}

	public void setRcretime(Date rcretime) {
		this.rcretime = rcretime;
	}

	public String getRmoduserid() {
		return rmoduserid;
	}

	public void setRmoduserid(String rmoduserid) {
		this.rmoduserid = rmoduserid;
	}

	public Date getRmodtime() {
		return rmodtime;
	}

	public void setRmodtime(Date rmodtime) {
		this.rmodtime = rmodtime;
	}

	public String getRvfyuserid() {
		return rvfyuserid;
	}

	public void setRvfyuserid(String rvfyuserid) {
		this.rvfyuserid = rvfyuserid;
	}

	public Date getRvfytime() {
		return rvfytime;
	}

	public void setRvfytime(Date rvfytime) {
		this.rvfytime = rvfytime;
	}

	@Override
	public String toString() {
		return "UserMasterAttendanceMod [srlnum=" + srlnum + ", userid=" + userid + ", attendanceid=" + attendanceid
				+ ", attendancedate=" + attendancedate + ", status=" + status + ", checkintime=" + checkintime
				+ ", checkouttime=" + checkouttime + ", totalhoursworked=" + totalhoursworked + ", checkinstatus="
				+ checkinstatus + ", checkoutstatus=" + checkoutstatus + ", remarks=" + remarks + ", overtimehours="
				+ overtimehours + ", shift=" + shift + ", holidaystatus=" + holidaystatus + ", checkinlocation="
				+ checkinlocation + ", checkoutlocation=" + checkoutlocation + ", rcreuserid=" + rcreuserid
				+ ", rcretime=" + rcretime + ", rmoduserid=" + rmoduserid + ", rmodtime=" + rmodtime + ", rvfyuserid="
				+ rvfyuserid + ", rvfytime=" + rvfytime + ", getSrlnum()=" + getSrlnum() + ", getUserid()="
				+ getUserid() + ", getAttendanceid()=" + getAttendanceid() + ", getAttendancedate()="
				+ getAttendancedate() + ", getStatus()=" + getStatus() + ", getCheckintime()=" + getCheckintime()
				+ ", getCheckouttime()=" + getCheckouttime() + ", getTotalhoursworked()=" + getTotalhoursworked()
				+ ", getCheckinstatus()=" + getCheckinstatus() + ", getCheckoutstatus()=" + getCheckoutstatus()
				+ ", getRemarks()=" + getRemarks() + ", getOvertimehours()=" + getOvertimehours() + ", getShift()="
				+ getShift() + ", getHolidaystatus()=" + getHolidaystatus() + ", getCheckinlocation()="
				+ getCheckinlocation() + ", getCheckoutlocation()=" + getCheckoutlocation() + ", getRcreuserid()="
				+ getRcreuserid() + ", getRcretime()=" + getRcretime() + ", getRmoduserid()=" + getRmoduserid()
				+ ", getRmodtime()=" + getRmodtime() + ", getRvfyuserid()=" + getRvfyuserid() + ", getRvfytime()="
				+ getRvfytime() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

	public UserMasterAttendanceMod(Long srlnum, String userid, String attendanceid, Date attendancedate, String status,
			Date checkintime, Date checkouttime, String totalhoursworked, String checkinstatus, String checkoutstatus,
			String remarks, Date overtimehours, String shift, String holidaystatus, String checkinlocation,
			String checkoutlocation, String rcreuserid, Date rcretime, String rmoduserid, Date rmodtime,
			String rvfyuserid, Date rvfytime) {
		super();
		this.srlnum = srlnum;
		this.userid = userid;
		this.attendanceid = attendanceid;
		this.attendancedate = attendancedate;
		this.status = status;
		this.checkintime = checkintime;
		this.checkouttime = checkouttime;
		this.totalhoursworked = totalhoursworked;
		this.checkinstatus = checkinstatus;
		this.checkoutstatus = checkoutstatus;
		this.remarks = remarks;
		this.overtimehours = overtimehours;
		this.shift = shift;
		this.holidaystatus = holidaystatus;
		this.checkinlocation = checkinlocation;
		this.checkoutlocation = checkoutlocation;
		this.rcreuserid = rcreuserid;
		this.rcretime = rcretime;
		this.rmoduserid = rmoduserid;
		this.rmodtime = rmodtime;
		this.rvfyuserid = rvfyuserid;
		this.rvfytime = rvfytime;
	}

	public UserMasterAttendanceMod() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
            
}