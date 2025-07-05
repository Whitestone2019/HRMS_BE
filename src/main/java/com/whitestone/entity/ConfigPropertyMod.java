package com.whitestone.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "CONFIG_PROPERTY_MOD_TBL", schema = "HRMSUSER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigPropertyMod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "S_NO")
    private Long sno;

    @Column(name = "PROPERTY_TYPE", nullable = false, length = 2)
    private String propertytype;

    @Column(name = "PROPERTY_CODE", nullable = false, length = 5)
    private String propertycode;

    @Column(name = "PROPERTY_DESC", length = 100)
    private String propertydesc;

    @Column(name = "RCRE_TIME")
    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss") 
    private Date rcretime;

    @Column(name = "RCRE_USER_ID", nullable = false, length = 15)
    private String rcreuserid;

    @Column(name = "DEL_FLG", length = 1)
    private String delflg;

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

	public Long getSno() {
		return sno;
	}

	public void setSno(Long sno) {
		this.sno = sno;
	}

	public String getPropertytype() {
		return propertytype;
	}

	public void setPropertytype(String propertytype) {
		this.propertytype = propertytype;
	}

	public String getPropertycode() {
		return propertycode;
	}

	public void setPropertycode(String propertycode) {
		this.propertycode = propertycode;
	}

	public String getPropertydesc() {
		return propertydesc;
	}

	public void setPropertydesc(String propertydesc) {
		this.propertydesc = propertydesc;
	}

	public Date getRcretime() {
		return rcretime;
	}

	public void setRcretime(Date rcretime) {
		this.rcretime = rcretime;
	}

	public String getRcreuserid() {
		return rcreuserid;
	}

	public void setRcreuserid(String rcreuserid) {
		this.rcreuserid = rcreuserid;
	}

	public String getDelflg() {
		return delflg;
	}

	public void setDelflg(String delflg) {
		this.delflg = delflg;
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
		return "ConfigPropertyMod [sno=" + sno + ", propertytype=" + propertytype + ", propertycode=" + propertycode
				+ ", propertydesc=" + propertydesc + ", rcretime=" + rcretime + ", rcreuserid=" + rcreuserid
				+ ", delflg=" + delflg + ", rmoduserid=" + rmoduserid + ", rmodtime=" + rmodtime + ", rvfyuserid="
				+ rvfyuserid + ", rvfytime=" + rvfytime + ", getSno()=" + getSno() + ", getPropertytype()="
				+ getPropertytype() + ", getPropertycode()=" + getPropertycode() + ", getPropertydesc()="
				+ getPropertydesc() + ", getRcretime()=" + getRcretime() + ", getRcreuserid()=" + getRcreuserid()
				+ ", getDelflg()=" + getDelflg() + ", getRmoduserid()=" + getRmoduserid() + ", getRmodtime()="
				+ getRmodtime() + ", getRvfyuserid()=" + getRvfyuserid() + ", getRvfytime()=" + getRvfytime()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	public ConfigPropertyMod(Long sno, String propertytype, String propertycode, String propertydesc, Date rcretime,
			String rcreuserid, String delflg, String rmoduserid, Date rmodtime, String rvfyuserid, Date rvfytime) {
		super();
		this.sno = sno;
		this.propertytype = propertytype;
		this.propertycode = propertycode;
		this.propertydesc = propertydesc;
		this.rcretime = rcretime;
		this.rcreuserid = rcreuserid;
		this.delflg = delflg;
		this.rmoduserid = rmoduserid;
		this.rmodtime = rmodtime;
		this.rvfyuserid = rvfyuserid;
		this.rvfytime = rvfytime;
	}

	public ConfigPropertyMod() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}