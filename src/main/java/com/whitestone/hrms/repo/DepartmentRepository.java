package com.whitestone.hrms.repo;
import org.springframework.data.jpa.repository.JpaRepository;

import com.whitestone.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}