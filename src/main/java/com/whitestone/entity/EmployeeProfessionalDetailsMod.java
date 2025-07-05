package com.whitestone.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "EMPLOYEE_PROFESSIONAL_DETAILS_MOD_TBL", schema = "HRMSUSER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeProfessionalDetailsMod {
    @Id
    
    @Column(name = "SRL_NUM",nullable = false)
    private Long srlnum;
    
    @Column(name = "USER_ID",nullable = false, length = 15)
    private String userid;

    @Column(name = "ORGANISATION", nullable = false, length = 100)
    private String organisation;

    @Column(name = "LOCATION", nullable = false, length = 100)
    private String location;

    @Column(name = "ORG_EMP_ID", nullable = false, length = 15)
    private String orgempid;

    @Column(name = "ORG_DEPT", nullable = false, length = 100)
    private String orgdept;

    @Column(name = "ORG_ROLE", nullable = false, length = 100)
    private String orgrole;

    @Column(name = "JOINING_DATE", nullable = false)
    @JsonFormat(pattern = "YYYY-MM-DD")
    private Date joiningdate;

    @Column(name = "RELIEVING_DATE", nullable = false)
    @JsonFormat(pattern = "YYYY-MM-DD")
    private Date relievingdate;

    @Column(name = "CTC", nullable = false, length = 20)
    private String ctc;

    @Column(name = "ADDITIONAL_INFORMATION", length = 100)
    private String additionalinformation;

    @Column(name = "OFFER_LETTER", nullable = false, length = 100)
    private String offerletter;

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
		return "EmployeeProfessionalDetailsMod [srlnum=" + srlnum + ", userid=" + userid + ", organisation="
				+ organisation + ", location=" + location + ", orgempid=" + orgempid + ", orgdept=" + orgdept
				+ ", orgrole=" + orgrole + ", joiningdate=" + joiningdate + ", relievingdate=" + relievingdate
				+ ", ctc=" + ctc + ", additionalinformation=" + additionalinformation + ", offerletter=" + offerletter
				+ ", delflg=" + delflg + ", rcreuserid=" + rcreuserid + ", rcretime=" + rcretime + ", rmoduserid="
				+ rmoduserid + ", rmodtime=" + rmodtime + ", rvfyuserid=" + rvfyuserid + ", rvfytime=" + rvfytime
				+ ", getSrlnum()=" + getSrlnum() + ", getUserid()=" + getUserid() + ", getOrganisation()="
				+ getOrganisation() + ", getLocation()=" + getLocation() + ", getOrgempid()=" + getOrgempid()
				+ ", getOrgdept()=" + getOrgdept() + ", getOrgrole()=" + getOrgrole() + ", getJoiningdate()="
				+ getJoiningdate() + ", getRelievingdate()=" + getRelievingdate() + ", getCtc()=" + getCtc()
				+ ", getAdditionalinformation()=" + getAdditionalinformation() + ", getOfferletter()="
				+ getOfferletter() + ", getDelflg()=" + getDelflg() + ", getRcreuserid()=" + getRcreuserid()
				+ ", getRcretime()=" + getRcretime() + ", getRmoduserid()=" + getRmoduserid() + ", getRmodtime()="
				+ getRmodtime() + ", getRvfyuserid()=" + getRvfyuserid() + ", getRvfytime()=" + getRvfytime()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	public EmployeeProfessionalDetailsMod(Long srlnum, String userid, String organisation, String location,
			String orgempid, String orgdept, String orgrole, Date joiningdate, Date relievingdate, String ctc,
			String additionalinformation, String offerletter, String delflg, String rcreuserid, Date rcretime,
			String rmoduserid, Date rmodtime, String rvfyuserid, Date rvfytime) {
		super();
		this.srlnum = srlnum;
		this.userid = userid;
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
		this.delflg = delflg;
		this.rcreuserid = rcreuserid;
		this.rcretime = rcretime;
		this.rmoduserid = rmoduserid;
		this.rmodtime = rmodtime;
		this.rvfyuserid = rvfyuserid;
		this.rvfytime = rvfytime;
	}

	public EmployeeProfessionalDetailsMod() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setUserid(Long userid) {
		// TODO Auto-generated method stub
		
	}

	public void setRcreuserid(Long userid) {
		// TODO Auto-generated method stub
		
	}
    
}