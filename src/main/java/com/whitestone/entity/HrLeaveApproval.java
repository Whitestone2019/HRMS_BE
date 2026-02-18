package com.whitestone.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "hr_leave_approval")
public class HrLeaveApproval {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "employee_id", nullable = false, length = 50)
    private String employeeId;
    
    @Column(name = "leave_type", nullable = false, length = 50)
    private String leaveType;
    
    @Column(name = "requested_date")
    @Temporal(TemporalType.DATE)
    private Date requestedDate;
    
    @Column(name = "status", length = 20)
    private String status = "pending"; // pending, approved, rejected, cancelled
    
    @Column(name = "del_flag", length = 1)
    private String delFlag = "N";
    
    // Audit fields
    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    
    @Column(name = "created_by", length = 50)
    private String createdBy;
    
    @Column(name = "updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;
    
    @Column(name = "updated_by", length = 50)
    private String updatedBy;
    
    @Column(name = "hr_name", length = 100)
    private String hrName;
    
    @Column(name = "EMAIL_ID", length = 50)
    private String emailid;
    
    @Column(name = "manager_name", length = 100)
    private String managerName;
    
    @Column(name = "manager_email", length = 100)
    private String managerEmail;
    
    @Column(name = "hr_user_id", length = 50)
    private String hrUserId;
    
    @Column(name = "from_date")
    @Temporal(TemporalType.DATE)
    private Date fromDate;
    
    @Column(name = "to_date")
    @Temporal(TemporalType.DATE)
    private Date toDate;
    
    @Column(name = "no_of_days")
    private Double noOfDays;
    
    @Column(name = "reason", length = 500)
    private String reason;
    
    @Column(name = "leave_id")
    private Long leaveId;
    
    @Column(name = "approved_date")
    @Temporal(TemporalType.DATE)
    private Date approvedDate;
    
    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;
    
    // Constructors
    public HrLeaveApproval() {
        this.createdOn = new Date();
        this.updatedOn = new Date();
        this.requestedDate = new Date();
    }
    
    public HrLeaveApproval(String employeeId, String leaveType) {
        this();
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.status = "pending";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public Date getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getHrName() {
        return hrName;
    }

    public void setHrName(String hrName) {
        this.hrName = hrName;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getHrUserId() {
        return hrUserId;
    }

    public void setHrUserId(String hrUserId) {
        this.hrUserId = hrUserId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Double getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(Double noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Long leaveId) {
        this.leaveId = leaveId;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}