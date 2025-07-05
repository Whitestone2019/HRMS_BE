package com.whitestone.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "EMPLOYEE_SKILL_TABLE", schema = "HRMSUSER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeSkill {
    @Id
    
    @Column(name = "USER_ID", nullable = false, length = 15)
    private String userid;

    @Column(name = "SKILL_ID", nullable = false, length = 15)
    private String skillid;

    @Column(name = "SKILL", length = 30)
    private String skill;

    @Column(name = "PROFICIENCY_LEVEL", length = 15)
    private String proficiencylevel;

    @Column(name = "CERTIFICATION", length = 100)
    private String certification;

    @Column(name = "YEARS_OF_EXP", length = 15)
    private String yearsofexp;

    @Column(name = "LAST_UPDATED", length = 15)
    private String lastupdated;

    @Column(name = "COMMENTS", length = 100)
    private String comments;

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

	public String getSkillid() {
		return skillid;
	}

	public void setSkillid(String skillid) {
		this.skillid = skillid;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getProficiencylevel() {
		return proficiencylevel;
	}

	public void setProficiencylevel(String proficiencylevel) {
		this.proficiencylevel = proficiencylevel;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public String getYearsofexp() {
		return yearsofexp;
	}

	public void setYearsofexp(String yearsofexp) {
		this.yearsofexp = yearsofexp;
	}

	public String getLastupdated() {
		return lastupdated;
	}

	public void setLastupdated(String lastupdated) {
		this.lastupdated = lastupdated;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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
		return "EmployeeSkill [userid=" + userid + ", skillid=" + skillid + ", skill=" + skill + ", proficiencylevel="
				+ proficiencylevel + ", certification=" + certification + ", yearsofexp=" + yearsofexp
				+ ", lastupdated=" + lastupdated + ", comments=" + comments + ", entitycreflg=" + entitycreflg
				+ ", delflg=" + delflg + ", rcreuserid=" + rcreuserid + ", rcretime=" + rcretime + ", rmoduserid="
				+ rmoduserid + ", rmodtime=" + rmodtime + ", rvfyuserid=" + rvfyuserid + ", rvfytime=" + rvfytime
				+ ", getUserid()=" + getUserid() + ", getSkillid()=" + getSkillid() + ", getSkill()=" + getSkill()
				+ ", getProficiencylevel()=" + getProficiencylevel() + ", getCertification()=" + getCertification()
				+ ", getYearsofexp()=" + getYearsofexp() + ", getLastupdated()=" + getLastupdated() + ", getComments()="
				+ getComments() + ", getEntitycreflg()=" + getEntitycreflg() + ", getDelflg()=" + getDelflg()
				+ ", getRcreuserid()=" + getRcreuserid() + ", getRcretime()=" + getRcretime() + ", getRmoduserid()="
				+ getRmoduserid() + ", getRmodtime()=" + getRmodtime() + ", getRvfyuserid()=" + getRvfyuserid()
				+ ", getRvfytime()=" + getRvfytime() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	public EmployeeSkill(String userid, String skillid, String skill, String proficiencylevel, String certification,
			String yearsofexp, String lastupdated, String comments, String entitycreflg, String delflg,
			String rcreuserid, Date rcretime, String rmoduserid, Date rmodtime, String rvfyuserid, Date rvfytime) {
		super();
		this.userid = userid;
		this.skillid = skillid;
		this.skill = skill;
		this.proficiencylevel = proficiencylevel;
		this.certification = certification;
		this.yearsofexp = yearsofexp;
		this.lastupdated = lastupdated;
		this.comments = comments;
		this.entitycreflg = entitycreflg;
		this.delflg = delflg;
		this.rcreuserid = rcreuserid;
		this.rcretime = rcretime;
		this.rmoduserid = rmoduserid;
		this.rmodtime = rmodtime;
		this.rvfyuserid = rvfyuserid;
		this.rvfytime = rvfytime;
	}

	public EmployeeSkill() {
		super();
		// TODO Auto-generated constructor stub
	}
            
}