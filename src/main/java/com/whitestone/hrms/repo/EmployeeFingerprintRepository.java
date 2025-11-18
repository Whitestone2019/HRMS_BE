package com.whitestone.hrms.repo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.whitestone.entity.EmployeeFingerprint;

public interface EmployeeFingerprintRepository extends JpaRepository<EmployeeFingerprint, Long> {
    Optional<EmployeeFingerprint> findByEmployeeId(String employeeId);
}