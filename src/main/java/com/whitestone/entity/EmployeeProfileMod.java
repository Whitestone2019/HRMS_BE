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
@Table(name = "EMPLOYEE_PROFILE_MOD_TBL", schema = "HRMSUSER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeProfileMod {
    
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "HRMSUSER.USER_ID_SEQ", allocationSize = 1)
    @Column(name = "USER_ID",  length = 15)
    private Long userid;	

    @Column(name = "EMP_ID", nullable = false, length = 15)
    private String empid;

    @Column(name = "EMPLOYEE_NAME", length = 80)
    private String employeename;

    @Column(name = "DATE_OF_BIRTH" )
    @JsonFormat(pattern = "YYYY-MM-DD") 
    private Date dateofbirth;

    @Column(name = "BLOOD_GROUP", length = 10)
    private String bloodgroup;

    @Column(name = "EMAIL_ID",nullable = false, length = 320)
    private String emailid;

    @Column(name = "OFFICIAL_EMAIL", length = 320)
    private String officialemail;

    @Column(name = "FIRST_NAME", nullable = false, length = 80)
    private String firstname;

    @Column(name = "LAST_NAME", nullable = false, length = 80)
    private String lastname;

    @Column(name = "MOBILE_NUMBER", length = 30)
    private String mobilenumber;

    @Column(name = "PARENT_NAME", length = 80)
    private String parentname;

    @Column(name = "PARENT_MOB_NUM", length = 30)
    private String parentmobnum;

    @Column(name = "SPOUSE_NAME", nullable = true, length = 80)
    private String spousename;

    @Column(name = "SPOUSE_MOB_NUM", length = 30)
    private String spousemobnum;

    @Column(name = "AADHAAR_NUMBER", nullable = true, length = 100)
    private String aadhaarnumber;

    @Column(name = "PAN_NUMBER", length = 100)
    private String pannumber;

    @Column(name = "ENTITY_CRE_FLG", length = 1)
    private String entitycreflg;

    @Column(name = "DEL_FLG", length = 1)
    private String delflg;

    @Column(name = "RCRE_USER_ID",  length = 15)
    private String rcreuserid;

    @Column(name = "RCRE_TIME" )
    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss") 
    private Date rcretime;

    @Column(name = "RMOD_USER_ID", length = 15)
    private String rmoduserid;

    @Column(name = "RMOD_TIME" )
    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss") 
    private Date rmodtime;

    @Column(name = "RVFY_USER_ID", length = 15)
    private String rvfyuserid;

    @Column(name = "RVFY_TIME")
    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss") 
    private Date rvfytime;
    
    @Column(name = "UAN_NUMBER", length = 15)
    private String uannumber;

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getEmpid() {
		return empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	public String getEmployeename() {
		return employeename;
	}

	public void setEmployeename(String employeename) {
		this.employeename = employeename;
	}

	public Date getDateofbirth() {
		return dateofbirth;
	}

	public void setDateofbirth(Date dateofbirth) {
		this.dateofbirth = dateofbirth;
	}

	public String getBloodgroup() {
		return bloodgroup;
	}

	public void setBloodgroup(String bloodgroup) {
		this.bloodgroup = bloodgroup;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getOfficialemail() {
		return officialemail;
	}

	public void setOfficialemail(String officialemail) {
		this.officialemail = officialemail;
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

	public String getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getParentname() {
		return parentname;
	}

	public void setParentname(String parentname) {
		this.parentname = parentname;
	}

	public String getParentmobnum() {
		return parentmobnum;
	}

	public void setParentmobnum(String parentmobnum) {
		this.parentmobnum = parentmobnum;
	}

	public String getSpousename() {
		return spousename;
	}

	public void setSpousename(String spousename) {
		this.spousename = spousename;
	}

	public String getSpousemobnum() {
		return spousemobnum;
	}

	public void setSpousemobnum(String spousemobnum) {
		this.spousemobnum = spousemobnum;
	}

	public String getAadhaarnumber() {
		return aadhaarnumber;
	}

	public void setAadhaarnumber(String aadhaarnumber) {
		this.aadhaarnumber = aadhaarnumber;
	}

	public String getPannumber() {
		return pannumber;
	}

	public void setPannumber(String pannumber) {
		this.pannumber = pannumber;
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

	public String getUannumber() {
		return uannumber;
	}

	public void setUannumber(String uannumber) {
		this.uannumber = uannumber;
	}

	@Override
	public String toString() {
		return "EmployeeProfileMod [userid=" + userid + ", empid=" + empid + ", employeename=" + employeename
				+ ", dateofbirth=" + dateofbirth + ", bloodgroup=" + bloodgroup + ", emailid=" + emailid
				+ ", officialemail=" + officialemail + ", firstname=" + firstname + ", lastname=" + lastname
				+ ", mobilenumber=" + mobilenumber + ", parentname=" + parentname + ", parentmobnum=" + parentmobnum
				+ ", spousename=" + spousename + ", spousemobnum=" + spousemobnum + ", aadhaarnumber=" + aadhaarnumber
				+ ", pannumber=" + pannumber + ", entitycreflg=" + entitycreflg + ", delflg=" + delflg + ", rcreuserid="
				+ rcreuserid + ", rcretime=" + rcretime + ", rmoduserid=" + rmoduserid + ", rmodtime=" + rmodtime
				+ ", rvfyuserid=" + rvfyuserid + ", rvfytime=" + rvfytime + ", uannumber=" + uannumber
				+ ", getUserid()=" + getUserid() + ", getEmpid()=" + getEmpid() + ", getEmployeename()="
				+ getEmployeename() + ", getDateofbirth()=" + getDateofbirth() + ", getBloodgroup()=" + getBloodgroup()
				+ ", getEmailid()=" + getEmailid() + ", getOfficialemail()=" + getOfficialemail() + ", getFirstname()="
				+ getFirstname() + ", getLastname()=" + getLastname() + ", getMobilenumber()=" + getMobilenumber()
				+ ", getParentname()=" + getParentname() + ", getParentmobnum()=" + getParentmobnum()
				+ ", getSpousename()=" + getSpousename() + ", getSpousemobnum()=" + getSpousemobnum()
				+ ", getAadhaarnumber()=" + getAadhaarnumber() + ", getPannumber()=" + getPannumber()
				+ ", getEntitycreflg()=" + getEntitycreflg() + ", getDelflg()=" + getDelflg() + ", getRcreuserid()="
				+ getRcreuserid() + ", getRcretime()=" + getRcretime() + ", getRmoduserid()=" + getRmoduserid()
				+ ", getRmodtime()=" + getRmodtime() + ", getRvfyuserid()=" + getRvfyuserid() + ", getRvfytime()="
				+ getRvfytime() + ", getUannumber()=" + getUannumber() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}

	public EmployeeProfileMod(Long userid, String empid, String employeename, Date dateofbirth, String bloodgroup,
			String emailid, String officialemail, String firstname, String lastname, String mobilenumber,
			String parentname, String parentmobnum, String spousename, String spousemobnum, String aadhaarnumber,
			String pannumber, String entitycreflg, String delflg, String rcreuserid, Date rcretime, String rmoduserid,
			Date rmodtime, String rvfyuserid, Date rvfytime, String uannumber) {
		super();
		this.userid = userid;
		this.empid = empid;
		this.employeename = employeename;
		this.dateofbirth = dateofbirth;
		this.bloodgroup = bloodgroup;
		this.emailid = emailid;
		this.officialemail = officialemail;
		this.firstname = firstname;
		this.lastname = lastname;
		this.mobilenumber = mobilenumber;
		this.parentname = parentname;
		this.parentmobnum = parentmobnum;
		this.spousename = spousename;
		this.spousemobnum = spousemobnum;
		this.aadhaarnumber = aadhaarnumber;
		this.pannumber = pannumber;
		this.entitycreflg = entitycreflg;
		this.delflg = delflg;
		this.rcreuserid = rcreuserid;
		this.rcretime = rcretime;
		this.rmoduserid = rmoduserid;
		this.rmodtime = rmodtime;
		this.rvfyuserid = rvfyuserid;
		this.rvfytime = rvfytime;
		this.uannumber = uannumber;
	}

	public EmployeeProfileMod() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Object getDepartment() {
		// TODO Auto-generated method stub
		return null;
	}
      
}