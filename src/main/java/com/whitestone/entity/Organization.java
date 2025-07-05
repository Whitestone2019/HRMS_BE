package com.whitestone.entity;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "Organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Column(name = "legal_business_name", nullable = false)
    private String legalBusinessName;

    @Column(name = "tax_id", nullable = false, unique = true)
    private String taxId;

    // Contact Information fields
    @Column(name = "business_email", nullable = false, unique = true)
    private String businessEmail;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "website")
    private String website;

    // Business Address fields
    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "RCRE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rcreTime;

    @Column(name = "RMOD_USER_ID", length = 15)
    private String rmodUserId;

    @Column(name = "RMOD_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rmodTime;
    
    @Column(name = "ENTITY_CRE_FLG", length = 1)
    private String entityCreFlg;

    @Column(name = "DEL_FLG", length = 1)
    private String delFlg;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getLegalBusinessName() {
		return legalBusinessName;
	}

	public void setLegalBusinessName(String legalBusinessName) {
		this.legalBusinessName = legalBusinessName;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getBusinessEmail() {
		return businessEmail;
	}

	public void setBusinessEmail(String businessEmail) {
		this.businessEmail = businessEmail;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	@Override
	public String toString() {
		return "Organization [id=" + id + ", organizationName=" + organizationName + ", legalBusinessName="
				+ legalBusinessName + ", taxId=" + taxId + ", businessEmail=" + businessEmail + ", phoneNumber="
				+ phoneNumber + ", website=" + website + ", streetAddress=" + streetAddress + ", city=" + city
				+ ", state=" + state + ", zipCode=" + zipCode + ", country=" + country + ", rcreTime=" + rcreTime
				+ ", rmodUserId=" + rmodUserId + ", rmodTime=" + rmodTime + ", entityCreFlg=" + entityCreFlg
				+ ", delFlg=" + delFlg + ", getId()=" + getId() + ", getOrganizationName()=" + getOrganizationName()
				+ ", getLegalBusinessName()=" + getLegalBusinessName() + ", getTaxId()=" + getTaxId()
				+ ", getBusinessEmail()=" + getBusinessEmail() + ", getPhoneNumber()=" + getPhoneNumber()
				+ ", getWebsite()=" + getWebsite() + ", getStreetAddress()=" + getStreetAddress() + ", getCity()="
				+ getCity() + ", getState()=" + getState() + ", getZipCode()=" + getZipCode() + ", getCountry()="
				+ getCountry() + ", getRcreTime()=" + getRcreTime() + ", getRmodUserId()=" + getRmodUserId()
				+ ", getRmodTime()=" + getRmodTime() + ", getEntityCreFlg()=" + getEntityCreFlg() + ", getDelFlg()="
				+ getDelFlg() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

	public Organization(Long id, String organizationName, String legalBusinessName, String taxId, String businessEmail,
			String phoneNumber, String website, String streetAddress, String city, String state, String zipCode,
			String country, Date rcreTime, String rmodUserId, Date rmodTime, String entityCreFlg, String delFlg) {
		super();
		this.id = id;
		this.organizationName = organizationName;
		this.legalBusinessName = legalBusinessName;
		this.taxId = taxId;
		this.businessEmail = businessEmail;
		this.phoneNumber = phoneNumber;
		this.website = website;
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
		this.rcreTime = rcreTime;
		this.rmodUserId = rmodUserId;
		this.rmodTime = rmodTime;
		this.entityCreFlg = entityCreFlg;
		this.delFlg = delFlg;
	}

	public Organization() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    
}
