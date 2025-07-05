package com.whitestone.entity;
 
import java.sql.Date;

import java.sql.Timestamp;

import javax.persistence.Column;

import javax.persistence.Entity;

import javax.persistence.GeneratedValue;

import javax.persistence.GenerationType;

import javax.persistence.Id;

import javax.persistence.SequenceGenerator;

import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
 
@Entity

@Table(name = "WSSL_CALENDER_MOD", schema = "HRMSUSER")

@JsonIgnoreProperties(ignoreUnknown = true)

public class WsslCalendarMod {
 
    @Id

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CALENDAR_SEQ")

    @SequenceGenerator(name = "CALENDAR_SEQ", sequenceName = "WSSL_CALENDER_MOD_SEQ", allocationSize = 1)

    @Column(name = "CALENDER_ID", nullable = false)

    private Long calenderId;
 
    @Column(name = "EVENT_NAME", nullable = false, length = 100)

    private String eventName;
 
    @Column(name = "EVENT_DATE")

    private Date eventDate; // Ensure this field is present and mapped correctly
 
    @Column(name = "EVENT_TYPE", nullable = false, length = 50)

    private String eventType;
 
    @Column(name = "DESCRIPTION", nullable = true)

    private String description;
 
    @Column(name = "IS_PUBLIC", length = 1)

    private String isPublic;
 
    @Column(name = "CREATED_BY", nullable = true, length = 50)

    private String createdBy;
 
    @Column(name = "CREATED_DATE", nullable = true)

    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")

    private Timestamp createdDate;
 
    @Column(name = "UPDATED_BY", nullable = true, length = 50)

    private String updatedBy;
 
    @Column(name = "UPDATED_DATE", nullable = true)

    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")

    private Timestamp updatedDate;
 
    // Getters and Setters
 
    public Long getCalenderId() {

        return calenderId;

    }
 
    public void setCalenderId(Long calenderId) {

        this.calenderId = calenderId;

    }
 
    public String getEventName() {

        return eventName;

    }
 
    public void setEventName(String eventName) {

        this.eventName = eventName;

    }
 
    public Date getEventDate() {

        return eventDate;

    }
 
    public void setEventDate(Date eventDate) {

        this.eventDate = eventDate;

    }
 
    public String getEventType() {

        return eventType;

    }
 
    public void setEventType(String eventType) {

        this.eventType = eventType;

    }
 
    public String getDescription() {

        return description;

    }
 
    public void setDescription(String description) {

        this.description = description;

    }
 
    public String getIsPublic() {

        return isPublic;

    }
 
    public void setIsPublic(String isPublic) {

        this.isPublic = isPublic;

    }
 
    public String getCreatedBy() {

        return createdBy;

    }
 
    public void setCreatedBy(String createdBy) {

        this.createdBy = createdBy;

    }
 
    public Timestamp getCreatedDate() {

        return createdDate;

    }
 
    public void setCreatedDate(Timestamp createdDate) {

        this.createdDate = createdDate;

    }
 
    public String getUpdatedBy() {

        return updatedBy;

    }
 
    public void setUpdatedBy(String updatedBy) {

        this.updatedBy = updatedBy;

    }
 
    public Timestamp getUpdatedDate() {

        return updatedDate;

    }
 
    public void setUpdatedDate(Timestamp updatedDate) {

        this.updatedDate = updatedDate;

    }

}

 