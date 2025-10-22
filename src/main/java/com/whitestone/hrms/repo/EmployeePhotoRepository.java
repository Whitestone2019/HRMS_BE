package com.whitestone.hrms.repo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.EmployeePhoto;

@Repository
public interface EmployeePhotoRepository extends JpaRepository<EmployeePhoto, Long> {
   // List<EmployeePhoto> findByEmployeeId(String employeeId);
    Optional<EmployeePhoto> findByEmployeeId(String employeeId);
}
