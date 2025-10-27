package com.whitestone.hrms.repo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.whitestone.entity.AttendanceChangeRequest;

public interface AttendanceChangeRequestRepository extends JpaRepository<AttendanceChangeRequest, Long> {
    List<AttendanceChangeRequest> findByStatusAndEmployeeIdIn(String status, List<String> empIds);
    
    boolean existsByEmployeeIdAndAttendanceDate(String employeeId, LocalDate attendanceDate);
    
    Optional<AttendanceChangeRequest> findByEmployeeIdAndAttendanceDateAndStatus(String employeeId, LocalDate attendanceDate, String status);

}
