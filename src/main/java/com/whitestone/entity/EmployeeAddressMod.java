package com.whitestone.entity;
import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "EMPLOYEE_ADDRESS_MOD_TBL", schema = "HRMSUSER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeAddressMod {
    @Id
    
    @Column(name = "USER_ID", nullable = false, length = 15)
    private Long userid;

    @Column(name = "PRESENT_ADDRESS_LINE1", nullable = false, length = 200)
    private String presentaddressline1;

    @Column(name = "PRESENT_ADDRESS_LINE2", length = 200)
    private String presentaddressline2;

    @Column(name = "PRESENT_CITY",nullable = false, length = 200)
    private String presentcity;

    @Column(name = "PRESENT_STATE",nullable = false, length = 200)
    private String presentstate;

    @Column(name = "PRESENT_COUNTRY",nullable = false, length = 100)
    private String presentcountry;

    @Column(name = "PRESENT_POSTAL_CODE",nullable = false, length = 100)
    private String presentpostalcode;

    @Column(name = "PERMANENT_ADDRESS_LINE1", nullable = false, length = 200)
    private String permanentaddressline1;

    @Column(name = "PERMANENT_ADDRESS_LINE2", length = 200)
    private String permanentaddressline2;

    @Column(name = "PERMANENT_CITY",nullable = false, length = 200)
    private String permanentcity;

    @Column(name = "PERMANENT_STATE",nullable = false, length = 200)
    private String permanentstate;

    @Column(name = "PERMANENT_COUNTRY",nullable = false, length = 100)
    private String permanentcountry;

    @Column(name = "PERMANENT_POSTAL_CODE",nullable = false, length = 100)
    private String permanentpostalcode;

    @Column(name = "DEL_FLG", length = 1)
    private String delflg;

    @Column(name = "RCRE_USER_ID", nullable = false, length = 15)
    private String rcreuserid;

    @Column(name = "RCRE_TIME" ,nullable = false)
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

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getPresentaddressline1() {
		return presentaddressline1;
	}

	public void setPresentaddressline1(String presentaddressline1) {
		this.presentaddressline1 = presentaddressline1;
	}

	public String getPresentaddressline2() {
		return presentaddressline2;
	}

	public void setPresentaddressline2(String presentaddressline2) {
		this.presentaddressline2 = presentaddressline2;
	}

	public String getPresentcity() {
		return presentcity;
	}

	public void setPresentcity(String presentcity) {
		this.presentcity = presentcity;
	}

	public String getPresentstate() {
		return presentstate;
	}

	public void setPresentstate(String presentstate) {
		this.presentstate = presentstate;
	}

	public String getPresentcountry() {
		return presentcountry;
	}

	public void setPresentcountry(String presentcountry) {
		this.presentcountry = presentcountry;
	}

	public String getPresentpostalcode() {
		return presentpostalcode;
	}

	public void setPresentpostalcode(String presentpostalcode) {
		this.presentpostalcode = presentpostalcode;
	}

	public String getPermanentaddressline1() {
		return permanentaddressline1;
	}

	public void setPermanentaddressline1(String permanentaddressline1) {
		this.permanentaddressline1 = permanentaddressline1;
	}

	public String getPermanentaddressline2() {
		return permanentaddressline2;
	}

	public void setPermanentaddressline2(String permanentaddressline2) {
		this.permanentaddressline2 = permanentaddressline2;
	}

	public String getPermanentcity() {
		return permanentcity;
	}

	public void setPermanentcity(String permanentcity) {
		this.permanentcity = permanentcity;
	}

	public String getPermanentstate() {
		return permanentstate;
	}

	public void setPermanentstate(String permanentstate) {
		this.permanentstate = permanentstate;
	}

	public String getPermanentcountry() {
		return permanentcountry;
	}

	public void setPermanentcountry(String permanentcountry) {
		this.permanentcountry = permanentcountry;
	}

	public String getPermanentpostalcode() {
		return permanentpostalcode;
	}

	public void setPermanentpostalcode(String permanentpostalcode) {
		this.permanentpostalcode = permanentpostalcode;
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
		return "EmployeeAddressMod [userid=" + userid + ", presentaddressline1=" + presentaddressline1
				+ ", presentaddressline2=" + presentaddressline2 + ", presentcity=" + presentcity + ", presentstate="
				+ presentstate + ", presentcountry=" + presentcountry + ", presentpostalcode=" + presentpostalcode
				+ ", permanentaddressline1=" + permanentaddressline1 + ", permanentaddressline2="
				+ permanentaddressline2 + ", permanentcity=" + permanentcity + ", permanentstate=" + permanentstate
				+ ", permanentcountry=" + permanentcountry + ", permanentpostalcode=" + permanentpostalcode
				+ ", delflg=" + delflg + ", rcreuserid=" + rcreuserid + ", rcretime=" + rcretime + ", rmoduserid="
				+ rmoduserid + ", rmodtime=" + rmodtime + ", rvfyuserid=" + rvfyuserid + ", rvfytime=" + rvfytime
				+ ", getUserid()=" + getUserid() + ", getPresentaddressline1()=" + getPresentaddressline1()
				+ ", getPresentaddressline2()=" + getPresentaddressline2() + ", getPresentcity()=" + getPresentcity()
				+ ", getPresentstate()=" + getPresentstate() + ", getPresentcountry()=" + getPresentcountry()
				+ ", getPresentpostalcode()=" + getPresentpostalcode() + ", getPermanentaddressline1()="
				+ getPermanentaddressline1() + ", getPermanentaddressline2()=" + getPermanentaddressline2()
				+ ", getPermanentcity()=" + getPermanentcity() + ", getPermanentstate()=" + getPermanentstate()
				+ ", getPermanentcountry()=" + getPermanentcountry() + ", getPermanentpostalcode()="
				+ getPermanentpostalcode() + ", getDelflg()=" + getDelflg() + ", getRcreuserid()=" + getRcreuserid()
				+ ", getRcretime()=" + getRcretime() + ", getRmoduserid()=" + getRmoduserid() + ", getRmodtime()="
				+ getRmodtime() + ", getRvfyuserid()=" + getRvfyuserid() + ", getRvfytime()=" + getRvfytime()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	public EmployeeAddressMod(Long userid, String presentaddressline1, String presentaddressline2, String presentcity,
			String presentstate, String presentcountry, String presentpostalcode, String permanentaddressline1,
			String permanentaddressline2, String permanentcity, String permanentstate, String permanentcountry,
			String permanentpostalcode, String delflg, String rcreuserid, Date rcretime, String rmoduserid,
			Date rmodtime, String rvfyuserid, Date rvfytime) {
		super();
		this.userid = userid;
		this.presentaddressline1 = presentaddressline1;
		this.presentaddressline2 = presentaddressline2;
		this.presentcity = presentcity;
		this.presentstate = presentstate;
		this.presentcountry = presentcountry;
		this.presentpostalcode = presentpostalcode;
		this.permanentaddressline1 = permanentaddressline1;
		this.permanentaddressline2 = permanentaddressline2;
		this.permanentcity = permanentcity;
		this.permanentstate = permanentstate;
		this.permanentcountry = permanentcountry;
		this.permanentpostalcode = permanentpostalcode;
		this.delflg = delflg;
		this.rcreuserid = rcreuserid;
		this.rcretime = rcretime;
		this.rmoduserid = rmoduserid;
		this.rmodtime = rmodtime;
		this.rvfyuserid = rvfyuserid;
		this.rvfytime = rvfytime;
	}

	public EmployeeAddressMod() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static EmployeeAddressMod get() {
		// TODO Auto-generated method stub
		return null;
	}

    
}
