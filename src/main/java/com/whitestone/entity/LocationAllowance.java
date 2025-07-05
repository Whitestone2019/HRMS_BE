package com.whitestone.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "location_allowance")
public class LocationAllowance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String locationName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AllowanceType type; // Type of allowance

    @Column(nullable = false)
    private Double amount;

    public enum AllowanceType {
        PER_DAY_ALLOWANCE,
        PG_RENT
    }

    // Constructors
    public LocationAllowance() {}

    public LocationAllowance(String locationName, AllowanceType type, Double amount) {
        this.locationName = locationName;
        this.type = type;
        this.amount = amount;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public AllowanceType getType() {
        return type;
    }

    public void setType(AllowanceType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
