package com.whitestone.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_project_history")
public class EmployeeProjectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emp_id")
    private String empId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "project_duration")
    private String projectDuration;

    @Column(name = "location")
    private String location;

    @Column(name = "client_info")
    private String clientInfo;

    @Column(name = "vendor_details")
    private String vendorDetails;

    @Column(name = "tech_park_name")
    private String techParkName;

    @Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "mode_of_work")
    private String modeOfWork;

    @Column(name = "rcre_user_id")
    private String rcreUserId;

    @Column(name = "rcre_time")
    private LocalDateTime rcreTime;

    @Column(name = "rmod_user_id")
    private String rmodUserId;

    @Column(name = "rmod_time")
    private LocalDateTime rmodTime;

    @Column(name = "entity_cre_flg")
    private String entityCreFlg;

    @Column(name = "del_flg")
    private String delFlg;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getProjectDuration() { return projectDuration; }
    public void setProjectDuration(String projectDuration) { this.projectDuration = projectDuration; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getClientInfo() { return clientInfo; }
    public void setClientInfo(String clientInfo) { this.clientInfo = clientInfo; }

    public String getVendorDetails() { return vendorDetails; }
    public void setVendorDetails(String vendorDetails) { this.vendorDetails = vendorDetails; }

    public String getTechParkName() { return techParkName; }
    public void setTechParkName(String techParkName) { this.techParkName = techParkName; }

    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }

    public String getModeOfWork() { return modeOfWork; }
    public void setModeOfWork(String modeOfWork) { this.modeOfWork = modeOfWork; }

    public String getRcreUserId() { return rcreUserId; }
    public void setRcreUserId(String rcreUserId) { this.rcreUserId = rcreUserId; }

    public LocalDateTime getRcreTime() { return rcreTime; }
    public void setRcreTime(LocalDateTime rcreTime) { this.rcreTime = rcreTime; }

    public String getRmodUserId() { return rmodUserId; }
    public void setRmodUserId(String rmodUserId) { this.rmodUserId = rmodUserId; }

    public LocalDateTime getRmodTime() { return rmodTime; }
    public void setRmodTime(LocalDateTime rmodTime) { this.rmodTime = rmodTime; }

    public String getEntityCreFlg() { return entityCreFlg; }
    public void setEntityCreFlg(String entityCreFlg) { this.entityCreFlg = entityCreFlg; }

    public String getDelFlg() { return delFlg; }
    public void setDelFlg(String delFlg) { this.delFlg = delFlg; }
}