package com.whitestone.hrms.repo;
import com.whitestone.entity.HrmsMenuMaintenance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HrmsMenuMaintenanceRepository extends JpaRepository<HrmsMenuMaintenance, String> {
	//You can define custom query methods here if needed.
}