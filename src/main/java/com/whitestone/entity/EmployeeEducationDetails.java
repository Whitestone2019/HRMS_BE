package com.whitestone.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "EMPLOYEE_EDUCATION_DETAILS_TBL", schema = "HRMSUSER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeEducationDetails {
    @Id
    
    @Column(name = "USER_ID", nullable = false, length = 15)
    private String userid;

    @Column(name = "REG_NUM", length = 80)
    private String regnum;

    @Column(name = "INSTITUTION", length = 50)
    private String institution;

    @Column(name = "QUALIFICATION", length = 50)
    private String qualification;

    @Column(name = "FIELD_OF_STUDY", length = 50)
    private String fieldofstudy;

    @Column(name = "PERCENTAGE", length = 50)
    private String percentage;

    @Column(name = "YEAR_OF_GRADUATION", length = 6)
    private String yearofgraduation;

    @Column(name = "ADDITIONAL_NOTES", length = 100)
    private String additionalnotes;

    @Column(name = "DURATION")
    private Long duration;

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

	public String getRegnum() {
		return regnum;
	}

	public void setRegnum(String regnum) {
		this.regnum = regnum;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getFieldofstudy() {
		return fieldofstudy;
	}

	public void setFieldofstudy(String fieldofstudy) {
		this.fieldofstudy = fieldofstudy;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public String getYearofgraduation() {
		return yearofgraduation;
	}

	public void setYearofgraduation(String yearofgraduation) {
		this.yearofgraduation = yearofgraduation;
	}

	public String getAdditionalnotes() {
		return additionalnotes;
	}

	public void setAdditionalnotes(String additionalnotes) {
		this.additionalnotes = additionalnotes;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
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
		return "EmployeeEducationDetails [userid=" + userid + ", regnum=" + regnum + ", institution=" + institution
				+ ", qualification=" + qualification + ", fieldofstudy=" + fieldofstudy + ", percentage=" + percentage
				+ ", yearofgraduation=" + yearofgraduation + ", additionalnotes=" + additionalnotes + ", duration="
				+ duration + ", entitycreflg=" + entitycreflg + ", delflg=" + delflg + ", rcreuserid=" + rcreuserid
				+ ", rcretime=" + rcretime + ", rmoduserid=" + rmoduserid + ", rmodtime=" + rmodtime + ", rvfyuserid="
				+ rvfyuserid + ", rvfytime=" + rvfytime + ", getUserid()=" + getUserid() + ", getRegnum()="
				+ getRegnum() + ", getInstitution()=" + getInstitution() + ", getQualification()=" + getQualification()
				+ ", getFieldofstudy()=" + getFieldofstudy() + ", getPercentage()=" + getPercentage()
				+ ", getYearofgraduation()=" + getYearofgraduation() + ", getAdditionalnotes()=" + getAdditionalnotes()
				+ ", getDuration()=" + getDuration() + ", getEntitycreflg()=" + getEntitycreflg() + ", getDelflg()="
				+ getDelflg() + ", getRcreuserid()=" + getRcreuserid() + ", getRcretime()=" + getRcretime()
				+ ", getRmoduserid()=" + getRmoduserid() + ", getRmodtime()=" + getRmodtime() + ", getRvfyuserid()="
				+ getRvfyuserid() + ", getRvfytime()=" + getRvfytime() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}

	public EmployeeEducationDetails(String userid, String regnum, String institution, String qualification,
			String fieldofstudy, String percentage, String yearofgraduation, String additionalnotes, Long duration,
			String entitycreflg, String delflg, String rcreuserid, Date rcretime, String rmoduserid, Date rmodtime,
			String rvfyuserid, Date rvfytime) {
		super();
		this.userid = userid;
		this.regnum = regnum;
		this.institution = institution;
		this.qualification = qualification;
		this.fieldofstudy = fieldofstudy;
		this.percentage = percentage;
		this.yearofgraduation = yearofgraduation;
		this.additionalnotes = additionalnotes;
		this.duration = duration;
		this.entitycreflg = entitycreflg;
		this.delflg = delflg;
		this.rcreuserid = rcreuserid;
		this.rcretime = rcretime;
		this.rmoduserid = rmoduserid;
		this.rmodtime = rmodtime;
		this.rvfyuserid = rvfyuserid;
		this.rvfytime = rvfytime;
	}

	public EmployeeEducationDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

}
