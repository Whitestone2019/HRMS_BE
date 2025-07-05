package com.whitestone.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "salary_template_components")
public class SalaryTemplateComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "component_id")
    private Long componentId;

    @Column(name = "component_name")
    private String componentName;

	@Column(name = "calculation_type")
    @Enumerated(EnumType.STRING)
    private CalculationType calculationType;

    @Column(name = "value")
    private Double value;

    @Column(name = "monthly_amount")
    private Double monthlyAmount;

    @Column(name = "annual_amount")
    private Double annualAmount;

    @Column(name = "is_earning")
    private Boolean isEarning;
    
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

    @ManyToOne
    @JoinColumn(name = "template_id", referencedColumnName = "template_id", nullable = false)
    @JsonBackReference
    private SalaryTemplate salaryTemplate;

    // Getters and setters

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public CalculationType getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(CalculationType calculationType) {
        this.calculationType = calculationType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getMonthlyAmount() {
        return monthlyAmount;
    }

    public void setMonthlyAmount(Double monthlyAmount) {
        this.monthlyAmount = monthlyAmount;
    }

    public Double getAnnualAmount() {
        return annualAmount;
    }

    public void setAnnualAmount(Double annualAmount) {
        this.annualAmount = annualAmount;
    }

    public Boolean getIsEarning() {
        return isEarning;
    }

    public void setIsEarning(Boolean isEarning) {
        this.isEarning = isEarning;
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

    public SalaryTemplate getSalaryTemplate() {
        return salaryTemplate;
    }

    public void setSalaryTemplate(SalaryTemplate salaryTemplate) {
        this.salaryTemplate = salaryTemplate;
    }

    public enum CalculationType {
        FIXED, PERCENTAGE, BASICPERCENTAGE;

        @JsonCreator
        public static CalculationType fromString(String value) {
            return CalculationType.valueOf(value.toUpperCase());
        }
    }

    @Override
	public String toString() {
		return "SalaryTemplateComponent [componentId=" + componentId + ", componentName=" + componentName
				+ ", calculationType=" + calculationType + ", value=" + value + ", monthlyAmount=" + monthlyAmount
				+ ", annualAmount=" + annualAmount + ", isEarning=" + isEarning + ", entitycreflg=" + entitycreflg
				+ ", delflg=" + delflg + ", rcreuserid=" + rcreuserid + ", rcretime=" + rcretime + ", rmoduserid="
				+ rmoduserid + ", rmodtime=" + rmodtime + ", rvfyuserid=" + rvfyuserid + ", rvfytime=" + rvfytime
				+ ", salaryTemplate=" + salaryTemplate + ", getComponentId()=" + getComponentId()
				+ ", getComponentName()=" + getComponentName() + ", getCalculationType()=" + getCalculationType()
				+ ", getValue()=" + getValue() + ", getMonthlyAmount()=" + getMonthlyAmount() + ", getAnnualAmount()="
				+ getAnnualAmount() + ", getIsEarning()=" + getIsEarning() + ", getEntitycreflg()=" + getEntitycreflg()
				+ ", getDelflg()=" + getDelflg() + ", getRcreuserid()=" + getRcreuserid() + ", getRcretime()="
				+ getRcretime() + ", getRmoduserid()=" + getRmoduserid() + ", getRmodtime()=" + getRmodtime()
				+ ", getRvfyuserid()=" + getRvfyuserid() + ", getRvfytime()=" + getRvfytime() + ", getSalaryTemplate()="
				+ getSalaryTemplate() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

	public SalaryTemplateComponent(Long componentId, String componentName, CalculationType calculationType,
			Double value, Double monthlyAmount, Double annualAmount, Boolean isEarning, String entitycreflg,
			String delflg, String rcreuserid, Date rcretime, String rmoduserid, Date rmodtime, String rvfyuserid,
			Date rvfytime, SalaryTemplate salaryTemplate) {
		super();
		this.componentId = componentId;
		this.componentName = componentName;
		this.calculationType = calculationType;
		this.value = value;
		this.monthlyAmount = monthlyAmount;
		this.annualAmount = annualAmount;
		this.isEarning = isEarning;
		this.entitycreflg = entitycreflg;
		this.delflg = delflg;
		this.rcreuserid = rcreuserid;
		this.rcretime = rcretime;
		this.rmoduserid = rmoduserid;
		this.rmodtime = rmodtime;
		this.rvfyuserid = rvfyuserid;
		this.rvfytime = rvfytime;
		this.salaryTemplate = salaryTemplate;
	}

	public SalaryTemplateComponent() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
