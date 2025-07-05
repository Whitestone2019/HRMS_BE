package com.whitestone.hrms.repo;
import com.whitestone.entity.HrmsMenuMaintenanceMod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HrmsMenuMaintenanceModRepository extends JpaRepository<HrmsMenuMaintenanceMod, String> {
	//You can define custom query methods here if needed.
}