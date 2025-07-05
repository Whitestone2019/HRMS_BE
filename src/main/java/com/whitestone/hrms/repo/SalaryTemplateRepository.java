package com.whitestone.hrms.repo;
import org.springframework.data.jpa.repository.JpaRepository;

import com.whitestone.entity.SalaryTemplate;

public interface SalaryTemplateRepository extends JpaRepository<SalaryTemplate, Long> {
	
	
}
