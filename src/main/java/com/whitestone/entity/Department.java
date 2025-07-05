package com.whitestone.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "departments")
public class Department {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", length = 100) // Adding column name
    private String name;

    @Column(name = "CODE", length = 50) // Adding column name
    private String code;

    @Column(name = "DESCRIPTION", length = 255) // Adding column name
    private String description;

    @Column(name = "STATUS", length = 20) // Adding column name
    private String status;

    @Column(name = "EMPLOYEE_COUNT") // Adding column name
    private int employeeCount;

    @Column(name = "ENTITY_CRE_FLG", length = 1)
    private String entityCreFlg;

    @Column(name = "DEL_FLG", length = 1)
    private String delFlg;

    @Column(name = "RCRE_USER_ID", nullable = false, length = 15)
    private String rCreUserId;

    @Column(name = "RCRE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rCreTime;

    @Column(name = "RMOD_USER_ID", length = 15)
    private String rModUserId;

    @Column(name = "RMOD_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rModTime;

    @Column(name = "RVFY_USER_ID", length = 15)
    private String rVfyUserId;

    @Column(name = "RVFY_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date rVfyTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getEmployeeCount() {
		return employeeCount;
	}

	public void setEmployeeCount(int employeeCount) {
		this.employeeCount = employeeCount;
	}

	public String getEntityCreFlg() {
		return entityCreFlg;
	}

	public void setEntityCreFlg(String entityCreFlg) {
		this.entityCreFlg = entityCreFlg;
	}

	public String getDelFlg() {
		return delFlg;
	}

	public void setDelFlg(String delFlg) {
		this.delFlg = delFlg;
	}

	public String getrCreUserId() {
		return rCreUserId;
	}

	public void setrCreUserId(String rCreUserId) {
		this.rCreUserId = rCreUserId;
	}

	public Date getrCreTime() {
		return rCreTime;
	}

	public void setrCreTime(Date rCreTime) {
		this.rCreTime = rCreTime;
	}

	public String getrModUserId() {
		return rModUserId;
	}

	public void setrModUserId(String rModUserId) {
		this.rModUserId = rModUserId;
	}

	public Date getrModTime() {
		return rModTime;
	}

	public void setrModTime(Date rModTime) {
		this.rModTime = rModTime;
	}

	public String getrVfyUserId() {
		return rVfyUserId;
	}

	public void setrVfyUserId(String rVfyUserId) {
		this.rVfyUserId = rVfyUserId;
	}

	public Date getrVfyTime() {
		return rVfyTime;
	}

	public void setrVfyTime(Date rVfyTime) {
		this.rVfyTime = rVfyTime;
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + ", code=" + code + ", description=" + description
				+ ", status=" + status + ", employeeCount=" + employeeCount + ", entityCreFlg=" + entityCreFlg
				+ ", delFlg=" + delFlg + ", rCreUserId=" + rCreUserId + ", rCreTime=" + rCreTime + ", rModUserId="
				+ rModUserId + ", rModTime=" + rModTime + ", rVfyUserId=" + rVfyUserId + ", rVfyTime=" + rVfyTime
				+ ", getId()=" + getId() + ", getName()=" + getName() + ", getCode()=" + getCode()
				+ ", getDescription()=" + getDescription() + ", getStatus()=" + getStatus() + ", getEmployeeCount()="
				+ getEmployeeCount() + ", getEntityCreFlg()=" + getEntityCreFlg() + ", getDelFlg()=" + getDelFlg()
				+ ", getrCreUserId()=" + getrCreUserId() + ", getrCreTime()=" + getrCreTime() + ", getrModUserId()="
				+ getrModUserId() + ", getrModTime()=" + getrModTime() + ", getrVfyUserId()=" + getrVfyUserId()
				+ ", getrVfyTime()=" + getrVfyTime() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

	public Department(Long id, String name, String code, String description, String status, int employeeCount,
			String entityCreFlg, String delFlg, String rCreUserId, Date rCreTime, String rModUserId, Date rModTime,
			String rVfyUserId, Date rVfyTime) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.description = description;
		this.status = status;
		this.employeeCount = employeeCount;
		this.entityCreFlg = entityCreFlg;
		this.delFlg = delFlg;
		this.rCreUserId = rCreUserId;
		this.rCreTime = rCreTime;
		this.rModUserId = rModUserId;
		this.rModTime = rModTime;
		this.rVfyUserId = rVfyUserId;
		this.rVfyTime = rVfyTime;
	}

	public Department() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}