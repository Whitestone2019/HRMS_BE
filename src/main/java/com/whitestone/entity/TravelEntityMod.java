package com.whitestone.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "EMPLOYEE_TRAVEL_MOD_TBL")
public class TravelEntityMod {

    @Id
    @Column(name = "USER_ID", nullable = false, length = 15)
    private String userId;

    @Column(name = "EMP_ID", nullable = false, length = 15)
    private String empId;

    @Column(name = "TRAVEL_ID", nullable = false, length = 20)
    private String travelId;

    @Column(name = "EMPLOYEE_DEPARTMENT", length = 50)
    private String employeeDepartment;

    @Column(name = "PLACE_OF_VISIT", length = 100)
    private String placeOfVisit;

    @Column(name = "EXPECTED_DATE_OF_DEPARTURE")
    @Temporal(TemporalType.DATE)
    private Date expectedDateOfDeparture;

    @Column(name = "EXPECTED_DATE_OF_ARRIVAL")
    @Temporal(TemporalType.DATE)
    private Date expectedDateOfArrival;

    @Column(name = "PURPOSE_OF_VISIT", length = 50)
    private String purposeOfVisit;

    @Column(name = "EXPECTED_DURATION_IN_DAYS", length = 10)
    private String expectedDurationInDays;

    @Column(name = "IS_BILLABLE_TO_CUSTOMER", length = 30)
    private String isBillableToCustomer;

    @Column(name = "CUSTOMER_NAME", length = 80)
    private String customerName;

    @Column(name = "ENTITY_CRE_FLG", length = 1)
    private String entityCreFlg;

    @Column(name = "DEL_FLG", length = 1)
    private String delFlg;

    @Column(name = "RCRE_USER_ID", length = 15)
    private String rcreUserId;

    @Column(name = "RCRE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rcreTime;

    @Column(name = "RMOD_USER_ID", length = 15)
    private String rmodUserId;

    @Column(name = "RMOD_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rmodTime;

    @Column(name = "RVFY_USER_ID", length = 15)
    private String rvfyUserId;

    @Column(name = "RVFY_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rvfyTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getTravelId() {
		return travelId;
	}

	public void setTravelId(String travelId) {
		this.travelId = travelId;
	}

	public String getEmployeeDepartment() {
		return employeeDepartment;
	}

	public void setEmployeeDepartment(String employeeDepartment) {
		this.employeeDepartment = employeeDepartment;
	}

	public String getPlaceOfVisit() {
		return placeOfVisit;
	}

	public void setPlaceOfVisit(String placeOfVisit) {
		this.placeOfVisit = placeOfVisit;
	}

	public Date getExpectedDateOfDeparture() {
		return expectedDateOfDeparture;
	}

	public void setExpectedDateOfDeparture(Date expectedDateOfDeparture) {
		this.expectedDateOfDeparture = expectedDateOfDeparture;
	}

	public Date getExpectedDateOfArrival() {
		return expectedDateOfArrival;
	}

	public void setExpectedDateOfArrival(Date expectedDateOfArrival) {
		this.expectedDateOfArrival = expectedDateOfArrival;
	}

	public String getPurposeOfVisit() {
		return purposeOfVisit;
	}

	public void setPurposeOfVisit(String purposeOfVisit) {
		this.purposeOfVisit = purposeOfVisit;
	}

	public String getExpectedDurationInDays() {
		return expectedDurationInDays;
	}

	public void setExpectedDurationInDays(String expectedDurationInDays) {
		this.expectedDurationInDays = expectedDurationInDays;
	}

	public String getIsBillableToCustomer() {
		return isBillableToCustomer;
	}

	public void setIsBillableToCustomer(String isBillableToCustomer) {
		this.isBillableToCustomer = isBillableToCustomer;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public String getRcreUserId() {
		return rcreUserId;
	}

	public void setRcreUserId(String rcreUserId) {
		this.rcreUserId = rcreUserId;
	}

	public Date getRcreTime() {
		return rcreTime;
	}

	public void setRcreTime(Date rcreTime) {
		this.rcreTime = rcreTime;
	}

	public String getRmodUserId() {
		return rmodUserId;
	}

	public void setRmodUserId(String rmodUserId) {
		this.rmodUserId = rmodUserId;
	}

	public Date getRmodTime() {
		return rmodTime;
	}

	public void setRmodTime(Date rmodTime) {
		this.rmodTime = rmodTime;
	}

	public String getRvfyUserId() {
		return rvfyUserId;
	}

	public void setRvfyUserId(String rvfyUserId) {
		this.rvfyUserId = rvfyUserId;
	}

	public Date getRvfyTime() {
		return rvfyTime;
	}

	public void setRvfyTime(Date rvfyTime) {
		this.rvfyTime = rvfyTime;
	}

	@Override
	public String toString() {
		return "TravelEntityMod [userId=" + userId + ", empId=" + empId + ", travelId=" + travelId
				+ ", employeeDepartment=" + employeeDepartment + ", placeOfVisit=" + placeOfVisit
				+ ", expectedDateOfDeparture=" + expectedDateOfDeparture + ", expectedDateOfArrival="
				+ expectedDateOfArrival + ", purposeOfVisit=" + purposeOfVisit + ", expectedDurationInDays="
				+ expectedDurationInDays + ", isBillableToCustomer=" + isBillableToCustomer + ", customerName="
				+ customerName + ", entityCreFlg=" + entityCreFlg + ", delFlg=" + delFlg + ", rcreUserId=" + rcreUserId
				+ ", rcreTime=" + rcreTime + ", rmodUserId=" + rmodUserId + ", rmodTime=" + rmodTime + ", rvfyUserId="
				+ rvfyUserId + ", rvfyTime=" + rvfyTime + ", getUserId()=" + getUserId() + ", getEmpId()=" + getEmpId()
				+ ", getTravelId()=" + getTravelId() + ", getEmployeeDepartment()=" + getEmployeeDepartment()
				+ ", getPlaceOfVisit()=" + getPlaceOfVisit() + ", getExpectedDateOfDeparture()="
				+ getExpectedDateOfDeparture() + ", getExpectedDateOfArrival()=" + getExpectedDateOfArrival()
				+ ", getPurposeOfVisit()=" + getPurposeOfVisit() + ", getExpectedDurationInDays()="
				+ getExpectedDurationInDays() + ", getIsBillableToCustomer()=" + getIsBillableToCustomer()
				+ ", getCustomerName()=" + getCustomerName() + ", getEntityCreFlg()=" + getEntityCreFlg()
				+ ", getDelFlg()=" + getDelFlg() + ", getRcreUserId()=" + getRcreUserId() + ", getRcreTime()="
				+ getRcreTime() + ", getRmodUserId()=" + getRmodUserId() + ", getRmodTime()=" + getRmodTime()
				+ ", getRvfyUserId()=" + getRvfyUserId() + ", getRvfyTime()=" + getRvfyTime() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	public TravelEntityMod(String userId, String empId, String travelId, String employeeDepartment, String placeOfVisit,
			Date expectedDateOfDeparture, Date expectedDateOfArrival, String purposeOfVisit,
			String expectedDurationInDays, String isBillableToCustomer, String customerName, String entityCreFlg,
			String delFlg, String rcreUserId, Date rcreTime, String rmodUserId, Date rmodTime, String rvfyUserId,
			Date rvfyTime) {
		super();
		this.userId = userId;
		this.empId = empId;
		this.travelId = travelId;
		this.employeeDepartment = employeeDepartment;
		this.placeOfVisit = placeOfVisit;
		this.expectedDateOfDeparture = expectedDateOfDeparture;
		this.expectedDateOfArrival = expectedDateOfArrival;
		this.purposeOfVisit = purposeOfVisit;
		this.expectedDurationInDays = expectedDurationInDays;
		this.isBillableToCustomer = isBillableToCustomer;
		this.customerName = customerName;
		this.entityCreFlg = entityCreFlg;
		this.delFlg = delFlg;
		this.rcreUserId = rcreUserId;
		this.rcreTime = rcreTime;
		this.rmodUserId = rmodUserId;
		this.rmodTime = rmodTime;
		this.rvfyUserId = rvfyUserId;
		this.rvfyTime = rvfyTime;
	}

	public TravelEntityMod() {
		super();
		// TODO Auto-generated constructor stub
	}

   
}
