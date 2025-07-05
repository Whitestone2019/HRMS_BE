package com.whitestone.hrms.repo;
import org.springframework.data.jpa.repository.JpaRepository;

import com.whitestone.entity.SalaryComponent;

public interface SalaryComponentRepository extends JpaRepository<SalaryComponent, Long> {
    // Custom query methods can be added if needed
}
