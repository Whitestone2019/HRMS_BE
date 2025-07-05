package com.whitestone.hrms.repo;
import java.util.List;

import com.whitestone.entity.SalaryTemplateComponent.CalculationType;

public class SalaryTemplatePayload {

    private Template template;
    private List<Component> components;

    // Getters and Setters

    public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public List<Component> getComponents() {
		return components;
	}

	public void setComponents(List<Component> components) {
		this.components = components;
	}

	public static class Template {
        private String templateName;
        private String description;
        private double annualCTC;
        private String rcreuserid;
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
		public double getAnnualCTC() {
			return annualCTC;
		}
		public void setAnnualCTC(double annualCTC) {
			this.annualCTC = annualCTC;
		}
		public String getRcreuserid() {
			return rcreuserid;
		}
		public void setRcreuserid(String rcreuserid) {
			this.rcreuserid = rcreuserid;
		}

        // Getters and Setters
    }

    public static class Component {
        private String componentName;
        private CalculationType calculationType; 
        private double value;
        private double monthlyAmount;
        private double annualAmount;
        
        private boolean isEarning;
        
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
		public double getValue() {
			return value;
		}
		public void setValue(double value) {
			this.value = value;
		}
		public double getMonthlyAmount() {
			return monthlyAmount;
		}
		public void setMonthlyAmount(double monthlyAmount) {
			this.monthlyAmount = monthlyAmount;
		}
		public double getAnnualAmount() {
			return annualAmount;
		}
		public void setAnnualAmount(double annualAmount) {
			this.annualAmount = annualAmount;
		}
		public boolean isEarning() {
			return isEarning;
		}
		public void setEarning(boolean isEarning) {
			this.isEarning = isEarning;
		}
        
        
		
    }
}
