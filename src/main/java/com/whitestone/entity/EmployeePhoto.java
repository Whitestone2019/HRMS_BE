package com.whitestone.entity;

import javax.persistence.*;

@Entity
@Table(name = "EMPLOYEE_PHOTO")
public class EmployeePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String employeeId;

    @Column(nullable = false)
    private String fileName; // e.g., EMP001_photo.jpg

    @Column(nullable = false)
    private String fileType; // image/jpeg, image/png, etc.

    @Column(nullable = false)
    private String fileUrl; // URL to access the photo, e.g., /photos/EMP001_photo.jpg

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
}
