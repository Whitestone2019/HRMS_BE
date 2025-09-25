
package com.whitestone.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class EmployeePermissionMasterTbl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String empid;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp endTime;

    private Float hours;
    private String teamemail;
    private String reason;
    private String status;
    private String delflg;
    private String entitycreflg;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmpid() { return empid; }
    public void setEmpid(String empid) { this.empid = empid; }
    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }
    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }
    public Float getHours() { return hours; }
    public void setHours(Float hours) { this.hours = hours; }
    public String getTeamemail() { return teamemail; }
    public void setTeamemail(String teamemail) { this.teamemail = teamemail; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getDelflg() { return delflg; }
    public void setDelflg(String delflg) { this.delflg = delflg; }
    public String getEntitycreflg() { return entitycreflg; }
    public void setEntitycreflg(String entitycreflg) { this.entitycreflg = entitycreflg; }
}