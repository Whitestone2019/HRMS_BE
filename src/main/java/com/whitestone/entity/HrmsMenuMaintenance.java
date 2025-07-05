package com.whitestone.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "HRMS_MENU_MAINTENANCE_TBL", schema = "HRMSUSER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class HrmsMenuMaintenance {
    @Id
    
    @Column(name = "MENU_ID", nullable = false, length = 20)
    private String menuid;

    @Column(name = "MENU_NAME", nullable = false, length = 50)
    private String menuname;

    @Column(name = "DESCRIPTION", length = 50)
    private String description;

    @Column(name = "PARENT_MENU_ID", length = 50)
    private String parentmenuid;

    @Column(name = "URL_PATH", length = 100)
    private String urlpath;

    @Column(name = "ICON", length = 50)
    private String icon;

    @Column(name = "MENU_ORDER", length = 50)
    private String menuorder;

    @Column(name = "ENTITY_CRE_FLG", length = 1)
    private String entitycreflg;

    @Column(name = "DEL_FLG", length = 1)
    private String delflg;

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

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
	}

	public String getMenuname() {
		return menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParentmenuid() {
		return parentmenuid;
	}

	public void setParentmenuid(String parentmenuid) {
		this.parentmenuid = parentmenuid;
	}

	public String getUrlpath() {
		return urlpath;
	}

	public void setUrlpath(String urlpath) {
		this.urlpath = urlpath;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getMenuorder() {
		return menuorder;
	}

	public void setMenuorder(String menuorder) {
		this.menuorder = menuorder;
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
		return "HrmsMenuMaintenance [menuid=" + menuid + ", menuname=" + menuname + ", description=" + description
				+ ", parentmenuid=" + parentmenuid + ", urlpath=" + urlpath + ", icon=" + icon + ", menuorder="
				+ menuorder + ", entitycreflg=" + entitycreflg + ", delflg=" + delflg + ", rcreuserid=" + rcreuserid
				+ ", rcretime=" + rcretime + ", rmoduserid=" + rmoduserid + ", rmodtime=" + rmodtime + ", rvfyuserid="
				+ rvfyuserid + ", rvfytime=" + rvfytime + ", getMenuid()=" + getMenuid() + ", getMenuname()="
				+ getMenuname() + ", getDescription()=" + getDescription() + ", getParentmenuid()=" + getParentmenuid()
				+ ", getUrlpath()=" + getUrlpath() + ", getIcon()=" + getIcon() + ", getMenuorder()=" + getMenuorder()
				+ ", getEntitycreflg()=" + getEntitycreflg() + ", getDelflg()=" + getDelflg() + ", getRcreuserid()="
				+ getRcreuserid() + ", getRcretime()=" + getRcretime() + ", getRmoduserid()=" + getRmoduserid()
				+ ", getRmodtime()=" + getRmodtime() + ", getRvfyuserid()=" + getRvfyuserid() + ", getRvfytime()="
				+ getRvfytime() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

	public HrmsMenuMaintenance(String menuid, String menuname, String description, String parentmenuid, String urlpath,
			String icon, String menuorder, String entitycreflg, String delflg, String rcreuserid, Date rcretime,
			String rmoduserid, Date rmodtime, String rvfyuserid, Date rvfytime) {
		super();
		this.menuid = menuid;
		this.menuname = menuname;
		this.description = description;
		this.parentmenuid = parentmenuid;
		this.urlpath = urlpath;
		this.icon = icon;
		this.menuorder = menuorder;
		this.entitycreflg = entitycreflg;
		this.delflg = delflg;
		this.rcreuserid = rcreuserid;
		this.rcretime = rcretime;
		this.rmoduserid = rmoduserid;
		this.rmodtime = rmodtime;
		this.rvfyuserid = rvfyuserid;
		this.rvfytime = rvfytime;
	}

	public HrmsMenuMaintenance() {
		super();
		// TODO Auto-generated constructor stub
	}

}