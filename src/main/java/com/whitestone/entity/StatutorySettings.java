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
@Table(name = "statutory_settings")
public class StatutorySettings {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "PF_RATE", nullable = false)
	private Double pfRate;

	@Column(name = "PF_MAX_LIMIT", nullable = false)
	private Double pfMaxLimit;

	@Column(name = "PF_ENABLED", nullable = false)
	private Boolean pfEnabled;

	@Column(name = "ESI_RATE", nullable = false)
	private Double esiRate;

	@Column(name = "ESI_MAX_LIMIT", nullable = false)
	private Double esiMaxLimit;

	@Column(name = "ESI_ENABLED", nullable = false)
	private Boolean esiEnabled;

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

	public Double getPfRate() {
		return pfRate;
	}

	public void setPfRate(Double pfRate) {
		this.pfRate = pfRate;
	}

	public Double getPfMaxLimit() {
		return pfMaxLimit;
	}

	public void setPfMaxLimit(Double pfMaxLimit) {
		this.pfMaxLimit = pfMaxLimit;
	}

	public Boolean getPfEnabled() {
		return pfEnabled;
	}

	public void setPfEnabled(Boolean pfEnabled) {
		this.pfEnabled = pfEnabled;
	}

	public Double getEsiRate() {
		return esiRate;
	}

	public void setEsiRate(Double esiRate) {
		this.esiRate = esiRate;
	}

	public Double getEsiMaxLimit() {
		return esiMaxLimit;
	}

	public void setEsiMaxLimit(Double esiMaxLimit) {
		this.esiMaxLimit = esiMaxLimit;
	}

	public Boolean getEsiEnabled() {
		return esiEnabled;
	}

	public void setEsiEnabled(Boolean esiEnabled) {
		this.esiEnabled = esiEnabled;
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
		return "StatutorySettings [id=" + id + ", pfRate=" + pfRate + ", pfMaxLimit=" + pfMaxLimit + ", pfEnabled="
				+ pfEnabled + ", esiRate=" + esiRate + ", esiMaxLimit=" + esiMaxLimit + ", esiEnabled=" + esiEnabled
				+ ", entityCreFlg=" + entityCreFlg + ", delFlg=" + delFlg + ", rCreUserId=" + rCreUserId + ", rCreTime="
				+ rCreTime + ", rModUserId=" + rModUserId + ", rModTime=" + rModTime + ", rVfyUserId=" + rVfyUserId
				+ ", rVfyTime=" + rVfyTime + ", getId()=" + getId() + ", getPfRate()=" + getPfRate()
				+ ", getPfMaxLimit()=" + getPfMaxLimit() + ", getPfEnabled()=" + getPfEnabled() + ", getEsiRate()="
				+ getEsiRate() + ", getEsiMaxLimit()=" + getEsiMaxLimit() + ", getEsiEnabled()=" + getEsiEnabled()
				+ ", getEntityCreFlg()=" + getEntityCreFlg() + ", getDelFlg()=" + getDelFlg() + ", getrCreUserId()="
				+ getrCreUserId() + ", getrCreTime()=" + getrCreTime() + ", getrModUserId()=" + getrModUserId()
				+ ", getrModTime()=" + getrModTime() + ", getrVfyUserId()=" + getrVfyUserId() + ", getrVfyTime()="
				+ getrVfyTime() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

	public StatutorySettings(Long id, Double pfRate, Double pfMaxLimit, Boolean pfEnabled, Double esiRate,
			Double esiMaxLimit, Boolean esiEnabled, String entityCreFlg, String delFlg, String rCreUserId,
			Date rCreTime, String rModUserId, Date rModTime, String rVfyUserId, Date rVfyTime) {
		super();
		this.id = id;
		this.pfRate = pfRate;
		this.pfMaxLimit = pfMaxLimit;
		this.pfEnabled = pfEnabled;
		this.esiRate = esiRate;
		this.esiMaxLimit = esiMaxLimit;
		this.esiEnabled = esiEnabled;
		this.entityCreFlg = entityCreFlg;
		this.delFlg = delFlg;
		this.rCreUserId = rCreUserId;
		this.rCreTime = rCreTime;
		this.rModUserId = rModUserId;
		this.rModTime = rModTime;
		this.rVfyUserId = rVfyUserId;
		this.rVfyTime = rVfyTime;
	}

	public StatutorySettings() {
		super();
		// TODO Auto-generated constructor stub
	}

    

    // Getters and Setters
}