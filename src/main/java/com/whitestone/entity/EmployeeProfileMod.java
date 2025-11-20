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
    
    @Column(name = "PHOTO_PATH", length = 500)
    private String photoPath;

    @Column(name = "AADHAR_PATH", length = 500)
    private String aadharPath;

    @Column(name = "PAN_PATH", length = 500)
    private String panPath;

    @Column(name = "TENTH_PATH", length = 500)
    private String tenthPath;

    @Column(name = "TWELFTH_PATH", length = 500)
    private String twelfthPath;

    @Column(name = "DEGREE_PATH", length = 500)
    private String degreePath;
    
 // ADD THESE NEW COLUMNS TO YOUR TABLE & ENTITY

    @Column(name = "GENDER", length = 10)
    private String gender;

    @Column(name = "MARITAL_STATUS", length = 20)
    private String maritalstatus;

    @Column(name = "NATIONALITY", length = 50)
    private String nationality = "Indian";

    @Column(name = "DATE_OF_JOINING")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateofjoining;

    @Column(name = "DESIGNATION", length = 100)
    private String designation;

    @Column(name = "DEPARTMENT", length = 100)
    private String department;

    @Column(name = "WORK_LOCATION", length = 50)
    private String worklocation;

    @Column(name = "REPORTING_MANAGER", length = 15)
    private String reportingmanager;

    @Column(name = "EMERGENCY_CONTACT_NAME", length = 80)
    private String emergencycontactname;

    @Column(name = "EMERGENCY_CONTACT_NUMBER", length = 30)
    private String emergencycontactnumber;

    @Column(name = "EMERGENCY_CONTACT_RELATION", length = 50)
    private String emergencycontactrelation;

    @Column(name = "ALTERNATE_MOBILE_NUMBER", length = 30)
    private String alternatemobilenumber;

    @Column(name = "PASSPORT_NUMBER", length = 20)
    private String passportnumber;

    @Column(name = "DRIVING_LICENSE", length = 30)
    private String drivinglicense;

    @Column(name = "ESI_NUMBER", length = 20)
    private String esinumber;

	
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


	public String getPhotoPath() {
		return photoPath;
	}


	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}


	public String getAadharPath() {
		return aadharPath;
	}


	public void setAadharPath(String aadharPath) {
		this.aadharPath = aadharPath;
	}


	public String getPanPath() {
		return panPath;
	}


	public void setPanPath(String panPath) {
		this.panPath = panPath;
	}


	public String getTenthPath() {
		return tenthPath;
	}


	public void setTenthPath(String tenthPath) {
		this.tenthPath = tenthPath;
	}


	public String getTwelfthPath() {
		return twelfthPath;
	}


	public void setTwelfthPath(String twelfthPath) {
		this.twelfthPath = twelfthPath;
	}


	public String getDegreePath() {
		return degreePath;
	}


	public void setDegreePath(String degreePath) {
		this.degreePath = degreePath;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getMaritalstatus() {
		return maritalstatus;
	}


	public void setMaritalstatus(String maritalstatus) {
		this.maritalstatus = maritalstatus;
	}


	public String getNationality() {
		return nationality;
	}


	public void setNationality(String nationality) {
		this.nationality = nationality;
	}


	public Date getDateofjoining() {
		return dateofjoining;
	}


	public void setDateofjoining(Date dateofjoining) {
		this.dateofjoining = dateofjoining;
	}


	public String getDesignation() {
		return designation;
	}


	public void setDesignation(String designation) {
		this.designation = designation;
	}


	public String getWorklocation() {
		return worklocation;
	}


	public void setWorklocation(String worklocation) {
		this.worklocation = worklocation;
	}


	public String getReportingmanager() {
		return reportingmanager;
	}


	public void setReportingmanager(String reportingmanager) {
		this.reportingmanager = reportingmanager;
	}


	public String getEmergencycontactname() {
		return emergencycontactname;
	}


	public void setEmergencycontactname(String emergencycontactname) {
		this.emergencycontactname = emergencycontactname;
	}


	public String getEmergencycontactnumber() {
		return emergencycontactnumber;
	}


	public void setEmergencycontactnumber(String emergencycontactnumber) {
		this.emergencycontactnumber = emergencycontactnumber;
	}


	public String getEmergencycontactrelation() {
		return emergencycontactrelation;
	}


	public void setEmergencycontactrelation(String emergencycontactrelation) {
		this.emergencycontactrelation = emergencycontactrelation;
	}


	public String getAlternatemobilenumber() {
		return alternatemobilenumber;
	}


	public void setAlternatemobilenumber(String alternatemobilenumber) {
		this.alternatemobilenumber = alternatemobilenumber;
	}


	public String getPassportnumber() {
		return passportnumber;
	}


	public void setPassportnumber(String passportnumber) {
		this.passportnumber = passportnumber;
	}


	public String getDrivinglicense() {
		return drivinglicense;
	}


	public void setDrivinglicense(String drivinglicense) {
		this.drivinglicense = drivinglicense;
	}


	public String getEsinumber() {
		return esinumber;
	}


	public void setEsinumber(String esinumber) {
		this.esinumber = esinumber;
	}


	public void setDepartment(String department) {
		this.department = department;
	}


	public String getDepartment() {
		return department;
	}



}