package com.whitestone.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CompanyLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private int employeeCount;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public int getEmployeeCount() {
		return employeeCount;
	}
	public CompanyLocation(Long id, String name, String address, String city, String state, String zip,
			int employeeCount) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.employeeCount = employeeCount;
	}
	public void setEmployeeCount(int employeeCount) {
		this.employeeCount = employeeCount;
	}
	@Override
	public String toString() {
		return "CompanyLocation [id=" + id + ", name=" + name + ", address=" + address + ", city=" + city + ", state="
				+ state + ", zip=" + zip + ", employeeCount=" + employeeCount + ", getId()=" + getId() + ", getName()="
				+ getName() + ", getAddress()=" + getAddress() + ", getCity()=" + getCity() + ", getState()="
				+ getState() + ", getZip()=" + getZip() + ", getEmployeeCount()=" + getEmployeeCount() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	public CompanyLocation() {
		super();
		// TODO Auto-generated constructor stub
	}

   
}