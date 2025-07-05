package com.whitestone.hrms.repo;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whitestone.entity.usermaintenancemod;
@EnableJpaRepositories(basePackages = "com.whitestone.hrms.repo")
@Repository
public interface usermaintenancemodRepository extends JpaRepository<usermaintenancemod, String> {
	@Query("SELECT u FROM usermaintenancemod u WHERE u.emp_id = :emp_id")
	Optional<usermaintenancemod> findByEmpId(@Param("emp_id") String empid);

}