package com.whitestone.hrms.repo;
import com.whitestone.entity.EmployeeSkill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, String> {
	//You can define custom query methods here if needed.
}