package com.whitestone.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "user_maintenance_tbl", schema = "HRMSUSER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class usermaintenance {
    @Id
    
    @Column(name = "USER_ID", nullable = false, length = 20)
    private String userid;

    @Column(name = "EMP_ID", nullable = false, length = 20)
    private String empid;

    @Column(name = "PASSWORD", length = 100)
    private String password;

    @Column(name = "USER_NAME", length = 50)
    private String username;

    @Column(name = "FIRST_NAME", length = 50)
    private String firstname;

    @Column(name = "LAST_NAME", length = 50)
    private String lastname;

    @Column(name = "EMAIL_ID", length = 50)
    private String emailid;

    @Column(name = "PHONE_NUMBER", length = 13)
    private String phonenumber;

    @Column(name = "ROLE_ID", length = 10)
    private String roleid;
    
    @Column(name = "REPOTE_TO", length = 10)
    private String repoteTo;

    @Column(name = "STATUS", length = 10)
    private String status;

    @Column(name = "LAST_LOGIN")
    @JsonFormat(pattern = "YYYY-MM-DD")
    private Date lastlogin;

    @Column(name = "DISABLE_FROM_DATE")
    @JsonFormat(pattern = "YYYY-MM-DD")
    private Date disablefromdate;

    @Column(name = "DISABLE_TO_DATE")
    @JsonFormat(pattern = "YYYY-MM-DD")
    private Date disabletodate;

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

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getEmpid() {
		return empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getRepoteTo() {
		return repoteTo;
	}

	public void setRepoteTo(String repoteTo) {
		this.repoteTo = repoteTo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}

	public Date getDisablefromdate() {
		return disablefromdate;
	}

	public void setDisablefromdate(Date disablefromdate) {
		this.disablefromdate = disablefromdate;
	}

	public Date getDisabletodate() {
		return disabletodate;
	}

	public void setDisabletodate(Date disabletodate) {
		this.disabletodate = disabletodate;
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
		return "usermaintenance [userid=" + userid + ", empid=" + empid + ", password=" + password + ", username="
				+ username + ", firstname=" + firstname + ", lastname=" + lastname + ", emailid=" + emailid
				+ ", phonenumber=" + phonenumber + ", roleid=" + roleid + ", repoteTo=" + repoteTo + ", status="
				+ status + ", lastlogin=" + lastlogin + ", disablefromdate=" + disablefromdate + ", disabletodate="
				+ disabletodate + ", rcreuserid=" + rcreuserid + ", rcretime=" + rcretime + ", rmoduserid=" + rmoduserid
				+ ", rmodtime=" + rmodtime + ", rvfyuserid=" + rvfyuserid + ", rvfytime=" + rvfytime + ", getUserid()="
				+ getUserid() + ", getEmpid()=" + getEmpid() + ", getPassword()=" + getPassword() + ", getUsername()="
				+ getUsername() + ", getFirstname()=" + getFirstname() + ", getLastname()=" + getLastname()
				+ ", getEmailid()=" + getEmailid() + ", getPhonenumber()=" + getPhonenumber() + ", getRoleid()="
				+ getRoleid() + ", getRepoteTo()=" + getRepoteTo() + ", getStatus()=" + getStatus()
				+ ", getLastlogin()=" + getLastlogin() + ", getDisablefromdate()=" + getDisablefromdate()
				+ ", getDisabletodate()=" + getDisabletodate() + ", getRcreuserid()=" + getRcreuserid()
				+ ", getRcretime()=" + getRcretime() + ", getRmoduserid()=" + getRmoduserid() + ", getRmodtime()="
				+ getRmodtime() + ", getRvfyuserid()=" + getRvfyuserid() + ", getRvfytime()=" + getRvfytime()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	public usermaintenance(String userid, String empid, String password, String username, String firstname,
			String lastname, String emailid, String phonenumber, String roleid, String repoteTo, String status,
			Date lastlogin, Date disablefromdate, Date disabletodate, String rcreuserid, Date rcretime,
			String rmoduserid, Date rmodtime, String rvfyuserid, Date rvfytime) {
		super();
		this.userid = userid;
		this.empid = empid;
		this.password = password;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.emailid = emailid;
		this.phonenumber = phonenumber;
		this.roleid = roleid;
		this.repoteTo = repoteTo;
		this.status = status;
		this.lastlogin = lastlogin;
		this.disablefromdate = disablefromdate;
		this.disabletodate = disabletodate;
		this.rcreuserid = rcreuserid;
		this.rcretime = rcretime;
		this.rmoduserid = rmoduserid;
		this.rmodtime = rmodtime;
		this.rvfyuserid = rvfyuserid;
		this.rvfytime = rvfytime;
	}

	public usermaintenance() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
    
}
