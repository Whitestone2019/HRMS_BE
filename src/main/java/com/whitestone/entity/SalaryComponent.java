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
@Table(name = "salary_components")
public class SalaryComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "calculation_type", nullable = false, length = 50)
    private String calculationType;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "taxable")
    private Boolean taxable;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "ENTITY_CRE_FLG", length = 1)
    private String entityCreFlg = "Y";

    @Column(name = "DEL_FLG", length = 1)
    private String delFlg = "N";

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCalculationType() {
		return calculationType;
	}

	public void setCalculationType(String calculationType) {
		this.calculationType = calculationType;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Boolean getTaxable() {
		return taxable;
	}

	public void setTaxable(Boolean taxable) {
		this.taxable = taxable;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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
		return "SalaryComponent [id=" + id + ", name=" + name + ", type=" + type + ", calculationType="
				+ calculationType + ", amount=" + amount + ", taxable=" + taxable + ", description=" + description
				+ ", active=" + active + ", entityCreFlg=" + entityCreFlg + ", delFlg=" + delFlg + ", rCreUserId="
				+ rCreUserId + ", rCreTime=" + rCreTime + ", rModUserId=" + rModUserId + ", rModTime=" + rModTime
				+ ", rVfyUserId=" + rVfyUserId + ", rVfyTime=" + rVfyTime + ", getId()=" + getId() + ", getName()="
				+ getName() + ", getType()=" + getType() + ", getCalculationType()=" + getCalculationType()
				+ ", getAmount()=" + getAmount() + ", getTaxable()=" + getTaxable() + ", getDescription()="
				+ getDescription() + ", getActive()=" + getActive() + ", getEntityCreFlg()=" + getEntityCreFlg()
				+ ", getDelFlg()=" + getDelFlg() + ", getrCreUserId()=" + getrCreUserId() + ", getrCreTime()="
				+ getrCreTime() + ", getrModUserId()=" + getrModUserId() + ", getrModTime()=" + getrModTime()
				+ ", getrVfyUserId()=" + getrVfyUserId() + ", getrVfyTime()=" + getrVfyTime() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	public SalaryComponent(Long id, String name, String type, String calculationType, Double amount, Boolean taxable,
			String description, Boolean active, String entityCreFlg, String delFlg, String rCreUserId, Date rCreTime,
			String rModUserId, Date rModTime, String rVfyUserId, Date rVfyTime) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.calculationType = calculationType;
		this.amount = amount;
		this.taxable = taxable;
		this.description = description;
		this.active = active;
		this.entityCreFlg = entityCreFlg;
		this.delFlg = delFlg;
		this.rCreUserId = rCreUserId;
		this.rCreTime = rCreTime;
		this.rModUserId = rModUserId;
		this.rModTime = rModTime;
		this.rVfyUserId = rVfyUserId;
		this.rVfyTime = rVfyTime;
	}

	public SalaryComponent() {
		super();
		// TODO Auto-generated constructor stub
	}

	
    
}
