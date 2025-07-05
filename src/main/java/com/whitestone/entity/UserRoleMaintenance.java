package com.whitestone.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "USER_ROLE_MAINTENANCE_TBL", schema = "HRMSUSER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRoleMaintenance {
    @Id
    
    @Column(name = "ROLE_ID", nullable = false, length = 10)
    private String roleid;

    @Column(name = "ROLE_NAME", nullable = false, length = 50)
    private String rolename;

    @Column(name = "DESCRIPTION", length = 50)
    private String description;

    @Column(name = "PERMISSIONS", length = 20)
    private String permissions;

    @Column(name = "STATUS", length = 20)
    private String status;

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

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPermissions() {
		return permissions;
	}

	public void setPermissions(String permissions) {
		this.permissions = permissions;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		return "UserRoleMaintenance [roleid=" + roleid + ", rolename=" + rolename + ", description=" + description
				+ ", permissions=" + permissions + ", status=" + status + ", entitycreflg=" + entitycreflg + ", delflg="
				+ delflg + ", rcreuserid=" + rcreuserid + ", rmoduserid=" + rmoduserid + ", rvfyuserid=" + rvfyuserid
				+ ", getRoleid()=" + getRoleid() + ", getRolename()=" + getRolename() + ", getDescription()="
				+ getDescription() + ", getPermissions()=" + getPermissions() + ", getStatus()=" + getStatus()
				+ ", getEntitycreflg()=" + getEntitycreflg() + ", getDelflg()=" + getDelflg() + ", getRcreuserid()="
				+ getRcreuserid() + ", getRmoduserid()=" + getRmoduserid() + ", getRvfyuserid()=" + getRvfyuserid()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	public UserRoleMaintenance(String roleid, String rolename, String description, String permissions, String status,
			String entitycreflg, String delflg, String rcreuserid, Date rcretime, String rmoduserid, Date rmodtime,
			String rvfyuserid, Date rvfytime) {
		super();
		this.roleid = roleid;
		this.rolename = rolename;
		this.description = description;
		this.permissions = permissions;
		this.status = status;
		this.entitycreflg = entitycreflg;
		this.delflg = delflg;
		this.rcreuserid = rcreuserid;
		this.rcretime = rcretime;
		this.rmoduserid = rmoduserid;
		this.rmodtime = rmodtime;
		this.rvfyuserid = rvfyuserid;
		this.rvfytime = rvfytime;
	}

	public UserRoleMaintenance() {
		super();
		// TODO Auto-generated constructor stub
	}
        
}