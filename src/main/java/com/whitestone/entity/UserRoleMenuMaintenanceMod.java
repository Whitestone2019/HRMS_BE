package com.whitestone.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(name = "USER_ROLE_MENU_MAINTENANCE_MOD_TBL", schema = "HRMSUSER")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRoleMenuMaintenanceMod {
    @Id
    
    @Column(name = "MAPPING_ID", nullable = false, length = 20)
    private String mappingid;

    @Column(name = "ROLE_ID", nullable = false, length = 20)
    private String roleid;

    @Column(name = "MENU_ID", nullable = false, length = 50)
    private String menuid;

    @Column(name = "PERMISSIONS", length = 50)
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

	public String getMappingid() {
		return mappingid;
	}

	public void setMappingid(String mappingid) {
		this.mappingid = mappingid;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getMenuid() {
		return menuid;
	}

	public void setMenuid(String menuid) {
		this.menuid = menuid;
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
		return "UserRoleMenuMaintenanceMod [mappingid=" + mappingid + ", roleid=" + roleid + ", menuid=" + menuid
				+ ", permissions=" + permissions + ", status=" + status + ", entitycreflg=" + entitycreflg + ", delflg="
				+ delflg + ", rcreuserid=" + rcreuserid + ", rcretime=" + rcretime + ", rmoduserid=" + rmoduserid
				+ ", rmodtime=" + rmodtime + ", rvfyuserid=" + rvfyuserid + ", rvfytime=" + rvfytime
				+ ", getMappingid()=" + getMappingid() + ", getRoleid()=" + getRoleid() + ", getMenuid()=" + getMenuid()
				+ ", getPermissions()=" + getPermissions() + ", getStatus()=" + getStatus() + ", getEntitycreflg()="
				+ getEntitycreflg() + ", getDelflg()=" + getDelflg() + ", getRcreuserid()=" + getRcreuserid()
				+ ", getRcretime()=" + getRcretime() + ", getRmoduserid()=" + getRmoduserid() + ", getRmodtime()="
				+ getRmodtime() + ", getRvfyuserid()=" + getRvfyuserid() + ", getRvfytime()=" + getRvfytime()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	public UserRoleMenuMaintenanceMod(String mappingid, String roleid, String menuid, String permissions, String status,
			String entitycreflg, String delflg, String rcreuserid, Date rcretime, String rmoduserid, Date rmodtime,
			String rvfyuserid, Date rvfytime) {
		super();
		this.mappingid = mappingid;
		this.roleid = roleid;
		this.menuid = menuid;
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

	public UserRoleMenuMaintenanceMod() {
		super();
		// TODO Auto-generated constructor stub
	}
        
}