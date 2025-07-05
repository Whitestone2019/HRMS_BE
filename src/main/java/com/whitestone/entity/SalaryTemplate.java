package com.whitestone.entity;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "salary_template")
public class SalaryTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long templateId;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "description")
    private String description;

    @Column(name = "annual_ctc")
    private Double annualCTC;

    @Column(name = "ENTITY_CRE_FLG", length = 1)

    private String entitycreflg;

    @Column(name = "DEL_FLG", length = 1)

    private String delflg;

    @Column(name = "RCRE_USER_ID", nullable = false, length = 15)

    private String rcreuserid;

    @Column(name = "RCRE_TIME")

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
    
    @OneToMany(mappedBy = "salaryTemplate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SalaryTemplateComponent> components;

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getAnnualCTC() {
		return annualCTC;
	}

	public void setAnnualCTC(Double annualCTC) {
		this.annualCTC = annualCTC;
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

	public List<SalaryTemplateComponent> getComponents() {
		return components;
	}

	public void setComponents(List<SalaryTemplateComponent> components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "SalaryTemplate [templateId=" + templateId + ", templateName=" + templateName + ", description="
				+ description + ", annualCTC=" + annualCTC + ", entitycreflg=" + entitycreflg + ", delflg=" + delflg
				+ ", rcreuserid=" + rcreuserid + ", rcretime=" + rcretime + ", rmoduserid=" + rmoduserid + ", rmodtime="
				+ rmodtime + ", rvfyuserid=" + rvfyuserid + ", rvfytime=" + rvfytime + ", components=" + components
				+ ", getTemplateId()=" + getTemplateId() + ", getTemplateName()=" + getTemplateName()
				+ ", getDescription()=" + getDescription() + ", getAnnualCTC()=" + getAnnualCTC()
				+ ", getEntitycreflg()=" + getEntitycreflg() + ", getDelflg()=" + getDelflg() + ", getRcreuserid()="
				+ getRcreuserid() + ", getRcretime()=" + getRcretime() + ", getRmoduserid()=" + getRmoduserid()
				+ ", getRmodtime()=" + getRmodtime() + ", getRvfyuserid()=" + getRvfyuserid() + ", getRvfytime()="
				+ getRvfytime() + ", getComponents()=" + getComponents() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	public SalaryTemplate(Long templateId, String templateName, String description, Double annualCTC,
			String entitycreflg, String delflg, String rcreuserid, Date rcretime, String rmoduserid, Date rmodtime,
			String rvfyuserid, Date rvfytime, List<SalaryTemplateComponent> components) {
		super();
		this.templateId = templateId;
		this.templateName = templateName;
		this.description = description;
		this.annualCTC = annualCTC;
		this.entitycreflg = entitycreflg;
		this.delflg = delflg;
		this.rcreuserid = rcreuserid;
		this.rcretime = rcretime;
		this.rmoduserid = rmoduserid;
		this.rmodtime = rmodtime;
		this.rvfyuserid = rvfyuserid;
		this.rvfytime = rvfytime;
		this.components = components;
	}

	public SalaryTemplate() {
		super();
		// TODO Auto-generated constructor stub
	}

    
    
    
}
