package com.whitestone.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "EMPLOYEE_PROFESSIONAL_DETAILS_TBL", schema = "HRMSUSER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeProfessionalDetails {
    @Id
    
    @Column(name = "USER_ID", length = 15)
    private String userid;

    @Column(name = "EXP_ID", length = 15)
    private String expid;

    @Column(name = "ORGANISATION", length = 100)
    private String organisation;

    @Column(name = "LOCATION", length = 100)
    private String location;

    @Column(name = "ORG_EMP_ID", length = 15)
    private String orgempid;

    @Column(name = "ORG_DEPT", length = 100)
    private String orgdept;

    @Column(name = "ORG_ROLE", length = 100)
    private String orgrole;

    @Column(name = "JOINING_DATE")
    @JsonFormat(pattern = "YYYY-MM-DD")
    private Date joiningdate;

    @Column(name = "RELIEVING_DATE")
    @JsonFormat(pattern = "YYYY-MM-DD")
    private Date relievingdate;

    @Column(name = "CTC", length = 20)
    private String ctc;

    @Column(name = "ADDITIONAL_INFORMATION", length = 100)
    private String additionalinformation;

    @Column(name = "OFFER_LETTER", length = 100)
    private String offerletter;

    @Column(name = "ENTITY_CRE_FLG", length = 1)
    private String entitycreflg;

    @Column(name = "DEL_FLG", length = 1)
    private String delflg;

    @Column(name = "RCRE_USER_ID", nullable = false, length = 15)
    private String rcreuserid;

    @Column(name = "RCRE_TIME")
    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
    private Date rcretime;

    @Column(name = "RMOD_USER_ID", length = 15)
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

	public String getExpid() {
		return expid;
	}

	public void setExpid(String expid) {
		this.expid = expid;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOrgempid() {
		return orgempid;
	}

	public void setOrgempid(String orgempid) {
		this.orgempid = orgempid;
	}

	public String getOrgdept() {
		return orgdept;
	}

	public void setOrgdept(String orgdept) {
		this.orgdept = orgdept;
	}

	public String getOrgrole() {
		return orgrole;
	}

	public void setOrgrole(String orgrole) {
		this.orgrole = orgrole;
	}

	public Date getJoiningdate() {
		return joiningdate;
	}

	public void setJoiningdate(Date joiningdate) {
		this.joiningdate = joiningdate;
	}

	public Date getRelievingdate() {
		return relievingdate;
	}

	public void setRelievingdate(Date relievingdate) {
		this.relievingdate = relievingdate;
	}

	public String getCtc() {
		return ctc;
	}

	public void setCtc(String ctc) {
		this.ctc = ctc;
	}

	public String getAdditionalinformation() {
		return additionalinformation;
	}

	public void setAdditionalinformation(String additionalinformation) {
		this.additionalinformation = additionalinformation;
	}

	public String getOfferletter() {
		return offerletter;
	}

	public void setOfferletter(String offerletter) {
		this.offerletter = offerletter;
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

	@Override
	public String toString() {
		return "EmployeeProfessionalDetails [userid=" + userid + ", expid=" + expid + ", organisation=" + organisation
				+ ", location=" + location + ", orgempid=" + orgempid + ", orgdept=" + orgdept + ", orgrole=" + orgrole
				+ ", joiningdate=" + joiningdate + ", relievingdate=" + relievingdate + ", ctc=" + ctc
				+ ", additionalinformation=" + additionalinformation + ", offerletter=" + offerletter
				+ ", entitycreflg=" + entitycreflg + ", delflg=" + delflg + ", rcreuserid=" + rcreuserid + ", rcretime="
				+ rcretime + ", rmoduserid=" + rmoduserid + ", rmodtime=" + rmodtime + ", rvfyuserid=" + rvfyuserid
				+ ", rvfytime=" + rvfytime + ", getUserid()=" + getUserid() + ", getExpid()=" + getExpid()
				+ ", getOrganisation()=" + getOrganisation() + ", getLocation()=" + getLocation() + ", getOrgempid()="
				+ getOrgempid() + ", getOrgdept()=" + getOrgdept() + ", getOrgrole()=" + getOrgrole()
				+ ", getJoiningdate()=" + getJoiningdate() + ", getRelievingdate()=" + getRelievingdate()
				+ ", getCtc()=" + getCtc() + ", getAdditionalinformation()=" + getAdditionalinformation()
				+ ", getOfferletter()=" + getOfferletter() + ", getEntitycreflg()=" + getEntitycreflg()
				+ ", getDelflg()=" + getDelflg() + ", getRcreuserid()=" + getRcreuserid() + ", getRcretime()="
				+ getRcretime() + ", getRmoduserid()=" + getRmoduserid() + ", getRmodtime()=" + getRmodtime()
				+ ", getRvfyuserid()=" + getRvfyuserid() + ", getRvfytime()=" + getRvfytime() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	public EmployeeProfessionalDetails(String userid, String expid, String organisation, String location,
			String orgempid, String orgdept, String orgrole, Date joiningdate, Date relievingdate, String ctc,
			String additionalinformation, String offerletter, String entitycreflg, String delflg, String rcreuserid,
			Date rcretime, String rmoduserid, Date rmodtime, String rvfyuserid, Date rvfytime) {
		super();
		this.userid = userid;
		this.expid = expid;
		this.organisation = organisation;
		this.location = location;
		this.orgempid = orgempid;
		this.orgdept = orgdept;
		this.orgrole = orgrole;
		this.joiningdate = joiningdate;
		this.relievingdate = relievingdate;
		this.ctc = ctc;
		this.additionalinformation = additionalinformation;
		this.offerletter = offerletter;
		this.entitycreflg = entitycreflg;
		this.delflg = delflg;
		this.rcreuserid = rcreuserid;
		this.rcretime = rcretime;
		this.rmoduserid = rmoduserid;
		this.rmodtime = rmodtime;
		this.rvfyuserid = rvfyuserid;
		this.rvfytime = rvfytime;
	}

	public EmployeeProfessionalDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

}