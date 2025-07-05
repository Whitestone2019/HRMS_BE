package com.whitestone.hrms.repo;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.whitestone.entity.Designation;

public interface DesignationRepository extends JpaRepository<Designation, Long> {
	 List<Designation> findByDelFlgNot(String delFlg);
	
}