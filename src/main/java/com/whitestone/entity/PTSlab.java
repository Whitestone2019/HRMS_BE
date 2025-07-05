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
@Table(name = "pt_slabs")
public class PTSlab {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "INCOME_MIN", nullable = false)
	private Double incomeMin;

	@Column(name = "INCOME_MAX", nullable = false)
	private Double incomeMax;

	@Column(name = "TAX_AMOUNT", nullable = false)
	private Double taxAmount;

		@Column(name = "ENTITY_CRE_FLG", length = 1)
		private String entityCreFlg = "";
	
		@Column(name = "DEL_FLG", length = 1)
		private String delFlg = "";
	
		@Column(name = "RCRE_USER_ID", nullable = false, length = 15)
		private String rCreUserId = "";
	
		@Column(name = "RCRE_TIME")
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date rCreTime;
	
		@Column(name = "RMOD_USER_ID", length = 15)
		private String rModUserId = "";
	
		@Column(name = "RMOD_TIME")
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date rModTime;
	
		@Column(name = "RVFY_USER_ID", length = 15)
		private String rVfyUserId = "";
	
		@Column(name = "RVFY_TIME")
		@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date rVfyTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getIncomeMin() {
		return incomeMin;
	}

	public void setIncomeMin(Double incomeMin) {
		this.incomeMin = incomeMin;
	}

	public Double getIncomeMax() {
		return incomeMax;
	}

	public void setIncomeMax(Double incomeMax) {
		this.incomeMax = incomeMax;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
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
		return "PTSlab [id=" + id + ", incomeMin=" + incomeMin + ", incomeMax=" + incomeMax + ", taxAmount=" + taxAmount
				+ ", entityCreFlg=" + entityCreFlg + ", delFlg=" + delFlg + ", rCreUserId=" + rCreUserId + ", rCreTime="
				+ rCreTime + ", rModUserId=" + rModUserId + ", rModTime=" + rModTime + ", rVfyUserId=" + rVfyUserId
				+ ", rVfyTime=" + rVfyTime + ", getId()=" + getId() + ", getIncomeMin()=" + getIncomeMin()
				+ ", getIncomeMax()=" + getIncomeMax() + ", getTaxAmount()=" + getTaxAmount() + ", getEntityCreFlg()="
				+ getEntityCreFlg() + ", getDelFlg()=" + getDelFlg() + ", getrCreUserId()=" + getrCreUserId()
				+ ", getrCreTime()=" + getrCreTime() + ", getrModUserId()=" + getrModUserId() + ", getrModTime()="
				+ getrModTime() + ", getrVfyUserId()=" + getrVfyUserId() + ", getrVfyTime()=" + getrVfyTime()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	public PTSlab(Long id, Double incomeMin, Double incomeMax, Double taxAmount, String entityCreFlg, String delFlg,
			String rCreUserId, Date rCreTime, String rModUserId, Date rModTime, String rVfyUserId, Date rVfyTime) {
		super();
		this.id = id;
		this.incomeMin = incomeMin;
		this.incomeMax = incomeMax;
		this.taxAmount = taxAmount;
		this.entityCreFlg = entityCreFlg;
		this.delFlg = delFlg;
		this.rCreUserId = rCreUserId;
		this.rCreTime = rCreTime;
		this.rModUserId = rModUserId;
		this.rModTime = rModTime;
		this.rVfyUserId = rVfyUserId;
		this.rVfyTime = rVfyTime;
	}

	public PTSlab() {
		super();
		// TODO Auto-generated constructor stub
	}


    // Getters and Setters
}