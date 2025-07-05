package com.whitestone.entity;

import java.sql.Timestamp; // Correct import
import java.time.Instant;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "EMPLOYEE_SKILL_MOD_TABLE", schema = "HRMSUSER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeSkillMod {
    @Id
    @Column(name = "SRL_NUM", nullable = false)
    private Long srlnum;
    
    @Column(name = "USER_ID", nullable = false, length = 15)
    private Long userid;

    @Column(name = "SKILL", length = 30)
    private String skill;

    @Column(name = "PROFICIENCY_LEVEL", length = 15)
    private String proficiencylevel;

    @Column(name = "CERTIFICATION", length = 100)
    private String certification;

    @Column(name = "YEARS_OF_EXP", length = 15)
    private String yearsofexp;

    @Column(name = "LAST_UPDATED", nullable = false, length = 15)
    private String lastupdated;

    @Column(name = "DEL_FLG", length = 1)
    private String delflg;

    @Column(name = "RCRE_USER_ID", nullable = false, length = 15)
    private String rcreuserid;

    @Column(name = "RCRE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rcretime;

    @Column(name = "RMOD_USER_ID", length = 15)
    private String rmoduserid;

    @Column(name = "RMOD_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rmodtime;

    @Column(name = "RVFY_USER_ID", length = 15)
    private String rvfyuserid;

    @Column(name = "RVFY_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rvfytime;

    public Long getSrlnum() {
        return srlnum;
    }

    public void setSrlnum(Long srlnum) {
        this.srlnum = srlnum;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
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

    public void setLastupdated(String currentDateTime) {
        this.lastupdated = currentDateTime;
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
        return "EmployeeSkillMod [srlnum=" + srlnum + ", userid=" + userid + ", skill=" + skill + ", proficiencylevel="
                + proficiencylevel + ", certification=" + certification + ", yearsofexp=" + yearsofexp
                + ", lastupdated=" + lastupdated + ", delflg=" + delflg + ", rcreuserid=" + rcreuserid + ", rcretime="
                + rcretime + ", rmoduserid=" + rmoduserid + ", rmodtime=" + rmodtime + ", rvfyuserid=" + rvfyuserid
                + ", rvfytime=" + rvfytime + "]";
    }

    public EmployeeSkillMod(Long srlnum, Long userid, String skill, String proficiencylevel, String certification,
                            String yearsofexp, String lastupdated, String delflg, String rcreuserid, Date rcretime, 
                            String rmoduserid, Date rmodtime, String rvfyuserid, Date rvfytime) {
        this.srlnum = srlnum;
        this.userid = userid;
        this.skill = skill;
        this.proficiencylevel = proficiencylevel;
        this.certification = certification;
        this.yearsofexp = yearsofexp;
        this.lastupdated = lastupdated;
        this.delflg = delflg;
        this.rcreuserid = rcreuserid;
        this.rcretime = rcretime;
        this.rmoduserid = rmoduserid;
        this.rmodtime = rmodtime;
        this.rvfyuserid = rvfyuserid;
        this.rvfytime = rvfytime;
    }

    public EmployeeSkillMod() {
        super();
    }



}
